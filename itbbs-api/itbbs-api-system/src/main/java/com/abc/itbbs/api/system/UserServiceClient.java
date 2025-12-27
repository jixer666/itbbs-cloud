package com.abc.itbbs.api.system;

import com.abc.itbbs.api.system.domain.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author LiJunXi
 * @date 2025/12/24
 */
@FeignClient(contextId = "UserServiceClient", value = "itbbs-system")
public interface UserServiceClient {

    @PostMapping
    void saveUser(User user);

    @GetMapping
    User getUserByUsername(String username);

    @GetMapping
    User getUserByEmail(String email);

}
