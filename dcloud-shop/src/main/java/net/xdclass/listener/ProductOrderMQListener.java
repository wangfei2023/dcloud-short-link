/**
 * @project dcloud-short-link
 * @description 消费者监听（超时关闭订单）
 * @author Administrator
 * @date 2023/7/6 0006 22:56:39
 * @version 1.0
 */

package net.xdclass.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.exception.BizException;
import net.xdclass.model.EventMessage;
import net.xdclass.service.ProductOrderService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RabbitListener(queuesToDeclare = {@Queue("order.close.queue")})
public class ProductOrderMQListener {
    @Autowired
    private ProductOrderService productOrderService;

    @RabbitHandler
    public void productOrderHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        log.info("监听到消息ProductOrderMQListener message消息内容:{}", message);
        try {
            //TODO 处理业务逻辑
            productOrderService.closeProductOrder(eventMessage);

        } catch (Exception e) {

            //处理业务异常，还有进行其他操作，比如记录失败原因
            log.error("消费失败:{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        log.info("消费成功:{}", eventMessage);


    }


}


