package com.abc.itbbs.blog.controller;

import com.abc.itbbs.blog.domain.dto.CollectRecordDTO;
import com.abc.itbbs.blog.service.CollectRecordService;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏记录控制器
 *
 * @author LiJunXi
 * @date 2026-01-05
 */
@Api(tags = "收藏记录接口")
@RestController
@RequestMapping("/blog/collectRecord")
public class CollectRecordController {

    @Autowired
    private CollectRecordService collectRecordService;

    @ApiOperation("查询收藏记录分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getCollectRecordPage(CollectRecordDTO collectRecordDTO) {
        PageResult collectRecordPages = collectRecordService.getCollectRecordPageWithUiParam(collectRecordDTO);

        return ApiResult.success(collectRecordPages);
    }

    @ApiOperation("更新收藏记录")
    @PutMapping
    public ApiResult<Void> updateCollectRecord(@RequestBody CollectRecordDTO collectRecordDTO) {
        collectRecordService.updateCollectRecord(collectRecordDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增收藏记录")
    @PostMapping
    public ApiResult<Void> saveCollectRecord(@RequestBody CollectRecordDTO collectRecordDTO) {
        collectRecordService.saveCollectRecord(collectRecordDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除收藏记录")
    @DeleteMapping
    public ApiResult<Void> deleteCollectRecord(@RequestBody CollectRecordDTO collectRecordDTO) {
        collectRecordService.deleteCollectRecord(collectRecordDTO);

        return ApiResult.success();
    }

}
