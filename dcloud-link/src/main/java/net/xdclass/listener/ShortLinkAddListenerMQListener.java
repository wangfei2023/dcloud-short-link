package net.xdclass.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.EventMessageType;
import net.xdclass.exception.BizException;
import net.xdclass.model.EventMessage;
import net.xdclass.service.ShortLinkService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
//用于c端解析;
//rabbitmq是属于懒加载模式，需要@Bean注解
@Component
@Slf4j
@RabbitListener(queues = "short_link.add.link.queue")
public class ShortLinkAddListenerMQListener {
    @Autowired
    private ShortLinkService shortLinkService;
    /**
     *
     *
     * @param eventMessage
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {

        log.info("监听到消息ShortLinkAddLinkMQListener：message消息内容：{}", message);
        //监听更多的消息;
        long msgTag = message.getMessageProperties().getDeliveryTag();

        try {
            //TODO 处理业务
            eventMessage.setEventMessageType(EventMessageType.SHORT_LINK_ADD_LINK.name());
            shortLinkService.handerAddShortLink(eventMessage);
        } catch (Exception e) {
            // 处理业务失败，还要进行其他操作，比如记录失败原因
            log.error("消费失败{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        log.info("消费成功{}", eventMessage);
        //确认消息消费成功
     //   channel.basicAck(msgTag, false);
    }
}
