/**
 * @project dcloud-short-link
 * @description 自定义消息队列配置
 *  发送 关单消息-》延迟exchange-》order.close.delay.queue-》死信exchange-》
 * @author Administrator
 * @date 2023/7/6 0006 21:17:00
 * @version 1.0
 */

package net.xdclass.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
public class RabbitMQConfig {
    /**
     * 交换机
     */
    private String orderEventExchange="order.event.exchange";


    /**
     * 延迟队列, 不能被监听消费
     */
    private String orderCloseDelayQueue="order.close.delay.queue";

    /**
     * 关单队列, 延迟队列的消息过期后转发的队列,被消费者监听
     */
    private String orderCloseQueue="order.close.queue";


    /**
     * 进入延迟队列的路由key
     */
    private String orderCloseDelayRoutingKey="order.close.delay.routing.key";


    /**
     * 进入死信队列的路由key，消息过期进入死信队列的key
     */
    private String orderCloseRoutingKey="order.close.routing.key";

    /**
     * 过期时间 毫秒，临时改为1分钟定时关单
     */
    private Integer ttl=1000*60;

    /**
     * 消息转换器
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 创建交换机 Topic类型，也可以用dirct路由
     * 一般一个微服务一个交换机
     * @return
     */
    @Bean
    public Exchange orderEventExchange(){
        return new TopicExchange(orderEventExchange,true,false);
    }


    /**
     * 延迟队列
     */
    @Bean
    public Queue orderCloseDelayQueue(){

        Map<String,Object> args = new HashMap<>(3);
        args.put("x-dead-letter-exchange",orderEventExchange);
        args.put("x-dead-letter-routing-key",orderCloseRoutingKey);
        args.put("x-message-ttl",ttl);

        return new Queue(orderCloseDelayQueue,true,false,false,args);
    }


    /**
     * 死信队列，普通队列，用于被监听
     */
    @Bean
    public Queue orderCloseQueue(){

        return new Queue(orderCloseQueue,true,false,false);

    }

    /**
     * 第一个队列，即延迟队列的绑定关系建立
     * @return
     */
    @Bean
    public Binding orderCloseDelayBinding(){

        return new Binding(orderCloseDelayQueue,Binding.DestinationType.QUEUE,orderEventExchange,orderCloseDelayRoutingKey,null);
    }

    /**
     * 死信队列绑定关系建立
     * @return
     */
    @Bean
    public Binding orderCloseBinding(){

        return new Binding(orderCloseQueue,Binding.DestinationType.QUEUE,orderEventExchange,orderCloseRoutingKey,null);
    }
    
    //==============================================订单支付成功配置==========================================
    /**
     * @description TODO 
     * 更新订单队列
     * @return 
     * @author 
     * @date  
     */
    /**
     * 更新订单 队列
     */
    private String orderUpdateQueue="order.update.queue";

    /**
     * 根据订单发放流量包 队列
     */
    private String orderTrafficQueue="order.traffic.queue";

    /**
     * 微信回调通知routingKey,【发送消息使用】
     */
    private String orderUpdateTrafficRoutingKey="order.update.traffic.routing.key";

    /**
     * topic类型的binding key，用于绑定队列和交换机，是用于 订单 消费者，更新订单状态
     */
    private String orderUpdateBindingKey="order.update.*.routing.key";

    /**
     * topic类型的binding key，用于绑定队列和交换机，是用于 账号 消费者，发放流量包
     */
    private String orderTrafficBindingKey="order.*.traffic.routing.key";


    /**
     * 订单更新队列和交换机的绑定关系建立
     */
    @Bean
    public Binding orderUpdateBinding(){
        return new Binding(orderUpdateQueue,Binding.DestinationType.QUEUE, orderEventExchange,orderUpdateBindingKey,null);
    }


    /**
     * 发放流量包队列和交换机的绑定关系建立
     */
    @Bean
    public Binding orderTrafficBinding(){
        return new Binding(orderTrafficQueue,Binding.DestinationType.QUEUE, orderEventExchange,orderTrafficBindingKey,null);
    }


    /**
     * 更新订单状态队列 普通队列，用于被监听
     */
    @Bean
    public Queue orderUpdateQueue(){

        return new Queue(orderUpdateQueue,true,false,false);

    }

    /**
     * 发放流量包队列 普通队列，用于被监听
     */
    @Bean
    public Queue orderTrafficQueue(){

        return new Queue(orderTrafficQueue,true,false,false);

    }

}


