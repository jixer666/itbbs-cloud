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
 * 收藏记录实体
 *
 * @author LiJunXi
 * @date 2026-01-05
 */
@Data
@Builder
@TableName("tb_collect_record")
@AllArgsConstructor
@NoArgsConstructor
public class CollectRecord extends BaseEntity {

    @TableId
    @ApiModelProperty("收藏记录ID")
    private Long collectRecordId;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("业务类型")
    private Integer biz;

    @ApiModelProperty("目标ID")
    private Long targetId;

    @ApiModelProperty("用户ID")
    private Long userId;


}
