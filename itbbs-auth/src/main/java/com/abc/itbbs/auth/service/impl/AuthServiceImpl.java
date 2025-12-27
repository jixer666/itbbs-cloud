package com.abc.itbbs.auth.service.impl;

import com.abc.itbbs.api.system.MenuServiceClient;
import com.abc.itbbs.api.system.domain.entity.Menu;
import com.abc.itbbs.api.system.domain.enums.MenuTypeEnum;
import com.abc.itbbs.auth.constant.MenuConstants;
import com.abc.itbbs.auth.convert.MenuConvert;
import com.abc.itbbs.auth.domain.dto.CaptchaDTO;
import com.abc.itbbs.auth.domain.dto.LoginDTO;
import com.abc.itbbs.auth.domain.dto.RegisterDTO;
import com.abc.itbbs.auth.domain.vo.MenuRouterVO;
import com.abc.itbbs.auth.factory.LoginStrategyFactory;
import com.abc.itbbs.auth.service.AuthService;
import com.abc.itbbs.auth.strategy.AuthStrategy;
import com.abc.itbbs.common.captcha.domain.vo.CaptchaVO;
import com.abc.itbbs.common.captcha.service.CaptchaService;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.email.domain.dto.EmailDTO;
import com.abc.itbbs.common.email.domain.enums.EmailTypeEnum;
import com.abc.itbbs.common.email.domain.vo.EmailVO;
import com.abc.itbbs.common.email.service.EmailService;
import com.abc.itbbs.common.security.domain.dto.LoginUserDTO;
import com.abc.itbbs.common.security.service.TokenService;
import com.abc.itbbs.common.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MenuServiceClient menuServiceClient;

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

    @Override
    public List<MenuRouterVO> getMenuRoutes() {
        List<Menu> menus = ApiResult.invokeRemoteMethod(menuServiceClient.getMenusByUserId(SecurityUtils.getUserId()));

        return buildMenuRouter(MenuConstants.DEFAULT_MENU_PARENT_ID, menus);
    }

    private List<MenuRouterVO> buildMenuRouter(Long menuId, List<Menu> menus) {
        List<MenuRouterVO> routers = new ArrayList<>();

        List<Menu> filterMenus = menus.stream().filter(item -> item.getParentId().equals(menuId)).sorted(new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o1.getOrderNum() - o2.getOrderNum();
            }
        }).collect(Collectors.toList());

        for (Menu menu : filterMenus) {
            if (!menu.isAnNiu() && menu.getParentId().equals(menuId)) {
                List<MenuRouterVO> children = buildMenuRouter(menu.getMenuId(), menus);
                MenuRouterVO menuRouterVO = MenuConvert.convertToMenuRouterVO(menu, children);
                if (!menu.isFront()) {
                    routers.add(menuRouterVO);
                    continue;
                }

                routers.add(MenuConvert.buildDefaultParentMenuLayout(menuRouterVO));
            }
        }

        return routers;
    }

    @Override
    public List<MenuRouterVO> getMenuWhiteRoutes() {
        List<Menu> menus = ApiResult.invokeRemoteMethod(menuServiceClient.getMenusByMenuType(MenuTypeEnum.FRONT.getType()));

        return buildMenuRouter(MenuConstants.DEFAULT_MENU_PARENT_ID, menus);
    }
}
