package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.abc.itbbs.blog.convert.SensitiveWordConvert;
import com.abc.itbbs.blog.domain.dto.SensitiveWordDTO;
import com.abc.itbbs.blog.domain.dto.algorithm.SensitiveHit;
import com.abc.itbbs.blog.domain.dto.algorithm.SensitiveWordTrie;
import com.abc.itbbs.blog.domain.entity.SensitiveWord;
import com.abc.itbbs.blog.domain.enums.ArticleStatusEnum;
import com.abc.itbbs.blog.domain.enums.SensitiveWordBizEnum;
import com.abc.itbbs.blog.domain.enums.SensitiveWordLevelEnum;
import com.abc.itbbs.blog.domain.vo.SensitiveWordVO;
import com.abc.itbbs.blog.factory.SensitiveWordLevelStrategyFactory;
import com.abc.itbbs.blog.mapper.SensitiveWordMapper;
import com.abc.itbbs.blog.service.ArticleService;
import com.abc.itbbs.blog.service.SensitiveWordService;
import com.abc.itbbs.blog.strategy.sensitiveword.SensitiveWordLevelStrategy;
import com.abc.itbbs.common.core.domain.enums.StatusEnum;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.common.core.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 敏感词业务处理
 *
 * @author LiJunXi
 * @date 2025-12-31
 */
@Service
public class SensitiveWordServiceImpl extends BaseServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;

    @Override
    public PageResult getSensitiveWordPageWithUiParam(SensitiveWordDTO sensitiveWordDTO) {
        startPage();
        List<SensitiveWord> sensitiveWords = sensitiveWordMapper.selectSensitiveWordList(sensitiveWordDTO);
        List<SensitiveWordVO> sensitiveWordVOList = pageList2CustomList(sensitiveWords, (List<SensitiveWord> list) -> {
            return BeanUtil.copyToList(list, SensitiveWordVO.class);
        });

        return buildPageResult(sensitiveWordVOList);
    }

    @Override
    public void updateSensitiveWord(SensitiveWordDTO sensitiveWordDTO) {
        sensitiveWordDTO.checkUpdateParams();
        SensitiveWord sensitiveWord = sensitiveWordMapper.selectById(sensitiveWordDTO.getWordId());
        AssertUtils.isNotEmpty(sensitiveWord, "敏感词不存在");
        BeanUtils.copyProperties(sensitiveWordDTO, sensitiveWord);
        sensitiveWordMapper.updateById(sensitiveWord);
    }

    @Override
    public void saveSensitiveWord(SensitiveWordDTO sensitiveWordDTO) {
        sensitiveWordDTO.checkSaveParams();
        SensitiveWord sensitiveWord = SensitiveWordConvert.buildDefaultSensitiveWordBySensitiveWordDTO(sensitiveWordDTO);
        sensitiveWordMapper.insert(sensitiveWord);
    }

    @Override
    public void deleteSensitiveWord(SensitiveWordDTO sensitiveWordDTO) {
        sensitiveWordDTO.checkDeleteParams();

        sensitiveWordMapper.deleteBatchIds(sensitiveWordDTO.getSensitiveWordIds());
    }

    @Override
    public List<SensitiveWord> getSensitiveWordList() {
        return sensitiveWordMapper.selectList(
                new LambdaQueryWrapper<SensitiveWord>()
                 .select(
                         SensitiveWord::getWordId,
                         SensitiveWord::getWord,
                         SensitiveWord::getLevel
                 )
                .eq(SensitiveWord::getStatus, StatusEnum.NORMAL.getStatus())
        );
    }

    @Override
    public Boolean handleArticleContent(Long articleId, String content) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        SensitiveWordLevelEnum sensitiveWordLevelEnum = checkContent(content);

        if (Objects.isNull(sensitiveWordLevelEnum)) {
            ArticleService articleService = SpringUtil.getBean(ArticleService.class);
            articleService.updateStatus(articleId, ArticleStatusEnum.PUBLISHED.getStatus());

            // 验证通过
            return true;
        } else {
            SensitiveWordLevelStrategy sensitiveWordStrategy = SensitiveWordLevelStrategyFactory.getSensitiveWordStrategy(sensitiveWordLevelEnum.getType());
            sensitiveWordStrategy.run(SensitiveWordBizEnum.ARTICLE.getBiz(), articleId);

            // 验证未通过
            return false;
        }

    }

    @Override
    public SensitiveWordLevelEnum checkContent(String content) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        List<SensitiveHit> hits = new SensitiveWordTrie().findHits(content);
        if (CollUtil.isEmpty(hits)) {
            return null;
        }

        Optional<SensitiveHit> sensitiveHitOptional  = hits.stream().max(new Comparator<SensitiveHit>() {
            @Override
            public int compare(SensitiveHit o1, SensitiveHit o2) {
                return o1.getSensitiveWord().getLevel() - o2.getSensitiveWord().getLevel();
            }
        });
        if (!sensitiveHitOptional.isPresent()) {
            return null;
        }

        return SensitiveWordLevelEnum.getByType(sensitiveHitOptional.get().getSensitiveWord().getLevel());
    }
}