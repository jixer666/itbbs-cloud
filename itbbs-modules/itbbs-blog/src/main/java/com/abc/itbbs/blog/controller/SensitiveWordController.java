package com.abc.itbbs.blog.controller;

import com.abc.itbbs.blog.domain.dto.SensitiveWordDTO;
import com.abc.itbbs.blog.service.SensitiveWordService;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 敏感词控制器
 *
 * @author LiJunXi
 * @date 2025-12-31
 */
@Api(tags = "敏感词接口")
@RestController
@RequestMapping("/blog/sensitiveWord")
public class SensitiveWordController {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @ApiOperation("查询敏感词分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getSensitiveWordPage(SensitiveWordDTO sensitiveWordDTO) {
        PageResult sensitiveWordPages = sensitiveWordService.getSensitiveWordPageWithUiParam(sensitiveWordDTO);

        return ApiResult.success(sensitiveWordPages);
    }

    @ApiOperation("更新敏感词")
    @PutMapping
    public ApiResult<Void> updateSensitiveWord(@RequestBody SensitiveWordDTO sensitiveWordDTO) {
        sensitiveWordService.updateSensitiveWord(sensitiveWordDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增敏感词")
    @PostMapping
    public ApiResult<Void> saveSensitiveWord(@RequestBody SensitiveWordDTO sensitiveWordDTO) {
        sensitiveWordService.saveSensitiveWord(sensitiveWordDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除敏感词")
    @DeleteMapping
    public ApiResult<Void> deleteSensitiveWord(@RequestBody SensitiveWordDTO sensitiveWordDTO) {
        sensitiveWordService.deleteSensitiveWord(sensitiveWordDTO);

        return ApiResult.success();
    }

}
