package com.abc.itbbs.system.service;

import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.system.domain.dto.UserDTO;
import com.abc.itbbs.system.domain.dto.UserResetPwdDTO;
import com.abc.itbbs.system.domain.vo.UserRoleVO;
import com.abc.itbbs.api.system.domain.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    User getUserById(Long userId);

    User getUserByUsername(String username);

    void saveUser(User user);

    UserVO getUserInfoWithUiParam(Long userId);

    PageResult getUserPageWithUiParam(UserDTO userDTO);

    UserRoleVO getUserRole(Long userId);

    void saveUserRole(UserDTO userDTO);

    void resetPassword(UserResetPwdDTO userResetPwdDTO);

    void updateUser(UserDTO userDTO);

    User getUserByEmail(String email);

    Map<Long, User> getUserMapByUserIds(List<Long> userIds);

    UserVO getUserInfo(Long userId);
}
