package com.abc.itbbs.api.system;

import com.abc.itbbs.api.system.domain.dto.OssFileUploadDTO;
import com.abc.itbbs.api.system.domain.vo.FileVO;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author LiJunXi
 * @date 2025/12/30
 */
@FeignClient(contextId = "FileServiceClient", value = "itbbs-system")
public interface FileServiceClient {

    @PostMapping("/system/file/upload")
    ApiResult<FileVO> uploadOss(@Validated OssFileUploadDTO req);

}
