package com.abc.itbbs.api.system;

import com.abc.itbbs.api.system.domain.entity.Menu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2025/12/24
 */
@FeignClient(contextId = "MenuServiceClient", value = "itbbs-system")
public interface MenuServiceClient {

    @GetMapping
    List<Menu> getMenusByUserId(Long userId);

}
