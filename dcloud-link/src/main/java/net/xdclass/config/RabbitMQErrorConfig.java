package net.xdclass.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @project dcloud-short-link
 * @description 消息消费确认使用自动确认方式
 * @author Administrator
 * @date 2023/6/7 0007 22:30:41
 * @version 1.0
 */

@Configuration
@Data
public class RabbitMQErrorConfig {
    private String shortLinkErrorExchange = "short_link.error.exchange";

    private String shortLinkErrorQueue = "short_link.error.queue";

    private String shortLinkErrorRoutingKey = "short_link.error.routing.key";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 异常交换机
     * @return
     */
    @Bean
    public TopicExchange errorTopicExchange(){
        return new TopicExchange(shortLinkErrorExchange,true,false);
    }

    /**
     * 异常队列
     * @return
     */
    @Bean
    public Queue errorQueue(){
        return new Queue(shortLinkErrorQueue,true);
    }

    /**
     * 队列与交换机进行绑定
     * @return
     */
    @Bean
    public Binding BindingErrorQueueAndExchange(Queue errorQueue, TopicExchange errorTopicExchange){
        return BindingBuilder.bind(errorQueue).to(errorTopicExchange).with(shortLinkErrorRoutingKey);
    }


    /**
     * 配置 RepublishMessageRecoverer
     * 用途：消息重试一定次数后，用特定的routingKey转发到指定的交换机中，方便后续排查和告警
     *
     * 顶层是 MessageRecoverer接口，多个实现类
     *
     * @return
     */
    @Bean
    public MessageRecoverer messageRecoverer(){
        return new RepublishMessageRecoverer(rabbitTemplate,shortLinkErrorExchange,shortLinkErrorRoutingKey);
    }
}


