package com.abc.itbbs.system.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.common.security.util.SecurityUtils;
import com.abc.itbbs.system.domain.dto.DictDTO;
import com.abc.itbbs.system.domain.entity.Dict;

public class DictConvert {
    public static Dict buildDefaultDictByDictDTO(DictDTO dictDTO) {
        Dict dict = BeanUtil.copyProperties(dictDTO, Dict.class);
        dict.setDictId(IdUtils.getId());
        dict.setUserId(SecurityUtils.getUserId());
        dict.setInsertParams();

        return dict;
    }
}
