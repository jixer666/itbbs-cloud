package com.abc.itbbs.blog.task;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.blog.convert.ArticleConvert;
import com.abc.itbbs.blog.domain.dto.ArticleUpdateCountDTO;
import com.abc.itbbs.blog.service.ArticleService;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章浏览量、评论量和点赞量定时任务统计
 * @author LiJunXi
 * @date 2026/1/3
 */
@Slf4j
@Component
public class ArticleCountTask {

    @Autowired
    private ArticleService articleService;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void run() {
        log.info("=====开始执行文章计数定时任务=====");
        // 注意：在多服务中可能重复取到数据，但对数据无影响，所以这里并没有做幂等性处理
        List<String> articleIdSet = RedisUtils.sPop(CacheConstants.ARTICLE_WAIT_DO_TASK, 1000);
        if (CollUtil.isEmpty(articleIdSet)) {
            return;
        }

        try {
            List<ArticleUpdateCountDTO> articleUpdateCountDTOList = articleIdSet.stream()
                    .map(item -> {
                        Long articleId = Long.parseLong(item);

                        Integer articleViewsCount = articleService.getArticleViewsCount(articleId);

                        return ArticleConvert.buildArticleUpdateCountDTO(articleId, articleViewsCount, 0, 0);
                    }).collect(Collectors.toList());

            articleService.updateArticleCountBath(articleUpdateCountDTOList);
        } catch (Exception e) {
            log.error("执行文章计数定时任务出错：{}", e.getMessage(), e);
            // TODO 定时任务重试机制

        }

        log.info("=====结束执行文章计数定时任务=====");
    }

}
