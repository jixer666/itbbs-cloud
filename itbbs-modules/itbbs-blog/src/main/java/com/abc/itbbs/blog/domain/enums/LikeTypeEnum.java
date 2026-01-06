package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LikeTypeEnum {
    LIKE(1, "点赞"),
    UNLIKE(2, "取消点赞");

    private Integer type;
    private String desc;

}
