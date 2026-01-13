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
import io.milvus.v2.service.vector.response.InsertResp;

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

    public static InsertResp insert(List<?> dataList, String collectionName) {
        if (CollUtil.isEmpty(dataList)) {
            return null;
        }

        Gson gson = new Gson();
        List<JsonObject> dataJsonObjectList = dataList.stream().map(item -> gson.toJsonTree(item).getAsJsonObject()).collect(Collectors.toList());

        InsertReq insertReq = InsertReq.builder()
                .collectionName(collectionName)
                .data(dataJsonObjectList)
                .build();

        return client.insert(insertReq);
    }


}
