package com.abc.itbbs.common.job;

import com.abc.itbbs.api.system.JobLogServiceClient;
import com.abc.itbbs.api.system.domain.dto.JobLogSaveDTO;
import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.util.ExceptionUtil;
import com.abc.itbbs.common.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

/**
 * @author LiJunXi
 * @date 2026/1/3
 */
@Slf4j
@Component
public class TaskHelper {

    @Autowired
    private JobLogServiceClient jobLogServiceClient;

    public void run(String taskName, String invokeTarget, Function mainProcess, Function retryProcess) {
        log.info("=====开始执行{}=====", taskName);
        Date startDate = new Date();
        try {
            mainProcess.apply(taskName);
            recordTaskInfo(taskName, invokeTarget, startDate, CommonConstants.SUCCESS, null);
        } catch (Exception e) {
            log.error("执行{}出错：{}", taskName, e.getMessage(), e);
            // 定时任务重试机制
            retryProcess.apply(taskName);
            recordTaskInfo(taskName, invokeTarget, startDate, CommonConstants.FAIL, StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000));
        }

        log.info("=====结束执行{}=====", taskName);
    }

    private void recordTaskInfo(String taskName, String invokeTarget, Date startDate, Integer status, String errorMsg) {
        long runMs = new Date().getTime() - startDate.getTime();
        String jobMessage = String.format("【%s】总共耗时：%d毫秒", taskName, runMs);

        JobLogSaveDTO jobLogSaveDTO = new JobLogSaveDTO();
        jobLogSaveDTO.setJobName(taskName);
        jobLogSaveDTO.setInvokeTarget(invokeTarget);
        jobLogSaveDTO.setJobMessage(jobMessage);
        jobLogSaveDTO.setStatus(status);
        jobLogSaveDTO.setExceptionInfo(errorMsg);

        jobLogServiceClient.saveFeignJobLog(jobLogSaveDTO);
    }

}
