package com.abc.itbbs.api.blog;

import com.abc.itbbs.api.blog.domain.entity.Article;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author LiJunXi
 * @date 2025/1/13
 */
@FeignClient(contextId = "ArticleServiceClient", value = "itbbs-blog")
public interface ArticleServiceClient {

    @PostMapping("/blog/article/feign/list/map")
    ApiResult<Map<Long, Article>> getArticleMapByIds(@RequestBody List<Long> articleIds);

}
