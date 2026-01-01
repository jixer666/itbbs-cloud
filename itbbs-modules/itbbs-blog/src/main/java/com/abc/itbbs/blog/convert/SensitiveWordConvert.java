package com.abc.itbbs.blog.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.blog.domain.dto.SensitiveWordDTO;
import com.abc.itbbs.blog.domain.entity.SensitiveWord;
import com.abc.itbbs.common.core.util.IdUtils;

/**
 * 敏感词转换器
 *
 * @author LiJunXi
 * @date 2025-12-31
 */
public class SensitiveWordConvert {
    public static SensitiveWord buildDefaultSensitiveWordBySensitiveWordDTO(SensitiveWordDTO sensitiveWordDTO) {
        SensitiveWord sensitiveWord = BeanUtil.copyProperties(sensitiveWordDTO, SensitiveWord.class);
        sensitiveWord.setWordId(IdUtils.getId());
        sensitiveWord.setInsertParams();

        return sensitiveWord;
    }
}
