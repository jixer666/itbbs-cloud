package com.abc.itbbs.blog.convert;

import com.abc.itbbs.api.system.domain.dto.OssFileUploadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class TemplateConvert {

    public static OssFileUploadDTO convertOssFileUploadDTOByMultipartFile(MultipartFile multipartFile) {
        OssFileUploadDTO ossFileUploadDTO = new OssFileUploadDTO();
        try {
            ossFileUploadDTO.setFile(multipartFile);
            ossFileUploadDTO.setFileMd5(DigestUtils.md5DigestAsHex(multipartFile.getBytes()));
            ossFileUploadDTO.setFileType(multipartFile.getContentType());
        } catch (Exception e) {
            log.error("MD5计算出错：{}", e.getMessage(), e);
        }

        return ossFileUploadDTO;
    }

}
