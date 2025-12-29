package com.abc.itbbs.auth.convert;

import com.abc.itbbs.api.system.domain.entity.Menu;
import com.abc.itbbs.auth.constant.MenuConstants;
import com.abc.itbbs.auth.domain.vo.MenuRouterMetaVo;
import com.abc.itbbs.auth.domain.vo.MenuRouterVO;
import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class MenuConvert {

    public static MenuRouterVO convertToMenuRouterVO(Menu menu, List<MenuRouterVO> children) {
        MenuRouterVO menuRouterVO = new MenuRouterVO();
        menuRouterVO.setName(getMenuRouterName(menu));
        menuRouterVO.setChildren(children);
        menuRouterVO.setPath(getMenuRouterPath(menu));
        menuRouterVO.setComponent(getMenuRouterComponent(menu));
        menuRouterVO.setMeta(buildMetaVo(menu));
        menuRouterVO.setHidden(menu.getHidden().equals(CommonConstants.NO));
        menuRouterVO.setFront(menu.isFront());
        menuRouterVO.setMenuType(menu.getMenuType());
        if (CollectionUtils.isNotEmpty(children) && menu.isMuLu()) {
            menuRouterVO.setAlwaysShow(true);
            menuRouterVO.setRedirect(MenuConstants.NO_REDIRECT);
        }

        return menuRouterVO;
    }

    private static String getMenuRouterPath(Menu menu) {
        if (menu.isMuLu() || menu.isFront()) {
            return "/" + menu.getPath();
        }
        return menu.getPath();
    }

    private static String getMenuRouterName(Menu menu) {
        return StringUtils.capitalize(menu.getPath());
    }

    private static MenuRouterMetaVo buildMetaVo(Menu menu) {
        return MenuRouterMetaVo.builder()
                .title(menu.getMenuName())
                .icon(menu.getIcon())
                .build();
    }


    private static String getMenuRouterComponent(Menu menu) {
        if (StringUtils.isNotEmpty(menu.getComponent())) {
            return menu.getComponent();
        }

        return MenuConstants.LAYOUT;
    }

    public static MenuRouterVO buildDefaultParentMenuLayout(MenuRouterVO menuRouterVO) {
        MenuRouterVO parentMenuRouterVO = new MenuRouterVO();
        parentMenuRouterVO.setPath("/");
        parentMenuRouterVO.setComponent(MenuConstants.LAYOUT);
        parentMenuRouterVO.setChildren(Arrays.asList(menuRouterVO));

        return parentMenuRouterVO;
    }
}
