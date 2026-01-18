package com.abc.itbbs.resource.controller;

import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.resource.domain.dto.ResourceDTO;
import com.abc.itbbs.resource.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 资源控制器
 *
 * @author LiJunXi
 * @date 2026-01-18
 */
@Api(tags = "资源接口")
@RestController
@RequestMapping("/resource/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @ApiOperation("查询资源分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getResourcePage(ResourceDTO resourceDTO) {
        PageResult resourcePages = resourceService.getResourcePageWithUiParam(resourceDTO);

        return ApiResult.success(resourcePages);
    }

    @ApiOperation("更新资源")
    @PutMapping
    public ApiResult<Void> updateResource(@RequestBody ResourceDTO resourceDTO) {
        resourceService.updateResource(resourceDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增资源")
    @PostMapping
    public ApiResult<Void> saveResource(@RequestBody ResourceDTO resourceDTO) {
        resourceService.saveResource(resourceDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除资源")
    @DeleteMapping
    public ApiResult<Void> deleteResource(@RequestBody ResourceDTO resourceDTO) {
        resourceService.deleteResource(resourceDTO);

        return ApiResult.success();
    }

}
