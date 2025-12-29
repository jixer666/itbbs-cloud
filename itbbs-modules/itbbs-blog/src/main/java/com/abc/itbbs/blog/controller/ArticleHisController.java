package com.abc.itbbs.blog.controller;

import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.domain.dto.ArticleHisDTO;
import com.abc.itbbs.blog.service.ArticleHisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章历史修订控制器
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Api(tags = "文章历史修订接口")
@RestController
@RequestMapping("/blog/articleHis")
public class ArticleHisController {

    @Autowired
    private ArticleHisService articleHisService;

    @ApiOperation("查询文章历史修订分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getArticleHisPage(ArticleHisDTO articleHisDTO) {
        PageResult articleHisPages = articleHisService.getArticleHisPageWithUiParam(articleHisDTO);

        return ApiResult.success(articleHisPages);
    }

    @ApiOperation("更新文章历史修订")
    @PutMapping
    public ApiResult<Void> updateArticleHis(@RequestBody ArticleHisDTO articleHisDTO) {
        articleHisService.updateArticleHis(articleHisDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增文章历史修订")
    @PostMapping
    public ApiResult<Void> saveArticleHis(@RequestBody ArticleHisDTO articleHisDTO) {
        articleHisService.saveArticleHis(articleHisDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除文章历史修订")
    @DeleteMapping
    public ApiResult<Void> deleteArticleHis(@RequestBody ArticleHisDTO articleHisDTO) {
        articleHisService.deleteArticleHis(articleHisDTO);

        return ApiResult.success();
    }

}
