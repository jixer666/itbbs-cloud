package com.abc.itbbs.ai.runner;

import com.abc.itbbs.ai.constant.DocumentConstants;
import com.abc.itbbs.ai.constant.document.ArticleDocumentEntityConstants;
import com.abc.itbbs.ai.util.MilvusUtils;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.database.response.ListDatabasesResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化向量数据库和Collection
 * @author LiJunXi
 * @date 2026/1/13
 */
@Slf4j
@Component
public class MlivusInitRunner implements ApplicationRunner {

    @Value("${milvus.database}")
    private String database;

    @Override
    public void run(ApplicationArguments args) {
        createDatabase();
        createCollections();
    }

    private void createDatabase() {
        Boolean isDatabaseExist = MilvusUtils.hashDatabase(database);
        if (!isDatabaseExist) {
            log.info("向量数据库[{}]不存在，正在创建", database);
            MilvusUtils.createDatabase(database);
            log.info("向量数据库[{}]创建成功", database);
        }
    }

    private void createCollections() {
        Boolean hasDocumentChunkCollection = MilvusUtils.hashCollection(database, DocumentConstants.DOCUMENT_CHUNK_COLLECTION);
        if (!hasDocumentChunkCollection) {
            log.info("向量Collection[{}]不存在，正在创建", DocumentConstants.DOCUMENT_CHUNK_COLLECTION);
            createDocumentCollection();
            log.info("向量Collection[{}]创建成功", DocumentConstants.DOCUMENT_CHUNK_COLLECTION);
        }
    }

    private void createDocumentCollection() {
        MilvusClientV2 client = MilvusUtils.getClient();
        CreateCollectionReq.CollectionSchema schema = client.createSchema();
        schema.addField(AddFieldReq.builder()
                .fieldName(ArticleDocumentEntityConstants.DOCUMENT_CHUNK_ID)
                .dataType(DataType.Int64)
                .isPrimaryKey(true)
                .autoID(false)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName(ArticleDocumentEntityConstants.ARTICLE_ID)
                .dataType(DataType.Int64)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName(ArticleDocumentEntityConstants.DOCUMENT_CHUNK)
                .dataType(DataType.VarChar)
                .maxLength(600)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName(ArticleDocumentEntityConstants.DOCUMENT_CHUNK_VECTOR)
                .dataType(DataType.FloatVector)
                .dimension(768)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName(ArticleDocumentEntityConstants.CREATE_AT)
                .dataType(DataType.Int64)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName(ArticleDocumentEntityConstants.UPDATE_AT)
                .dataType(DataType.Int64)
                .build());


        IndexParam indexParamForIdField = IndexParam.builder()
                .fieldName(ArticleDocumentEntityConstants.ARTICLE_ID)
                .indexType(IndexParam.IndexType.STL_SORT)
                .build();
        IndexParam indexParamForVectorField = IndexParam.builder()
                .fieldName(ArticleDocumentEntityConstants.DOCUMENT_CHUNK_VECTOR)
                .indexType(IndexParam.IndexType.IVF_FLAT)
                .metricType(IndexParam.MetricType.COSINE) // 用余弦相似度
                .build();

        List<IndexParam> indexParams = new ArrayList<>();
        indexParams.add(indexParamForIdField);
        indexParams.add(indexParamForVectorField);

        CreateCollectionReq createCollectionReq = CreateCollectionReq.builder()
                .collectionName(DocumentConstants.DOCUMENT_CHUNK_COLLECTION)
                .databaseName(database)
                .collectionSchema(schema)
                .indexParams(indexParams)
                .build();

        client.createCollection(createCollectionReq);
    }
}
