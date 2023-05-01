package net.xdclass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/2 0002 0:17
 * @Version: 1.0
 * @Description:
 */
//mybatis扫描包
@MapperScan("net.xdclass.mapper")
//开启事务支持
@EnableTransactionManagement
//feign远程调用;
@EnableFeignClients
//服务的注册和发现;
@EnableDiscoveryClient
@SpringBootApplication

public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class,args);
    }
}