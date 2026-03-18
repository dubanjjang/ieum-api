package com.dubanjang.ieum.auth.interceptor;

import com.dubanjang.ieum.auth.application.dto.AuthInfo;
import com.dubanjang.ieum.auth.util.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtParseInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        String token = extractToken(request);
        if (token == null) return true;

        AuthInfo authInfo;
        try{
            authInfo = jwtProvider.validateToken(token);
        } catch (Exception e) {
            log.error(e.getMessage());
            authInfo = null;
        }

        request.setAttribute("authInfo", authInfo);
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("x-ieum-access");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Ieum ")) {
            return bearerToken.substring(5);
        }
        return null;
    }
}
