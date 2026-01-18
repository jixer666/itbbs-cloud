package com.abc.itbbs.blog.domain.vo;

import com.abc.itbbs.blog.domain.enums.NavbarEnum;
import com.abc.itbbs.common.core.constant.ServerConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiJunXi
 * @date 2026/1/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NavbarVO {

    private String name;

    private String url;

    public NavbarVO(NavbarEnum navbarEnum) {
        this.name = navbarEnum.getName();
        this.url = ServerConstants.ROOT_SERVICE + "/#" + navbarEnum.getPath();
    }

}
