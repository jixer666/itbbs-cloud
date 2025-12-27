package com.abc.itbbs.common.security.util;

import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.security.domain.dto.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class SecurityUtils {

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    public static Long getUserId() {
        try {
            return getLoginUser().getUserId();
        } catch (Exception e) {
            log.error("获取用户ID异常：{}", e.getMessage(), e);
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "获取用户ID异常");
        }
    }

    /**
     * 获取用户
     */
    public static LoginUserDTO getLoginUser() {
        try {
            return (LoginUserDTO) getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.error("获取用户信息异常：{}", e.getMessage(), e);
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "获取用户信息异常");
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
