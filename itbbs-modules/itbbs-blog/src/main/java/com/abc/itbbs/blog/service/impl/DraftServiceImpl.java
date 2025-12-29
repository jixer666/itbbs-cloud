package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.blog.convert.DraftConvert;
import com.abc.itbbs.blog.domain.dto.DraftDTO;
import com.abc.itbbs.blog.domain.entity.Draft;
import com.abc.itbbs.blog.domain.vo.DraftVO;
import com.abc.itbbs.blog.mapper.DraftMapper;
import com.abc.itbbs.blog.service.DraftService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 草稿业务处理
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
@Service
public class DraftServiceImpl extends BaseServiceImpl<DraftMapper, Draft> implements DraftService {

    @Autowired
    private DraftMapper draftMapper;

    @Override
    public PageResult getDraftPageWithUiParam(DraftDTO draftDTO) {
        startPage();
        List<Draft> drafts = draftMapper.selectDraftList(draftDTO);
        List<DraftVO> draftVOList = pageList2CustomList(drafts, (List<Draft> list) -> {
            return BeanUtil.copyToList(list, DraftVO.class);
        });

        return buildPageResult(draftVOList);
    }

    @Override
    public void updateDraft(DraftDTO draftDTO) {
        draftDTO.checkUpdateParams();
        Draft draft = draftMapper.selectById(draftDTO.getDraftId());
        AssertUtils.isNotEmpty(draft, "草稿不存在");
        BeanUtils.copyProperties(draftDTO, draft);
        draftMapper.updateById(draft);
    }

    @Override
    public void saveDraft(DraftDTO draftDTO) {
        draftDTO.checkSaveParams();
        Draft draft = DraftConvert.buildDefaultDraftByDraftDTO(draftDTO);
        draftMapper.insert(draft);
    }

    @Override
    public void deleteDraft(DraftDTO draftDTO) {
        draftDTO.checkDeleteParams();

        draftMapper.deleteBatchIds(draftDTO.getDraftIds());
    }
    

}