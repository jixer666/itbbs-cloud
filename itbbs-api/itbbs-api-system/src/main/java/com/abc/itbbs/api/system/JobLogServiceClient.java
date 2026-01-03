package com.abc.itbbs.api.system;

import com.abc.itbbs.api.system.domain.dto.JobLogSaveDTO;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author LiJunXi
 * @date 2025/12/24
 */
@FeignClient(contextId = "JobLogServiceClient", value = "itbbs-system")
public interface JobLogServiceClient {

    @PostMapping("/system/jobLog/feign/info")
    ApiResult<Void> saveFeignJobLog(@RequestBody JobLogSaveDTO jobLogSaveDTO);

}
