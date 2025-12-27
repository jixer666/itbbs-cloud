package com.abc.itbbs.common.security.filter;

import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.module.threadlocal.ThreadLocalTempVar;
import com.abc.itbbs.common.core.util.StringUtils;
import com.abc.itbbs.common.security.domain.dto.LoginUserDTO;
import com.abc.itbbs.common.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Boolean isFeignRequest = (Boolean) request.getAttribute(CommonConstants.FEIGN_REQUEST_FLAG);
        if (Objects.nonNull(isFeignRequest) && isFeignRequest) {
            chain.doFilter(request, response);
            return;
        }

        String token = tokenService.getToken(request);
        ThreadLocalTempVar.setTempTokenVar(token);
        if (StringUtils.isEmpty(token)) {
            chain.doFilter(request, response);
            return;
        }

        LoginUserDTO loginUserDTO = tokenService.getLoginUserDTO(token);
        if (Objects.isNull(loginUserDTO)) {
            chain.doFilter(request, response);
            return;
        }

        tokenService.validateToken(loginUserDTO);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDTO, null, loginUserDTO.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        try {
            chain.doFilter(request, response);
        } finally {
            ThreadLocalTempVar.removeTempTokenVar();
        }
    }
}
