package com.abajin.innovation.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 统一响应结果测试
 */
class ResultTest {

    @Test
    void defaultConstructor_createsEmptyResult() {
        // Act
        Result<String> result = new Result<>();

        // Assert
        assertNull(result.getCode());
        assertNull(result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void parameterizedConstructor_createsResultWithValues() {
        // Act
        Result<String> result = new Result<>(200, "成功", "数据");

        // Assert
        assertEquals(200, result.getCode());
        assertEquals("成功", result.getMessage());
        assertEquals("数据", result.getData());
    }

    @Test
    void success_withData_returnsSuccessResult() {
        // Arrange
        String data = "测试数据";

        // Act
        Result<String> result = Result.success(data);

        // Assert
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertEquals(data, result.getData());
    }

    @Test
    void success_withMessageAndData_returnsCustomResult() {
        // Arrange
        String message = "自定义成功消息";
        String data = "测试数据";

        // Act
        Result<String> result = Result.success(message, data);

        // Assert
        assertEquals(200, result.getCode());
        assertEquals(message, result.getMessage());
        assertEquals(data, result.getData());
    }

    @Test
    void success_withNullData_returnsSuccessWithNullData() {
        // Act
        Result<Object> result = Result.success(null);

        // Assert
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void error_withMessage_returnsErrorResult() {
        // Arrange
        String errorMessage = "操作失败";

        // Act
        Result<Object> result = Result.error(errorMessage);

        // Assert
        assertEquals(500, result.getCode());
        assertEquals(errorMessage, result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void error_withCodeAndMessage_returnsCustomErrorResult() {
        // Arrange
        int errorCode = 404;
        String errorMessage = "资源不存在";

        // Act
        Result<Object> result = Result.error(errorCode, errorMessage);

        // Assert
        assertEquals(errorCode, result.getCode());
        assertEquals(errorMessage, result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void error_withCommonErrorCodes_returnsCorrectResults() {
        // Test 400 Bad Request
        Result<Object> badRequest = Result.error(400, "请求参数错误");
        assertEquals(400, badRequest.getCode());

        // Test 401 Unauthorized
        Result<Object> unauthorized = Result.error(401, "未授权");
        assertEquals(401, unauthorized.getCode());

        // Test 403 Forbidden
        Result<Object> forbidden = Result.error(403, "禁止访问");
        assertEquals(403, forbidden.getCode());

        // Test 404 Not Found
        Result<Object> notFound = Result.error(404, "资源不存在");
        assertEquals(404, notFound.getCode());

        // Test 500 Internal Server Error
        Result<Object> serverError = Result.error(500, "服务器内部错误");
        assertEquals(500, serverError.getCode());
    }

    @Test
    void setters_updateValuesCorrectly() {
        // Arrange
        Result<String> result = new Result<>();

        // Act
        result.setCode(201);
        result.setMessage("创建成功");
        result.setData("新数据");

        // Assert
        assertEquals(201, result.getCode());
        assertEquals("创建成功", result.getMessage());
        assertEquals("新数据", result.getData());
    }

    @Test
    void success_withDifferentTypes_worksCorrectly() {
        // Test with Integer
        Result<Integer> intResult = Result.success(42);
        assertEquals(42, intResult.getData());

        // Test with Boolean
        Result<Boolean> boolResult = Result.success(true);
        assertTrue(boolResult.getData());
    }

    @Test
    void chainingResults_maintainsIndependence() {
        // Act
        Result<String> result1 = Result.success("数据1");
        Result<String> result2 = Result.success("数据2");

        // Assert
        assertEquals("数据1", result1.getData());
        assertEquals("数据2", result2.getData());
        assertNotSame(result1, result2);
    }
}
