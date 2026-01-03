package com.abc.itbbs.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.system.convert.JobLogConvert;
import com.abc.itbbs.system.domain.dto.JobLogDTO;
import com.abc.itbbs.system.domain.entity.JobLog;
import com.abc.itbbs.system.domain.vo.JobLogVO;
import com.abc.itbbs.system.mapper.JobLogMapper;
import com.abc.itbbs.system.service.JobLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务日志业务处理
 *
 * @author LiJunXi
 * @date 2026-01-03
 */
@Service
public class JobLogServiceImpl extends BaseServiceImpl<JobLogMapper, JobLog> implements JobLogService {

    @Autowired
    private JobLogMapper jobLogMapper;

    @Override
    public PageResult getJobLogPageWithUiParam(JobLogDTO jobLogDTO) {
        startPage();
        List<JobLog> jobLogs = jobLogMapper.selectJobLogList(jobLogDTO);
        List<JobLogVO> jobLogVOList = pageList2CustomList(jobLogs, (List<JobLog> list) -> {
            return BeanUtil.copyToList(list, JobLogVO.class);
        });

        return buildPageResult(jobLogVOList);
    }

    @Override
    public void updateJobLog(JobLogDTO jobLogDTO) {
        jobLogDTO.checkUpdateParams();
        JobLog jobLog = jobLogMapper.selectById(jobLogDTO.getJobLogId());
        AssertUtils.isNotEmpty(jobLog, "定时任务日志不存在");
        BeanUtils.copyProperties(jobLogDTO, jobLog);
        jobLogMapper.updateById(jobLog);
    }

    @Override
    public void saveJobLog(JobLogDTO jobLogDTO) {
        jobLogDTO.checkSaveParams();
        JobLog jobLog = JobLogConvert.buildDefaultJobLogByJobLogDTO(jobLogDTO);
        jobLogMapper.insert(jobLog);
    }

    @Override
    public void deleteJobLog(JobLogDTO jobLogDTO) {
        jobLogDTO.checkDeleteParams();

        jobLogMapper.deleteBatchIds(jobLogDTO.getJobLogIds());
    }
    

}