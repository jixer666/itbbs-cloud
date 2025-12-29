package com.abc.itbbs.blog.service;

import java.util.Map;

/**
 * @author LiJunXi
 * @date 2025/12/29
 */
public interface TemplateService {

    void saveStaticHtmlToOss(String templateName, Map<String, Object> contextMap);

}
