package com.abc.itbbs.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.abc.itbbs.ai.constant.DocumentConstants;
import com.abc.itbbs.ai.service.DocumentService;
import com.abc.itbbs.ai.util.MilvusUtils;
import com.abc.itbbs.api.ai.domain.dto.DocumentDTO;
import com.abc.itbbs.common.ai.model.DocumentChunk;
import com.abc.itbbs.common.ai.strategy.splitter.DocumentSplitter;
import com.abc.itbbs.common.ai.strategy.splitter.RecursiveDocumentSplitter;
import io.milvus.v2.client.MilvusClientV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private MilvusClientV2 milvusClientV2;

    @Override
    public void vectorSave(DocumentDTO documentDTO) {
        documentDTO.checkVectorSaveParams();

        List<DocumentChunk> documentChunks = splitDocument(documentDTO);

        saveToMilvus(documentChunks);
    }

    private void saveToMilvus(List<DocumentChunk> documentChunks) {
        if (CollUtil.isEmpty(documentChunks)) {
            return;
        }

        MilvusUtils.insert(documentChunks, DocumentConstants.DOCUMENT_CHUNK_COLLECTION);
    }

    private List<DocumentChunk> splitDocument(DocumentDTO documentDTO) {
        DocumentSplitter documentSplitter = new RecursiveDocumentSplitter(
                DocumentConstants.CHUNK_SIZE, DocumentConstants.CHUNK_OVER_LAP
        );
        return documentSplitter.split(documentDTO.getContent());
    }


}
