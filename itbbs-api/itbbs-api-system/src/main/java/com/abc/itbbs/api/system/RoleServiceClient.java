package com.abc.itbbs.api.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2025/12/24
 */
@FeignClient(contextId = "RoleServiceClient", value = "itbbs-system")
public interface RoleServiceClient {

    @GetMapping
    List<String> getRoleKeysByUserId(Long userId);
}
