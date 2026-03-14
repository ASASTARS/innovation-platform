package com.abajin.innovation.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 密码工具类测试
 */
class PasswordUtilTest {

    @Test
    void bcryptPasswordEncoder_encode_generatesHash() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";

        // Act
        String encodedPassword = encoder.encode(password);

        // Assert
        assertNotNull(encodedPassword);
        assertTrue(encodedPassword.startsWith("$2a$")); // BCrypt 哈希以 $2a$ 开头
        assertNotEquals(password, encodedPassword);
    }

    @Test
    void bcryptPasswordEncoder_matchesWithCorrectPassword_returnsTrue() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "mySecurePassword123";
        String encodedPassword = encoder.encode(password);

        // Act
        boolean matches = encoder.matches(password, encodedPassword);

        // Assert
        assertTrue(matches);
    }

    @Test
    void bcryptPasswordEncoder_matchesWithWrongPassword_returnsFalse() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "correctPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = encoder.encode(password);

        // Act
        boolean matches = encoder.matches(wrongPassword, encodedPassword);

        // Assert
        assertFalse(matches);
    }

    @Test
    void bcryptPasswordEncoder_differentEncodesOfSamePassword_areDifferent() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "samePassword";

        // Act
        String encoded1 = encoder.encode(password);
        String encoded2 = encoder.encode(password);

        // Assert
        assertNotEquals(encoded1, encoded2); // BCrypt 每次生成的哈希都不同
        assertTrue(encoder.matches(password, encoded1));
        assertTrue(encoder.matches(password, encoded2));
    }

    @Test
    void bcryptPasswordEncoder_withEmptyPassword_encodesSuccessfully() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "";

        // Act
        String encodedPassword = encoder.encode(password);

        // Assert
        assertNotNull(encodedPassword);
        assertTrue(encoder.matches(password, encodedPassword));
    }

    @Test
    void bcryptPasswordEncoder_withLongPassword_encodesSuccessfully() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "a".repeat(50); // 50个字符的长密码 (BCrypt 最大支持 72 字节)

        // Act
        String encodedPassword = encoder.encode(password);

        // Assert
        assertNotNull(encodedPassword);
        assertTrue(encoder.matches(password, encodedPassword));
    }

    @Test
    void bcryptPasswordEncoder_withSpecialCharacters_encodesSuccessfully() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        String encodedPassword = encoder.encode(password);

        // Assert
        assertNotNull(encodedPassword);
        assertTrue(encoder.matches(password, encodedPassword));
    }

    @Test
    void bcryptPasswordEncoder_withUnicodeCharacters_encodesSuccessfully() {
        // Arrange
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "密码123🔐";

        // Act
        String encodedPassword = encoder.encode(password);

        // Assert
        assertNotNull(encodedPassword);
        assertTrue(encoder.matches(password, encodedPassword));
    }
}
