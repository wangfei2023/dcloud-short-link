package net.xdclass.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/2 0002 23:22
 * @Version: 1.0
 * @Description:
 */
@ConfigurationProperties(value = "sms")
@Configuration
@Data
public class SmsConfig {
    private String templateId;

    private String appCode;

}