package com.abc.itbbs.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author LiJunXi
 * @date 2025/12/23
 */
@EnableFeignClients(basePackages = "com.abc.itbbs")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.abc.itbbs")
public class ItbbsResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItbbsResourceApplication.class, args);
    }
}
