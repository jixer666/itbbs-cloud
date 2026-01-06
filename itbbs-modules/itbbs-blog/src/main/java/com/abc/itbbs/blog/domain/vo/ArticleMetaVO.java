package com.abc.itbbs.blog.domain.vo;

import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/1/2
 */
@Data
public class ArticleMetaVO {

    private Integer viewsCount;

    private Integer collectCount;

    private Boolean isCollected;

    private Integer likeCount;

    private Boolean isLiked;

}
