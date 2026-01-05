package com.abc.itbbs.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author LiJunXi
 * @date 2025/12/23
 */
@EnableScheduling
@EnableFeignClients(basePackages = "com.abc.itbbs")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.abc.itbbs")
public class ItbbsBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItbbsBlogApplication.class, args);
    }
}
