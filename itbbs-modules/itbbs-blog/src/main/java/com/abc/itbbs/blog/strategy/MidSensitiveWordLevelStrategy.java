package com.abc.itbbs.blog.strategy;

import org.springframework.stereotype.Service;

@Service
public class MidSensitiveWordLevelStrategy implements SensitiveWordLevelStrategy {

    @Override
    public void run(String biz, Long bizId) {
        // TODO 人工审核
    }
}
