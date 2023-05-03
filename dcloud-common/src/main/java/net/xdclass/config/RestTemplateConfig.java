package net.xdclass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/2 0002 22:02
 * @Version: 1.0
 * @Description:RestTemplate的配置;
 * 、*
 *
 * todo:主要是针对客户端发送请求处理;
 *
 */
/*
todo:它是个函数式接口，用于根据URI和HttpMethod创建出一个ClientHttpRequest来发送请求~客户端发送请求;
 */
@Configuration
public class  RestTemplateConfig {
   @Bean
    public RestTemplate  restTemplate(ClientHttpRequestFactory factory){
       return new RestTemplate(factory);

   }
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(10000);
        factory.setConnectTimeout(10000);
        return factory;
    }
}