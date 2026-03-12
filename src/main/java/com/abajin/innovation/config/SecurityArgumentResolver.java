package com.abajin.innovation.config;

import com.abajin.innovation.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Spring Security 参数解析器
 * 用于将 @RequestAttribute("userId") 解析为当前登录用户的ID
 */
@Component
public class SecurityArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 支持 @RequestAttribute("userId") 注解的 Long 类型参数
        return parameter.hasParameterAnnotation(org.springframework.web.bind.annotation.RequestAttribute.class) 
            && parameter.getParameterName().equals("userId")
            && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // 从 request attribute 中获取 userId（由 RoleInterceptor 设置）
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request != null) {
            Object userId = request.getAttribute("userId");
            if (userId != null) {
                return userId;
            }
        }
        return null;
    }
}
