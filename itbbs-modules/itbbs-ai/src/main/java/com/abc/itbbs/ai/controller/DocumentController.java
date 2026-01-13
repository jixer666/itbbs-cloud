package com.abc.itbbs.ai.controller;

import com.abc.itbbs.ai.service.DocumentService;
import com.abc.itbbs.api.ai.domain.dto.DocumentDTO;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "文档接口")
@RestController
@RequestMapping("/ai/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @ApiOperation("远程调用向量化存储")
    @PostMapping("/feign/vector/save")
    public ApiResult<Void> vectorSave(@RequestBody DocumentDTO documentDTO) {
        documentService.vectorSave(documentDTO);

        return ApiResult.success();
    }



}
