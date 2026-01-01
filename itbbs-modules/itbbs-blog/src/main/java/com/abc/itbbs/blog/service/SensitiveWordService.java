package com.abc.itbbs.blog.service;

import com.abc.itbbs.blog.domain.dto.SensitiveWordDTO;
import com.abc.itbbs.blog.domain.entity.SensitiveWord;
import com.abc.itbbs.blog.domain.enums.SensitiveWordLevelEnum;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 敏感词接口
 *
 * @author LiJunXi
 * @date 2025-12-31
 */
public interface SensitiveWordService extends IService<SensitiveWord> {

    PageResult getSensitiveWordPageWithUiParam(SensitiveWordDTO sensitiveWordDTO);

    void updateSensitiveWord(SensitiveWordDTO sensitiveWordDTO);

    void saveSensitiveWord(SensitiveWordDTO sensitiveWordDTO);

    void deleteSensitiveWord(SensitiveWordDTO sensitiveWordDTO);

    List<SensitiveWord> getSensitiveWordList();

    Boolean handleArticleContent(Long articleId, String content);

    SensitiveWordLevelEnum checkContent(String content);
}
