package com.abajin.innovation.service;

import com.abajin.innovation.common.Constants;
import com.abajin.innovation.entity.User;
import com.abajin.innovation.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * 用户详情服务实现测试
 */
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRealName("Test User");
        testUser.setRole(Constants.ROLE_STUDENT);
        testUser.setStatus(Constants.USER_STATUS_ENABLED);
    }

    @Test
    void loadUserByUsername_withValidUser_returnsUserDetails() {
        // Arrange
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT")));
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void loadUserByUsername_withNonExistingUser_throwsException() {
        // Arrange
        when(userMapper.selectByUsername("nonexistent")).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nonexistent"));
        assertEquals("用户不存在: nonexistent", exception.getMessage());
    }

    @Test
    void loadUserByUsername_withDisabledUser_throwsException() {
        // Arrange
        testUser.setStatus(Constants.USER_STATUS_DISABLED);
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("testuser"));
        assertEquals("账户已被禁用", exception.getMessage());
    }

    @Test
    void loadUserByUsername_withSchoolAdminRole_returnsCorrectAuthorities() {
        // Arrange
        testUser.setRole(Constants.ROLE_SCHOOL_ADMIN);
        when(userMapper.selectByUsername("admin")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(userDetails);
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SCHOOL_ADMIN")));
    }

    @Test
    void loadUserByUsername_withCollegeAdminRole_returnsCorrectAuthorities() {
        // Arrange
        testUser.setRole(Constants.ROLE_COLLEGE_ADMIN);
        when(userMapper.selectByUsername("collegeadmin")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("collegeadmin");

        // Assert
        assertNotNull(userDetails);
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_COLLEGE_ADMIN")));
    }

    @Test
    void loadUserByUsername_withTeacherRole_returnsCorrectAuthorities() {
        // Arrange
        testUser.setRole(Constants.ROLE_TEACHER);
        when(userMapper.selectByUsername("teacher")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("teacher");

        // Assert
        assertNotNull(userDetails);
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TEACHER")));
    }

    @Test
    void loadUserByUsername_withNullRole_returnsEmptyAuthorities() {
        // Arrange
        testUser.setRole(null);
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails);
        assertTrue(userDetails.getAuthorities().isEmpty());
    }
}
