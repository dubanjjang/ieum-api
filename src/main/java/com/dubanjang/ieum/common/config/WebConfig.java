package com.dubanjang.ieum.common.config;

import com.dubanjang.ieum.auth.interceptor.AuthInterceptor;
import com.dubanjang.ieum.auth.interceptor.JwtParseInterceptor;
import com.dubanjang.ieum.auth.resolver.AuthUserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtParseInterceptor jwtParseInterceptor;
    private final AuthInterceptor authInterceptor;
    private final AuthUserResolver authUserResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtParseInterceptor)
                .order(1)
                .addPathPatterns("/api/**");

        registry.addInterceptor(authInterceptor)
                .order(2)
                .addPathPatterns("/api/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserResolver);
    }
}