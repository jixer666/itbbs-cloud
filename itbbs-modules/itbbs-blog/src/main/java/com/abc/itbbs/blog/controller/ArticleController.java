package com.abc.itbbs.blog.controller;

import com.abc.itbbs.blog.domain.vo.ArticleVO;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章控制器
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Api(tags = "文章接口")
@RestController
@RequestMapping("/blog/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation("查询文章分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getArticlePage(ArticleDTO articleDTO) {
        PageResult articlePages = articleService.getArticlePageWithUiParam(articleDTO);

        return ApiResult.success(articlePages);
    }

    @ApiOperation("更新文章")
    @PutMapping
    public ApiResult<Void> updateArticle(@RequestBody ArticleDTO articleDTO) {
        articleService.updateArticle(articleDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增文章草稿")
    @PostMapping("/draft")
    public ApiResult<ArticleVO> saveArticleDraft() {
        ArticleVO articleVO = articleService.saveArticleDraft();

        return ApiResult.success(articleVO);
    }

    @ApiOperation("删除文章")
    @DeleteMapping
    public ApiResult<Void> deleteArticle(@RequestBody ArticleDTO articleDTO) {
        articleService.deleteArticle(articleDTO);

        return ApiResult.success();
    }

}
