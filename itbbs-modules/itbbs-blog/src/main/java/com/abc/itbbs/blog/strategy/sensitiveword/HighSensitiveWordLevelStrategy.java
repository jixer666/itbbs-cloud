package com.abc.itbbs.blog.strategy.sensitiveword;

import org.springframework.stereotype.Service;

@Service
public class HighSensitiveWordLevelStrategy implements SensitiveWordLevelStrategy {

    @Override
    public void run(String biz, Long bizId) {
        // 直接拦截不做处理
    }
}
