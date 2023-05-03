package net.xdclass.service.impl;

import net.xdclass.service.NotifyService;
import net.xdclass.utils.JsonData;
import net.xdclass.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/3 0003 11:26
 * @Version: 1.0
 * @Description:
 */
@Service
public class NotifyServiceImpl implements NotifyService {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    //todo:开启异步调用;
   // @Async
    // @Async造成失效：
    /*
    注解@Async的方法不是public方法
注解@Async的返回值只能为void或者Future
注解@Async方法使用static修饰也会失效
spring无法扫描到异步类，没加注解@Async 或 @EnableAsync注解
     */
    public void send() {
        String response =null;
        try {
            TimeUnit.MICROSECONDS.sleep(20000);
            ResponseEntity<String> forEntity = restTemplate.getForEntity("https://blog.csdn.net/qq_20042935/article/details/122556820", String.class);
             response = forEntity.getBody();
            //:todo:将body体封装进去

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}