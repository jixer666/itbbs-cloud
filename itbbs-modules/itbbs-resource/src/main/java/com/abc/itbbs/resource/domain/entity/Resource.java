package com.abc.itbbs.resource.domain.entity;

import com.abc.itbbs.common.core.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 资源实体
 *
 * @author LiJunXi
 * @date 2026-01-18
 */
@Data
@Builder
@TableName("tb_resource")
@AllArgsConstructor
@NoArgsConstructor
public class Resource extends BaseEntity {

    @TableId
    @ApiModelProperty("资源ID")
    private Long resourceId;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("资源类型")
    private Integer type;

    @ApiModelProperty("资源分类")
    private Integer category;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("文件数量")
    private Integer fileCount;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("所需积分")
    private Integer point;

    @ApiModelProperty("下载量")
    private Integer downloadCount;

    @ApiModelProperty("秒杀开始时间")
    private Date startTime;

    @ApiModelProperty("秒杀结束时间")
    private Date endTime;


}
