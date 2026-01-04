package com.abc.itbbs.blog.controller;

import com.abc.itbbs.blog.domain.dto.LikeRecordDTO;
import com.abc.itbbs.blog.service.LikeRecordService;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 点赞记录控制器
 *
 * @author LiJunXi
 * @date 2026-01-04
 */
@Api(tags = "点赞记录接口")
@RestController
@RequestMapping("/blog/likeRecord")
public class LikeRecordController {

    @Autowired
    private LikeRecordService likeRecordService;

    @ApiOperation("查询点赞记录分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getLikeRecordPage(LikeRecordDTO likeRecordDTO) {
        PageResult likeRecordPages = likeRecordService.getLikeRecordPageWithUiParam(likeRecordDTO);

        return ApiResult.success(likeRecordPages);
    }

    @ApiOperation("更新点赞记录")
    @PutMapping
    public ApiResult<Void> updateLikeRecord(@RequestBody LikeRecordDTO likeRecordDTO) {
        likeRecordService.updateLikeRecord(likeRecordDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增点赞记录")
    @PostMapping
    public ApiResult<Void> saveLikeRecord(@RequestBody LikeRecordDTO likeRecordDTO) {
        likeRecordService.saveLikeRecord(likeRecordDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除点赞记录")
    @DeleteMapping
    public ApiResult<Void> deleteLikeRecord(@RequestBody LikeRecordDTO likeRecordDTO) {
        likeRecordService.deleteLikeRecord(likeRecordDTO);

        return ApiResult.success();
    }

}
