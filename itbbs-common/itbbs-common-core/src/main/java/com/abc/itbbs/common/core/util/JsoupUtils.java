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
        if (html == null || html.trim().isEmpty() || maxChars <= 0) {
            return "";
        }

        // 如果 HTML 原本就很短，直接返回
        String plainText = Jsoup.parse(html).text();
        if (plainText.length() <= maxChars) {
            return html;
        }

        Document doc = Jsoup.parse(html);
        Element body = doc.body();

        // 清空 body，重新构建
        StringBuilder result = new StringBuilder();
        int[] charCount = {0};
        boolean[] shouldStop = {false};

        // 递归构建截断后的 HTML
        buildTruncatedHtml(body, result, charCount, maxChars, shouldStop);

        return result.toString();
    }

    private static void buildTruncatedHtml(Node node, StringBuilder result,
                                           int[] charCount, int maxChars, boolean[] shouldStop) {
        if (shouldStop[0]) {
            return;
        }

        // 处理元素节点
        if (node instanceof Element) {
            Element element = (Element) node;

            // 开始标签
            result.append("<").append(element.tagName());

            // 添加属性
            for (org.jsoup.nodes.Attribute attr : element.attributes()) {
                result.append(" ").append(attr.getKey());
                if (!attr.getValue().isEmpty()) {
                    result.append("=\"").append(attr.getValue()).append("\"");
                }
            }
            result.append(">");

            // 递归处理子节点
            for (Node child : element.childNodes()) {
                buildTruncatedHtml(child, result, charCount, maxChars, shouldStop);
                if (shouldStop[0]) {
                    break;
                }
            }

            // 结束标签
            result.append("</").append(element.tagName()).append(">");
        }
        // 处理文本节点
        else if (node instanceof TextNode) {
            TextNode textNode = (TextNode) node;
            String text = textNode.getWholeText();

            if (charCount[0] >= maxChars) {
                shouldStop[0] = true;
                return;
            }

            int remaining = maxChars - charCount[0];
            if (text.length() <= remaining) {
                // 文本可以完整保留
                result.append(text);
                charCount[0] += text.length();
            } else {
                // 需要截断文本
                String truncatedText = smartTruncateText(text, remaining);
                result.append(truncatedText);
                charCount[0] = maxChars;
                shouldStop[0] = true;
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
        if (lastPunctuation > maxLength * 0.7 && lastPunctuation > 0) {
            return text.substring(0, lastPunctuation + 1) + "…";
        }

        // 2. 在空格处截断（避免截断单词）
        int lastSpace = truncated.lastIndexOf(' ');
        if (lastSpace > maxLength * 0.8 && lastSpace > 0) {
            return text.substring(0, lastSpace) + "…";
        }

        // 3. 直接截断并添加省略号
        return truncated + "…";
    }

    private static int findLastPunctuation(String text) {
        // 中英文标点符号
        String punctuations = "。！？.!?;；，,";
        int maxPos = -1;

        for (int i = 0; i < punctuations.length(); i++) {
            int pos = text.lastIndexOf(punctuations.charAt(i));
            if (pos > maxPos) {
                maxPos = pos;
            }
        }

        return maxPos;
    }

}
