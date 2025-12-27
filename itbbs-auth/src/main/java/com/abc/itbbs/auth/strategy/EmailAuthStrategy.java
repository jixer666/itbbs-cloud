package com.abc.itbbs.auth.strategy;

import com.abc.itbbs.api.system.UserServiceClient;
import com.abc.itbbs.auth.domain.dto.LoginDTO;
import com.abc.itbbs.auth.domain.dto.RegisterDTO;
import com.abc.itbbs.common.email.service.EmailService;
import com.abc.itbbs.common.security.domain.dto.LoginUserDTO;
import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailAuthStrategy implements AuthStrategy {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private EmailService emailService;

    @Override
    public LoginUserDTO authenticate(LoginDTO loginDTO) {
        return null;
    }

    @Override
    public User doRegister(RegisterDTO registerDTO) {
        preRegisterCheck(registerDTO);

        // todo 邮箱注册
        return null;
    }

    public void preRegisterCheck(RegisterDTO registerDTO) {
        registerDTO.checkEmailParams();
        User user = userServiceClient.getUserByEmail(registerDTO.getEmail());
        AssertUtils.isEmpty(user, "邮箱已被绑定");

        Boolean checkEmailCode = emailService.checkEmailCode(registerDTO.getEmailUuid(), registerDTO.getEmailCode());
        AssertUtils.isTrue(checkEmailCode, "邮箱验证码错误");
    }
}
