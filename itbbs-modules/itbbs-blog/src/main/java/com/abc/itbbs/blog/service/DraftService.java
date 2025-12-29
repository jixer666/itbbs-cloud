package com.abc.itbbs.blog.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.domain.dto.DraftDTO;
import com.abc.itbbs.blog.domain.entity.Draft;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 草稿接口
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
public interface DraftService extends IService<Draft> {

    PageResult getDraftPageWithUiParam(DraftDTO draftDTO);

    void updateDraft(DraftDTO draftDTO);

    void saveDraft(DraftDTO draftDTO);

    void deleteDraft(DraftDTO draftDTO);
}
