package com.abc.itbbs.system.convert;


import com.abc.itbbs.common.core.util.IdUtils;
import com.abc.itbbs.common.core.util.StringUtils;
import com.abc.itbbs.system.domain.dto.GenerateTableColumnDTO;
import com.abc.itbbs.system.domain.entity.GenerateTableColumn;
import com.abc.itbbs.system.util.GenerateUtils;

public class GenerateTableColumnConvert {

    public static GenerateTableColumn buildDefaultGenerateTableColumnByGenerateTableColumnDTO(GenerateTableColumnDTO generateTableColumnDTO) {
        return null;
    }

    public static void initDefaultGenerateTableColumn(GenerateTableColumn generateTableColumn) {
        generateTableColumn.setGenTableColumnId(IdUtils.getId());
        generateTableColumn.setColumnType(GenerateUtils.getColumnType(generateTableColumn.getColumnType()));
        generateTableColumn.setJavaField(StringUtils.toCamelCase(generateTableColumn.getColumnName()));
        generateTableColumn.setJavaType(GenerateUtils.getJavaType(generateTableColumn.getColumnType()));
        generateTableColumn.setInsertParams();
    }
}
