package net.xdclass.config;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
///**
// * @Author: MrWang
// * @Contact: 1850195602@qq.com
// * @Date: 2023/5/6 0006 22:42
// * @Version: 1.0
// * @Description:
// */
//@ConfigurationProperties(prefix = "aliyun")
//@Configuration
//@Data
//@Component
////public class OssConfig {
////    private String endpoint;
////    private String accessKeyId;
////    private String accessKeySecret;
////    private String bucketname;
////}

import com.aliyun.oss.OSSClient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by macro on 2018/5/17.
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@ConfigurationProperties(prefix = "aliyun.oss")
@Configuration
@Data
public class OssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketname;
    private String objectName;
}
