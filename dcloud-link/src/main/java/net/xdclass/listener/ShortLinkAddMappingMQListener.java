package net.xdclass.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.EventMessageType;
import net.xdclass.exception.BizException;
import net.xdclass.model.EventMessage;
import net.xdclass.service.ShortLinkService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

//用于b端查询;
@Component
@Slf4j
@RabbitListener(queuesToDeclare = {@Queue("short_link.add.mapping.queue")})
public class ShortLinkAddMappingMQListener {
    @Autowired
    private ShortLinkService shortLinkService;
    /**
     * @param eventMessage
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        log.info("监听到消息ShortLinkAddMappingMQListener：message消息内容：{}", message);
        long msgTag = message.getMessageProperties().getDeliveryTag();

        try {
         //todo:制造异常;
        //   int i =10/0;
            //todo:处理业务逻辑;
            //(1)SHORT_LINK_ADD_MAPPING处理
            eventMessage.setEventMessageType(EventMessageType.SHORT_LINK_ADD_MAPPING.name());
            shortLinkService.handerAddShortLink(eventMessage);
        } catch (Exception e) {
            // 处理业务失败，还要进行其他操作，比如记录失败原因
            log.error("消费失败{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        log.info("消费成功{}", eventMessage);
        //确认消息消费成功(手动确认,现在要改为自动确认)
      ///  channel.basicAck(msgTag, false);
    }
}
