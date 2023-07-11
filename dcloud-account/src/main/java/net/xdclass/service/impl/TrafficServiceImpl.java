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
import net.xdclass.enums.EventMessageType;
import net.xdclass.feign.ProductFeignService;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.TrafficManage;
import net.xdclass.model.EventMessage;
import net.xdclass.model.TrafficDO;
import net.xdclass.request.TrafficPageRequest;
import net.xdclass.service.TrafficService;
import net.xdclass.utils.JsonData;
import net.xdclass.utils.JsonUtil;
import net.xdclass.vo.ProductVo;
import net.xdclass.vo.TrafficVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            java.util.Date date = Date.from(expireDateTime.atZone(ZoneId.systemDefault()).toInstant());
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

    public TrafficVo beanProceross(TrafficDO trafficDO){
         TrafficVo trafficVo = new TrafficVo();
         BeanUtils.copyProperties(trafficDO,trafficVo);
         return trafficVo;
     }

}


