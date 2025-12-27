package com.abc.itbbs.common.email.domain.dto;

import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.core.util.StringUtils;
import com.abc.itbbs.common.email.domain.enums.EmailTypeEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class EmailDTO {

    private String email;

    // 生成验证码过程中的参数
    private Integer emailType;
    private String emailUuid;
    private String emailCode;

    // 发送邮件设置参数
    private String from;
    private String to;
    private Map<String, String> detailsMap = new HashMap<>();

    public void checkSendParams() {
        if (StringUtils.isNull(email)){
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "邮箱不能为空");
        }
        if (Objects.isNull(emailType)){
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "邮箱类型不能为空");
        }
    }

    public void checkSendRegisterEmailParams() {
        checkSendParams();
        if (!EmailTypeEnum.REGISTER.getType().equals(emailType)){
            throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), "邮箱类型不匹配");
        }
    }
}
