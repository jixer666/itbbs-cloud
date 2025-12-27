package com.abc.itbbs.system.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleMenuTreeVO {

    private List<Long> checkMenuIds;

    private List<MenuVO> menus;

}
