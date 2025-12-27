package com.abc.itbbs.common.oss.domain.dto;

import com.abc.itbbs.common.oss.domain.entity.File;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: LiJunXi
 * @Description:
 * @Date: 2025-08-04  20:27
 */
@Slf4j
@Data
public class OssFileDTO extends File {

    private byte[] fileData;

    private MultipartFile multipartFile;

}
