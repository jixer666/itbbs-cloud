package com.abc.itbbs.common.core.config;

import com.abc.itbbs.common.core.constant.ServerConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 设置访问源地址
        config.addAllowedOriginPattern(ServerConstants.ROOT_SERVICE);
        config.addAllowedOriginPattern(ServerConstants.BLOG_SERVICE);
        config.addAllowedOriginPattern(ServerConstants.SYSTEM_SERVICE);
        config.addAllowedOriginPattern(ServerConstants.AUTH_SERVICE);
        config.addAllowedOriginPattern(ServerConstants.OSS_SERVICE);
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 允许携带Cookie
        config.setAllowCredentials(true);
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

}
