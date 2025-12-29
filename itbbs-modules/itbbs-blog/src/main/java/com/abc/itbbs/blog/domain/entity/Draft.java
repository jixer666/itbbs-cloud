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
 * 草稿实体
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
@Builder
@TableName("tb_draft")
@AllArgsConstructor
@NoArgsConstructor
public class Draft extends BaseEntity {

    @TableId
    @ApiModelProperty("草稿ID")
    private Long draftId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

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


}
