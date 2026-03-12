package com.abajin.innovation.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类
 * 用于生成BCrypt密码哈希
 */
public class PasswordUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String encodedPassword = encoder.encode(password);
        System.out.println("密码: " + password);
        System.out.println("BCrypt哈希: " + encodedPassword);
        
        // 验证
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("验证结果: " + matches);
    }
}
