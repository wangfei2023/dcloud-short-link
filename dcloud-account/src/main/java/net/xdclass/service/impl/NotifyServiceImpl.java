package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.component.SmsComponent;
import net.xdclass.config.SmsConfig;
import net.xdclass.constant.RedisKey;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.service.NotifyService;
import net.xdclass.utils.CheckUtil;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JsonData;
import net.xdclass.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
@Slf4j
public class NotifyServiceImpl implements NotifyService {
    //todo：验证码10min有效;
    private static final int EXPERTIME=1000*60*10;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SmsComponent smsComponent;
    @Autowired
    private SmsConfig smsConfig;
    @Autowired
   private StringRedisTemplate redisTemplate;
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

    @Override
    public JsonData sendCode(SendCodeEnum userRegister, String to) {
        String catchKey = String.format(RedisKey.CHECK_CODE_KEY, userRegister.name(), to);
        String catchValue = redisTemplate.opsForValue().get(catchKey);
        //判断缓存里面是否存在验证码;
        if (StringUtils.isNoneBlank(catchValue)){
            long ttl = Long.parseLong(catchKey.split("_")[1]);
            if (CommonUtil.getCurrentTimestamp()-ttl<60){
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }
        String code = CommonUtil.getRandomCode(6);
        long timestamp = CommonUtil.getCurrentTimestamp();
         String value=code+"_"+timestamp;
         redisTemplate.opsForValue().set(catchKey,value,EXPERTIME,TimeUnit.MILLISECONDS);
        //todo:发送邮箱验证码
        if (CheckUtil.isEmail(to)){

         //todo:发送邮箱验证码
        }else if (CheckUtil.isPhone(to)){
        //todo:发送手机验证码
            smsComponent.send(to,smsConfig.getTemplateId(),CommonUtil.getRandomCode(6));
        }
        return null;
    }
}