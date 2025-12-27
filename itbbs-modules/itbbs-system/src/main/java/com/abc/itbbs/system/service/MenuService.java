package com.abc.itbbs.system.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.api.system.domain.entity.Menu;
import com.abc.itbbs.system.domain.dto.MenuDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<Menu> getMenusByUserId(Long userId);

    PageResult getMenuPageWithUiParam(MenuDTO menuDTO);

    void updateMenu(MenuDTO menuDTO);

    void saveMenu(MenuDTO menuDTO);

    List<Long> getMenuIdsByRoleId(Long roleId);

    void deleteMenu(MenuDTO menuDTO);

    List<Menu> getMenusByMenuType(Integer type);
}
