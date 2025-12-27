package com.abc.itbbs.api.system.domain.entity;

import com.abc.itbbs.api.system.domain.enums.MenuTypeEnum;
import com.abc.itbbs.common.core.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("tb_menu")
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseEntity {

    @TableId
    private Long menuId;

    private Long parentId;

    private String menuName;

    private String path;

    private String component;

    private Integer menuType;

    private Integer orderNum;

    private String perms;

    private String icon;

    private Long userId;

    private Integer hidden;

    @JsonIgnore
    public boolean isMuLu() {
        return menuType.equals(MenuTypeEnum.MU_LU.getType());
    }

    @JsonIgnore
    public boolean isCaiDan() {
        return menuType.equals(MenuTypeEnum.CAI_DAN.getType());
    }

    @JsonIgnore
    public boolean isAnNiu() {
        return menuType.equals(MenuTypeEnum.AN_NIU.getType());
    }

    @JsonIgnore
    public Boolean isFront() {
        return menuType.equals(MenuTypeEnum.FRONT.getType());
    }
}
