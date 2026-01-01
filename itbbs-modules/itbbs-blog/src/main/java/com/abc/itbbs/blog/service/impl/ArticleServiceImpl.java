package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.api.system.UserServiceClient;
import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.api.system.domain.vo.UserVO;
import com.abc.itbbs.blog.domain.dto.ArticleHisDTO;
import com.abc.itbbs.blog.service.ArticleHisService;
import com.abc.itbbs.common.core.constant.ServiceConstants;
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
import com.abc.itbbs.common.security.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章业务处理
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
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
            filePath = ServiceConstants.OSS_SERVICE + "/article" + filePath;
        }

        lambdaUpdate().set(Article::getHtmlFilePath, filePath)
                .eq(Article::getArticleId, articleId)
                .update();
    }
}