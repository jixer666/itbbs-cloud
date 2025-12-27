package com.abc.itbbs.system.service;

import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.system.domain.dto.GenerateTableDTO;
import com.abc.itbbs.system.domain.entity.GenerateTable;
import com.abc.itbbs.system.domain.vo.GenerateTablePreviewVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.http.ResponseEntity;

public interface GenerateTableService extends IService<GenerateTable> {

    PageResult getGenerateTablePageWithUiParam(GenerateTableDTO menuDTO);

    void updateGenerateTable(GenerateTableDTO menuDTO);

    void saveGenerateTable(GenerateTableDTO menuDTO);

    void deleteGenerateTable(GenerateTableDTO menuDTO);

    PageResult getGenerateDbPageWithUiParam(GenerateTableDTO generateTableTableDTO);

    void importTable(GenerateTableDTO generateTableTableDTO);

    GenerateTablePreviewVO previewCode(Long genTableId);

    GenerateTable getGenerateTableByGenTableId(Long genTableId);

    ResponseEntity<byte[]> downloadCode(GenerateTableDTO generateTableDTO);
}
