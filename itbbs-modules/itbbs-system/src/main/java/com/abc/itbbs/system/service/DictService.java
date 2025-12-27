package com.abc.itbbs.system.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.system.domain.dto.DictDTO;
import com.abc.itbbs.system.domain.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DictService extends IService<Dict> {

    PageResult getDictPageWithUiParam(DictDTO dictDTO);

    void updateDict(DictDTO dictDTO);

    void saveDict(DictDTO dictDTO);

    void deleteDict(DictDTO dictDTO);
}
