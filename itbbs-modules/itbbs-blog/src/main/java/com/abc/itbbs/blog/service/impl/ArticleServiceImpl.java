package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.abc.itbbs.api.system.UserServiceClient;
import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.api.system.domain.vo.UserVO;
import com.abc.itbbs.blog.convert.LikeRecordConvert;
import com.abc.itbbs.blog.domain.dto.ArticleHisDTO;
import com.abc.itbbs.blog.domain.dto.ArticleUpdateCountDTO;
import com.abc.itbbs.blog.domain.dto.LikeRecordDTO;
import com.abc.itbbs.blog.domain.entity.LikeRecord;
import com.abc.itbbs.blog.domain.enums.ArticleStatusEnum;
import com.abc.itbbs.blog.domain.enums.LikeTypeEnum;
import com.abc.itbbs.blog.domain.vo.ArticleMetaVO;
import com.abc.itbbs.blog.service.ArticleHisService;
import com.abc.itbbs.blog.service.LikeRecordService;
import com.abc.itbbs.blog.util.BlogRedisUtils;
import com.abc.itbbs.common.core.constant.CommonConstants;
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

    @Override
    public PageResult getArticlePageWithUiParam(ArticleDTO articleDTO) {
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
        articleMetaVO.setCollectCount(CommonConstants.ZERO);

        return articleMetaVO;
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

        RLock lock = redissonClient.getLock(CacheConstants.getFinalKey(CacheConstants.ARTICLE_VIEW_LOCK, articleId));
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
            RedisUtils.set(CacheConstants.ARTICLE_VIEWS_COUNT, count, 24, TimeUnit.HOURS);

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
        RedisUtils.zAdd(CacheConstants.getFinalKey(CacheConstants.ARTICLE_WAIT_DO_TASK), articleId, (double) new Date().getTime());
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

        checkAndRebuildArticleLikeCountCache(articleId);

        String articleLikeCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_LIKE_COUNT, articleId);
        String userLikeSetKey = CacheConstants.getFinalKey(CacheConstants.USER_LIKE_SET, SecurityUtils.getUserId(), LikeTypeEnum.ARTICLE.getType());

        // 缓存点赞数量
        RedisUtils.inc(articleLikeCountKey, 24, TimeUnit.HOURS);

        // 缓存用户点赞列表
        Long size = RedisUtils.sGetSetSize(userLikeSetKey);
        if (size > CacheConstants.USER_LIKE_SET_LENGTH) {
            // 超过了最大点赞长度，抛弃第一个
            RedisUtils.zRemoveRange(userLikeSetKey, 0, 0);
        }
        RedisUtils.zAdd(userLikeSetKey, articleId, new Date().getTime());

        // 点赞数加入到待同步集合
        RedisUtils.zAdd(CacheConstants.getFinalKey(CacheConstants.ARTICLE_WAIT_DO_TASK), articleId, new Date().getTime());

        // 点赞记录加入到待同步集合
        LikeRecordDTO likeRecordDTO = LikeRecordConvert.buildDefaultLikeRecordDTO(articleId, LikeTypeEnum.ARTICLE.getType());
        RedisUtils.zAdd(CacheConstants.getFinalKey(CacheConstants.LIKE_RECORD_WAIT_DO_TASK), JSONUtil.toJsonStr(likeRecordDTO), new Date().getTime());
    }

    private void checkAndRebuildArticleLikeCountCache(Long articleId) {
        String articleLikeCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_LIKE_COUNT, articleId);
        String userLikeSetKey = CacheConstants.getFinalKey(CacheConstants.USER_LIKE_SET, SecurityUtils.getUserId(), LikeTypeEnum.ARTICLE.getType());

        Long isExist = BlogRedisUtils.checkArticleLikeCountKeyExist(articleLikeCountKey, userLikeSetKey);
        if (isExist != -1) {
          return;
        }

        // 需要重建缓存
        rebuildLikeCountCache(articleId);
        rebuildUserLikeSetCache(articleId);
    }

    private void rebuildUserLikeSetCache(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        String userLikeSetKey = CacheConstants.getFinalKey(CacheConstants.USER_LIKE_SET, SecurityUtils.getUserId(), LikeTypeEnum.ARTICLE.getType());

        // 第一层检查
        if (RedisUtils.hasKey(userLikeSetKey)) {
            return;
        }

        RLock lock = redissonClient.getLock(CacheConstants.getFinalKey(CacheConstants.USER_LIKE_SET_LOCK, articleId));
        try {
            // 等待5s，锁持有10s
            boolean isLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!isLock) {
                return;
            }

            // 第二层检查
            if (RedisUtils.hasKey(userLikeSetKey)) {
                return;
            }

            List<LikeRecord> likeRecords = likeRecordService.selectLikeTargetIdsByUserId(SecurityUtils.getUserId(), true);
            Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
            for (LikeRecord likeRecord : likeRecords) {
                tuples.add(new DefaultTypedTuple<>(likeRecord.getTargetId().toString(), likeRecord.getCreateTime().getTime() * 1.0));
            }
            // 防止缓存穿透
            tuples.add(new DefaultTypedTuple<>(CommonConstants.ZERO.toString(), Double.MAX_VALUE));
            RedisUtils.zAdd(userLikeSetKey, tuples);

        } catch (Exception e) {
            log.error("用户点赞集合重建加锁失败：{}", e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    private void rebuildLikeCountCache(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");
        String articleLikeCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_LIKE_COUNT, articleId);

        // 第一层检查
        if (RedisUtils.hasKey(articleLikeCountKey)) {
            return;
        }

        RLock lock = redissonClient.getLock(CacheConstants.getFinalKey(CacheConstants.ARTICLE_LIKE_LOCK, articleId));
        try {
            // 等待5s，锁持有10s
            boolean isLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!isLock) {
                return;
            }

            // 第二层检查
            if (RedisUtils.hasKey(articleLikeCountKey)) {
                return;
            }

            Integer count = articleMapper.selectArticleLikeCount(articleId);
            RedisUtils.set(articleLikeCountKey, count, 24, TimeUnit.HOURS);

        } catch (Exception e) {
            log.error("点赞重建加锁失败：{}", e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Integer getArticleLikeCount(Long articleId) {
        AssertUtils.isNotEmpty(articleId, "文章ID不能为空");

        rebuildLikeCountCache(articleId);

        String likeCountKey = CacheConstants.getFinalKey(CacheConstants.ARTICLE_LIKE_COUNT, articleId);
        String likeCountStr = RedisUtils.get(likeCountKey);

        return Integer.valueOf(likeCountStr);
    }
}