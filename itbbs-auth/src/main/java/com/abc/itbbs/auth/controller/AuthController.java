package com.abc.itbbs.auth.controller;

import com.abc.itbbs.auth.domain.dto.CaptchaDTO;
import com.abc.itbbs.auth.domain.dto.LoginDTO;
import com.abc.itbbs.auth.domain.dto.RegisterDTO;
import com.abc.itbbs.auth.domain.vo.MenuRouterVO;
import com.abc.itbbs.auth.service.AuthService;
import com.abc.itbbs.common.captcha.domain.vo.CaptchaVO;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.email.domain.dto.EmailDTO;
import com.abc.itbbs.common.email.domain.vo.EmailVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public ApiResult<String> login(@RequestBody LoginDTO loginDTO,
                                   HttpServletResponse response) {
        String token = authService.login(loginDTO);

        setLoginCookie(response, token);

        return ApiResult.success(token);
    }

    private void setLoginCookie(HttpServletResponse response, String token) {
        String tokenCookie = String.format(
                "Authorization=%s; Domain=.itbbs.com; Path=/; Max-Age=%d; HttpOnly; %s SameSite=%s",
                token,
                7 * 24 * 60 * 60, // 7天
                "", // 生产环境加Secure
                "Lax"
        );
        String isLoginCookie = String.format(
                "Is-Login=%s; Domain=.itbbs.com; Path=/; Max-Age=%d; %s SameSite=%s",
                1,
                7 * 24 * 60 * 60, // 7天
                "", // 生产环境加Secure
                "Lax"
        );
        response.addHeader("Set-Cookie", tokenCookie);
        response.addHeader("Set-Cookie", isLoginCookie);
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


    @ApiOperation("获取菜单权限")
    @GetMapping("/routes")
    public ApiResult<List<MenuRouterVO>> getMenuRoutes() {
        List<MenuRouterVO> routers = authService.getMenuRoutes();

        return ApiResult.success(routers);
    }

    @ApiOperation("获取白名单菜单权限")
    @GetMapping("/white/routes")
    public ApiResult<List<MenuRouterVO>> getMenuWhiteRoutes() {
        List<MenuRouterVO> routers = authService.getMenuWhiteRoutes();

        return ApiResult.success(routers);
    }
}
