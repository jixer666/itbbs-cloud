package com.abc.itbbs.gateway.config;

import com.abc.itbbs.gateway.constants.GatewayConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GatewayCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> allowedOrigins = Arrays.asList(
                GatewayConstants.BLOG_SERVICE,
                GatewayConstants.ORDER_SERVICE,
                GatewayConstants.AUTH_SERVICE,
                GatewayConstants.SYSTEM_SERVICE,
                GatewayConstants.OSS_SERVICE,
                GatewayConstants.ROOT_SERVICE
        );

        // 使用 addAllowedOriginPattern 方法
        allowedOrigins.forEach(config::addAllowedOriginPattern);

        // 允许的方法
        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));

        // 允许的请求头
        config.setAllowedHeaders(Arrays.asList(
                "*"
        ));

        // 暴露的响应头
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Set-Cookie",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));

        // 允许凭证
        config.setAllowCredentials(true);

        // 预检请求缓存时间
        config.setMaxAge(1800L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}