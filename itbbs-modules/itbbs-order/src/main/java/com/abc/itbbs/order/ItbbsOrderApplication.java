package com.abc.itbbs.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author LiJunXi
 * @date 2026/1/19
 */
@EnableScheduling
@EnableFeignClients(basePackages = "com.abc.itbbs")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.abc.itbbs")
public class ItbbsOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItbbsOrderApplication.class, args);
    }
}
