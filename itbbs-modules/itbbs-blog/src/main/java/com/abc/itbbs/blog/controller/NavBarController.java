package com.abc.itbbs.blog.controller;

import com.abc.itbbs.blog.domain.vo.NavbarVO;
import com.abc.itbbs.blog.service.NavbarService;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/1/18
 */
@RestController
@RequestMapping("/blog/navbar")
public class NavBarController {

    @Autowired
    private NavbarService navbarService;

    @ApiOperation("获取导航栏列表")
    @GetMapping("/list")
    public ApiResult<List<NavbarVO>> getNavbarList() {
        List<NavbarVO> navbarList = navbarService.getNavbarList();

        return ApiResult.success(navbarList);
    }



}
