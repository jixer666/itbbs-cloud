package com.abc.itbbs.common.core.exception;

import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private Integer code;

    private String message;

    public GlobalException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public GlobalException(BizCodeEnum bizCodeEnum) {
        super(bizCodeEnum.getMsg());
        this.code = bizCodeEnum.getCode();
        this.message = bizCodeEnum.getMsg();
    }
}