package com.abc.itbbs.blog.service.impl;

import com.abc.itbbs.blog.domain.enums.NavbarEnum;
import com.abc.itbbs.blog.domain.vo.NavbarVO;
import com.abc.itbbs.blog.service.NavbarService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LiJunXi
 * @date 2026/1/18
 */
@Service
public class NavbarServiceImpl implements NavbarService {

    @Override
    public List<NavbarVO> getNavbarList() {
        return Arrays.stream(NavbarEnum.values()).map(NavbarVO::new).collect(Collectors.toList());
    }
}
