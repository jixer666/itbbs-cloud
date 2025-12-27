package com.abc.itbbs.system.controller;

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



}
