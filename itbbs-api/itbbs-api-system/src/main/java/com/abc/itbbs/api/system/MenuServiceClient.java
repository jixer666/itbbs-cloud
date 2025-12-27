package com.abc.itbbs.api.system;

import com.abc.itbbs.api.system.domain.entity.Menu;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2025/12/24
 */
@FeignClient(contextId = "MenuServiceClient", value = "itbbs-system")
public interface MenuServiceClient {

    @GetMapping("/system/menu/feign/userId/{userId}")
    ApiResult<List<Menu>> getMenusByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/system/menu/feign/type/{menuType}")
    ApiResult<List<Menu>> getMenusByMenuType(@PathVariable("menuType") Integer menuType);
}
