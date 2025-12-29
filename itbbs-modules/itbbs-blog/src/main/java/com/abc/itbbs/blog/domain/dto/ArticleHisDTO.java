package com.abc.itbbs.blog.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 文章历史修订DTO对象
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
public class ArticleHisDTO {

    private Long articleHisId;

    private Long articleId;

    private String title;

    private String content;

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


    // 用于批量删除
    private List<Long> articleHisIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "文章历史修订参数不能为空");
        AssertUtils.isNotEmpty(articleHisId, "文章历史修订ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "文章历史修订参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "文章历史修订参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(articleHisIds), "文章历史修订ID列表不能为空");
    }
}
