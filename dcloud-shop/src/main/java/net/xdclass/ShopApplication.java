/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/6/27 0027 21:00:57
 * @version 1.0
 */

package net.xdclass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("net.xdclass.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class ShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class,args);
    }
}


