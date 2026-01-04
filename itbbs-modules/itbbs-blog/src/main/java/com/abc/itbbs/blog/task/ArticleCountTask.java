package com.abc.itbbs.blog.task;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.blog.convert.ArticleConvert;
import com.abc.itbbs.blog.domain.dto.ArticleUpdateCountDTO;
import com.abc.itbbs.blog.service.ArticleService;
import com.abc.itbbs.common.job.JobConstants;
import com.abc.itbbs.common.job.TaskHelper;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文章浏览量、评论量和点赞量定时任务统计
 *
 * @author LiJunXi
 * @date 2026/1/3
 */
@Slf4j
@Component
public class ArticleCountTask {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TaskHelper taskHelper;

    @Scheduled(fixedRate = 10 * 1000)
    public void run() {
        Map<String, List<String>> taskContext = new HashMap<>();

        taskHelper.run(JobConstants.ARTICLE_COUNT_TASK_NAME, JobConstants.ARTICLE_COUNT_TASK_TARGET, task -> {
            // 注意：在多服务中可能重复取到数据，但对数据无影响，所以这里并没有做幂等性处理
            List<String> articleIdSet = RedisUtils.sPop(CacheConstants.ARTICLE_WAIT_DO_TASK, 1000);
            if (CollUtil.isEmpty(articleIdSet)) {
                return null;
            }

            taskContext.put("articleIdSet", articleIdSet);

            List<ArticleUpdateCountDTO> articleUpdateCountDTOList = articleIdSet.stream()
                    .map(item -> {
                        Long articleId = Long.parseLong(item);

                        Integer articleViewsCount = articleService.getArticleViewsCount(articleId);

                        return ArticleConvert.buildArticleUpdateCountDTO(articleId, articleViewsCount, 0, 0);
                    }).collect(Collectors.toList());

            articleService.updateArticleCountBath(articleUpdateCountDTOList);

            return null;
        }, task -> {
            // 重新放入集合
            // TODO 重试次数限制
            List<String> articleIdSet = taskContext.get("articleIdSet");
            RedisUtils.sSet(CacheConstants.ARTICLE_WAIT_DO_TASK, articleIdSet.toArray());

            return null;
        });
    }

}
