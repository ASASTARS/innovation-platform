package com.abajin.innovation.interceptor;

import com.abajin.innovation.annotation.RequiresRole;
import com.abajin.innovation.common.Constants;
import com.abajin.innovation.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 角色权限拦截器
 * 检查用户角色是否满足接口要求
 * 使用 Spring Security 进行权限验证
 */
@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 只处理方法级别的handler
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequiresRole requiresRole = handlerMethod.getMethodAnnotation(RequiresRole.class);

        // 从 Spring Security 的 SecurityContext 获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 对所有已认证请求设置 userId 和 role，供 @RequestAttribute 等使用
        if (authentication != null && authentication.isAuthenticated()) {
            // 从 token 中获取 userId
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    request.setAttribute("userId", jwtUtil.getUserIdFromToken(token));
                }
            }
            // 从认证信息中获取角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                String authorityName = authority.getAuthority();
                if (authorityName.startsWith("ROLE_")) {
                    request.setAttribute("role", authorityName.substring(5));
                    break;
                }
            }
        }

        // 如果没有@RequiresRole注解，则放行
        if (requiresRole == null) {
            return true;
        }

        // 未认证时拒绝
        if (authentication == null || !authentication.isAuthenticated()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String userRole = (String) request.getAttribute("role");
        if (userRole == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 获取允许的角色列表
        String[] allowedRoles = requiresRole.value();
        boolean allowAdmin = requiresRole.allowAdmin();

        // 如果允许管理员访问，且用户是管理员，则放行
        if (allowAdmin) {
            if (Constants.ROLE_SCHOOL_ADMIN.equals(userRole) || 
                Constants.ROLE_COLLEGE_ADMIN.equals(userRole)) {
                return true;
            }
        }

        // 检查用户角色是否在允许列表中
        List<String> allowedRoleList = Arrays.asList(allowedRoles);
        if (allowedRoleList.contains(userRole)) {
            return true;
        }

        // 权限不足
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }
}
