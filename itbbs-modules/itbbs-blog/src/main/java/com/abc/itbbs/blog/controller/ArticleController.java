package com.abc.itbbs.blog.controller;

import com.abc.itbbs.api.blog.domain.entity.Article;
import com.abc.itbbs.blog.domain.vo.ArticleMetaVO;
import com.abc.itbbs.blog.domain.vo.ArticleVO;
import com.abc.itbbs.common.core.annotation.RateLimiter;
import com.abc.itbbs.common.core.domain.enums.LimitType;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        PageResult articlePages = articleService.getArticlePageWithUiParamV2(articleDTO);

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

    @ApiOperation("获取文章元信息")
    @GetMapping("/info/meta/{articleId}")
    public ApiResult<ArticleMetaVO> getArticleMetaInfo(@PathVariable("articleId") Long articleId) {
        ArticleMetaVO articleMetaVO = articleService.getArticleMetaInfo(articleId);

        return ApiResult.success(articleMetaVO);
    }

    @ApiOperation("增加文章浏览量")
    @RateLimiter(
            key = "'itbbs:increaseArticleViewsCount:' + #articleId",
            time = 60,
            count = 10,
            limitType = LimitType.IP
    )
    @PostMapping("/viewsCount/{articleId}")
    public ApiResult<Void> increaseArticleViewsCount(@PathVariable("articleId") Long articleId) {
        articleService.increaseArticleViewsCount(articleId);

        return ApiResult.success();
    }

    @ApiOperation("文章点赞")
    @RateLimiter(
            key = "'itbbs:increaseArticleLikeCount:' + #articleId",
            time = 1,
            count = 2,
            limitType = LimitType.IP
    )
    @PostMapping("/likeCount/{articleId}")
    public ApiResult<Void> increaseArticleLikeCount(@PathVariable("articleId") Long articleId) {
        articleService.increaseArticleLikeCount(articleId);

        return ApiResult.success();
    }

    @ApiOperation("文章收藏")
    @RateLimiter(
            key = "'itbbs:increaseArticleCollectCount:' + #articleId",
            time = 1,
            count = 2,
            limitType = LimitType.IP
    )
    @PostMapping("/collectCount/{articleId}")
    public ApiResult<Void> increaseArticleCollectCount(@PathVariable("articleId") Long articleId) {
        articleService.increaseArticleCollectCount(articleId);

        return ApiResult.success();
    }

    @ApiOperation("远程批量查询文章")
    @PostMapping("/feign/list/map")
    public ApiResult<Map<Long, Article>> getArticleMapByIds(@RequestBody List<Long> articleIds) {
        Map<Long, Article> articleMap = articleService.getArticleMapByIds(articleIds);

        return ApiResult.success(articleMap);
    }

}
