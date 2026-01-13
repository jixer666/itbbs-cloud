package com.abc.itbbs.common.ai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 文本块实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentChunk {
    private String content;           // 块文本内容
    private int startIndex;        // 在原文中的起始位置
    private int endIndex;          // 在原文中的结束位置
    private Map<String, Object> metadata; // 元数据
    
    public DocumentChunk(String content, int startIndex, int endIndex) {
        this(content, startIndex, endIndex, new HashMap<>());
    }
    
    public int length() {
        return content.length();
    }
}