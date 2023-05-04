package net.xdclass.component;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.SmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/2 0002 22:01
 * @Version: 1.0
 * @Description:发送短信验证码配置;
 */
@Component
@Slf4j
public class SmsComponent {

    /**
     * 发送地址
     */
    private static final String URL_TEMPLATE = "https://jmsms.market.alicloudapi.com/sms/send?mobile=%s&templateId=%s&value=%s";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SmsConfig smsConfig;


    /**
     * 发送短信验证码
     * @param to
     * @param templateId
     * @param value
     */
    /**
     * 发送短信验证码
     *
     * @param to
     * @param templateId
     * @param value
     */
    @Async("threadPoolTaskExecutor")
    //todo:  value发送短信内容;
    public void send(String to, String templateId, String value) {
        //对请求格式化
        String url = String.format(URL_TEMPLATE, to, templateId, value);
        HttpHeaders headers = new HttpHeaders();
        //todo:请求头有鉴权认证;smsConfig.getAppCode()自己的app-code;
        headers.set("Authorization", "APPCODE " + smsConfig.getAppCode());
        //获取头部内容;
        HttpEntity entity = new HttpEntity<>(headers);
       // ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
      //  log.info("url={},body={}", url, response.getBody());
//        if (response.getStatusCode().is2xxSuccessful()) {
//            log.info("发送短信验证码成功");
//        } else {
//            log.error("发送短信验证码失败:{}", response.getBody());
//        }
    }
}