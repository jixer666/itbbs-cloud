package com.abc.itbbs.auth.service;

import com.abc.itbbs.auth.domain.dto.CaptchaDTO;
import com.abc.itbbs.auth.domain.dto.LoginDTO;
import com.abc.itbbs.auth.domain.dto.RegisterDTO;
import com.abc.itbbs.auth.domain.vo.MenuRouterVO;
import com.abc.itbbs.common.captcha.domain.vo.CaptchaVO;
import com.abc.itbbs.common.email.domain.dto.EmailDTO;
import com.abc.itbbs.common.email.domain.vo.EmailVO;

import java.util.List;

public interface AuthService {

    String login(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);

    CaptchaVO getCaptchaImg(CaptchaDTO captchaDTO);

    EmailVO sendRegisterEmail(EmailDTO emailDTO);

    List<MenuRouterVO> getMenuRoutes();

    List<MenuRouterVO> getMenuWhiteRoutes();
}
