package com.abc.itbbs.common.ai.strategy.splitter;

import com.abc.itbbs.common.ai.model.DocumentChunk;

import java.util.*;

/**
 * 递归分词器
 */
public class RecursiveDocumentSplitter implements DocumentSplitter {
    
    // 默认分隔符（按优先级排序）
    private static final List<String> DEFAULT_SEPARATORS = Arrays.asList(
        "\n\n\n",     // 大段落分隔
        "\n\n",       // 段落分隔
        "\n",         // 换行
        "。", "？", "！",  // 中文句末标点
        "；",          // 分号
        "，",          // 逗号
        " ",          // 空格
        ""            // 最后按字符分割（fallback）
    );
    
    private final int chunkSize;          // 目标块大小（字符数）
    private final int chunkOverlap;       // 块重叠大小
    private final List<String> separators; // 分隔符列表（按优先级）
    
    public RecursiveDocumentSplitter(int chunkSize, int chunkOverlap) {
        this(chunkSize, chunkOverlap, DEFAULT_SEPARATORS);
    }
    
    public RecursiveDocumentSplitter(int chunkSize, int chunkOverlap, List<String> separators) {
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.separators = new ArrayList<>(separators);
        // 确保有空字符串作为fallback
        if (!this.separators.contains("")) {
            this.separators.add("");
        }
    }
    
    /**
     * 递归分割文本
     */
    public List<DocumentChunk> split(String text) {
        return recursiveSplit(text, 0);
    }
    
    /**
     * 递归分割核心算法
     */
    private List<DocumentChunk> recursiveSplit(String text, int startIndex) {
        List<DocumentChunk> chunks = new ArrayList<>();
        
        // 如果文本长度小于等于块大小，直接返回
        if (text.length() <= chunkSize) {
            chunks.add(new DocumentChunk(text, startIndex, startIndex + text.length()));
            return chunks;
        }
        
        // 尝试使用各个分隔符进行分割
        String separator = findBestSeparator(text);
        List<String> parts = splitBySeparator(text, separator);
        
        // 如果没有合适的分隔符，回退到字符分割
        if (parts.isEmpty() || parts.size() == 1) {
            return splitByCharacters(text, startIndex);
        }
        
        // 递归处理每个部分
        int currentIndex = startIndex;
        for (String part : parts) {
            if (part.length() <= chunkSize) {
                chunks.add(new DocumentChunk(part, currentIndex, currentIndex + part.length()));
            } else {
                // 递归分割过大的部分
                chunks.addAll(recursiveSplit(part, currentIndex));
            }
            currentIndex += part.length() + separator.length();
        }
        
        // 应用重叠策略
        return applyOverlap(chunks);
    }
    
    /**
     * 找到最佳分隔符
     */
    private String findBestSeparator(String text) {
        for (String separator : separators) {
            if (separator.isEmpty()) {
                continue; // 空字符串是fallback，最后使用
            }
            
            // 检查分隔符是否在文本中出现，并且能产生合适的分块
            if (text.contains(separator)) {
                List<String> parts = splitBySeparator(text, separator);
                
                // 检查分割结果是否合理
                if (isGoodSplit(parts)) {
                    return separator;
                }
            }
        }
        
        // 如果没有找到合适的分隔符，使用空字符串（字符分割）
        return "";
    }
    
    /**
     * 判断分割结果是否合理
     */
    private boolean isGoodSplit(List<String> parts) {
        if (parts.isEmpty()) {
            return false;
        }
        
        // 检查是否有部分过大需要继续分割
        boolean hasOversized = false;
        boolean hasGoodSized = false;
        
        for (String part : parts) {
            if (part.length() > chunkSize) {
                hasOversized = true;
            }
            if (part.length() >= chunkSize * 0.5 && part.length() <= chunkSize) {
                hasGoodSized = true;
            }
        }
        
        // 如果所有部分都合适，或者虽然有大的但也有合适的部分
        return !hasOversized || hasGoodSized;
    }
    
    /**
     * 按分隔符分割文本
     */
    private List<String> splitBySeparator(String text, String separator) {
        if (separator.isEmpty()) {
            return Collections.singletonList(text);
        }
        
        List<String> parts = new ArrayList<>();
        int start = 0;
        int end;
        
        while ((end = text.indexOf(separator, start)) != -1) {
            parts.add(text.substring(start, end));
            start = end + separator.length();
        }
        
        // 添加最后一部分
        if (start < text.length()) {
            parts.add(text.substring(start));
        }
        
        return parts;
    }
    
    /**
     * 按字符分割（fallback）
     */
    private List<DocumentChunk> splitByCharacters(String text, int startIndex) {
        List<DocumentChunk> chunks = new ArrayList<>();
        int length = text.length();
        int index = 0;
        
        while (index < length) {
            int end = Math.min(index + chunkSize, length);
            chunks.add(new DocumentChunk(text.substring(index, end),
                                   startIndex + index, 
                                   startIndex + end));
            index = end - chunkOverlap; // 应用重叠
        }
        
        return chunks;
    }
    
    /**
     * 应用重叠策略
     */
    private List<DocumentChunk> applyOverlap(List<DocumentChunk> chunks) {
        if (chunkOverlap <= 0) {
            return chunks;
        }
        
        List<DocumentChunk> overlappedChunks = new ArrayList<>();
        
        for (int i = 0; i < chunks.size(); i++) {
            DocumentChunk current = chunks.get(i);
            
            // 如果是第一个块或者最后一个块，保持原样
            if (i == 0 || i == chunks.size() - 1) {
                overlappedChunks.add(current);
                continue;
            }
            
            // 添加上下文重叠
            DocumentChunk previous = chunks.get(i - 1);
            int overlapStart = Math.max(previous.getStartIndex(), 
                                       current.getStartIndex() - chunkOverlap);
            
            if (overlapStart < current.getStartIndex()) {
                // 创建带有重叠的块
                String overlappedText = previous.getContent().substring(
                    overlapStart - previous.getStartIndex()
                ) + current.getContent();
                
                overlappedChunks.add(new DocumentChunk(
                    overlappedText,
                    overlapStart,
                    current.getEndIndex()
                ));
            } else {
                overlappedChunks.add(current);
            }
        }
        
        return overlappedChunks;
    }
}