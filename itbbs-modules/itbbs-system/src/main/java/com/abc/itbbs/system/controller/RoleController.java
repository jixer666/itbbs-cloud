package com.abc.itbbs.system.controller;

import com.abc.itbbs.common.core.annotation.Permission;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.system.domain.dto.RoleDTO;
import com.abc.itbbs.system.domain.vo.RoleMenuTreeVO;
import com.abc.itbbs.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "角色接口")
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Permission("system:role:list")
    @ApiOperation("查询角色分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getRolePage(RoleDTO roleDTO) {
        PageResult rolePages = roleService.getRolePageWithUiParam(roleDTO);

        return ApiResult.success(rolePages);
    }

    @Permission("system:role:edit")
    @ApiOperation("更新角色")
    @PutMapping
    public ApiResult<Void> updateRole(@RequestBody RoleDTO roleDTO) {
        roleService.updateRole(roleDTO);

        return ApiResult.success();
    }

    @Permission("system:role:add")
    @ApiOperation("新增角色")
    @PostMapping
    public ApiResult<Void> saveRole(@RequestBody RoleDTO roleDTO) {
        roleService.saveRole(roleDTO);

        return ApiResult.success();
    }

    @Permission("system:role:list")
    @ApiOperation("获取角色树型菜单权限")
    @GetMapping("/getRoleMenuTree/{roleId}")
    public ApiResult<RoleMenuTreeVO> getRoleMenuTree(@PathVariable Long roleId) {
        RoleMenuTreeVO roleMenuTreeVO = roleService.getRoleMenuTree(roleId);

        return ApiResult.success(roleMenuTreeVO);
    }

    @Permission("system:role:add")
    @ApiOperation("分配菜单权限")
    @PostMapping("/saveRoleMenu")
    public ApiResult<Void> saveRoleMenu(@RequestBody RoleDTO roleDTO) {
        roleService.saveRoleMenu(roleDTO);

        return ApiResult.success();
    }

    @Permission("system:role:delete")
    @ApiOperation("删除角色")
    @DeleteMapping
    public ApiResult<Void> deleteRole(@RequestBody RoleDTO roleDTO) {
        roleService.deleteRole(roleDTO);

        return ApiResult.success();
    }

    @ApiOperation("远程通过用户ID获取角色Key列表")
    @GetMapping("/system/role/feign/userId/{userId}")
    public ApiResult<List<String>>  getRoleKeysByUserId(@PathVariable("userId") Long userId) {
        List<String> roleKeyList = roleService.getRoleKeysByUserId(userId);

        return ApiResult.success(roleKeyList);
    }
}
