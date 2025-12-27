package com.abc.itbbs.auth.strategy;

import com.abc.itbbs.auth.domain.dto.LoginDTO;
import com.abc.itbbs.auth.domain.dto.RegisterDTO;
import com.abc.itbbs.common.security.domain.dto.LoginUserDTO;
import com.abc.itbbs.api.system.domain.entity.User;

public interface AuthStrategy {

    LoginUserDTO authenticate(LoginDTO loginDTO);

    User doRegister(RegisterDTO registerDTO);

}
