package com.abc.itbbs.blog.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.blog.domain.enums.ArticleStatusEnum;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.math.BigDecimal;
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

    private Integer status;

    private BigDecimal price;


    // 用于批量删除
    private List<Long> articleIds;

    // 分页查询
    private Long pageNum = 1L;
    private Long pageSize = 10L;
    private Integer loadType;
    private Long beginPage;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "文章参数不能为空");
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        AssertUtils.isNotEmpty(status, "文章状态不能为空");
        AssertUtils.isTrue(ArticleStatusEnum.DRAFT.getStatus().equals(status) || ArticleStatusEnum.PENDING.getStatus().equals(status)
                || ArticleStatusEnum.DELETE.getStatus().equals(status), "文章状态不可取");
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


    public Boolean isDraft() {
        return ArticleStatusEnum.DRAFT.getStatus().equals(status);
    }

    public Boolean isPending() {
        return ArticleStatusEnum.PENDING.getStatus().equals(status);
    }

    public void checkQueryPageParams() {
        AssertUtils.isNotEmpty(this, "分页参数不能为空");
        AssertUtils.isNotEmpty(this.getPageNum(), "页码不能为空");
        AssertUtils.isNotEmpty(this.getPageSize(), "页数不能为空");
        AssertUtils.isNotEmpty(this.getLoadType(), "文章加载类型不能为空");
    }
}
