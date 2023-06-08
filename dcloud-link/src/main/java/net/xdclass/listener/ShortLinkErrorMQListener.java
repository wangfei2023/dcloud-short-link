package net.xdclass.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.EventMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @project dcloud-short-link
 * @description 监听异常信息
 * @author Administrator
 * @date 2023/6/8 0008 21:35:41
 * @version 1.0
 */
@Component
@Slf4j
@RabbitListener(queues = "short_link.error.queue")
public class ShortLinkErrorMQListener {
    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        log.info("告警：监听消息ShortLinkMQListener message消息内容",eventMessage);
        log.info("告警：message消息内容",message);
        log.info("发送成功");
    }
}



