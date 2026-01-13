package com.abc.itbbs.ai.runner;

import com.abc.itbbs.ai.constant.DocumentConstants;
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
                .fieldName("document_chunk_id")
                .dataType(DataType.Int64)
                .isPrimaryKey(true)
                .autoID(false)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("article_id")
                .dataType(DataType.Int64)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("document_chunk_vector")
                .dataType(DataType.FloatVector)
                .dimension(5)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("created_at")
                .dataType(DataType.Int64)
                .build());
        schema.addField(AddFieldReq.builder()
                .fieldName("updated_at")
                .dataType(DataType.Int64)
                .build());


        IndexParam indexParamForIdField = IndexParam.builder()
                .fieldName("article_id")
                .indexType(IndexParam.IndexType.STL_SORT)
                .build();
        IndexParam indexParamForVectorField = IndexParam.builder()
                .fieldName("document_chunk_vector")
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
