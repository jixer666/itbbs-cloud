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
 * 分类实体
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Data
@Builder
@TableName("tb_category")
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

    @TableId
    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("用户ID")
    private Long userId;


}
