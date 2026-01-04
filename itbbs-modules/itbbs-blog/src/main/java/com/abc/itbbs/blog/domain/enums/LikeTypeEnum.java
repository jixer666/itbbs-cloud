package com.abc.itbbs.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LikeTypeEnum {
    ARTICLE(1, "文章"),
    COMMENT(2, "评论");

    private Integer type;
    private String desc;

}
