package com.abc.itbbs.blog.domain.dto.algorithm;

import com.abc.itbbs.blog.domain.entity.SensitiveWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrieNode {

    /** 子节点 */
    private final Map<Character, TrieNode> children = new HashMap<>();

    /** 是否是词尾 */
    private boolean end;

    /** 词尾存放的敏感词对象（支持一个词多条记录） */
    private List<SensitiveWord> payload;

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public List<SensitiveWord> getPayload() {
        return payload;
    }

    public void addPayload(SensitiveWord word) {
        if (payload == null) {
            payload = new ArrayList<>();
        }
        payload.add(word);
    }
}