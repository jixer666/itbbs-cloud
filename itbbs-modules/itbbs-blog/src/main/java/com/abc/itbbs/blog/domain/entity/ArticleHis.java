package com.abc.itbbs.blog.domain.entity;

import com.abc.itbbs.common.core.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章历史修订实体
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
@Builder
@TableName("tb_article_his")
@AllArgsConstructor
@NoArgsConstructor
public class ArticleHis extends BaseEntity {

    @TableId
    @ApiModelProperty("历史记录ID")
    private Long articleHisId;

    @ApiModelProperty("文章ID")
    private Long articleId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("标签")
    private String tagDetails;

    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("可见范围")
    private Integer visibleRange;

    @ApiModelProperty("创作声明")
    private Integer creationStatement;


}
