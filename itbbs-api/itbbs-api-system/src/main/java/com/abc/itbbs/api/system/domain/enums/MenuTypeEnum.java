package com.abc.itbbs.api.system.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuTypeEnum {
    MU_LU(1, "目录"),
    CAI_DAN(2, "菜单"),
    AN_NIU(3, "按钮"),
    FRONT_NAVBAR(4, "前台导航栏"),
    FRONT_ROUTER(5, "前台路由");

    private Integer type;
    private String desc;

}
