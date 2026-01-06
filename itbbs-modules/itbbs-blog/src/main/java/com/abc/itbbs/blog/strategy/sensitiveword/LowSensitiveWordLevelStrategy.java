package com.abc.itbbs.blog.strategy.sensitiveword;

import org.springframework.stereotype.Service;

@Service
public class LowSensitiveWordLevelStrategy implements SensitiveWordLevelStrategy {

    @Override
    public void run(String biz, Long bizId) {
        // TODO 发邮件提醒，记录提醒次数，达到提醒阈值就走人工审核
    }
}
