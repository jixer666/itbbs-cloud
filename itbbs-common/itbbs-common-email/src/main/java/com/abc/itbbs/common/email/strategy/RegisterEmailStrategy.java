package com.abc.itbbs.common.email.strategy;

import cn.hutool.core.util.RandomUtil;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.email.constant.EmailConstants;
import com.abc.itbbs.common.email.convert.EmailConvert;
import com.abc.itbbs.common.email.domain.dto.EmailDTO;
import com.abc.itbbs.common.email.domain.vo.EmailVO;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RegisterEmailStrategy implements EmailStrategy {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public EmailVO sendEmail(EmailDTO emailDTO) {
        saveRegisterCode(emailDTO);

        try {
            doSend(mailSender, emailDTO);
        } catch (Exception e) {
            log.error("发送注册邮件出错，{}", e.getMessage(), e);
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "发送注册邮件出错");
        }

        return EmailConvert.buildEmailVoByEmailDTO(emailDTO);
    }

    private void saveRegisterCode(EmailDTO emailDTO) {
        String emailCacheKey = CacheConstants.getFinalKey(CacheConstants.EMAIL_UUID, emailDTO.getEmailUuid());
        String emailCode = RandomUtil.randomNumbers(6);

        emailDTO.getDetailsMap().put(EmailConstants.EMAIL_CODE, emailCode);

        RedisUtils.set(emailCacheKey, emailCode, CacheConstants.EMAIL_UUID_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    @Override
    public String getTitle(Map<String, String> params) {
        return EmailConstants.REGISTER_TITLE;
    }

    @Override
    public String getContent(Map<String, String> params) {
        return String.format(EmailConstants.REGISTER_CONTENT, params.get(EmailConstants.EMAIL_CODE));
    }
}
