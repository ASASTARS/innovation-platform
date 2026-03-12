package com.abajin.innovation.controller;

import com.abajin.innovation.common.Result;
import com.abajin.innovation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 当前用户相关接口（如修改密码）
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 修改当前用户密码
     * PUT /api/users/me/password
     * 请求体：{ "oldPassword": "原密码", "newPassword": "新密码" }
     */
    @PutMapping("/me/password")
    public Result<Void> changePassword(
            @RequestBody Map<String, String> body,
            @RequestAttribute("userId") Long userId) {
        try {
            String oldPassword = body.get("oldPassword");
            String newPassword = body.get("newPassword");
            userService.changePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
