package com.abc.itbbs.blog.domain.dto.algorithm;

import com.abc.itbbs.blog.domain.entity.SensitiveWord;
import com.abc.itbbs.common.core.util.StringUtils;

import java.util.*;

public class SensitiveWordTrie {

    private final TrieNode root = new TrieNode();

    /* ===================== 构建 ===================== */

    public void buildTrie(List<SensitiveWord> words) {
        for (SensitiveWord sw : words) {
            if (sw == null || sw.getWord() == null || StringUtils.isEmpty(sw.getWord())) {
                continue;
            }

            TrieNode node = root;
            for (char c : sw.getWord().toCharArray()) {
                node = node.getChildren()
                        .computeIfAbsent(c, k -> new TrieNode());
            }
            node.setEnd(true);
            node.addPayload(sw);
        }
    }

    /* ===================== 查找 ===================== */

    /** 查找所有命中（返回命中位置 + 对象） */
    public List<SensitiveHit> findHits(String text) {
        List<SensitiveHit> hits = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return hits;
        }

        int length = text.length();
        for (int i = 0; i < length; i++) {
            TrieNode node = root;
            int j = i;

            while (j < length) {
                char c = text.charAt(j);
                node = node.getChildren().get(c);
                if (node == null) break;

                if (node.isEnd() && node.getPayload() != null) {
                    for (SensitiveWord sw : node.getPayload()) {
                        hits.add(new SensitiveHit(i, j, sw));
                    }
                }
                j++;
            }
        }
        return hits;
    }

    /** 是否包含敏感词 */
    public boolean contains(String text) {
        return !findHits(text).isEmpty();
    }

    /* ===================== 替换 ===================== */

    public String replace(String text, char replaceChar) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        char[] chars = text.toCharArray();
        int length = chars.length;

        for (int i = 0; i < length; i++) {
            TrieNode node = root;
            int j = i;

            while (j < length) {
                char c = chars[j];
                node = node.getChildren().get(c);
                if (node == null) break;

                if (node.isEnd()) {
                    for (int k = i; k <= j; k++) {
                        chars[k] = replaceChar;
                    }
                }
                j++;
            }
        }
        return new String(chars);
    }
}
