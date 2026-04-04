package com.abajin.innovation.controller;

import com.abajin.innovation.common.Result;
import com.abajin.innovation.config.CasConfig;
import com.abajin.innovation.dto.CasLoginResponse;
import com.abajin.innovation.dto.CasMergeDTO;
import com.abajin.innovation.dto.CompleteProfileDTO;
import com.abajin.innovation.service.CasService;
import com.abajin.innovation.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * CAS统一身份认证Controller
 */
@RestController
@RequestMapping("/auth/cas")
public class CasAuthController {

    @Autowired
    private CasConfig casConfig;

    @Autowired
    private CasService casService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取CAS功能状态
     */
    @GetMapping("/status")
    public Result<Map<String, Object>> getStatus() {
        Map<String, Object> data = new HashMap<>();
        data.put("enabled", casService.isCasEnabled());
        if (casService.isCasEnabled()) {
            data.put("loginUrl", casConfig.getServerLoginUrl());
        }
        return Result.success(data);
    }

    /**
     * 发起CAS登录
     * 重定向到CAS服务器登录页面
     */
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        if (!casService.isCasEnabled()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CAS功能未启用");
            return;
        }

        // Mock模式：直接重定向到前端，带上mock ticket
        if (casConfig.getMockMode()) {
            // 使用Base64编码避免中文字符问题
            String mockTicket = "MOCK-2021001-ZhangSan";  // 使用拼音避免中文编码问题
            String redirectUrl = "http://localhost:3001/cas-callback?ticket=" + mockTicket;
            response.sendRedirect(redirectUrl);
            return;
        }

        // 构造回调地址
        String serviceUrl = casConfig.getClientHostUrl() + "/auth/cas/validate";
        String encodedServiceUrl = URLEncoder.encode(serviceUrl, StandardCharsets.UTF_8);

        // 重定向到CAS登录页面
        String casLoginUrl = casConfig.getServerLoginUrl() + "?service=" + encodedServiceUrl;
        response.sendRedirect(casLoginUrl);
    }

    /**
     * CAS回调验证ticket
     * CAS服务器会重定向到这个地址，并携带ticket参数
     */
    @GetMapping("/validate")
    public Result<CasLoginResponse> validate(@RequestParam("ticket") String ticket) {
        try {
            if (!casService.isCasEnabled()) {
                return Result.error("CAS功能未启用");
            }

            // 构造service URL（必须与login时的一致）
            String serviceUrl = casConfig.getClientHostUrl() + "/auth/cas/validate";

            // 验证ticket并处理登录
            CasLoginResponse response = casService.validateTicketAndLogin(ticket, serviceUrl);

            return Result.success(response);
        } catch (Exception e) {
            return Result.error("CAS登录失败: " + e.getMessage());
        }
    }

    /**
     * 合并本地账号
     * 当检测到同名本地账号时，用户可以选择合并
     */
    @PostMapping("/merge")
    public Result<CasLoginResponse> mergeAccount(@Valid @RequestBody CasMergeDTO dto) {
        try {
            if (!casService.isCasEnabled()) {
                return Result.error("CAS功能未启用");
            }

            // 注意：这里需要额外的realName参数，前端需要从needMerge响应中获取
            // 为了简化，我们从duplicateAccount中获取realName
            // 实际实现中，前端应该在merge请求中包含realName

            // 这里需要改进：应该从session或临时存储中获取CAS用户信息
            // 简化实现：要求前端传递完整信息
            throw new RuntimeException("请使用完整的合并接口");

        } catch (Exception e) {
            return Result.error("账号合并失败: " + e.getMessage());
        }
    }

    /**
     * 合并本地账号（完整版本）
     * 前端需要传递casUid, realName, password
     */
    @PostMapping("/merge-with-name")
    public Result<CasLoginResponse> mergeAccountWithName(
            @RequestParam("casUid") String casUid,
            @RequestParam("realName") String realName,
            @RequestParam("password") String password) {
        try {
            if (!casService.isCasEnabled()) {
                return Result.error("CAS功能未启用");
            }

            CasLoginResponse response = casService.mergeAccountWithRealName(casUid, realName, password);
            return Result.success(response);

        } catch (Exception e) {
            return Result.error("账号合并失败: " + e.getMessage());
        }
    }

    /**
     * 完善用户资料
     * CAS新用户首次登录后需要完善邮箱、手机号、学院等信息
     */
    @PostMapping("/complete-profile")
    public Result<String> completeProfile(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CompleteProfileDTO dto) {
        try {
            if (!casService.isCasEnabled()) {
                return Result.error("CAS功能未启用");
            }

            // 从token中获取用户ID
            String token = authHeader.substring(7); // 移除 "Bearer "
            Long userId = jwtUtil.getUserIdFromToken(token);

            if (userId == null) {
                return Result.error("无效的token");
            }

            casService.completeProfile(userId, dto);
            return Result.success("资料完善成功");

        } catch (Exception e) {
            return Result.error("资料完善失败: " + e.getMessage());
        }
    }
}
