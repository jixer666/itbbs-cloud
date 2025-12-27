package com.abc.itbbs.common.email.strategy;

import cn.hutool.extra.spring.SpringUtil;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.email.domain.dto.EmailDTO;
import com.abc.itbbs.common.email.domain.vo.EmailVO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

public interface EmailStrategy {

    EmailVO sendEmail(EmailDTO emailDTO);

    String getTitle(Map<String, String> params);

    String getContent(Map<String, String> params);

    default void doSend(JavaMailSender mailSender, EmailDTO emailDTO) {
        SpringUtil.getBean(ThreadPoolTaskExecutor.class).submit(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(emailDTO.getFrom());
                helper.setTo(emailDTO.getTo());
                helper.setSubject(getTitle(emailDTO.getDetailsMap()));
                helper.setText(getContent(emailDTO.getDetailsMap()), true);
                mailSender.send(message);
            } catch (MessagingException e) {
                throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
            }
        });
    }

}
