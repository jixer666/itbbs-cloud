package com.abc.itbbs.system.mapper;

import com.abc.itbbs.system.domain.dto.JobLogDTO;
import com.abc.itbbs.system.domain.entity.JobLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 定时任务日志Mapper接口
 *
 * @author LiJunXi
 * @date 2026-01-03
 */
@Mapper
public interface JobLogMapper extends BaseMapper<JobLog> {
    List<JobLog> selectJobLogList(JobLogDTO jobLogDTO);
}
