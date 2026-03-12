package com.abajin.innovation.config;

import com.abajin.innovation.interceptor.RoleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 * 注意：认证已由 Spring Security 的 JwtAuthenticationFilter 处理
 * 这里只配置角色权限拦截器
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private RoleInterceptor roleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加角色权限拦截器（验证角色）
        // 注意：认证已由 Spring Security 处理，这里只处理角色权限
        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login", "/auth/register")
                .order(1);
    }
}
