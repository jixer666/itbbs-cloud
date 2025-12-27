package com.abc.itbbs.auth.domain.dto;

import com.abc.itbbs.common.captcha.domain.enums.CaptchaEnum;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

@Data
public class CaptchaDTO {

    private Integer type = CaptchaEnum.CHAR.getType();


    // 生成验证码过程中的参数
    private String uuid;
    private String code;
    private String img;


    public boolean isMathType() {
        return type.equals(CaptchaEnum.MATH.getType());
    }

    public boolean isCharType() {
        return type.equals(CaptchaEnum.CHAR.getType());
    }

    public void checkCaptchaImgParams() {
        AssertUtils.isNotEmpty(this, "参数不能为空");
        AssertUtils.isNotEmpty(type, "类型不能为空");
    }
}
