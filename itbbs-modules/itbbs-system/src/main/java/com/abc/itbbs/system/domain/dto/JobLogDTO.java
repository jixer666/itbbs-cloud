package com.abc.itbbs.system.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.common.core.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 定时任务日志DTO对象
 *
 * @author LiJunXi
 * @date 2026-01-03
 */
@Data
public class JobLogDTO {

    private Long jobLogId;

    private String jobName;

    private String invokeTarget;

    private String jobMessage;

    private String exceptionInfo;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


    // 用于批量删除
    private List<Long> jobLogIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "定时任务日志参数不能为空");
        AssertUtils.isNotEmpty(jobLogId, "定时任务日志ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "定时任务日志参数不能为空");
        AssertUtils.isNotEmpty(jobName, "日志名称不能为空");
        AssertUtils.isNotEmpty(invokeTarget, "调用目标字符串不能为空");
        AssertUtils.isNotEmpty(jobMessage, "日志信息不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "定时任务日志参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(jobLogIds), "定时任务日志ID列表不能为空");
    }
}
