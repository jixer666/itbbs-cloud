package com.abc.itbbs.common.captcha.convert;

import com.abc.itbbs.common.captcha.domain.dto.CaptchaDTO;
import com.abc.itbbs.common.captcha.domain.vo.CaptchaVO;

public class CaptchaConvert {
    public static CaptchaVO convertCaptchaVOByCaptchaDTO(CaptchaDTO captchaDTO) {
        return CaptchaVO.builder()
                .img(captchaDTO.getImg())
                .uuid(captchaDTO.getUuid())
                .build();
    }
}
