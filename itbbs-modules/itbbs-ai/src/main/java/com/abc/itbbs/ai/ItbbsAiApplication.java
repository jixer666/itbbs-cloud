package com.abc.itbbs.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author LiJunXi
 * @date 2026/1/12
 */
@EnableFeignClients(basePackages = "com.abc.itbbs")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.abc.itbbs")
public class ItbbsAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItbbsAiApplication.class, args);
    }
}
