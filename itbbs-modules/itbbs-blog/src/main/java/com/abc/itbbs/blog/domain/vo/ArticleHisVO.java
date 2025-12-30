package com.abc.itbbs.blog.domain.vo;

import java.util.Date;
import lombok.Data;

/**
 * 文章历史修订VO对象
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
public class ArticleHisVO {

    private Long articleHisId;

    private Long articleId;

    private String title;

    private String content;

    private String summary;

    private Long categoryId;

    private String categoryName;

    private String tagDetails;

    private String cover;

    private Integer type;

    private Integer visibleRange;

    private Integer creationStatement;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


}
