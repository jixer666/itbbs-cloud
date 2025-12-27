package com.abc.itbbs.auth.service.impl;

import com.abc.itbbs.auth.domain.dto.CaptchaDTO;
import com.abc.itbbs.auth.domain.dto.LoginDTO;
import com.abc.itbbs.auth.domain.dto.RegisterDTO;
import com.abc.itbbs.auth.factory.LoginStrategyFactory;
import com.abc.itbbs.auth.service.AuthService;
import com.abc.itbbs.auth.strategy.AuthStrategy;
import com.abc.itbbs.common.captcha.domain.vo.CaptchaVO;
import com.abc.itbbs.common.captcha.service.CaptchaService;
import com.abc.itbbs.common.email.domain.dto.EmailDTO;
import com.abc.itbbs.common.email.domain.enums.EmailTypeEnum;
import com.abc.itbbs.common.email.domain.vo.EmailVO;
import com.abc.itbbs.common.email.service.EmailService;
import com.abc.itbbs.common.security.domain.dto.LoginUserDTO;
import com.abc.itbbs.common.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private EmailService emailService;

    @Override
    public String login(LoginDTO loginDTO) {
        AuthStrategy authStrategy = LoginStrategyFactory.getAuthStrategy(loginDTO.getAuthType());
        LoginUserDTO loginUser = authStrategy.authenticate(loginDTO);
        return tokenService.createToken(loginUser);
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        AuthStrategy authStrategy = LoginStrategyFactory.getAuthStrategy(registerDTO.getAuthType());
        authStrategy.doRegister(registerDTO);
    }

    @Override
    public CaptchaVO getCaptchaImg(CaptchaDTO captchaDTO) {
        captchaDTO.checkCaptchaImgParams();

        return captchaService.getCaptchaImg(captchaDTO.getType());
    }

    @Override
    public EmailVO sendRegisterEmail(EmailDTO emailDTO) {
        emailDTO.checkSendRegisterEmailParams();
        emailDTO.setEmailType(EmailTypeEnum.REGISTER.getType());

        return emailService.sendEmail(emailDTO);
    }



}
