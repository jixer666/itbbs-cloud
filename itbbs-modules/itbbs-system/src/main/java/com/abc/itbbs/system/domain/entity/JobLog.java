package com.abc.itbbs.system.domain.entity;

import com.abc.itbbs.common.core.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定时任务日志实体
 *
 * @author LiJunXi
 * @date 2026-01-03
 */
@Data
@Builder
@TableName("tb_job_log")
@AllArgsConstructor
@NoArgsConstructor
public class JobLog extends BaseEntity {

    @TableId
    @ApiModelProperty("定时任务日志ID")
    private Long jobLogId;

    @ApiModelProperty("任务名称")
    private String jobName;

    @ApiModelProperty("调用目标字符串")
    private String invokeTarget;

    @ApiModelProperty("日志信息")
    private String jobMessage;

    @ApiModelProperty("异常信息")
    private String exceptionInfo;

}
