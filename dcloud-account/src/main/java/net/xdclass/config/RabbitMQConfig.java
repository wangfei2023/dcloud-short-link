/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/10 0010 18:43:37
 * @version 1.0
 */

package net.xdclass.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMQConfig {
    /**
     * @description TODO 
     * 消息转换器;       
     * @return 
     * @author 
     * @date  
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();

    }
}


