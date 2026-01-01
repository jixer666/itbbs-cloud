package com.abc.itbbs.api.system;

import com.abc.itbbs.api.system.domain.dto.OssFileUploadDTO;
import com.abc.itbbs.api.system.domain.vo.FileVO;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author LiJunXi
 * @date 2025/12/30
 */
@FeignClient(contextId = "FileServiceClient", value = "itbbs-system")
public interface OssServiceClient {

    @PostMapping(value = "/system/oss/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResult<FileVO> uploadOss(OssFileUploadDTO req);

}
