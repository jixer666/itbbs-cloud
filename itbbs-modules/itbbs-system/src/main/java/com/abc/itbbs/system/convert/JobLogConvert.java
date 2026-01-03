package com.abc.itbbs.system.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.system.domain.dto.JobLogDTO;
import com.abc.itbbs.system.domain.entity.JobLog;

/**
 * 定时任务日志转换器
 *
 * @author LiJunXi
 * @date 2026-01-03
 */
public class JobLogConvert {
    public static JobLog buildDefaultJobLogByJobLogDTO(JobLogDTO jobLogDTO) {
        JobLog jobLog = BeanUtil.copyProperties(jobLogDTO, JobLog.class);
        jobLog.setJobLogId(IdUtils.getId());
        jobLog.setInsertParams();

        return jobLog;
    }

    public static JobLog buildDefaultJobLogByTask(String taskName, String invokeTarget, Integer status, String errorMsg) {
        JobLog jobLog = new JobLog();
        jobLog.setJobLogId(IdUtils.getId());
        jobLog.setInsertParams();

        jobLog.setJobMessage(taskName);
        jobLog.setStatus(status);
        jobLog.setInvokeTarget(invokeTarget);
        jobLog.setExceptionInfo(errorMsg);

        return jobLog;
    }
}
