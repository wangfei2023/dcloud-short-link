package net.xdclass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/3 0003 14:18
 * @Version: 1.0
 * @Description:自定义线程ThreadPoolTaskExecutor
 */
@Configuration
//开启异步调用;
@EnableAsync
public class ThreadPoolTaskConfig {
    @Bean("threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //维护线程的最少数量,即使没有执行任务,也会一直存活;
        //如果设置allowCoreThreadTimeout=true（默认false）时，核心线程会超时关闭
        executor.setCorePoolSize(16);
        //最大线程池数量，当线程数>=corePoolSize，且任务队列已满时。线程池会创建新线程来处理任务
        //当线程数=maxPoolSize，且任务队列已满时，线程池会拒绝处理任务而抛出异常
        executor.setMaxPoolSize(64);
        //缓存队列（阻塞队列）当核心线程数达到最大时，新任务会放在队列中排队等待执行
        //todo:当请求量大于最大线程池数量;
        //缓存队列（阻塞队列）当核心线程数达到最大时，新任务会放在队列中排队等待执行
        executor.setQueueCapacity(124);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("短链平台自定义线程");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}