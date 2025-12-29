package com.abc.itbbs.blog.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 文章VO对象
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
public class ArticleVO {

    private Long articleId;

    private Long userId;

    private String title;

    private String content;

    private Long categoryId;

    private String tagDetails;

    private String cover;

    private Integer type;

    private Integer visibleRange;

    private Integer creationStatement;

    private Integer viewsCount;

    private Integer likeCount;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
