package com.abc.itbbs.common.email.service;

import com.abc.itbbs.common.email.domain.dto.EmailDTO;
import com.abc.itbbs.common.email.domain.vo.EmailVO;

public interface EmailService {

    EmailVO sendEmail(EmailDTO emailDTO);

    void invalidEmailCode(String emailUuid);

    Boolean checkEmailCode(java.lang.String emailUuid, java.lang.String emailCode);
}
