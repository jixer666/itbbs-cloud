package com.abc.itbbs.common.ai.model;

import lombok.Data;

import java.util.Map;

@Data
public class Document {

    private String content;        // 文档内容
    private Map<String, Object> metadata; // 元数据

}
