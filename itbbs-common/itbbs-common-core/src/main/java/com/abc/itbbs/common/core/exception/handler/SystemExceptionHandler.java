package com.abc.itbbs.common.core.exception.handler;

import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.net.BindException;

@Slf4j
@RestControllerAdvice
public class SystemExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    public ApiResult<Void> handleGlobalException(GlobalException e) {
        log.error("自定义出现异常: {}, ", e.getMessage(), e);
        return ApiResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = BindException.class)
    public ApiResult<Void> handleBindException(BindException e) {
        log.error("校验出现异常: {}, ", e.getMessage(), e);
        return ApiResult.fail(BizCodeEnum.VALID_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResult<Void> handleException(Exception e) {
        log.error("系统出现异常: {}, ", e.getMessage(), e);
        return ApiResult.fail(BizCodeEnum.SYSTEM_ERROR.getCode(), BizCodeEnum.SYSTEM_ERROR.getMsg());
    }

}
