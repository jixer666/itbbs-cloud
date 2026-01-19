package com.abc.itbbs.common.core.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.List;

public class JsoupUtils {

    public static String parseHtml(String htmlContent) {
        String plainText = Jsoup.parse(htmlContent).text();
        // 优化处理：去除换行和多余空格
        plainText = plainText.replaceAll("\\s+", " ").trim();

        return plainText;
    }



    /**
     * 智能截断HTML内容，保持标签完整
     * @param html HTML内容
     * @param maxChars 最大字符数
     * @return 截断后的HTML（保持结构完整）
     */
    public static String truncateHtml(String html, int maxChars) {
        if (html == null || html.trim().isEmpty()) {
            return "";
        }

        Document doc = Jsoup.parse(html);
        Element body = doc.body();

        int[] charCount = {0};
        boolean[] shouldStop = {false};

        // 递归遍历节点
        truncateNode(body, charCount, maxChars, shouldStop);

        return body.html();
    }

    private static void truncateNode(Node node, int[] charCount, int maxChars, boolean[] shouldStop) {
        if (shouldStop[0]) {
            node.remove();
            return;
        }

        if (node instanceof TextNode) {
            TextNode textNode = (TextNode) node;
            String text = textNode.getWholeText();

            if (charCount[0] + text.length() <= maxChars) {
                // 文本完全保留
                charCount[0] += text.length();
            } else {
                // 需要截断文本
                int remaining = maxChars - charCount[0];
                if (remaining > 0) {
                    // 智能截断：避免在中文中间截断
                    String truncatedText = smartTruncateText(text, remaining);
                    textNode.text(truncatedText);
                    charCount[0] = maxChars;
                    shouldStop[0] = true;

                    // 移除后面的兄弟节点
                    removeFollowingSiblings(node.parentNode(), node);
                } else {
                    textNode.text("");
                    shouldStop[0] = true;
                }
            }
        }

        // 递归处理子节点
        if (!shouldStop[0] && node.childNodeSize() > 0) {
            List<Node> children = node.childNodes();
            for (int i = 0; i < children.size(); i++) {
                truncateNode(children.get(i), charCount, maxChars, shouldStop);
                if (shouldStop[0]) {
                    break;
                }
            }
        }
    }

    private static String smartTruncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }

        String truncated = text.substring(0, maxLength);

        // 1. 尝试在标点符号处截断
        int lastPunctuation = findLastPunctuation(truncated);
        if (lastPunctuation > maxLength * 0.7) {
            return text.substring(0, lastPunctuation + 1);
        }

        // 2. 在HTML实体边界处理（如&nbsp;）
        if (truncated.contains("&")) {
            // 确保不截断HTML实体
            int lastAmp = truncated.lastIndexOf('&');
            if (lastAmp > maxLength - 10) { // 实体可能在边界
                // 查找实体结束
                for (int i = lastAmp; i < Math.min(lastAmp + 10, text.length()); i++) {
                    if (text.charAt(i) == ';') {
                        return text.substring(0, i + 1);
                    }
                }
                // 如果没有找到分号，回退到前一个位置
                return text.substring(0, lastAmp);
            }
        }

        // 3. 在空格处截断（避免截断单词）
        int lastSpace = truncated.lastIndexOf(' ');
        if (lastSpace > maxLength * 0.9) {
            return text.substring(0, lastSpace);
        }

        // 4. 添加省略号
        return truncated + "…";
    }

    private static int findLastPunctuation(String text) {
        // 中英文标点符号
        String[] punctuations = {"。", "！", "？", ".", "!", "?", ";", "；", "，", ","};
        int maxPos = -1;

        for (String punc : punctuations) {
            int pos = text.lastIndexOf(punc);
            if (pos > maxPos) {
                maxPos = pos;
            }
        }

        return maxPos;
    }

    private static void removeFollowingSiblings(Node parent, Node currentNode) {
        if (parent == null) return;

        List<Node> siblings = parent.childNodes();
        int currentIndex = siblings.indexOf(currentNode);

        if (currentIndex >= 0) {
            // 从当前节点的下一个开始移除
            for (int i = siblings.size() - 1; i > currentIndex; i--) {
                siblings.get(i).remove();
            }
        }
    }

}
