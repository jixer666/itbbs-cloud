package com.abc.itbbs.blog.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 文章DTO对象
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
public class ArticleDTO {

    private Long articleId;

    private String title;

    private String content;

    private String summary;

    private Long categoryId;

    private List<String> tagDetailsList;

    private String cover;

    private Integer type;

    private Integer visibleRange;

    private Integer creationStatement;

    private Integer viewsCount;

    private Integer likeCount;


    // 用于批量删除
    private List<Long> articleIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "文章参数不能为空");
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        checkSaveParams();
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "文章参数不能为空");
        AssertUtils.isNotEmpty(title, "文章标题不能为空");
        AssertUtils.isNotEmpty(content, "文章内容不能为空");
        AssertUtils.isNotEmpty(categoryId, "文章分类不能为空");
        AssertUtils.isNotEmpty(visibleRange, "文章可见范围不能为空");
        AssertUtils.isNotEmpty(type, "文章类型不能为空");
        AssertUtils.isNotEmpty(creationStatement, "文章创作声明不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "文章参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(articleIds), "文章ID列表不能为空");
    }
}
