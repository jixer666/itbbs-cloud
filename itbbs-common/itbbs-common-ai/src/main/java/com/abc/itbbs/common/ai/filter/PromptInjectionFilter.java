package com.abc.itbbs.common.ai.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 提示词注入防护过滤器
 */
public class PromptInjectionFilter {

    /**
     * 关键词黑名单
     */
    private static final Set<String> BLACKLIST_KEYWORDS = new HashSet<>(Arrays.asList(
            "忽略", "忘记", "忽略之前", "忘记之前", "系统指令", "指令覆盖",
            "密码", "密钥", "机密", "安全限制", "绕过", "跳过", "跳过安全检查",
            "扮演", "模拟", "假扮", "替代", "取代", "重新定义"
    ));

    /**
     * 检查输入是否包含潜在的注入内容
     */
    public static boolean containsInjectionAttempt(String userInput) {
        String lowerInput = userInput.toLowerCase();

        // 检查关键词
        for (String keyword : BLACKLIST_KEYWORDS) {
            if (lowerInput.contains(keyword.toLowerCase())) {
                return true;
            }
        }

        // 检查常见的注入模式
        Pattern injectionPattern = Pattern.compile(
            "(ignore|forget|disregard).*?(previous|prior|earlier)|" +
            "(system|security|safety).*?(bypass|override|circumvent)|" +
            "(role|identity|character).*?(change|become|transform)"
        );

        return injectionPattern.matcher(lowerInput).find();
    }

    /**
     * 清理和验证用户输入
     */
    public static String sanitizeInput(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            throw new IllegalArgumentException("输入不能为空");
        }

        // 检查是否存在注入尝试
        if (containsInjectionAttempt(userInput)) {
            throw new SecurityException("检测到潜在的提示词注入尝试");
        }

        // 移除危险字符序列
        String sanitized = userInput.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");

        // 验证长度（防止过长输入）
        if (sanitized.length() > 1000) {  // 根据需要调整最大长度
            throw new IllegalArgumentException("输入内容过长");
        }

        return sanitized;
    }
}