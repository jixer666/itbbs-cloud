package com.abc.itbbs.system.mapper;

import com.abc.itbbs.system.domain.dto.GenerateTableColumnDTO;
import com.abc.itbbs.system.domain.entity.GenerateTableColumn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GenerateTableColumnMapper extends BaseMapper<GenerateTableColumn> {
    List<GenerateTableColumn> selectGenerateTableColumnList(GenerateTableColumnDTO generateTableColumnDTO);

    List<GenerateTableColumn> selectDbTableColumnsByName(String tableName);

    void deleteGenerateTableColumnByGenTableIds(@Param("list") List<Long> genTableIds);
}
