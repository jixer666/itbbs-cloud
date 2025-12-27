package com.abc.itbbs.system.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.system.domain.dto.DictDataDTO;
import com.abc.itbbs.system.domain.entity.DictData;
import com.abc.itbbs.system.domain.vo.DictDataVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DictDataService extends IService<DictData> {

    PageResult getDictDataPageWithUiParam(DictDataDTO dictDataDTO);

    void updateDictData(DictDataDTO dictDataDTO);

    void saveDictData(DictDataDTO dictDataDTO);

    void deleteDictData(DictDataDTO dictDataDTO);

    List<DictDataVO> getDictDataByDictKey(String dictKey);
}
