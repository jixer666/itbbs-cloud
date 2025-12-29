package com.abc.itbbs.blog.service.impl;

import com.abc.itbbs.api.system.FileServiceClient;
import com.abc.itbbs.blog.service.TemplateService;
import com.abc.itbbs.common.core.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * @author LiJunXi
 * @date 2025/12/29
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void saveStaticHtmlToOss(String templateName, Map<String, Object> contextMap) {
        AssertUtils.isNotEmpty(templateName, "模板名称不能为空");

        Context context = new Context();
        context.setVariables(contextMap);

        String htmlContent = templateEngine.process(templateName, context);

//         fileServiceClient.uploadOss();
    }
}
