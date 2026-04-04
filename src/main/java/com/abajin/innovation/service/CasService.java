package com.abajin.innovation.service;

import com.abajin.innovation.common.Constants;
import com.abajin.innovation.config.CasConfig;
import com.abajin.innovation.converter.LoginUserDTOConverter;
import com.abajin.innovation.dto.CasLoginResponse;
import com.abajin.innovation.dto.CompleteProfileDTO;
import com.abajin.innovation.entity.College;
import com.abajin.innovation.entity.User;
import com.abajin.innovation.mapper.CollegeMapper;
import com.abajin.innovation.mapper.UserMapper;
import com.abajin.innovation.util.JwtUtil;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * CAS统一身份认证服务
 */
@Service
public class CasService {

    @Autowired
    private CasConfig casConfig;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 验证CAS ticket并处理登录
     */
    @Transactional
    public CasLoginResponse validateTicketAndLogin(String ticket, String serviceUrl) {
        // 1. 验证ticket
        Map<String, Object> casUserInfo = validateTicket(ticket, serviceUrl);
        String casUid = (String) casUserInfo.get("uid");
        String cn = (String) casUserInfo.get("cn");
        String userName = (String) casUserInfo.get("user_name");

        // 2. 根据cas_uid查询用户
        User user = userMapper.selectByCasUid(casUid);

        if (user != null) {
            // 用户已存在，直接登录
            return handleExistingCasUser(user);
        }

        // 3. 用户不存在，检查是否有同名本地账号
        List<User> duplicateUsers = userMapper.selectByRealNameAndAuthType(cn, Constants.AUTH_TYPE_LOCAL);

        if (!duplicateUsers.isEmpty()) {
            // 有同名本地账号，返回合并提示
            CasLoginResponse response = new CasLoginResponse();
            response.setNeedMerge(true);
            response.setDuplicateAccount(duplicateUsers.get(0));
            response.setCasUid(casUid);
            response.setCasName(cn);
            return response;
        }

        // 4. 创建新的CAS用户
        return createNewCasUser(casUid, cn, userName);
    }

    /**
     * 验证CAS ticket
     */
    private Map<String, Object> validateTicket(String ticket, String serviceUrl) {
        // Mock模式：用于本地测试
        if (casConfig.getMockMode() && ticket.startsWith("MOCK-")) {
            return validateMockTicket(ticket);
        }

        // 真实CAS验证
        try {
            Cas20ServiceTicketValidator validator = new Cas20ServiceTicketValidator(
                    casConfig.getServerUrlPrefix()
            );
            validator.setEncoding("UTF-8");

            Assertion assertion = validator.validate(ticket, serviceUrl);
            AttributePrincipal principal = assertion.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();

            String casUid = principal.getName(); // uid
            String cn = (String) attributes.get("cn"); // 姓名
            String userName = (String) attributes.get("user_name");

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("uid", casUid);
            result.put("cn", cn);
            result.put("user_name", userName);

            return result;
        } catch (TicketValidationException e) {
            throw new RuntimeException("CAS ticket验证失败: " + e.getMessage());
        }
    }

    /**
     * 验证Mock ticket（用于本地测试）
     */
    private Map<String, Object> validateMockTicket(String ticket) {
        // 从ticket中提取用户信息：MOCK-{uid}-{name}
        String[] parts = ticket.split("-", 3);
        if (parts.length < 3) {
            throw new RuntimeException("Mock ticket格式错误");
        }

        String casUid = parts[1];
        String cn = parts[2];

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("uid", casUid);
        result.put("cn", cn);
        result.put("user_name", cn);

        return result;
    }

    /**
     * 处理已存在的CAS用户登录
     */
    private CasLoginResponse handleExistingCasUser(User user) {
        // 检查账户状态
        if (user.getStatus() == Constants.USER_STATUS_DISABLED) {
            throw new RuntimeException("账户已被禁用");
        }

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        CasLoginResponse response = new CasLoginResponse();
        response.setToken(token);
        response.setUser(LoginUserDTOConverter.convert(user));

        // 检查是否需要完善资料
        if (user.getIsProfileComplete() != null && user.getIsProfileComplete() == 0) {
            response.setNeedCompleteProfile(true);
        } else {
            response.setNeedCompleteProfile(false);
        }

        return response;
    }

    /**
     * 创建新的CAS用户
     */
    private CasLoginResponse createNewCasUser(String casUid, String cn, String userName) {
        User user = new User();
        user.setUsername(casUid); // 使用学号/工号作为用户名
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // 随机密码，不可用
        user.setRealName(cn);
        user.setRole(Constants.ROLE_STUDENT); // 默认学生角色
        user.setAuthType(Constants.AUTH_TYPE_CAS);
        user.setCasUid(casUid);
        user.setIsProfileComplete(0); // 需要完善资料
        user.setStatus(Constants.USER_STATUS_ENABLED);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        CasLoginResponse response = new CasLoginResponse();
        response.setToken(token);
        response.setUser(LoginUserDTOConverter.convert(user));
        response.setNeedCompleteProfile(true);

        return response;
    }

    /**
     * 合并本地账号
     */
    @Transactional
    public CasLoginResponse mergeAccount(String casUid, String password) {
        // 1. 根据casUid获取CAS用户信息（从临时创建的用户或待合并状态）
        // 这里我们需要先验证是否有对应的CAS登录尝试
        // 简化处理：直接根据realName查找本地账号

        // 2. 查找同名本地账号
        List<User> localUsers = userMapper.selectByRealNameAndAuthType(null, Constants.AUTH_TYPE_LOCAL);

        // 由于我们需要通过casUid来识别，这里改为先查CAS信息
        // 实际上在前端调用时，应该传递更多信息
        // 为了简化，我们假设前端已经确认了要合并的账号

        throw new RuntimeException("请使用完整的合并接口，提供realName参数");
    }

    /**
     * 合并本地账号（完整版本）
     */
    @Transactional
    public CasLoginResponse mergeAccountWithRealName(String casUid, String realName, String password) {
        // 1. 查找本地账号
        List<User> localUsers = userMapper.selectByRealNameAndAuthType(realName, Constants.AUTH_TYPE_LOCAL);

        if (localUsers.isEmpty()) {
            throw new RuntimeException("未找到对应的本地账号");
        }

        User localUser = localUsers.get(0);

        // 2. 验证密码
        if (!passwordEncoder.matches(password, localUser.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 3. 更新账号为双认证模式
        localUser.setAuthType(Constants.AUTH_TYPE_BOTH);
        localUser.setCasUid(casUid);
        localUser.setUpdateTime(LocalDateTime.now());
        userMapper.update(localUser);

        // 4. 生成JWT token
        String token = jwtUtil.generateToken(localUser.getId(), localUser.getUsername(), localUser.getRole());

        CasLoginResponse response = new CasLoginResponse();
        response.setToken(token);
        response.setUser(LoginUserDTOConverter.convert(localUser));
        response.setNeedCompleteProfile(false);

        return response;
    }

    /**
     * 完善用户资料
     */
    @Transactional
    public void completeProfile(Long userId, CompleteProfileDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户信息
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getCollegeId() != null) {
            user.setCollegeId(dto.getCollegeId());
            College college = collegeMapper.selectById(dto.getCollegeId());
            if (college != null) {
                user.setCollegeName(college.getName());
            }
        }
        if (dto.getRole() != null && !dto.getRole().isEmpty()) {
            user.setRole(dto.getRole());
        }

        // 标记资料已完善
        user.setIsProfileComplete(1);
        user.setUpdateTime(LocalDateTime.now());

        userMapper.update(user);
    }

    /**
     * 检查CAS功能是否启用
     */
    public boolean isCasEnabled() {
        return casConfig.getEnabled() != null && casConfig.getEnabled();
    }
}
