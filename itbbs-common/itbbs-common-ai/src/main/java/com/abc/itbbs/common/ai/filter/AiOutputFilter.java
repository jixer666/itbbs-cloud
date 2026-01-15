package com.abc.itbbs.common.ai.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * AI输出内容过滤器
 */
public class AiOutputFilter {
    
    /**
     * 敏感词黑名单
     */
    private static final Set<String> SENSITIVE_KEYWORDS =  new HashSet<>(Arrays.asList(
        "系统提示", "内部信息", "管理员密码", "数据库密码", "API密钥", "私钥", 
        "系统配置", "内部地址", "内部端口", "安全令牌", "认证凭据",
        "内部流程", "开发信息", "调试信息", "错误堆栈", "系统路径"
    ));
    
    /**
     * 正则表达式模式，用于匹配敏感信息
     */
    private static final List<Pattern> SENSITIVE_PATTERNS = Arrays.asList(
        // 匹配IP地址
        Pattern.compile("\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b"),
        // 匹配邮箱（但排除正常用户输入的邮箱）
        Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"),
        // 匹配URL（特别是内部地址）
        Pattern.compile("https?://[^\\s]*internal[^\\s]*", Pattern.CASE_INSENSITIVE),
        // 匹配各种密码格式
        Pattern.compile("(password|pwd|secret|token|key)\\s*[=:][^\\n\\r]{5,}", Pattern.CASE_INSENSITIVE),
        // 匹配文件路径
        Pattern.compile("([A-Za-z]:\\\\[\\\\\\/].*?|[\\/]([^\\s]*[\\/][^\\s]*)+)")
    );
    
    /**
     * 检查内容是否包含敏感信息
     */
    public static boolean containsSensitiveInfo(String content) {
        if (content == null || content.isEmpty()) {
            return false;
        }
        
        String lowerContent = content.toLowerCase();
        
        // 检查关键词
        for (String keyword : SENSITIVE_KEYWORDS) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        
        // 检查正则模式
        for (Pattern pattern : SENSITIVE_PATTERNS) {
            if (pattern.matcher(content).find()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 过滤敏感内容
     */
    public static String filterContent(String content) {
        if (content == null) {
            return null;
        }
        
        String filtered = content;
        
        // 替换敏感词
        for (String keyword : SENSITIVE_KEYWORDS) {
            filtered = filtered.replaceAll("(?i)" + Pattern.quote(keyword), "***");
        }
        
        // 替换敏感模式
        for (Pattern pattern : SENSITIVE_PATTERNS) {
            filtered = pattern.matcher(filtered).replaceAll("***");
        }
        
        return filtered;
    }
    
    /**
     * 验证AI输出内容是否合规
     */
    public static ValidationResult validateOutput(String aiResponse) {
        ValidationResult result = new ValidationResult();
        result.isValid = true;
        result.filteredContent = aiResponse;
        
        if (aiResponse == null) {
            result.isValid = false;
            result.errorReason = "AI响应为空";
            return result;
        }
        
        // 检查长度
        if (aiResponse.length() > 10000) {  // 根据需要调整
            result.isValid = false;
            result.errorReason = "AI响应内容过长";
            return result;
        }
        
        // 检查是否包含敏感信息
        if (containsSensitiveInfo(aiResponse)) {
            result.isValid = false;
            result.errorReason = "AI响应包含敏感信息";
            // 同时提供过滤后的版本
            result.filteredContent = filterContent(aiResponse);
            return result;
        }
        
        // 检查是否包含系统指令相关的内容
        if (aiResponse.toLowerCase().contains("系统指令") || 
            aiResponse.toLowerCase().contains("ignore") ||
            aiResponse.toLowerCase().contains("forget")) {
            result.isValid = false;
            result.errorReason = "AI响应包含系统指令相关内容";
            return result;
        }
        
        return result;
    }
    
    /**
     * 验证结果类
     */
    public static class ValidationResult {
        public boolean isValid;
        public String errorReason;
        public String filteredContent;
    }
}