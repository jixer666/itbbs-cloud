package com.abc.itbbs.common.core.domain.vo;

import com.abc.itbbs.common.core.constant.HttpStatus;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private T data;

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(HttpStatus.SUCCESS, null, data);
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<>(HttpStatus.SUCCESS, null, null);
    }

    public static <T> ApiResult<T> fail(String msg) {
        return new ApiResult<>(HttpStatus.ERROR, msg, null);
    }

    public static <T> ApiResult<T> fail(Integer code, String msg) {
        return new ApiResult<>(code, msg, null);
    }

    public static <T> ApiResult<T> fail(BizCodeEnum bizCodeEnum) {
        return new ApiResult<>(bizCodeEnum.getCode(), bizCodeEnum.getMsg(),null);
    }

    public static <T> T invokeRemoteMethod(ApiResult<T> apiResult) {
        AssertUtils.isNotEmpty(apiResult, "返回参数不能为空");
        AssertUtils.isTrue(HttpStatus.SUCCESS == apiResult.getCode(), apiResult.getMsg());

        return apiResult.getData();
    }
}
