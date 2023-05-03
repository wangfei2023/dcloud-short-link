package net.xdclass.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);

    }
    //一下方法会造成每次前端访问都会创建一个新的simpleClientHttpRequestFactory,所以要进行优化;
//    @Bean
//    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setReadTimeout(10000);
//        factory.setConnectTimeout(10000);
//        return factory;
//    }

    /**
     * @return
     */
    //对RestTemplate进行优化;
    @Bean
    public HttpClient httpClient() {
        //todo:请求方式：
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
   /*
        www.baidu.com请求是每个用户最高访问量是200,
        www.xinlang同时访问最高达500;
         */
        //设置整个连接池最大连接数
        connectionManager.setMaxTotal(500);

        //MaxPerRoute路由是对maxTotal的细分,每个主机的并发，这里route指的是域名(每一个链接数;)

        connectionManager.setDefaultMaxPerRoute(200);
        RequestConfig requestConfig = RequestConfig.custom()
                //返回数据的超时时间
                .setSocketTimeout(20000)
                //连接上服务器的超时时间
                .setConnectTimeout(10000)

                //从连接池中获取连接的超时时间
                .setConnectionRequestTimeout(1000)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }
}