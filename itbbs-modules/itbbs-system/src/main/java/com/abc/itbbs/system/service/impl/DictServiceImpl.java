package com.abc.itbbs.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.system.convert.DictConvert;
import com.abc.itbbs.system.domain.dto.DictDTO;
import com.abc.itbbs.system.domain.entity.Dict;
import com.abc.itbbs.system.domain.vo.DictVO;
import com.abc.itbbs.system.mapper.DictMapper;
import com.abc.itbbs.system.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends BaseServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public PageResult getDictPageWithUiParam(DictDTO dictDTO) {
        startPage();
        List<Dict> dicts = dictMapper.selectDictList(dictDTO);
        List<DictVO> dictVOList = pageList2CustomList(dicts, (List<Dict> list) -> {
            return BeanUtil.copyToList(list, DictVO.class);
        });

        return buildPageResult(dictVOList);
    }

    @Override
    public void updateDict(DictDTO dictDTO) {
        dictDTO.checkUpdateParams();
        Dict dict = dictMapper.selectById(dictDTO.getDictId());
        AssertUtils.isNotEmpty(dict, "字典不存在");
        BeanUtils.copyProperties(dictDTO, dict);
        dictMapper.updateById(dict);
    }

    @Override
    public void saveDict(DictDTO dictDTO) {
        dictDTO.checkSaveParams();
        Dict dict = DictConvert.buildDefaultDictByDictDTO(dictDTO);
        dictMapper.insert(dict);
    }

    @Override
    public void deleteDict(DictDTO dictDTO) {
        dictDTO.checkDeleteParams();

        dictMapper.deleteBatchIds(dictDTO.getDictIds());
    }



}
