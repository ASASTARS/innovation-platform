package com.abajin.innovation.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Redis 工具类测试
 */
class RedisUtilTest {

    private RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private ZSetOperations<String, Object> zSetOperations;
    private RedisUtil redisUtil;

    @BeforeEach
    void setUp() throws Exception {
        // 手动创建 mock
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        zSetOperations = mock(ZSetOperations.class);
        
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        
        // 创建被测对象并通过反射注入 mock
        redisUtil = new RedisUtil();
        Field redisTemplateField = RedisUtil.class.getDeclaredField("redisTemplate");
        redisTemplateField.setAccessible(true);
        redisTemplateField.set(redisUtil, redisTemplate);
    }

    @Test
    void buildKey_withSingleParam_returnsKey() {
        // Act
        String key = redisUtil.buildKey("user");

        // Assert
        assertEquals("user", key);
    }

    @Test
    void buildKey_withMultipleParams_returnsJoinedKey() {
        // Act
        String key = redisUtil.buildKey("user", "123", "profile");

        // Assert
        assertEquals("user.123.profile", key);
    }

    @Test
    void buildKey_withEmptyParams_returnsEmptyString() {
        // Act
        String key = redisUtil.buildKey();

        // Assert
        assertEquals("", key);
    }

    @Test
    void exist_withExistingKey_returnsTrue() {
        // Arrange
        when(redisTemplate.hasKey("existingKey")).thenReturn(true);

        // Act
        boolean exists = redisUtil.exist("existingKey");

        // Assert
        assertTrue(exists);
    }

    @Test
    void exist_withNonExistingKey_returnsFalse() {
        // Arrange
        when(redisTemplate.hasKey("nonExistingKey")).thenReturn(false);

        // Act
        boolean exists = redisUtil.exist("nonExistingKey");

        // Assert
        assertFalse(exists);
    }

    @Test
    void del_withExistingKey_returnsTrue() {
        // Arrange
        when(redisTemplate.delete("keyToDelete")).thenReturn(true);

        // Act
        boolean deleted = redisUtil.del("keyToDelete");

        // Assert
        assertTrue(deleted);
    }

    @Test
    void del_withNonExistingKey_returnsFalse() {
        // Arrange
        when(redisTemplate.delete("nonExistingKey")).thenReturn(false);

        // Act
        boolean deleted = redisUtil.del("nonExistingKey");

        // Assert
        assertFalse(deleted);
    }

    @Test
    void set_withKeyAndValue_callsOpsForValue() {
        // Act
        redisUtil.set("myKey", "myValue");

        // Assert
        verify(valueOperations).set("myKey", "myValue");
    }

    @Test
    void get_withExistingKey_returnsValue() {
        // Arrange
        when(valueOperations.get("myKey")).thenReturn("myValue");

        // Act
        String value = redisUtil.get("myKey");

        // Assert
        assertEquals("myValue", value);
    }

    @Test
    void get_withNonExistingKey_returnsNull() {
        // Arrange
        when(valueOperations.get("nonExistingKey")).thenReturn(null);

        // Act
        String value = redisUtil.get("nonExistingKey");

        // Assert
        assertNull(value);
    }

    @Test
    void setNx_withNewKey_returnsTrue() {
        // Arrange
        when(valueOperations.setIfAbsent("newKey", "value", 60L, TimeUnit.SECONDS))
                .thenReturn(true);

        // Act
        boolean result = redisUtil.setNx("newKey", "value", 60L, TimeUnit.SECONDS);

        // Assert
        assertTrue(result);
    }

    @Test
    void setNx_withExistingKey_returnsFalse() {
        // Arrange
        when(valueOperations.setIfAbsent("existingKey", "value", 60L, TimeUnit.SECONDS))
                .thenReturn(false);

        // Act
        boolean result = redisUtil.setNx("existingKey", "value", 60L, TimeUnit.SECONDS);

        // Assert
        assertFalse(result);
    }

    @Test
    void zAdd_withValidParams_returnsTrue() {
        // Arrange
        when(zSetOperations.add("zsetKey", "member", 100.0)).thenReturn(true);

        // Act
        boolean result = redisUtil.zAdd("zsetKey", "member", 100L);

        // Assert
        assertTrue(result);
    }

    @Test
    void countZset_withValidKey_returnsCount() {
        // Arrange
        when(zSetOperations.size("zsetKey")).thenReturn(10L);

        // Act
        Long count = redisUtil.countZset("zsetKey");

        // Assert
        assertEquals(10L, count);
    }

    @Test
    void rangeZset_withValidRange_returnsMembers() {
        // Arrange
        Set<Object> expectedSet = new HashSet<>();
        expectedSet.add("member1");
        expectedSet.add("member2");
        when(zSetOperations.range("zsetKey", 0, 10)).thenReturn(expectedSet);

        // Act
        Set<String> result = redisUtil.rangeZset("zsetKey", 0, 10);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void removeZset_withValidMember_returnsRemovedCount() {
        // Arrange
        when(zSetOperations.remove("zsetKey", "member")).thenReturn(1L);

        // Act
        Long result = redisUtil.removeZset("zsetKey", "member");

        // Assert
        assertEquals(1L, result);
    }

    @Test
    void score_withValidMember_returnsScore() {
        // Arrange
        when(zSetOperations.score("zsetKey", "member")).thenReturn(100.0);

        // Act
        Double score = redisUtil.score("zsetKey", "member");

        // Assert
        assertEquals(100.0, score);
    }

    @Test
    void score_withNonExistingMember_returnsNull() {
        // Arrange
        when(zSetOperations.score("zsetKey", "nonExisting")).thenReturn(null);

        // Act
        Double score = redisUtil.score("zsetKey", "nonExisting");

        // Assert
        assertNull(score);
    }

    @Test
    void rangeByScore_withValidRange_returnsMembers() {
        // Arrange
        Set<Object> expectedSet = new HashSet<>();
        expectedSet.add("member1");
        when(zSetOperations.rangeByScore("zsetKey", 0.0, 100.0)).thenReturn(expectedSet);

        // Act
        Set<String> result = redisUtil.rangeByScore("zsetKey", 0, 100);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void addScore_withValidParams_returnsNewScore() {
        // Arrange
        when(zSetOperations.incrementScore("zsetKey", "member", 10.0)).thenReturn(110.0);

        // Act
        Object result = redisUtil.addScore("zsetKey", "member", 10.0);

        // Assert
        assertEquals(110.0, result);
    }

    @Test
    void rank_withValidMember_returnsRank() {
        // Arrange
        when(zSetOperations.rank("zsetKey", "member")).thenReturn(5L);

        // Act
        Object result = redisUtil.rank("zsetKey", "member");

        // Assert
        assertEquals(5L, result);
    }

    @Test
    void removeZsetList_withMultipleMembers_removesAll() {
        // Arrange
        Set<String> members = new HashSet<>();
        members.add("member1");
        members.add("member2");

        // Act
        redisUtil.removeZsetList("zsetKey", members);

        // Assert
        verify(zSetOperations).remove("zsetKey", "member1");
        verify(zSetOperations).remove("zsetKey", "member2");
    }
}
