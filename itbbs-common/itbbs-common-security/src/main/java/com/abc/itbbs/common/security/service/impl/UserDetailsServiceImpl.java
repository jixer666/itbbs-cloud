package com.abc.itbbs.common.security.service.impl;

import com.abc.itbbs.api.system.MenuServiceClient;
import com.abc.itbbs.api.system.RoleServiceClient;
import com.abc.itbbs.api.system.UserServiceClient;
import com.abc.itbbs.api.system.domain.entity.Menu;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.common.security.context.SecurityAuthContext;
import com.abc.itbbs.common.security.domain.dto.LoginUserDTO;
import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.common.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private RoleServiceClient roleServiceClient;

    @Autowired
    private MenuServiceClient menuServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = ApiResult.invokeRemoteMethod(userServiceClient.getUserByUsername(username));
        AssertUtils.isNotEmpty(user, "用户不存在");
        AssertUtils.isTrue(SecurityUtils.matchesPassword(SecurityAuthContext.getContext().getCredentials().toString(),
                user.getPassword()), "账号或者密码错误");

        List<String> roles = ApiResult.invokeRemoteMethod(roleServiceClient.getRoleKeysByUserId(user.getUserId()));
        Set<String> perms = menuServiceClient.getMenusByUserId(user.getUserId())
                .stream()
                .map(Menu::getPerms)
                .filter(item -> !Objects.isNull(item))
                .collect(Collectors.toSet());

        return new LoginUserDTO(user, perms, new HashSet<>(roles));
    }
}
