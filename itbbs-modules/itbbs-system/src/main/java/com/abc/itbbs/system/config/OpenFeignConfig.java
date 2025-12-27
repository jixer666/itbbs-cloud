package com.abc.itbbs.system.config;

import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.module.threadlocal.ThreadLocalTempVar;
import com.abc.itbbs.common.security.service.TokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Configuration
public class OpenFeignConfig {

    @Value("${token.header}")
    private String tokenHeader;

    @Value("${system.whiteToken}")
    private String whiteToken;

    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 拿到当前线程的请求头，本质上是一个ThreadLocal
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                // 拿到token
                Object token = ThreadLocalTempVar.getTempTokenVar();
                // 放入自定义头到template请求中
                if (Objects.nonNull(token)) {
                    template.header(tokenHeader, token.toString());
                }
                // 放入feign调用token
                template.header(CommonConstants.FEIGN_TOKEN_HEADER, whiteToken);
            }
        };
    }
}