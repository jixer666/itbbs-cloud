package com.abc.itbbs.api.system;

import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author LiJunXi
 * @date 2025/12/24
 */
@FeignClient(contextId = "UserServiceClient", value = "itbbs-system")
public interface UserServiceClient {

    @PostMapping("/system/user/feign/user")
    ApiResult<Void> saveUser(User user);

    @GetMapping("/system/user/feign/username/{username}")
    ApiResult<User> getUserByUsername(@PathVariable("username") String username);

    @GetMapping("/system/user/feign/email/{email}")
    ApiResult<User> getUserByEmail(@PathVariable("email") String email);

}
