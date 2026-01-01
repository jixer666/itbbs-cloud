package com.abc.itbbs.blog.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.blog.domain.dto.DraftDTO;
import com.abc.itbbs.blog.domain.entity.Draft;

/**
 * 草稿转换器
 *
 * @author LiJunXi
 * @date 2025-12-29
 */
public class DraftConvert {
    public static Draft buildDefaultDraftByDraftDTO(DraftDTO draftDTO) {
        Draft draft = BeanUtil.copyProperties(draftDTO, Draft.class);
        draft.setDraftId(IdUtils.getId());
        draft.setInsertParams();

        return draft;
    }
}
