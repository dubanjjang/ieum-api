package com.dubanjang.ieum.auth.interceptor;

import com.dubanjang.ieum.auth.application.dto.AuthInfo;
import com.dubanjang.ieum.auth.util.JwtProvider;
import com.dubanjang.ieum.common.annotation.AuthUser;
import com.dubanjang.ieum.common.exception.ErrorCode;
import com.dubanjang.ieum.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
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

        boolean hasAuthUserAnnotation = Arrays.stream(handlerMethod.getMethodParameters())
                .anyMatch(param -> param.hasParameterAnnotation(AuthUser.class));

        if (!hasAuthUserAnnotation) {
            return true;
        }

        AuthInfo authInfo = (AuthInfo) request.getAttribute("authInfo");
        if (authInfo == null) throw new UnauthorizedException(ErrorCode.INVALID_TOLEN);
        return true;
    }
}
