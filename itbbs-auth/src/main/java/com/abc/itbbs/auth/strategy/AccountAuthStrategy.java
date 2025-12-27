package com.abc.itbbs.auth.strategy;

import com.abc.itbbs.api.system.UserServiceClient;
import com.abc.itbbs.auth.convert.UserConvert;
import com.abc.itbbs.auth.domain.dto.LoginDTO;
import com.abc.itbbs.auth.domain.dto.RegisterDTO;
import com.abc.itbbs.common.captcha.service.CaptchaService;
import com.abc.itbbs.common.security.context.SecurityAuthContext;
import com.abc.itbbs.common.security.domain.dto.LoginUserDTO;
import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AccountAuthStrategy implements AuthStrategy {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private CaptchaService captchaService;

    @Override
    public LoginUserDTO authenticate(LoginDTO loginDTO) {
        preLoginCheck(loginDTO);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        SecurityAuthContext.setContext(authToken);
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityAuthContext.removeContext();

        return (LoginUserDTO) authentication.getPrincipal();
    }

    public void preLoginCheck(LoginDTO loginDTO) {
        loginDTO.checkParams();
    }

    @Override
    public User doRegister(RegisterDTO registerDTO) {
        preRegisterCheck(registerDTO);
        User user = UserConvert.convertToUserByRegisterDTO(registerDTO);
        userServiceClient.saveUser(user);
        afterRegister(registerDTO);

        return user;
    }

    public void preRegisterCheck(RegisterDTO registerDTO) {
        registerDTO.checkAccountParams();
        User user = userServiceClient.getUserByUsername(registerDTO.getUsername());
        AssertUtils.isEmpty(user, "用户已存在");

        Boolean checkCaptcha = captchaService.checkCaptchaImg(registerDTO.getUuid(), registerDTO.getCode());
        AssertUtils.isTrue(checkCaptcha, "验证码错误");
    }

    private void afterRegister(RegisterDTO registerDTO) {
        captchaService.invalidCaptcha(registerDTO.getUuid());
    }
}
