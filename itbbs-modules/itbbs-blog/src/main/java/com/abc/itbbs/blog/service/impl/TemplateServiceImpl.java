package com.abc.itbbs.blog.service.impl;

import cn.hutool.json.JSONUtil;
import com.abc.itbbs.api.system.OssServiceClient;
import com.abc.itbbs.api.system.domain.vo.FileVO;
import com.abc.itbbs.blog.config.MultipartSupportConfig;
import com.abc.itbbs.blog.convert.TemplateConvert;
import com.abc.itbbs.blog.service.TemplateService;
import com.abc.itbbs.common.core.constant.FileSuffixConstants;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.common.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2025/12/29
 */
@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private OssServiceClient ossServiceClient;

    @Override
    public FileVO saveStaticToOss(String filename, String templateName, Map<String, Object> contextMap) {
        AssertUtils.isNotEmpty(templateName, "HTML模板名称不能为空");
        AssertUtils.isNotEmpty(filename, "文件名称不能为空");

        File file = generateFile(filename, templateName, contextMap);

        return saveToOss(file);
    }

    private FileVO saveToOss(File file) {
        if (Objects.isNull(file)) {
            return null;
        }

        // 将File转为MultipartFile
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);

        return ApiResult.invokeRemoteMethod(ossServiceClient.uploadOss(TemplateConvert.convertOssFileUploadDTOByMultipartFile(multipartFile)));
    }


    /**
     * 生成文件
     * @param filename
     * @param templateName
     * @param contextMap
     * @return
     */
    private File generateFile(String filename, String templateName, Map<String, Object> contextMap) {
        if (StringUtils.isEmpty(filename) || StringUtils.isEmpty(templateName)) {
            return null;
        }

        File file = null;
        try {
            Context context = new Context();
            context.setVariables(contextMap);

            String templateContent = templateEngine.process(templateName, context);

            file = File.createTempFile(filename, FileSuffixConstants.HTML);
            FileUtils.writeStringToFile(
                    file,
                    templateContent,
                    StandardCharsets.UTF_8,
                    false  // append: false表示覆盖，true表示追加
            );
        } catch (Exception e) {
            log.error("文件写入异常：{}", e.getMessage(), e);
        }

        return file;
    }

    @Override
    public FileVO saveJsonToOss(String filename, Map<String, String> contextMap) {
        AssertUtils.isFalse(contextMap.isEmpty(), "JSON内容不能为空");
        AssertUtils.isNotEmpty(filename, "文件名称不能为空");

        File file = generateJsonFile(filename, contextMap);

        return saveToOss(file);
    }

    /**
     * 生成JSON文件
     * @param filename
     * @param contextMap
     */
    private File generateJsonFile(String filename, Map<String, String> contextMap) {
        if (StringUtils.isEmpty(filename) || contextMap.isEmpty()) {
            return null;
        }

        File file = null;
        try {
            file = File.createTempFile(filename, FileSuffixConstants.JSON);
            FileUtils.writeStringToFile(
                    file,
                    JSONUtil.toJsonStr(contextMap),
                    StandardCharsets.UTF_8,
                    false  // append: false表示覆盖，true表示追加
            );
        } catch (Exception e) {
            log.error("文件写入异常：{}", e.getMessage(), e);
        }

        return file;
    }
}
