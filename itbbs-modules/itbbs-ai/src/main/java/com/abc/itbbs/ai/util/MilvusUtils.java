package com.abc.itbbs.ai.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.collection.request.HasCollectionReq;
import io.milvus.v2.service.database.request.CreateDatabaseReq;
import io.milvus.v2.service.database.response.ListDatabasesResp;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.data.BaseVector;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.InsertResp;
import io.milvus.v2.service.vector.response.SearchResp;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Milvus工具类
 */
public class MilvusUtils {

    private static MilvusClientV2 client;
    static {
        client = SpringUtil.getBean(MilvusClientV2.class);
    }

    public static MilvusClientV2 getClient() {
        return client;
    }

    public static void createDatabase(String database) {
        client.createDatabase(CreateDatabaseReq.builder()
                .databaseName(database)
                .build()
        );
    }

    public static ListDatabasesResp listDatabase() {
        return client.listDatabases();
    }

    public static Boolean hashDatabase(String database) {
        return listDatabase().getDatabaseNames().stream().anyMatch(item -> item.equalsIgnoreCase(database));
    }

    public static Boolean hashCollection(String database, String collection) {
        return client.hasCollection(
                HasCollectionReq.builder()
                        .databaseName(database)
                        .collectionName(collection)
                        .build()
        );
    }

    public static InsertResp insert(List<?> dataList, String database, String collectionName) {
        if (CollUtil.isEmpty(dataList)) {
            return null;
        }

        Gson gson = new Gson();
        List<JsonObject> dataJsonObjectList = dataList.stream().map(item -> gson.toJsonTree(item).getAsJsonObject()).collect(Collectors.toList());

        InsertReq insertReq = InsertReq.builder()
                .databaseName(database)
                .collectionName(collectionName)
                .data(dataJsonObjectList)
                .build();

        return client.insert(insertReq);
    }

    public static SearchResp searchSingle(String database, String collection, List<Float> vectorList, String field, Integer topK) {
        FloatVec queryVector = new FloatVec(vectorList);
        List<BaseVector> floatVecList = Collections.singletonList(queryVector);

        SearchReq searchReq = SearchReq.builder()
                .databaseName(database)
                .collectionName(collection)
                .data(floatVecList)
                .annsField(field)
                .topK(topK)
                .build();

        return client.search(searchReq);
    }

}
