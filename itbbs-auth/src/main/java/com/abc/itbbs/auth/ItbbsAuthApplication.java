package com.abc.itbbs.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author LiJunXi
 * @date 2025/12/23
 */
@EnableFeignClients(basePackages = "com.abc.itbbs")
@EnableDiscoveryClient
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }, scanBasePackages = "com.abc.itbbs")
public class ItbbsAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItbbsAuthApplication.class, args);
    }
}
