package com.abc.itbbs.blog.controller;

import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.domain.dto.CategoryDTO;
import com.abc.itbbs.blog.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类控制器
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Api(tags = "分类接口")
@RestController
@RequestMapping("/blog/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("查询分类分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getCategoryPage(CategoryDTO categoryDTO) {
        PageResult categoryPages = categoryService.getCategoryPageWithUiParam(categoryDTO);

        return ApiResult.success(categoryPages);
    }

    @ApiOperation("更新分类")
    @PutMapping
    public ApiResult<Void> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增分类")
    @PostMapping
    public ApiResult<Void> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.saveCategory(categoryDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除分类")
    @DeleteMapping
    public ApiResult<Void> deleteCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.deleteCategory(categoryDTO);

        return ApiResult.success();
    }

}
