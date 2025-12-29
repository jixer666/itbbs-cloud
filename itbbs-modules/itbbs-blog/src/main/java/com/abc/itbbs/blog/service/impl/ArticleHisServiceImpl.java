package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.blog.convert.ArticleHisConvert;
import com.abc.itbbs.blog.domain.dto.ArticleHisDTO;
import com.abc.itbbs.blog.domain.entity.ArticleHis;
import com.abc.itbbs.blog.domain.vo.ArticleHisVO;
import com.abc.itbbs.blog.mapper.ArticleHisMapper;
import com.abc.itbbs.blog.service.ArticleHisService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章历史修订业务处理
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Service
public class ArticleHisServiceImpl extends BaseServiceImpl<ArticleHisMapper, ArticleHis> implements ArticleHisService {

    @Autowired
    private ArticleHisMapper articleHisMapper;

    @Override
    public PageResult getArticleHisPageWithUiParam(ArticleHisDTO articleHisDTO) {
        startPage();
        List<ArticleHis> articleHiss = articleHisMapper.selectArticleHisList(articleHisDTO);
        List<ArticleHisVO> articleHisVOList = pageList2CustomList(articleHiss, (List<ArticleHis> list) -> {
            return BeanUtil.copyToList(list, ArticleHisVO.class);
        });

        return buildPageResult(articleHisVOList);
    }

    @Override
    public void updateArticleHis(ArticleHisDTO articleHisDTO) {
        articleHisDTO.checkUpdateParams();
        ArticleHis articleHis = articleHisMapper.selectById(articleHisDTO.getArticleHisId());
        AssertUtils.isNotEmpty(articleHis, "文章历史修订不存在");
        BeanUtils.copyProperties(articleHisDTO, articleHis);
        articleHisMapper.updateById(articleHis);
    }

    @Override
    public void saveArticleHis(ArticleHisDTO articleHisDTO) {
        articleHisDTO.checkSaveParams();
        ArticleHis articleHis = ArticleHisConvert.buildDefaultArticleHisByArticleHisDTO(articleHisDTO);
        articleHisMapper.insert(articleHis);
    }

    @Override
    public void deleteArticleHis(ArticleHisDTO articleHisDTO) {
        articleHisDTO.checkDeleteParams();

        articleHisMapper.deleteBatchIds(articleHisDTO.getArticleHisIds());
    }
    

}