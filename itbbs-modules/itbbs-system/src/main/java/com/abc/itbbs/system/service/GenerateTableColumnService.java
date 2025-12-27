package com.abc.itbbs.system.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.system.domain.dto.GenerateTableColumnDTO;
import com.abc.itbbs.system.domain.entity.GenerateTableColumn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface GenerateTableColumnService extends IService<GenerateTableColumn> {

    PageResult getGenerateTableColumnPageWithUiParam(GenerateTableColumnDTO menuDTO);

    void updateGenerateTableColumn(GenerateTableColumnDTO menuDTO);

    void saveGenerateTableColumn(GenerateTableColumnDTO menuDTO);

    void deleteGenerateTableColumn(GenerateTableColumnDTO menuDTO);

    void importTableColumn(String tableName, Long generateTableId);

    void updateGenerateTableColumns(List<GenerateTableColumn> tableColumns);

    void deleteGenerateTableColumnByGenTableIds(List<Long> genTableIds);
}
