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
 * 点赞记录实体
 *
 * @author LiJunXi
 * @date 2026-01-04
 */
@Data
@Builder
@TableName("tb_like_record")
@AllArgsConstructor
@NoArgsConstructor
public class LikeRecord extends BaseEntity {

    @TableId
    @ApiModelProperty("点赞记录ID")
    private Long likeRecordId;

    @ApiModelProperty("业务类型")
    private Integer biz;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("目标ID")
    private Long targetId;

    @ApiModelProperty("用户ID")
    private Long userId;


}
