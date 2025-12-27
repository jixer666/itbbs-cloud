package com.abc.itbbs.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author LiJunXi
 * @date 2025/12/23
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ItbbsGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItbbsGatewayApplication.class, args);
    }
}
