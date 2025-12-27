package com.abc.itbbs.auth.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.auth.domain.dto.RegisterDTO;
import com.abc.itbbs.auth.domain.vo.UserVO;
import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.api.system.domain.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class UserConvert {

    public static User convertToUserByRegisterDTO(RegisterDTO registerDTO) {
        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(new BCryptPasswordEncoder().encode(registerDTO.getPassword()))
                .nickname(CommonConstants.DEFAULT_NICKNAME)
                .avatar(CommonConstants.DEFAULT_AVATAR)
                .build();
        user.setUserId(IdUtils.getId());
        user.setCommonParams();

        return user;
    }

    public static UserVO convertToUserVO(User user, List<String> roleKeys) {
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        userVO.setRoles(roleKeys);
        return userVO;
    }
}
