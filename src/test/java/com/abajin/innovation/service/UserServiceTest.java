package com.abajin.innovation.service;

import com.abajin.innovation.common.Constants;
import com.abajin.innovation.dto.LoginDTO;
import com.abajin.innovation.dto.RegisterDTO;
import com.abajin.innovation.entity.College;
import com.abajin.innovation.entity.User;
import com.abajin.innovation.mapper.CollegeMapper;
import com.abajin.innovation.mapper.UserMapper;
import com.abajin.innovation.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 用户服务测试
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private CollegeMapper collegeMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private LoginDTO loginDTO;
    private RegisterDTO registerDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRealName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13800138000");
        testUser.setRole(Constants.ROLE_STUDENT);
        testUser.setStatus(Constants.USER_STATUS_ENABLED);
        testUser.setCollegeId(1L);
        testUser.setCollegeName("计算机学院");

        loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");

        registerDTO = new RegisterDTO();
        registerDTO.setUsername("newuser");
        registerDTO.setPassword("password123");
        registerDTO.setRealName("New User");
        registerDTO.setEmail("new@example.com");
        registerDTO.setPhone("13900139000");
        registerDTO.setRole(Constants.ROLE_STUDENT);
        registerDTO.setCollegeId(1L);
    }

    @Test
    void login_withValidCredentials_returnsToken() {
        // Arrange
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(1L, "testuser", Constants.ROLE_STUDENT)).thenReturn("jwt_token");

        // Act
        String token = userService.login(loginDTO);

        // Assert
        assertNotNull(token);
        assertEquals("jwt_token", token);
        verify(userMapper).selectByUsername("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtil).generateToken(1L, "testuser", Constants.ROLE_STUDENT);
    }

    @Test
    void login_withNonExistentUser_throwsException() {
        // Arrange
        when(userMapper.selectByUsername("testuser")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.login(loginDTO));
        assertEquals("用户名或密码错误", exception.getMessage());
    }

    @Test
    void login_withWrongPassword_throwsException() {
        // Arrange
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.login(loginDTO));
        assertEquals("用户名或密码错误", exception.getMessage());
    }

    @Test
    void login_withDisabledUser_throwsException() {
        // Arrange
        testUser.setStatus(Constants.USER_STATUS_DISABLED);
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.login(loginDTO));
        assertEquals("账户已被禁用", exception.getMessage());
    }

    @Test
    void register_withValidData_returnsUser() {
        // Arrange
        when(userMapper.selectByUsername("newuser")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        College college = new College();
        college.setId(1L);
        college.setName("计算机学院");
        when(collegeMapper.selectById(1L)).thenReturn(college);
        when(userMapper.insert(any(User.class))).thenReturn(1);

        // Act
        User result = userService.register(registerDTO);

        // Assert
        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(Constants.USER_STATUS_ENABLED, result.getStatus());
        assertEquals("计算机学院", result.getCollegeName());
        verify(userMapper).insert(any(User.class));
    }

    @Test
    void register_withExistingUsername_throwsException() {
        // Arrange
        when(userMapper.selectByUsername("newuser")).thenReturn(testUser);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.register(registerDTO));
        assertEquals("用户名已存在", exception.getMessage());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void register_withoutCollegeId_savesWithoutCollegeName() {
        // Arrange
        registerDTO.setCollegeId(null);
        when(userMapper.selectByUsername("newuser")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userMapper.insert(any(User.class))).thenReturn(1);

        // Act
        User result = userService.register(registerDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getCollegeName());
    }

    @Test
    void getUserById_withExistingUser_returnsUser() {
        // Arrange
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void getUserById_withNonExistingUser_returnsNull() {
        // Arrange
        when(userMapper.selectById(999L)).thenReturn(null);

        // Act
        User result = userService.getUserById(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void getUserByUsername_withExistingUser_returnsUser() {
        // Arrange
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        // Act
        User result = userService.getUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void changePassword_withValidData_updatesPassword() {
        // Arrange
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");
        when(userMapper.update(any(User.class))).thenReturn(1);

        // Act
        userService.changePassword(1L, "oldPassword", "newPassword123");

        // Assert
        verify(userMapper).update(any(User.class));
    }

    @Test
    void changePassword_withNullOldPassword_throwsException() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> userService.changePassword(1L, null, "newPassword123"));
        assertEquals("请输入原密码", exception.getMessage());
    }

    @Test
    void changePassword_withEmptyOldPassword_throwsException() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> userService.changePassword(1L, "", "newPassword123"));
        assertEquals("请输入原密码", exception.getMessage());
    }

    @Test
    void changePassword_withShortNewPassword_throwsException() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> userService.changePassword(1L, "oldPassword", "12345"));
        assertEquals("新密码长度不能少于6位", exception.getMessage());
    }

    @Test
    void changePassword_withNonExistingUser_throwsException() {
        // Arrange
        when(userMapper.selectById(1L)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> userService.changePassword(1L, "oldPassword", "newPassword123"));
        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void changePassword_withWrongOldPassword_throwsException() {
        // Arrange
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> userService.changePassword(1L, "wrongPassword", "newPassword123"));
        assertEquals("原密码错误", exception.getMessage());
    }
}
