package com.abc.itbbs.common.email.convert;


import com.abc.itbbs.common.email.constant.EmailConstants;
import com.abc.itbbs.common.email.domain.dto.EmailDTO;
import com.abc.itbbs.common.email.domain.vo.EmailVO;

public class EmailConvert {

    public static EmailVO buildEmailVoByEmailDTO(EmailDTO emailDTO) {
        EmailVO emailVO = new EmailVO();
        emailVO.setEmailUuid(emailDTO.getEmailUuid());
        emailVO.setEmail(emailDTO.getDetailsMap().get(EmailConstants.EMAIL_CODE));

        return emailVO;
    }
}
