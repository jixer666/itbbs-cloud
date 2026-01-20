package com.abc.itbbs.blog.service;

import com.abc.itbbs.api.system.domain.vo.FileVO;

import java.util.Map;

/**
 * @author LiJunXi
 * @date 2025/12/29
 */
public interface TemplateService {

    FileVO saveStaticToOss(String filename, String templateName, Map<String, Object> contextMap);

    FileVO saveJsonToOss(String filename, Map<String, String> contextMap);
}
