package com.abajin.innovation.util;

import com.abajin.innovation.config.JwtConfig;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * JWT 工具类测试
 */
@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private JwtUtil jwtUtil;

    private static final String SECRET = "mySecretKey123456789012345678901234567890";
    private static final long EXPIRATION = 86400000; // 1天

    @BeforeEach
    void setUp() {
        lenient().when(jwtConfig.getSecret()).thenReturn(SECRET);
        lenient().when(jwtConfig.getExpiration()).thenReturn(EXPIRATION);
    }

    @Test
    void generateToken_withValidParams_returnsToken() {
        // Arrange
        Long userId = 1L;
        String username = "testuser";
        String role = "STUDENT";

        // Act
        String token = jwtUtil.generateToken(userId, username, role);

        // Assert
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // JWT 应该有3部分
    }

    @Test
    void parseToken_withValidToken_returnsClaims() {
        // Arrange
        Long userId = 1L;
        String username = "testuser";
        String role = "STUDENT";
        String token = jwtUtil.generateToken(userId, username, role);

        // Act
        Claims claims = jwtUtil.parseToken(token);

        // Assert
        assertNotNull(claims);
        assertEquals(userId, claims.get("userId", Long.class));
        assertEquals(username, claims.get("username", String.class));
        assertEquals(role, claims.get("role", String.class));
        assertEquals(username, claims.getSubject());
    }

    @Test
    void parseToken_withInvalidToken_throwsException() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.parseToken(invalidToken));
    }

    @Test
    void validateToken_withValidToken_returnsTrue() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "testuser", "STUDENT");

        // Act
        boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_withInvalidToken_returnsFalse() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_withNullToken_returnsFalse() {
        // Act
        boolean isValid = jwtUtil.validateToken(null);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_withEmptyToken_returnsFalse() {
        // Act
        boolean isValid = jwtUtil.validateToken("");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void getUserIdFromToken_withValidToken_returnsUserId() {
        // Arrange
        Long expectedUserId = 100L;
        String token = jwtUtil.generateToken(expectedUserId, "testuser", "STUDENT");

        // Act
        Long userId = jwtUtil.getUserIdFromToken(token);

        // Assert
        assertEquals(expectedUserId, userId);
    }

    @Test
    void getRoleFromToken_withValidToken_returnsRole() {
        // Arrange
        String expectedRole = "ADMIN";
        String token = jwtUtil.generateToken(1L, "testuser", expectedRole);

        // Act
        String role = jwtUtil.getRoleFromToken(token);

        // Assert
        assertEquals(expectedRole, role);
    }

    @Test
    void getUsernameFromToken_withValidToken_returnsUsername() {
        // Arrange
        String expectedUsername = "john_doe";
        String token = jwtUtil.generateToken(1L, expectedUsername, "STUDENT");

        // Act
        String username = jwtUtil.getUsernameFromToken(token);

        // Assert
        assertEquals(expectedUsername, username);
    }

    @Test
    void generateToken_withDifferentUsers_generatesDifferentTokens() {
        // Arrange & Act
        String token1 = jwtUtil.generateToken(1L, "user1", "STUDENT");
        String token2 = jwtUtil.generateToken(2L, "user2", "TEACHER");

        // Assert
        assertNotEquals(token1, token2);
    }

    @Test
    void parseToken_withTamperedToken_throwsException() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "testuser", "STUDENT");
        String tamperedToken = token.substring(0, token.length() - 5) + "XXXXX";

        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.parseToken(tamperedToken));
    }
}
