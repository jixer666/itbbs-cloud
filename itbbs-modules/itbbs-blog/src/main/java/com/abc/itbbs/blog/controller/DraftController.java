package com.abc.itbbs.blog.controller;

import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.domain.dto.DraftDTO;
import com.abc.itbbs.blog.service.DraftService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 草稿控制器
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Api(tags = "草稿接口")
@RestController
@RequestMapping("/blog/draft")
public class DraftController {

    @Autowired
    private DraftService draftService;

    @ApiOperation("查询草稿分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getDraftPage(DraftDTO draftDTO) {
        PageResult draftPages = draftService.getDraftPageWithUiParam(draftDTO);

        return ApiResult.success(draftPages);
    }

    @ApiOperation("更新草稿")
    @PutMapping
    public ApiResult<Void> updateDraft(@RequestBody DraftDTO draftDTO) {
        draftService.updateDraft(draftDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增草稿")
    @PostMapping
    public ApiResult<Void> saveDraft(@RequestBody DraftDTO draftDTO) {
        draftService.saveDraft(draftDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除草稿")
    @DeleteMapping
    public ApiResult<Void> deleteDraft(@RequestBody DraftDTO draftDTO) {
        draftService.deleteDraft(draftDTO);

        return ApiResult.success();
    }

}
