package com.abc.itbbs.system.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.common.security.util.SecurityUtils;
import com.abc.itbbs.system.domain.dto.DictDataDTO;
import com.abc.itbbs.system.domain.entity.DictData;
import com.abc.itbbs.system.domain.vo.DictDataVO;

import java.util.List;

public class DictDataConvert {

    public static DictData buildDefaultDictDataByDictDataDTO(DictDataDTO dictDataDTO) {
        DictData dictData = BeanUtil.copyProperties(dictDataDTO, DictData.class);
        dictData.setDictDataId(IdUtils.getId());
        dictData.setUserId(SecurityUtils.getUserId());
        dictData.setInsertParams();

        return dictData;
    }

    public static List<DictDataVO> converDictDataVoListByDictDataList(List<DictData> dictDataList) {
        return BeanUtil.copyToList(dictDataList, DictDataVO.class);
    }

}
