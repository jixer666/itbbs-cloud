package com.abc.itbbs.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.ai.constant.DocumentConstants;
import com.abc.itbbs.ai.factory.DocumentBizStrategyFactory;
import com.abc.itbbs.ai.service.DocumentService;
import com.abc.itbbs.ai.strategy.documentbiz.DocumentBizStrategy;
import com.abc.itbbs.ai.util.MilvusUtils;
import com.abc.itbbs.api.ai.domain.dto.DocumentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    @Value("${milvus.database}")
    private String database;

    @Override
    public void vectorSave(DocumentDTO documentDTO) {
        documentDTO.checkVectorSaveParams();

        DocumentBizStrategy documentBizStrategy = DocumentBizStrategyFactory.getDocumentBizStrategy(documentDTO.getBiz());
        List<Object> documentDTOList = documentBizStrategy.run(documentDTO.getObj());

        log.info("开始保存向量数据：{}", documentDTO);
        saveToMilvus(documentDTOList);
        log.info("完成保存向量数据：{}", documentDTO);
    }


    private void saveToMilvus(List<Object> vectorList) {
        if (CollUtil.isEmpty(vectorList)) {
            return;
        }

        MilvusUtils.insert(vectorList, database, DocumentConstants.DOCUMENT_CHUNK_COLLECTION);
    }

}
