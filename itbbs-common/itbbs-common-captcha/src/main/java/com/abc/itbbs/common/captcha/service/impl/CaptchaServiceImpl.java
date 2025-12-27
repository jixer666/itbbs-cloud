package com.abc.itbbs.common.captcha.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.abc.itbbs.common.captcha.convert.CaptchaConvert;
import com.abc.itbbs.common.captcha.domain.dto.CaptchaDTO;
import com.abc.itbbs.common.captcha.domain.vo.CaptchaVO;
import com.abc.itbbs.common.captcha.service.CaptchaService;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Override
    public CaptchaVO getCaptchaImg(Integer captchaType) {
        AssertUtils.isNotEmpty(captchaType, "图像类型不能为空");

        CaptchaDTO captchaDTO = new CaptchaDTO();
        captchaDTO.setUuid(IdUtil.simpleUUID());

        buildCaptchaImg(captchaDTO);
        validCaptcha(captchaDTO);

        return CaptchaConvert.convertCaptchaVOByCaptchaDTO(captchaDTO);
    }

    @Override
    public Boolean checkCaptchaImg(String uuid, String code) {
        AssertUtils.isNotEmpty(uuid, "图像验证码uuid不能为空");
        AssertUtils.isNotEmpty(code, "图像验证码不能为空");

        String captchaCacheKey = CacheConstants.getFinalKey(CacheConstants.CAPTCHA_UUID, uuid);
        String trueCode = RedisUtils.get(captchaCacheKey);
        AssertUtils.isNotEmpty(trueCode, "验证码已失效，请刷新页面重试");

        return trueCode.equalsIgnoreCase(code);
    }

    private void buildCaptchaImg(CaptchaDTO captchaDTO) {
        String code = null;
        BufferedImage imageBuffer = null;
        if (captchaDTO.isMathType()) {
            String capText = captchaProducerMath.createText();
            String capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            imageBuffer = captchaProducerMath.createImage(capStr);
        } else if (captchaDTO.isCharType()) {
            String capStr = captchaProducer.createText();
            code = capStr;
            imageBuffer = captchaProducer.createImage(capStr);
        } else {
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "无效验证码类型");
        }

        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream();
        try {
            ImageIO.write(imageBuffer, "jpg", fastByteArrayOutputStream);
        } catch (IOException e) {
            log.error("图形验证码转换出错：{}", e.getMessage());
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "图形验证码转换出错");
        }

        captchaDTO.setCode(code);
        captchaDTO.setImg(Base64.encode(fastByteArrayOutputStream.toByteArray()));
    }

    private void validCaptcha(CaptchaDTO captchaDTO) {
        String captchaCacheKey = CacheConstants.getFinalKey(CacheConstants.CAPTCHA_UUID, captchaDTO.getUuid());
        RedisUtils.set(captchaCacheKey, captchaDTO.getCode(), CacheConstants.CAPTCHA_UUID_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    @Override
    public void invalidCaptcha(String uuid) {
        AssertUtils.isNotEmpty(uuid, "图像验证码uuid不能为空");
        String captchaCacheKey = CacheConstants.getFinalKey(CacheConstants.CAPTCHA_UUID, uuid);
        RedisUtils.del(captchaCacheKey);
    }
}
