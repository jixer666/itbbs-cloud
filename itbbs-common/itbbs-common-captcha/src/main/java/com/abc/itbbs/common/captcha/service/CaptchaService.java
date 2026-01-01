package com.abc.itbbs.common.captcha.service;

import com.abc.itbbs.common.captcha.domain.vo.CaptchaVO;

public interface CaptchaService {

    CaptchaVO getCaptchaImg(Integer captchaType);

    Boolean checkCaptchaImg(String uuid, String code);

    void invalidCaptcha(String uuid);
}
