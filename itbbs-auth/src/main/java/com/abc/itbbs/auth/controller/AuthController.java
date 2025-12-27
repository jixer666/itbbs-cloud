package com.abc.itbbs.auth.controller;

import com.abc.itbbs.auth.domain.dto.CaptchaDTO;
import com.abc.itbbs.auth.domain.dto.LoginDTO;
import com.abc.itbbs.auth.domain.dto.RegisterDTO;
import com.abc.itbbs.auth.service.AuthService;
import com.abc.itbbs.common.captcha.domain.vo.CaptchaVO;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.email.domain.dto.EmailDTO;
import com.abc.itbbs.common.email.domain.vo.EmailVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public ApiResult<String> login(@RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO);

        return ApiResult.success(token);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public ApiResult<Void> register(@RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);

        return ApiResult.success();
    }

    @ApiOperation("获取图形验证码")
    @GetMapping("/captcha")
    public ApiResult<CaptchaVO> getCaptchaImg(CaptchaDTO captchaDTO) {
        CaptchaVO captchaVO = authService.getCaptchaImg(captchaDTO);

        return ApiResult.success(captchaVO);
    }

    @ApiOperation("发送注册邮件")
    @PostMapping("/sendRegisterEmail")
    public ApiResult<EmailVO> sendRegisterEmail(@RequestBody EmailDTO emailDTO) {
        EmailVO emailRegisterVO = authService.sendRegisterEmail(emailDTO);

        return ApiResult.success(emailRegisterVO);
    }
}
