/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/10 0010 19:56:01
 * @version 1.0
 */

package net.xdclass.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.constant.RedisKey;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.EventMessageType;
import net.xdclass.enums.TaskStateEnum;
import net.xdclass.exception.BizException;
import net.xdclass.feign.ProductFeignService;
import net.xdclass.feign.ShortLinkFeignService;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.TrafficManage;
import net.xdclass.manager.TrafficTaskManage;
import net.xdclass.model.EventMessage;
import net.xdclass.model.TrafficDO;
import net.xdclass.model.TrafficTaskDO;
import net.xdclass.request.TrafficPageRequest;
import net.xdclass.request.UseTrafficRequest;
import net.xdclass.service.TrafficService;
import net.xdclass.utils.JsonData;
import net.xdclass.utils.JsonUtil;
import net.xdclass.utils.TimeUtil;
import net.xdclass.vo.ProductVo;
import net.xdclass.vo.TrafficVo;
import net.xdclass.vo.UserTrafficVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description TODO
 *   Map<String, Object> content = new HashMap<>(4);
 *         content.put("outTradeNo", outTradeNo);
 *         content.put("buyNum", productOrderDO.getBuyNum());
 *         content.put("accountNo", productOrderDO.getAccountNo());
 *         content.put("product", JsonUtil.obj2Json(productOrderDO.getProductSnapshot()));
 * @return
 * @author
 * @date
 */
@Service
@Slf4j
public class TrafficServiceImpl implements TrafficService {
    @Autowired
    private TrafficManage trafficManage;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private TrafficTaskManage trafficTaskManage;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;


    @Autowired
    private ShortLinkFeignService shortLinkFeignService;

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void handlerTraffcMessage(EventMessage eventMessage) {
        String eventMessageType = eventMessage.getEventMessageType();
        Long accountNo = eventMessage.getAccountNo();
        if (EventMessageType.PRODUCT_ORDER_PAY.name().equalsIgnoreCase(eventMessageType)){
            String content = eventMessage.getContent();
            Map<String,Object> orderInfoMap = JsonUtil.json2Obj(content, Map.class);
            //空间换时间;将订单信息存储在mq里面;
            String outTradeNo =(String) orderInfoMap.get("outTradeNo");
            Integer buyNum =(Integer) orderInfoMap.get("buyNum");
            String productStr =(String) orderInfoMap.get("product");
            ProductVo productVo = JsonUtil.json2Obj(productStr, ProductVo.class);
            log.info("商品信息productVo={}",productVo);
           //流量包有效期;
            LocalDateTime expireDateTime = LocalDateTime.now().plusDays(productVo.getValidDay());
            Date date = Date.from(expireDateTime.atZone(ZoneId.systemDefault()).toInstant());
            TrafficDO trafficDO = TrafficDO.builder()
                    .accountNo(accountNo)
                    .dayLimit(productVo.getDayTimes() * buyNum)
                    .totalLimit(productVo.getTotalTimes())
                    .pluginType(productVo.getPluginType())
                    .level(productVo.getLevel())
                    .productId(productVo.getId())
                    .outTradeNo(outTradeNo)
                    .expiredDate(date)
                    .build();
            int rows = trafficManage.add(trafficDO);
            log.info("消费消息新增流量包:rows={},trafficDO={}",rows,trafficDO);
            String totalTrafficTimeKey=String.format(RedisKey.DAY_TOTAL_TRAFFIC,accountNo);
            //当新增流量包时,删除对应得key
            redisTemplate.delete(totalTrafficTimeKey);
        }else if(EventMessageType.TRAFFIC_FREE_INIT.name().equalsIgnoreCase(eventMessageType)){
            //发放免费流量包;
            Long productId = Long.valueOf(eventMessage.getBizId());
           JsonData jsonData = productFeignService.detail(productId);
            ProductVo productVo = jsonData.getData(new TypeReference<ProductVo>() {
            });
            TrafficDO trafficDO = TrafficDO.builder()
                    .accountNo(accountNo)
                    .dayLimit(productVo.getDayTimes())
                    .totalLimit(productVo.getTotalTimes())
                    .pluginType(productVo.getPluginType())
                    .level(productVo.getLevel())
                    .productId(productVo.getId())
                    .outTradeNo("free_init")
                    .expiredDate(new java.util.Date())
                    .build();
            trafficManage.add(trafficDO);
        }else if (EventMessageType.TRAFFIC_USED.name().equals(eventMessage.getEventMessageType())){
            //流量包使用,检查是否成功使用;

            //检查task是否存在;
            Long trafficTaskId = Long.valueOf(eventMessage.getBizId());
            TrafficTaskDO trafficTaskDO = trafficTaskManage.findByIdAndAccountNo(trafficTaskId, accountNo);
            if (trafficTaskDO!=null&&trafficTaskDO.getLockState().equalsIgnoreCase(TaskStateEnum.LOCK.name())){
                //检查短链是否成功(存在)‘
                JsonData jsonData = shortLinkFeignService.check(trafficTaskDO.getBizId());
                //如果不成功,则恢复流量包;
                if (jsonData.getCode()!=0){
                    log.error("创建短链码失败,流量包回滚");
                    String useDateStr = TimeUtil.format(new Date(), "yyyy-MM-dd");
                    int rows = trafficManage.releaseUsedTimes(accountNo, trafficTaskDO.getTrafficId(), 1,useDateStr);
                    log.info("回滚流量包成功{}",rows);
                    //恢复流量包的时候,redis里面应删除对应得key;
                    String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);
                    redisTemplate.delete(totalTrafficTimesKey);
                }
                //删除task(也可以更新task状态,定时删除就行;)
                trafficTaskManage.deleteByIdAndAccount(trafficTaskId,accountNo);
            }

        }
    }

    @Override
    public Map<String, Object> page(TrafficPageRequest request) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        IPage<TrafficDO> trafficDOIPage = trafficManage.pageAvaliable(request.getPage(), request.getSize(), accountNo);
      //获取流量包;
        List<TrafficDO> records = trafficDOIPage.getRecords();
        List<TrafficVo> trafficVoList = records.stream().map(obj ->
                beanProceross(obj)
        ).collect(Collectors.toList());
        HashMap<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record",trafficDOIPage.getTotal());
        pageMap.put("total_page",trafficDOIPage.getPages());
        pageMap.put("current_data",trafficVoList);
        return pageMap;
    }

    @Override
    public TrafficVo detail(long trafficId) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        TrafficDO trafficDO = trafficManage.findByIdAndAccountNo(trafficId, accountNo);
//        TrafficVo trafficVo = new TrafficVo();
//        BeanUtils.copyProperties(trafficDO,trafficVo);
        return   beanProceross(trafficDO);
    }

    @Override
    public boolean deleteExpireTraffic() {
        boolean flag=trafficManage.deleteExpireTraffic();
        return flag;
    }
/**
 * @description TODO
 * * 查询用户全部可用流量包
 * * 遍历用户可用流量包
 *   * 判断是否更新-用日期判断
 *     * 没更新的流量包后加入【待更新集合】中
 *       * 增加【今天剩余可用总次数】
 *     * 已经更新的判断是否超过当天使用次数
 *       * 如果没超过则增加【今天剩余可用总次数】
 *       * 超过则忽略
 *
 * * 更新用户今日流量包相关数据
 * * 扣减使用的某个流量包使用次数
 * @return
 * @author
 * @date
 */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public JsonData reduce(UseTrafficRequest useTrafficRequest) {
        Long accountNo = useTrafficRequest.getAccountNo();
        //处理流量包,筛选未更新的流量包，当前使用的流量包;
        UserTrafficVo userTrafficVo = processTrafficList(accountNo);
        log.info("今天可用总次数：{},当前使用流量{}",userTrafficVo.getDayTotalLeftTimes(),userTrafficVo.getCurrentTrafficDo());
        if (userTrafficVo.getCurrentTrafficDo()==null){
            return JsonData.buildResult(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }
        log.info("待更新的流量包列表{}",userTrafficVo.getUnUpdateTrafficIds());
        if (userTrafficVo.getUnUpdateTrafficIds().size()>0){
            trafficManage.batchUpdateUsedTimes(accountNo,userTrafficVo.getUnUpdateTrafficIds());
        }
        //先更新,再扣减对应的流量包;
        int rows = trafficManage.addDayUsedTimes(accountNo, userTrafficVo.getCurrentTrafficDo().getId(), 1);
        //保存到task任务表里;
        TrafficTaskDO trafficTaskDO = TrafficTaskDO.builder().accountNo(accountNo)
                .bizId(useTrafficRequest.getBizId())
                .useTimes(1)
                .trafficId(userTrafficVo.getCurrentTrafficDo().getId())
                .lockState(TaskStateEnum.LOCK.name())
                .build();
        trafficTaskManage.add(trafficTaskDO);
        //扣减不成功;
        if (rows!=1){
            throw new BizException(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }
        //往redis设置下总的流量包的次数,短链服务那边递减,如果新增流量包,则删除这个key
        long leftSeconds=TimeUtil.getRemainSecondsOneDay(new Date());
        String totalTrafficTimeKey=String.format(RedisKey.DAY_TOTAL_TRAFFIC,accountNo);
        redisTemplate.opsForValue().set(totalTrafficTimeKey,userTrafficVo.getDayTotalLeftTimes()-1, leftSeconds, TimeUnit.SECONDS);
        EventMessage trafficUseEventMessage = EventMessage.builder().accountNo(accountNo).bizId(trafficTaskDO.getId() + "")
                .eventMessageType(EventMessageType.TRAFFIC_USED.name())
                .build();
        //发送延迟消息,进行事务回滚
        rabbitTemplate.convertAndSend(rabbitMQConfig.getTrafficEventExchange(),rabbitMQConfig.getTrafficReleaseDelayRoutingKey(),trafficUseEventMessage);
        return JsonData.buildSuccess();
    }

    private UserTrafficVo processTrafficList(Long accountNo) {
        List<TrafficDO> trafficDOList = trafficManage.selectAvailableTraffics(accountNo);
        if (trafficDOList==null || trafficDOList.size()==0){
            throw new BizException(BizCodeEnum.TRAFFIC_EXCEPTION);
        }
        Integer dayTotalLeftTimes = 0;
        TrafficDO CurrentTrafficDO = null;
        //没过期,但今天未更新的流量包;
         List<Long> unUpdateTrafficIds = new ArrayList<>();
       //今天日期;
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");
        for (TrafficDO trafficDO : trafficDOList){
            String trafficUpdatedate = TimeUtil.format( trafficDO.getGmtModified(), "yyyy-MM-dd");
            //已经更新的
            if (trafficUpdatedate.equalsIgnoreCase(today)){
                //已经更新,天剩余可用总次数=总次数-已用
                int dayleftTimes=trafficDO.getDayLimit()-trafficDO.getDayUsed();
                //总的流量包;
                dayTotalLeftTimes=dayTotalLeftTimes+dayleftTimes;
                //选取当次使用流量包;
                if (dayleftTimes>0 &&CurrentTrafficDO==null){
                    CurrentTrafficDO=trafficDO;
                }
            }else {
                //未更新
                dayTotalLeftTimes=dayTotalLeftTimes+trafficDO.getDayLimit();
                //记录未更新的流量包;
                unUpdateTrafficIds.add(trafficDO.getId());
                //选取当次使用的流量包
                if (CurrentTrafficDO==null){
                    CurrentTrafficDO=trafficDO;
                }
            }

        }
        UserTrafficVo userTrafficVo = new UserTrafficVo(dayTotalLeftTimes, CurrentTrafficDO, unUpdateTrafficIds);
        return userTrafficVo;
    }

    public TrafficVo beanProceross(TrafficDO trafficDO){
         TrafficVo trafficVo = new TrafficVo();
         BeanUtils.copyProperties(trafficDO,trafficVo);
         return trafficVo;
     }

}


