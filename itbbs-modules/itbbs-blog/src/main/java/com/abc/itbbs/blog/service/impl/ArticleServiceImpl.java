package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.api.system.UserServiceClient;
import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.blog.constant.TemplateConstants;
import com.abc.itbbs.blog.service.TemplateService;
import com.abc.itbbs.common.core.constant.FileSuffixConstants;
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
import com.abc.itbbs.common.security.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 文章业务处理
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private UserServiceClient userServiceClient;

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
                    articleVO.setNickname(user.getNickname());
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

        article.setUpdateTime(new Date());

        articleMapper.updateById(article);
    }

    @Override
    public void saveArticle(ArticleDTO articleDTO) {
        articleDTO.checkSaveParams();
        Article article = ArticleConvert.buildDefaultArticleByArticleDTO(articleDTO);

        transactionTemplate.execute(item -> {
            articleMapper.insert(article);

            return article;
        });

        afterSaveArticle(article);
    }

    private void afterSaveArticle(Article article) {
        CompletableFuture<Void> genHtmlFuture = CompletableFuture.runAsync(() -> {
            // 保存静态文件
            templateService.saveStaticToOss(article.getArticleId() + FileSuffixConstants.HTML,
                    TemplateConstants.ARTICLE_TEMPLATE,
                    BeanUtil.beanToMap(article, false, true)
            );
        }, threadPoolTaskExecutor);


        // TODO 审核文件内容
        // TODO 保存向量数据库
        // TODO 保存ES


        CompletableFuture.allOf(genHtmlFuture).join();
    }

    @Override
    public void deleteArticle(ArticleDTO articleDTO) {
        articleDTO.checkDeleteParams();

        articleMapper.deleteBatchIds(articleDTO.getArticleIds());
    }
    

}