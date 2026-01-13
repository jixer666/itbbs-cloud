package com.abc.itbbs.api.ai;

import com.abc.itbbs.api.ai.domain.dto.DocumentDTO;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author LiJunXi
 * @date 2025/1/13
 */
@FeignClient(contextId = "DocumentServiceClient", value = "itbbs-ai")
public interface DocumentServiceClient {

    @PostMapping("/ai/document/feign/vector/save")
    ApiResult<Void> vectorSave(@RequestBody DocumentDTO documentDTO);

}
