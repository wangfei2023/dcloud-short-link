package net.xdclass.service.impl;

import net.xdclass.service.NotifyService;
import net.xdclass.utils.JsonData;
import net.xdclass.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public JsonData send() {
//        try {
//            TimeUnit.MICROSECONDS.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://blog.csdn.net/qq_20042935/article/details/122556820", String.class);
        String body = forEntity.getBody();
        //:todo:将body体封装进去
        return JsonData.buildCodeAndMsg(1,body);
    }
}