package com.abc.itbbs.system.controller;

import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.system.domain.dto.JobLogDTO;
import com.abc.itbbs.system.service.JobLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务日志控制器
 *
 * @author LiJunXi
 * @date 2026-01-03
 */
@Api(tags = "定时任务日志接口")
@RestController
@RequestMapping("/system/jobLog")
public class JobLogController {

    @Autowired
    private JobLogService jobLogService;

    @ApiOperation("查询定时任务日志分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getJobLogPage(JobLogDTO jobLogDTO) {
        PageResult jobLogPages = jobLogService.getJobLogPageWithUiParam(jobLogDTO);

        return ApiResult.success(jobLogPages);
    }

    @ApiOperation("更新定时任务日志")
    @PutMapping
    public ApiResult<Void> updateJobLog(@RequestBody JobLogDTO jobLogDTO) {
        jobLogService.updateJobLog(jobLogDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增定时任务日志")
    @PostMapping
    public ApiResult<Void> saveJobLog(@RequestBody JobLogDTO jobLogDTO) {
        jobLogService.saveJobLog(jobLogDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除定时任务日志")
    @DeleteMapping
    public ApiResult<Void> deleteJobLog(@RequestBody JobLogDTO jobLogDTO) {
        jobLogService.deleteJobLog(jobLogDTO);

        return ApiResult.success();
    }

}
