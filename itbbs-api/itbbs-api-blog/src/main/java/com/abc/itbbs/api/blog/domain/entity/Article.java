package com.abc.itbbs.api.blog.domain.entity;

import com.abc.itbbs.common.core.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 文章实体
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
@Builder
@TableName("tb_article")
@AllArgsConstructor
@NoArgsConstructor
public class Article extends BaseEntity {

    @TableId
    @ApiModelProperty("文章ID")
    private Long articleId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("分类ID")
    private Long categoryId;

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

    @ApiModelProperty("浏览量")
    private Integer viewsCount;

    @ApiModelProperty("点赞量")
    private Integer likeCount;

    @ApiModelProperty("收藏量")
    private Integer collectCount;

    @ApiModelProperty("评论量")
    private Integer commentCount;

    @ApiModelProperty("分享量")
    private Integer shareCount;

    @ApiModelProperty("HTML文件路径")
    private String htmlFilePath;

    @ApiModelProperty("价格")
    private BigDecimal price;

}
