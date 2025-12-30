package com.abc.itbbs.common.core.util;

import org.jsoup.Jsoup;

public class JsoupUtils {

    public static String parseHtml(String htmlContent) {
        String plainText = Jsoup.parse(htmlContent).text();
        // 优化处理：去除换行和多余空格
        plainText = plainText.replaceAll("\\s+", " ").trim();

        return plainText;
    }
}
