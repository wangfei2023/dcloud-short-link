package net.xdclass.service.impl;

import com.alibaba.csp.sentinel.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONUtil;
import net.xdclass.compant.ShortLinkComponent;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.constant.RedisKey;
import net.xdclass.controller.request.*;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.DomainTypeEnum;
import net.xdclass.enums.EventMessageType;
import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.feign.TrafficFeignService;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manage.DomainManage;
import net.xdclass.manage.GroupCodeMappingManager;
import net.xdclass.manage.LinkGroupManage;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.mapper.LinkGroupMapper;
import net.xdclass.model.*;
import net.xdclass.service.ShortLinkService;
import net.xdclass.utils.*;
import net.xdclass.vo.ShortLinkVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/28 0028 22:45]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/28 0028 22:45]
 * @updateRemark : [说明本次修改内容]
 */
@Service
@Slf4j
public class ShortLinkServiceImpl implements ShortLinkService {
    @Autowired
    private ShortLinkManager shortLinkManager;
    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DomainManage domainManage;
    @Autowired
    private LinkGroupManage  linkGroupManage;
    @Autowired
    private ShortLinkComponent shortLinkComponent;
    @Autowired
    private GroupCodeMappingManager groupCodeMappingManager;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private TrafficFeignService trafficFeignService;

    @Override
    public ShortLinkVo parseLinkCode(String linkCode) {
        ShortLinkDO shortLinkDO = shortLinkManager.findByShortLinkCode(linkCode);
        if (StringUtils.isEmpty(JSON.toJSONString(shortLinkDO))){
            return null;
        }
        ShortLinkVo shortLinkVo = new ShortLinkVo();
        BeanUtils.copyProperties(shortLinkDO,shortLinkVo);
        return shortLinkVo;
    }
//创建短链-->预扣减-->从redis查询,如果rediskey>0,与扣减成功,发送到rabbitmq,客户端远程调用扣减流量包,查询流量包，更新流量包,在扣减流量包;
    @Override
    public JsonData createShortLink(ShortLinkAddRequest request) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        //预扣减流量包Lua脚本
        //需要预先检查下是否有足够多的可以进行创建
        String cacheKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);

        //检查key是否存在，然后递减，是否大于等于0，使用lua脚本
        // 如果key不存在，则未使用过，lua返回值是0； 新增流量包的时候，不用重新计算次数，直接删除key,消费的时候回计算更新
        //todo:if redis.call('get',KEYS[1]) 获取到值,代表key未过期,return redis.call('decr',KEYS[1])  -->KEYS递减; else return 0 end,
        // 否则key过期,返回0;
        //第一次创建短链从redis查询数据key为null;
        String script = "if redis.call('get',KEYS[1]) then return redis.call('decr',KEYS[1]) else return 0 end";
        Long leftTimes = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(cacheKey), "");
        if (leftTimes<0){
            redisTemplate.opsForValue().getAndSet(cacheKey,0);
        }
        log.info("今日流量包剩余次数:{}", leftTimes);

        if (leftTimes >= 0) {//有流量包使用;
            String newOriginalUrl = CommonUtil.addUrlPrefix(request.getOriginalUrl());
            request.setOriginalUrl(newOriginalUrl);

            EventMessage eventMessage = EventMessage.builder().accountNo(accountNo)
                    .content(JsonUtil.obj2Json(request))
                    .messageId(IDUtil.geneSnowFlakeID().toString())
                    .eventMessageType(EventMessageType.SHORT_LINK_ADD.name())
                    .build();

            rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(), rabbitMQConfig.getShortLinkAddRoutingKey(), eventMessage);
            return JsonData.buildSuccess();
      //流量包不足;
        } else {
            return JsonData.buildResult(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }
    }
    //生成长链摘要
//判断短链域名是否合法
//判断组名是否合法
//生成短链码
//加锁（加锁再查，不然查询后，加锁前有线程刚好新增）
//查询短链码是否存在
//构建短链mapping对象
//保存数据库
    @Override
    public boolean handerAddShortLink(EventMessage eventMessage) {
        //消费端处理逻辑;
        Long accountNo = eventMessage.getAccountNo();
        String eventMessageType = eventMessage.getEventMessageType();
        ShortLinkAddRequest shortLinkAddRequest = JsonUtil.json2Obj(eventMessage.getContent(), ShortLinkAddRequest.class);
        //短链域名校验
        DomainDO domainDO =this.checkDomain(shortLinkAddRequest.getDomainType(),shortLinkAddRequest.getDomainId(),accountNo);
        //校验组是否合法;
        LinkGroupDO linkGroupDO = this.checkGroup(shortLinkAddRequest.getGroupId(), accountNo);
        //长链摘要
        String originalUrlDigest = CommonUtil.MD5(shortLinkAddRequest.getOriginalUrl());
        //短链码重复标记;
         boolean duplicateCodeFlag = false;
        //生成短链
        String shortLinkCode = shortLinkComponent.createShortLinkCode(originalUrlDigest);
        //todo:加锁
        String script = "if redis.call('EXISTS',KEYS[1])==0 then redis.call('set',KEYS[1],ARGV[1]); redis.call('expire',KEYS[1],ARGV[2]); return 1;" +
                " elseif redis.call('get',KEYS[1]) == ARGV[1] then return 2;" +
                " else return 0; end;";
        Long result = redisTemplate.execute(new
        DefaultRedisScript<>(script, Long.class), Arrays.asList(shortLinkCode), accountNo,100);
        if (result>0){

            if (EventMessageType.SHORT_LINK_ADD_LINK.name().equals(eventMessageType)) {
                //c端处理
                ShortLinkDO shortLinkDOInDB = shortLinkManager.findByShortLinkCode(shortLinkCode);
                if (shortLinkDOInDB==null) {
                   boolean reduceFlag = reduceTraffic(eventMessage,shortLinkCode);
                   //短链服务扣减库存;(c端扣减流量包)
                   if (reduceFlag){
                       ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                               .accountNo(accountNo)
                               .code(shortLinkCode)
                               .title(shortLinkAddRequest.getTitle())
                               .originalUrl(shortLinkAddRequest.getOriginalUrl())
                               .domain(domainDO.getValue())
                               .groupId(linkGroupDO.getId())
                               .expired(shortLinkAddRequest.getExpired())
                               .sign(originalUrlDigest)
                               .state(ShortLinkStateEnum.ACTIVE.name())
                               .del(0)
                               .build();
                       //保存到数据库;
                       shortLinkManager.addShortLink(shortLinkDO);
                       return true;
                   }
                }else{
                    log.info("c端短链码已重复",eventMessage);
                    duplicateCodeFlag = true;
                }
            }else if (EventMessageType.SHORT_LINK_ADD_MAPPING.name().equals(eventMessageType)){
                GroupCodeMappingDO groupCodeMappingDOInDB = groupCodeMappingManager.findByCodeAndGroupId(shortLinkCode, linkGroupDO.getId(), accountNo);
                if (groupCodeMappingDOInDB==null) {
                    //b端处理
                    GroupCodeMappingDO groupCodeMappingDO = GroupCodeMappingDO.builder()
                            .accountNo(accountNo)
                            .code(shortLinkCode)
                            .title(shortLinkAddRequest.getTitle())
                            .originalUrl(shortLinkAddRequest.getOriginalUrl())
                            .domain(domainDO.getValue())
                            .groupId(linkGroupDO.getId())
                            .expired(shortLinkAddRequest.getExpired())
                            .sign(originalUrlDigest)
                            .state(ShortLinkStateEnum.ACTIVE.name())
                            .del(0)
                            .build();
                    //保存到数据库;
                    groupCodeMappingManager.add(groupCodeMappingDO);
                    return true;
                }else{
                    log.info("b端短链码已重复",eventMessage);
                    duplicateCodeFlag = true;
                }
        }else{
       //枷锁失败，自旋100s失败的可能是短链码已经被占用,需要重新生成;
                log.info("加锁失败",eventMessage);
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) { }
                duplicateCodeFlag = true;
                String newOriginalUrl = CommonUtil.addUrlPrefix(originalUrlDigest);
                shortLinkAddRequest.setOriginalUrl(newOriginalUrl);
                eventMessage.setContent(JsonUtil.obj2Json(shortLinkAddRequest));
                log.info("短链码生成失败,重新生成",eventMessage);
                //迭代生成短链;
                handerAddShortLink(eventMessage);
            }

     }

        return false;

    }
//    扣减流量包
    private boolean reduceTraffic(EventMessage eventMessage, String shortLinkCode) {

        UseTrafficRequest useTrafficRequest = UseTrafficRequest.builder().accountNo(eventMessage.getAccountNo())
                .bizId(shortLinkCode).build();
        JsonData jsonData = trafficFeignService.useTraffic(useTrafficRequest);
        if (jsonData.getCode()!=0){
            log.info("流量包不足,扣减失败",eventMessage);
            return false;
        }
        return true;

    }

    @Override
    public boolean handerUpdateShortLink(EventMessage eventMessage) {
        Long accountNo = eventMessage.getAccountNo();
        String eventMessageType = eventMessage.getEventMessageType();
        ShortLinkUpdateRequest shortLinkUpdateRequest = JsonUtil.json2Obj(eventMessage.getContent(), ShortLinkUpdateRequest.class);
        //校验短链域名;
        DomainDO domainDO = checkDomain(shortLinkUpdateRequest.getDomainType(), shortLinkUpdateRequest.getDomainId(), accountNo);
        if (EventMessageType.SHORT_LINK_UPDATE.name().equalsIgnoreCase(eventMessageType)){
            //todo:(更新域名，短链码和title)参数过多可以封装一个对象;
            //.accountNo(accountNo)防止越权;
            ShortLinkDO shortLinkDO = ShortLinkDO.builder().accountNo(accountNo).code(shortLinkUpdateRequest.getCode())
                    .title(shortLinkUpdateRequest.getTitle())
                    .domain(domainDO.getValue()).build();
            int rows=shortLinkManager.update(shortLinkDO);
            log.info("更新C端短链",rows);
        }else if (EventMessageType.SHORT_LINK_UPDATE_MAPPING.name().equalsIgnoreCase(eventMessageType)){
            //todo:
            GroupCodeMappingDO groupCodeMappingDO = GroupCodeMappingDO.builder().id(shortLinkUpdateRequest.getMappingId())
                    .accountNo(accountNo)
                    .groupId(shortLinkUpdateRequest.getGroupId())
                    .title(shortLinkUpdateRequest.getTitle())
                    .domain(domainDO.getValue()).build();
            int rows= groupCodeMappingManager.update(groupCodeMappingDO);
            log.info("更新B端短链",rows);
        }
        return false;
    }

    @Override
    public boolean handerDelShortLink(EventMessage eventMessage) {
        //消费端处理逻辑;
        Long accountNo = eventMessage.getAccountNo();
        String eventMessageType = eventMessage.getEventMessageType();
        ShortLinkDelRequest shortLinkDelRequest = JsonUtil.json2Obj(eventMessage.getContent(), ShortLinkDelRequest.class);
        if (EventMessageType.SHORT_LINK_DEL.name().equalsIgnoreCase(eventMessageType)){
            ShortLinkDO shortLinkDO = ShortLinkDO.builder().accountNo(accountNo).code(shortLinkDelRequest.getCode()).build();
            int rows = shortLinkManager.del(shortLinkDO);
            log.info("删除c端短链码={}",rows);
        }else if (EventMessageType.SHORT_LINK_DEL_MAPPING.name().equalsIgnoreCase(eventMessageType)){
            GroupCodeMappingDO groupCodeMappingDO = GroupCodeMappingDO.builder().id(Long.valueOf(shortLinkDelRequest.getMappingId()
            )).accountNo(accountNo)
                    .groupId(shortLinkDelRequest.getGroupId()).build();
            int rows = groupCodeMappingManager.del(groupCodeMappingDO);
            log.info("删除c端短链码={}",rows);
        }
        return false;
    }

    @Override
    public Map<String, Object> pageShortLinkByGroupId(ShortLinkPageRequest request) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        Map<String, Object> pageResult = groupCodeMappingManager.pageShortLinkByGroupId(request.getPage(), request.getSize(), accountNo, request.getGroupId());
        return pageResult;
    }

    @Override
    public JsonData del(ShortLinkDelRequest request) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        EventMessage eventMessage = EventMessage.builder()
                .accountNo(accountNo)
                .messageId(IDUtil.geneSnowFlakeID().toString())
                .content(JsonUtil.obj2Json(request))
                .eventMessageType(EventMessageType.SHORT_LINK_DEL.name()).build();
        //todo:
        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(),rabbitMQConfig.getShortLinkDelRoutingKey(),eventMessage);
        return JsonData.buildSuccess();
    }

    @Override
    public JsonData update(ShortLinkUpdateRequest request) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        EventMessage eventMessage = EventMessage.builder()
                .accountNo(accountNo)
                .messageId(IDUtil.geneSnowFlakeID().toString())
                .content(JsonUtil.obj2Json(request))
                .eventMessageType(EventMessageType.SHORT_LINK_UPDATE.name()).build();
        //todo:
        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(), rabbitMQConfig.getShortLinkUpdateRoutingKey(), eventMessage);
        return JsonData.buildSuccess();
    }

    /**
     * @description 校验域名
     *
     * @return
     * @author
     * @date
     */

    private DomainDO checkDomain(String domainType,Long domainId,Long accountNo){
        DomainDO domainDO;
        if (DomainTypeEnum.CUSTOM.name().equals(domainType)){
             domainDO = domainManage.findById(domainId, accountNo);
        }else{
            domainDO = domainManage.findDomainTypeAndId(domainId,DomainTypeEnum.OFFICIAL);
        }
        Assert.notNull(domainDO,"短链域名不合法");
        return domainDO;
    }

    /**
     * @description 校验组名
     *
     * @return
     * @author
     * @date
     */

    private LinkGroupDO checkGroup(Long groupId,Long accountNo) {
        LinkGroupDO linkGroupDO = linkGroupManage.detail(groupId, accountNo);
        Assert.notNull(linkGroupDO,"组名不合法");
        return linkGroupDO;
    }
}
