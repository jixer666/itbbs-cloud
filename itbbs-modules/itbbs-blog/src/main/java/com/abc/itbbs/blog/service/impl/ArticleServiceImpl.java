package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.abc.itbbs.api.system.UserServiceClient;
import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.api.system.domain.vo.UserVO;
import com.abc.itbbs.blog.domain.dto.ArticleHisDTO;
import com.abc.itbbs.blog.domain.dto.ArticlePreloadDTO;
import com.abc.itbbs.blog.domain.dto.ArticleUpdateCountDTO;
import com.abc.itbbs.blog.domain.enums.ArticleStatusEnum;
import com.abc.itbbs.blog.domain.enums.CollectBizEnum;
import com.abc.itbbs.blog.domain.enums.LikeBizEnum;
import com.abc.itbbs.blog.domain.vo.ArticleMetaVO;
import com.abc.itbbs.blog.factory.ArticleLoadStrategyFactory;
import com.abc.itbbs.blog.factory.CollectRecordStrategyFactory;
import com.abc.itbbs.blog.factory.LikeRecordStrategyFactory;
import com.abc.itbbs.blog.service.ArticleHisService;
import com.abc.itbbs.blog.service.CollectRecordService;
import com.abc.itbbs.blog.service.LikeRecordService;
import com.abc.itbbs.blog.strategy.articleload.ArticleLoadStrategy;
import com.abc.itbbs.blog.strategy.collectrecord.CollectRecordStrategy;
import com.abc.itbbs.blog.strategy.likerecord.LikeRecordStrategy;
import com.abc.itbbs.common.core.constant.ServerConstants;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.blog.convert.ArticleConvert;
import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.domain.entity.Article;
import com.abc.itbbs.blog.domain.vo.ArticleVO;
import com.abc.itbbs.blog.mapper.ArticleMapper;
import com.abc.itbbs.blog.service.ArticleService;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.abc.itbbs.common.mq.producer.RabbitMQProducer;
import com.abc.itbbs.common.oss.domain.enums.OssTypeEnum;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import com.abc.itbbs.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 文章业务处理
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Slf4j
@Service
public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Value("${oss.type}")
    private Integer ossType;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private ArticleHisService articleHisService;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private LikeRecordService likeRecordService;

    @Autowired
    private CollectRecordService collectRecordService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public PageResult getArticlePageWithUiParamV1(ArticleDTO articleDTO) {
        startPage();
        List<Article> articles = articleMapper.selectArticleListWithoutContent(articleDTO);

        Map<Long, User> userMap = ApiResult.invokeRemoteMethod(userServiceClient.getUserMapByUserIds(
                articles.stream().map(Article::getUserId).collect(Collectors.toList()))
        );

        List<ArticleVO> articleVOList = pageList2CustomList(articles, (List<Article> list) -> {
            return list.stream().map(item -> {
                ArticleVO articleVO = BeanUtil.copyProperties(item, ArticleVO.class);

                User user = userMap.get(item.getUserId());
                if (Objects.nonNull(user)) {
                    articleVO.setUserInfo(BeanUtil.copyProperties(user, UserVO.class));
                }

                return articleVO;
            }).collect(Collectors.toList());
        });

        return buildPageResult(articleVOList);
    }

    @Override
    public PageResult getArticlePageWithUiParamV2(ArticleDTO articleDTO) {
        articleDTO.checkQueryPageParams();

        ArticleLoadStrategy articleLoadStrategy = ArticleLoadStrategyFactory.getArticleLoadStrategy(articleDTO.getLoadType());
        AssertUtils.isNotEmpty(articleLoadStrategy, "文章加载类型不存在");
        List<ArticleVO> articleVOList = articleLoadStrategy.getArticleList(articleDTO);

        fillArticleVOPageParams(articleVOList);

        return buildPageResult(articleVOList, articleLoadStrategy.getArticleTotalSize());
    }

    private void fillArticleVOPageParams(List<ArticleVO> articleVOList) {
        if (CollUtil.isEmpty(articleVOList)) {
            return;
        }

        Map<Long, User> userMap = ApiResult.invokeRemoteMethod(
                userServiceClient.getUserMapByUserIds(
                    articleVOList.stream().map(ArticleVO::getUserId).collect(Collectors.toList())
                )
        );

        for (ArticleVO item : articleVOList) {
            User user = userMap.get(item.getUserId());
            if (Objects.nonNull(user)) {
                item.setUserInfo(BeanUtil.copyProperties(user, UserVO.class));
            }
        }
    }


    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        articleDTO.checkUpdateParams();

        Article article = articleMapper.selectById(articleDTO.getArticleId());
        AssertUtils.isNotEmpty(article, "文章不存在");
        AssertUtils.isTrue(SecurityUtils.getUserId().equals(article.getUserId()), "无权限");

        BeanUtils.copyProperties(articleDTO, article);
        ArticleConvert.fillArticleUpdateParams(article, articleDTO);

        transactionTemplate.execute(item -> {
            articleMapper.updateById(article);
            articleHisService.saveArticleHis(BeanUtil.copyProperties(article, ArticleHisDTO.class));
            return item;
        });

        if (articleDTO.isPending()) {
            afterUpdateArticle(article);
        }
    }

    private void afterUpdateArticle(Article article) {
        rabbitMQProducer.sendMessage(RabbitMQConstants.BLOG_ARTICLE_EXCHANGE, RabbitMQConstants.BLOG_ARTICLE_CREATE_KEY, article);
    }

    @Override
    public void deleteArticle(ArticleDTO articleDTO) {
        articleDTO.checkDeleteParams();

        articleMapper.deleteBatchIds(articleDTO.getArticleIds());
    }

    @Override
    public ArticleVO saveArticleDraft() {
        Article article = ArticleConvert.buildDefaultArticleDraft();

        articleMapper.insert(article);

        return BeanUtil.copyProperties(article, ArticleVO.class);
    }

    @Override
    public void updateStatus(Long articleId, Integer status) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        AssertUtils.isNotEmpty(status, "状态不能为空");

        lambdaUpdate().set(Article::getStatus, status)
                .eq(Article::getArticleId, articleId)
                .update();
    }

    @Override
    public void updateHtmlFilePathByArticleId(Long articleId, String filePath) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        AssertUtils.isNotEmpty(filePath, "文件路径不能为空");

        // 处理文章地址
        if (!OssTypeEnum.LOCAL.equals(ossType)) {
            filePath = ServerConstants.BLOG_SERVICE + "/article/" + filePath;
        }

        lambdaUpdate().set(Article::getHtmlFilePath, filePath)
                .eq(Article::getArticleId, articleId)
                .update();
    }

    @Override
    public ArticleMetaVO getArticleMetaInfo(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");

        ArticleMetaVO articleMetaVO = new ArticleMetaVO();
        articleMetaVO.setViewsCount(getArticleViewsCount(articleId));
        articleMetaVO.setLikeCount(getArticleLikeCount(articleId));
        articleMetaVO.setCollectCount(getArticleCollectCount(articleId));

        try {
            Long userId = SecurityUtils.getUserId();

            articleMetaVO.setIsLiked(isUserLikeArticle(userId, articleId));
            articleMetaVO.setIsCollected(isUserCollectArticle(userId, articleId));
        } catch (Exception e) {
            articleMetaVO.setIsLiked(false);
            articleMetaVO.setIsCollected(false);
        }
        return articleMetaVO;
    }

    private Boolean isUserCollectArticle(Long userId, Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");

        String articleIdSetCacheKey = CacheConstants.getFinalKey(CacheConstants.USER_COLLECT_SET, userId, CollectBizEnum.ARTICLE.getBiz());
        Set<String> articleIdSet = RedisUtils.zRange(articleIdSetCacheKey, 0, -1);
        return articleIdSet.contains(articleId.toString());
    }

    private Integer getArticleCollectCount(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");

        CollectRecordStrategy collectRecordStrategy = CollectRecordStrategyFactory.getCollectRecordStrategy(CollectBizEnum.ARTICLE.getBiz());
        collectRecordService.rebuildCollectCountCache(articleId, collectRecordStrategy);

        String collectCountKey = collectRecordStrategy.getCountCacheKey(articleId);
        String collectCountStr = RedisUtils.get(collectCountKey);

        return Integer.valueOf(collectCountStr);
    }

    private Boolean isUserLikeArticle(Long userId, Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");

        String articleIdSetCacheKey = CacheConstants.getFinalKey(CacheConstants.USER_LIKE_SET, userId, LikeBizEnum.ARTICLE.getBiz());
        Set<String> articleIdSet = RedisUtils.zRange(articleIdSetCacheKey, 0, -1);
        return articleIdSet.contains(articleId.toString());
    }


    @Override
    public Integer getArticleViewsCount(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");

        checkAndRebuildArticleViewsCountCache(articleId);
        
        String viewsCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_VIEWS_COUNT, articleId);
        String viewsCountStr = RedisUtils.get(viewsCountKey);

        return Integer.valueOf(viewsCountStr);
    }

    private void checkAndRebuildArticleViewsCountCache(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");

        String viewsCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_VIEWS_COUNT, articleId);
        // 第一层检查
        if (RedisUtils.hasKey(viewsCountKey)) {
            return;
        }

        RLock lock = redissonClient.getLock(CacheConstants.getFinalKey(CacheConstants.ARTICLE_VIEW_COUNT_LOCK, articleId));
        try {
            // 等待5s，锁持有10s
            boolean isLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!isLock) {
                return;
            }

            // 第二层检查
            if (RedisUtils.hasKey(viewsCountKey)) {
                return;
            }

            Integer count = articleMapper.selectArticleViewsCount(articleId);
            RedisUtils.set(viewsCountKey, count, 24, TimeUnit.HOURS);

        } catch (Exception e) {
            log.error("浏览量重建加锁失败：{}", e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void increaseArticleViewsCount(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        AssertUtils.isTrue(checkArticleExist(articleId), "文章不存在");

        // 目的是为了检查缓存是否过期
        checkAndRebuildArticleViewsCountCache(articleId);

        RedisUtils.inc(CacheConstants.getFinalKey(CacheConstants.ARTICLE_VIEWS_COUNT, articleId), 24, TimeUnit.HOURS);

        // 加入到待同步集合
        RedisUtils.zAdd(CacheConstants.getFinalKey(CacheConstants.ARTICLE_COUNT_WAIT_DO_TASK), articleId, (double) new Date().getTime());
    }

    private Boolean checkArticleExist(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");

        // TODO 待优化走缓存
        Integer count = articleMapper.selectCount(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getArticleId, articleId)
                        .eq(Article::getStatus, ArticleStatusEnum.PUBLISHED.getStatus())
        );

        return count > 0;
    }

    @Override
    public void updateArticleCountBath(List<ArticleUpdateCountDTO> articleUpdateCountDTOList) {
        if (CollectionUtils.isEmpty(articleUpdateCountDTOList)) {
            return;
        }

        articleMapper.updateArticleCountBath(articleUpdateCountDTOList);
    }

    @Override
    public void increaseArticleLikeCount(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        AssertUtils.isTrue(checkArticleExist(articleId), "文章不存在");

        likeRecordService.increaseLikeRecord(articleId, LikeBizEnum.ARTICLE.getBiz());
    }

    @Override
    public Integer getArticleLikeCount(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");

        LikeRecordStrategy likeRecordStrategy = LikeRecordStrategyFactory.getLikeRecordStrategy(LikeBizEnum.ARTICLE.getBiz());
        likeRecordService.rebuildLikeCountCache(articleId, likeRecordStrategy);

        String likeCountKey = likeRecordStrategy.getCountCacheKey(articleId);
        String likeCountStr = RedisUtils.get(likeCountKey);

        return Integer.valueOf(likeCountStr);
    }

    @Override
    public Integer selectArticleLikeCount(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");

        return articleMapper.selectArticleLikeCount(articleId);
    }

    @Override
    public void increaseArticleCollectCount(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        AssertUtils.isTrue(checkArticleExist(articleId), "文章不存在");

        collectRecordService.increaseRecordRecord(articleId, LikeBizEnum.ARTICLE.getBiz());
    }

    @Override
    public Integer selectArticleCollectCount(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");

        return articleMapper.selectArticleCollectCount(articleId);
    }

    @Override
    public List<Article> selectArticleList(ArticleDTO articleDTO) {

        return articleMapper.selectArticleList(articleDTO);
    }

    @Override
    public void loadArticleCache(ArticlePreloadDTO articlePreloadDTO) {
        applicationContext.getBeansOfType(ArticleLoadStrategy.class).forEach((k, v)->{
            v.saveArticleCache(articlePreloadDTO);
        });
    }

}