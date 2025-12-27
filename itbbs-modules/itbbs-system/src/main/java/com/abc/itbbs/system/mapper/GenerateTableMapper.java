package com.abc.itbbs.system.mapper;

import com.abc.itbbs.system.domain.dto.GenerateTableDTO;
import com.abc.itbbs.system.domain.entity.GenerateTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GenerateTableMapper extends BaseMapper<GenerateTable> {
    List<GenerateTable> selectGenerateTableList(GenerateTableDTO generateTableDTO);

    List<GenerateTable> selectDbTableList(GenerateTableDTO generateTableTableDTO);

    List<GenerateTable> selectDbTableListByNames(@Param("list") List<String> tableNames);

    GenerateTable selectGenerateTableByGenTableId(Long genTableId);

    List<GenerateTable> selectGenerateTableByGenTableIds(@Param("list") List<Long> genTableIds);

}
