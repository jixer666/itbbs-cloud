package com.abc.itbbs.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.system.constant.MenuConstants;
import com.abc.itbbs.system.convert.MenuConvert;
import com.abc.itbbs.system.domain.dto.MenuDTO;
import com.abc.itbbs.api.system.domain.entity.Menu;
import com.abc.itbbs.system.domain.vo.MenuVO;
import com.abc.itbbs.system.mapper.MenuMapper;
import com.abc.itbbs.system.service.MenuService;
import com.abc.itbbs.system.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getMenusByUserId(Long userId) {
        return menuMapper.selectMenusByUserId(userId);
    }

    @Override
    public PageResult getMenuPageWithUiParam(MenuDTO menuDTO) {
        startPage();
        List<Menu> menus = menuMapper.selectMenusByMenuDTO(menuDTO);
        List<MenuVO> menuVOList = pageList2CustomList(menus,
                (List<Menu> list) -> buildMenuVO(MenuConstants.DEFAULT_MENU_PARENT_ID, menus)
        );

        return buildPageResult(menuVOList);
    }

    private List<MenuVO> buildMenuVO(Long menuId, List<Menu> menus) {
        List<MenuVO> routers = new ArrayList<>();

        List<Menu> filterMenus = menus.stream().filter(item -> item.getParentId().equals(menuId)).sorted(new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o1.getOrderNum() - o2.getOrderNum();
            }
        }).collect(Collectors.toList());

        for (Menu menu : filterMenus) {
            List<MenuVO> children = buildMenuVO(menu.getMenuId(), menus);
            routers.add(MenuConvert.convertToMenuVO(menu, children));
        }

        return routers;
    }

    @Override
    public void updateMenu(MenuDTO menuDTO) {
        menuDTO.checkUpdateParams();
        Menu menu = menuMapper.selectById(menuDTO.getMenuId());
        AssertUtils.isNotEmpty(menu, "菜单不存在");
        BeanUtils.copyProperties(menuDTO, menu);
        menuMapper.updateById(menu);
    }

    @Override
    public void saveMenu(MenuDTO menuDTO) {
        menuDTO.checkSaveParams();
        Menu menu = MenuConvert.buildDefaultMenuByMenuDTO(menuDTO);
        menuMapper.insert(menu);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        AssertUtils.isNotEmpty(roleId, "角色ID不存在");

        return menuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    public void deleteMenu(MenuDTO menuDTO) {
        menuDTO.checkDeleteParams();
        checkIsMenuContainChildren(menuDTO.getMenuIds());
        checkIsRoleContainMenu(menuDTO.getMenuIds());

        menuMapper.deleteBatchIds(menuDTO.getMenuIds());
    }

    private void checkIsRoleContainMenu(List<Long> menuIds) {
        RoleService roleService = SpringUtil.getBean(RoleService.class);
        for (Long menuId : menuIds) {
            List<Long> roleIds = roleService.getRoleIdsByMenuId(menuId);
            if (CollUtil.isNotEmpty(roleIds)) {
                throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), String.format("菜单【%s】已被角色分配，无法删除", getById(menuId).getMenuName()));
            }
        }
    }

    private void checkIsMenuContainChildren(List<Long> menuIds) {
        for (Long menuId : menuIds) {
            List<Menu> children = getMenusByParentId(menuId);
            if (CollUtil.isNotEmpty(children)) {
                throw new GlobalException(BizCodeEnum.SYSTEM_ERROR.getCode(), String.format("菜单【%s】存在子菜单权限，无法删除", getById(menuId).getMenuName()));
            }
        }
    }

    private List<Menu> getMenusByParentId(Long menuId) {
        AssertUtils.isNotEmpty(menuId, "菜单ID列表不能为空");

        return menuMapper.selectMenusByParentId(menuId);
    }

    @Override
    public List<Menu> getMenusByMenuType(Integer type) {
        AssertUtils.isNotEmpty(type, "菜单类型不能为空");

        return menuMapper.selectMenusByMenuType(type);
    }
}
