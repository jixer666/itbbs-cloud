package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.blog.service.TemplateService;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.blog.convert.ArticleConvert;
import com.abc.itbbs.blog.domain.dto.ArticleDTO;
import com.abc.itbbs.blog.domain.entity.Article;
import com.abc.itbbs.blog.domain.vo.ArticleVO;
import com.abc.itbbs.blog.mapper.ArticleMapper;
import com.abc.itbbs.blog.service.ArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

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

    @Override
    public PageResult getArticlePageWithUiParam(ArticleDTO articleDTO) {
        startPage();
        List<Article> articles = articleMapper.selectArticleList(articleDTO);
        List<ArticleVO> articleVOList = pageList2CustomList(articles, (List<Article> list) -> {
            return BeanUtil.copyToList(list, ArticleVO.class);
        });

        return buildPageResult(articleVOList);
    }

    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        articleDTO.checkUpdateParams();
        Article article = articleMapper.selectById(articleDTO.getArticleId());
        AssertUtils.isNotEmpty(article, "文章不存在");
        BeanUtils.copyProperties(articleDTO, article);
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
        // 保存静态文件
        saveArticleStaticHtmlToOss(article);

        // TODO 保存向量数据库
        // TODO 保存ES
    }

    private void saveArticleStaticHtmlToOss(Article article) {
        templateService.saveStaticHtmlToOss("", BeanUtil.beanToMap(article, false, true));
    }

    @Override
    public void deleteArticle(ArticleDTO articleDTO) {
        articleDTO.checkDeleteParams();

        articleMapper.deleteBatchIds(articleDTO.getArticleIds());
    }
    

}