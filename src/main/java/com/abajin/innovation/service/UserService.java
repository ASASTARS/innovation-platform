package com.abajin.innovation.service;

import com.abajin.innovation.dto.LoginDTO;
import com.abajin.innovation.dto.RegisterDTO;
import com.abajin.innovation.entity.User;
import com.abajin.innovation.mapper.UserMapper;
import com.abajin.innovation.mapper.CollegeMapper;
import com.abajin.innovation.util.JwtUtil;
import com.abajin.innovation.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public String login(LoginDTO loginDTO) {
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        if (user.getStatus() == Constants.USER_STATUS_DISABLED) {
            throw new RuntimeException("账户已被禁用");
        }

        return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    @Transactional
    public User register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRealName(registerDTO.getRealName());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setRole(registerDTO.getRole());
        user.setCollegeId(registerDTO.getCollegeId());
        user.setStatus(Constants.USER_STATUS_ENABLED);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 如果提供了学院ID，查询学院名称
        if (registerDTO.getCollegeId() != null) {
            var college = collegeMapper.selectById(registerDTO.getCollegeId());
            if (college != null) {
                user.setCollegeName(college.getName());
            }
        }

        userMapper.insert(user);
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 修改当前用户密码
     * @param userId 当前用户ID（从 token 获取）
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new RuntimeException("请输入原密码");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("新密码长度不能少于6位");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }
}
