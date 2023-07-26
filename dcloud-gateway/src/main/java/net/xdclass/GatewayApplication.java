/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/26 0026 19:54:02
 * @version 1.0
 */

package net.xdclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}


