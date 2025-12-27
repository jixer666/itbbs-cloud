package com.abc.itbbs.common.email.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.abc.itbbs.common.email.constant.EmailConstants;
import com.abc.itbbs.common.email.domain.dto.EmailDTO;
import com.abc.itbbs.common.email.domain.vo.EmailVO;
import com.abc.itbbs.common.email.factory.EmailStrategyFactory;
import com.abc.itbbs.common.email.service.EmailService;
import com.abc.itbbs.common.email.strategy.EmailStrategy;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public EmailVO sendEmail(EmailDTO emailDTO) {
        emailDTO.checkSendParams();
        EmailStrategy emailStrategy = EmailStrategyFactory.getEmailStrategy(emailDTO.getEmailType());
        fillEmailParams(emailDTO);

        return emailStrategy.sendEmail(emailDTO);
    }

    private static void fillEmailParams(EmailDTO emailDTO) {
        emailDTO.setEmailUuid(RandomUtil.randomString(8));
        emailDTO.setFrom(EmailConstants.FROM_EMAIL);
        emailDTO.setTo(emailDTO.getEmail());
    }

    @Override
    public void invalidEmailCode(String emailUuid) {
        AssertUtils.isNotEmpty(emailUuid, "邮箱验证码不能为空");

        RedisUtils.del(CacheConstants.getFinalKey(CacheConstants.EMAIL_UUID, emailUuid));
    }

    @Override
    public Boolean checkEmailCode(String emailUuid, String emailCode) {
        AssertUtils.isNotEmpty(emailUuid, "邮箱验证码uuid不能为空");
        AssertUtils.isNotEmpty(emailCode, "邮箱验证码不能为空");

        String emailCacheKey = CacheConstants.getFinalKey(CacheConstants.EMAIL_UUID, emailUuid);
        String trueCode = RedisUtils.get(emailCacheKey);
        AssertUtils.isNotEmpty(trueCode, "邮箱验证码已失效");

        return trueCode.equalsIgnoreCase(emailCode);
    }
}