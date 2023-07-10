/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/10 0010 19:50:25
 * @version 1.0
 */

package net.xdclass.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.exception.BizException;
import net.xdclass.model.EventMessage;
import net.xdclass.service.TrafficService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queuesToDeclare = {
        @Queue("order.traffic.queue")
})
@Slf4j
public class TrafficMQListener {
    @Autowired
    private TrafficService trafficService;

    public void trafficHandle(EventMessage eventMessage, Message message, Channel channel){
        log.info("监听到消息trafficHandle:{}",eventMessage);
        try {
            trafficService.handlerTraffcMessage(eventMessage);
        } catch (Exception e) {
            log.error("消费者失败:{}",eventMessage);
            throw  new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        log.info("消费者消费成功:{}",eventMessage);
    }
}


