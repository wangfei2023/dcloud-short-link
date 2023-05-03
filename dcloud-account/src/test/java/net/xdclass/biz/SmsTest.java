package net.xdclass.biz;

import net.xdclass.AccountApplication;
import net.xdclass.component.SmsComponent;
import net.xdclass.config.SmsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/3 0003 10:17
 * @Version: 1.0
 * @Description:發送短信验证码测试;
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
public class SmsTest {
    @Autowired
    private SmsComponent smsComponent;
    @Autowired
    private SmsConfig config;
    @Test
    public void send(){
        smsComponent.send("17821088566",config.getTemplateId(),"666888");
    }
}