package com.abc.itbbs.common.core.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizCodeEnum {

    SYSTEM_ERROR(10000, "系统异常"),
    VALID_ERROR(10001, "参数校验异常"),
    LOGOUT_SUCCESS(20001, "退出登录成功"),
    BIZ_ERROR(9999, "");

    private Integer code;

    private String msg;
}
