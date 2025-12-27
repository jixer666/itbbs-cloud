package com.abc.itbbs.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author LiJunXi
 * @date 2025/12/23
 */
@EnableFeignClients(basePackages = "com.abc.itbbs")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.abc.itbbs")
public class ItbbsBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItbbsBlogApplication.class, args);
    }
}
