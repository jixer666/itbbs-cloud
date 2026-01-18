package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LiJunXi
 * @date 2026/1/18
 */
@Getter
@AllArgsConstructor
public enum NavbarEnum {

    BLOG("博客", "/index"),
    RESOURCE("下载", "/resource"),
    AI("AI助手", "/ai");

    private String name;
    private String path;
}
