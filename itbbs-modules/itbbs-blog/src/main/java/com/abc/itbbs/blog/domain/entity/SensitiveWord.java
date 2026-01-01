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
 * 敏感词实体
 *
 * @author LiJunXi
 * @date 2025-12-31
 */
@Data
@Builder
@TableName("tb_sensitive_word")
@AllArgsConstructor
@NoArgsConstructor
public class SensitiveWord extends BaseEntity {

    @TableId
    @ApiModelProperty("敏感词ID")
    private Long wordId;

    @ApiModelProperty("词语")
    private String word;

    @ApiModelProperty("处理级别")
    private Integer level;

    @ApiModelProperty("用户ID")
    private Long userId;


}
