package com.abc.itbbs.system.controller;

import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.common.core.annotation.Permission;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.security.util.SecurityUtils;
import com.abc.itbbs.system.domain.dto.UserDTO;
import com.abc.itbbs.system.domain.dto.UserResetPwdDTO;
import com.abc.itbbs.system.domain.vo.UserRoleVO;
import com.abc.itbbs.system.domain.vo.UserVO;
import com.abc.itbbs.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Permission(value = "system:user:query")
    @ApiOperation("查询用户信息")
    @GetMapping("/info")
    public ApiResult<UserVO> getUserInfo() {
        UserVO userVO = userService.getUserInfoWithUiParam(SecurityUtils.getUserId());

        return ApiResult.success(userVO);
    }

    @Permission(value = "system:user:list")
    @ApiOperation("查询用户分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getUserPage(UserDTO userDTO) {
        PageResult pageResult = userService.getUserPageWithUiParam(userDTO);

        return ApiResult.success(pageResult);
    }

    @Permission(value = "system:user:query")
    @ApiOperation("查询用户角色")
    @GetMapping("/getUserRole/{userId}")
    public ApiResult<UserRoleVO> getUserRole(@PathVariable Long userId) {
        UserRoleVO userRole = userService.getUserRole(userId);

        return ApiResult.success(userRole);
    }

    @Permission(value = "system:user:add")
    @ApiOperation("分配用户角色")
    @PostMapping("/saveUserRole")
    public ApiResult<Void> saveUserRole(@RequestBody UserDTO userDTO) {
        userService.saveUserRole(userDTO);

        return ApiResult.success();
    }

    @Permission(value = "system:user:edit")
    @ApiOperation("重置用户密码")
    @PutMapping("/resetPassword")
    public ApiResult<Void> resetPassword(@RequestBody UserResetPwdDTO userResetPwdDTO) {
        userService.resetPassword(userResetPwdDTO);

        return ApiResult.success();
    }

    @Permission(value = "system:user:edit")
    @ApiOperation("更新用户")
    @PutMapping
    public ApiResult<Void> updateUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);

        return ApiResult.success();
    }


    @ApiOperation("远程保存用户")
    @PostMapping("/feign/user")
    public ApiResult<Void> saveUserByFeign(@RequestBody User user) {
        userService.saveUser(user);

        return ApiResult.success();
    }

    @ApiOperation("远程保存用户")
    @GetMapping("/feign/username/{username}")
    public ApiResult<User> getUserByUsernameByFeign(@PathVariable("username") String username) {
        User user = userService.getUserByUsername(username);

        return ApiResult.success(user);
    }

    @ApiOperation("远程保存用户")
    @GetMapping("/feign/email/{email}")
    public ApiResult<User> getUserByEmail(@PathVariable("email") String email) {
        User user = userService.getUserByEmail(email);

        return ApiResult.success(user);
    }

    @ApiOperation("远程批量查询用户")
    @GetMapping("/feign/list/map")
    public ApiResult<Map<Long, User>> getUserMapByUserIds(@RequestBody List<Long> userIds) {
        Map<Long, User> userMap = userService.getUserMapByUserIds(userIds);

        return ApiResult.success(userMap);
    }


}
