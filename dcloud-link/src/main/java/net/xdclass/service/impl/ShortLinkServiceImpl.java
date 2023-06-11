package net.xdclass.service.impl;

import com.alibaba.csp.sentinel.util.IdUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.compant.ShortLinkComponent;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.enums.DomainTypeEnum;
import net.xdclass.enums.EventMessageType;
import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manage.DomainManage;
import net.xdclass.manage.GroupCodeMappingManager;
import net.xdclass.manage.LinkGroupManage;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.mapper.LinkGroupMapper;
import net.xdclass.model.*;
import net.xdclass.service.ShortLinkService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.IDUtil;
import net.xdclass.utils.JsonData;
import net.xdclass.utils.JsonUtil;
import net.xdclass.vo.ShortLinkVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    @Override
    public JsonData createShortLink(ShortLinkAddRequest request) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        EventMessage eventMessage = EventMessage.builder().accountNo(accountNo)
                .content(JsonUtil.obj2Json(request))
                .messageId(IDUtil.geneSnowFlakeID().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_ADD.name())
                .build();
        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(),rabbitMQConfig.getShortLinkAddRoutingKey(),eventMessage);
        return JsonData.buildSuccess();
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
        //生成短链
        String shortLinkCode = shortLinkComponent.createShortLinkCode(originalUrlDigest);
        ShortLinkDO ShortLinkCode = shortLinkManager.findByShortLinkCode(shortLinkCode);
        if (ShortLinkCode==null){}

        if (EventMessageType.SHORT_LINK_ADD_LINK.name().equals(eventMessageType)) {
            //c端处理
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
        }else if (EventMessageType.SHORT_LINK_ADD_MAPPING.name().equals(eventMessageType)){
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
        }

        return false;

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
        if (DomainTypeEnum.CUSTOM.name().equals("domainType")){
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
