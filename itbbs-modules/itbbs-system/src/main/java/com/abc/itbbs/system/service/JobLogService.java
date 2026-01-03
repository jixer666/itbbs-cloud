package com.abc.itbbs.system.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.system.domain.dto.JobLogDTO;
import com.abc.itbbs.system.domain.entity.JobLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 定时任务日志接口
 *
 * @author LiJunXi
 * @date 2026-01-03
 */
public interface JobLogService extends IService<JobLog> {

    PageResult getJobLogPageWithUiParam(JobLogDTO jobLogDTO);

    void updateJobLog(JobLogDTO jobLogDTO);

    void saveJobLog(JobLogDTO jobLogDTO);

    void deleteJobLog(JobLogDTO jobLogDTO);
}
