package net.xdclass.service.impl;

import com.alibaba.csp.sentinel.util.IdUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.enums.EventMessageType;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.model.EventMessage;
import net.xdclass.model.ShortLinkDO;
import net.xdclass.service.ShortLinkService;
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
}
