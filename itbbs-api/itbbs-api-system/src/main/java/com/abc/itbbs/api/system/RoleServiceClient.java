package com.abc.itbbs.api.system;

import com.abc.itbbs.common.core.domain.vo.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2025/12/24
 */
@FeignClient(contextId = "RoleServiceClient", value = "itbbs-system")
public interface RoleServiceClient {

    @GetMapping("/system/role/feign/userId/{userId}")
    ApiResult<List<String>> getRoleKeysByUserId(@PathVariable("userId") Long userId);
}
