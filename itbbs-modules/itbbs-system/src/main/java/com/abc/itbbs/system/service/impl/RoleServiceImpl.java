package com.abc.itbbs.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.system.convert.RoleConvert;
import com.abc.itbbs.system.domain.dto.RoleDTO;
import com.abc.itbbs.system.domain.entity.Role;
import com.abc.itbbs.system.domain.vo.RoleMenuTreeVO;
import com.abc.itbbs.system.domain.vo.RoleVO;
import com.abc.itbbs.system.mapper.RoleMapper;
import com.abc.itbbs.system.service.MenuService;
import com.abc.itbbs.system.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<String> getRoleKeysByUserId(Long userId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        return roleMapper.selectRoleKeysByUserId(userId);
    }

    @Override
    public PageResult getRolePageWithUiParam(RoleDTO roleDTO) {
        startPage();
        List<Role> roles = roleMapper.selectRoleList(roleDTO);
        List<RoleVO> roleVOList = pageList2CustomList(roles, (List<Role> list) -> {
            return BeanUtil.copyToList(list, RoleVO.class);
        });

        return buildPageResult(roleVOList);
    }

    @Override
    public void updateRole(RoleDTO roleDTO) {
        roleDTO.checkUpdateParams();
        Role menu = roleMapper.selectById(roleDTO.getRoleId());
        AssertUtils.isNotEmpty(menu, "菜单不存在");
        BeanUtils.copyProperties(roleDTO, menu);
        roleMapper.updateById(menu);
    }

    @Override
    public void saveRole(RoleDTO roleDTO) {
        roleDTO.checkSaveParams();
        Role menu = RoleConvert.buildDefaultRoleByRoleDTO(roleDTO);
        roleMapper.insert(menu);
    }


    @Override
    public RoleMenuTreeVO getRoleMenuTree(Long roleId) {
        AssertUtils.isNotEmpty(roleId, "角色ID不能为空");
        MenuService menuService = SpringUtil.getBean(MenuService.class);
        List<?> menus = menuService.getMenuPageWithUiParam(null).getList();
        List<Long> menuIds = menuService.getMenuIdsByRoleId(roleId);

        return RoleConvert.buildRoleMenuTree(menus, menuIds);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");

        return roleMapper.selectRoleIdsByUserId(userId);
    }

    @Override
    @Transactional
    public void saveRoleMenu(RoleDTO roleDTO) {
        roleDTO.checkSaveRoleMenuParams();
        deleteRoleAllMenuByRoleId(roleDTO.getRoleId());
        saveRoleMenu(roleDTO.getRoleId(), roleDTO.getMenuIds());
    }

    private void saveRoleMenu(Long roleId, List<Long> menuIds) {
        AssertUtils.isNotEmpty(roleId, "角色ID不能为空");
        if (CollUtil.isEmpty(menuIds)) {
            return;
        }

        roleMapper.saveRoleMenu(roleId, menuIds);
    }

    private void deleteRoleAllMenuByRoleId(Long roleId) {
        AssertUtils.isNotEmpty(roleId, "角色ID不能为空");

        roleMapper.deleteRoleMenuByRoleId(roleId);
    }

    @Override
    public void deleteRole(RoleDTO roleDTO) {
        roleDTO.checkDeleteParams();
        checkIsRoleContainMenu(roleDTO.getRoleIds());

        roleMapper.deleteBatchIds(roleDTO.getRoleIds());
    }

    private void checkIsRoleContainMenu(List<Long> roleIds) {
        MenuService menuService = SpringUtil.getBean(MenuService.class);
        for (Long roleId : roleIds) {
            List<Long> menuIds = menuService.getMenuIdsByRoleId(roleId);
            if (CollUtil.isNotEmpty(menuIds)) {
                throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), String.format("角色【%s】存在菜单权限，无法删除", getById(roleId).getRoleName()));
            }
        }
    }

    @Override
    public List<Long> getRoleIdsByMenuId(Long menuId) {
        AssertUtils.isNotEmpty(menuId, "菜单ID不能为空");

        return roleMapper.selectRoleIdsByMenuId(menuId);
    }

    @Override
    public void deleteRoleByUserId(Long userId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");

        roleMapper.deleteRoleByUserId(userId);
    }

    @Override
    public void saveUserRole(Long userId, List<Long> roleIds) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(roleIds), "角色ID列表不能为空");

        roleMapper.saveUserRole(userId, roleIds);
    }

    @Override
    public Boolean isAdmin(Long userId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        return getRoleKeysByUserId(userId).stream()
                .anyMatch(CommonConstants.SUPER_ADMIN::equals);
    }

    @Override
    public void saveUserRoleByRoleKeys(Long userId, List<String> roleKeys) {
        if (CollUtil.isNotEmpty(roleKeys)) {
            return;
        }

        List<Long> roleIds = getRoleListByRoleKeys(roleKeys).stream().map(Role::getRoleId).collect(Collectors.toList());
        saveUserRole(userId, roleIds);
    }

    @Override
    public List<Role> getRoleListByRoleKeys(List<String> roleKeys) {
        return CollUtil.isEmpty(roleKeys) ? new ArrayList<>() : roleMapper.selectRoleListByRoleKeys(roleKeys);
    }
}
