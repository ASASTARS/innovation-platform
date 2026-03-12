package com.abajin.innovation.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一将未捕获的异常转换为 Result 响应，避免前端看到堆栈信息
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 参数校验异常（@Valid 校验失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", message);
        return Result.error(400, message);
    }

    /**
     * 绑定参数异常（如表单提交）
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        log.warn("参数绑定失败: {}", message);
        return Result.error(400, message);
    }

    /**
     * 请求体解析异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("请求体解析失败: {}", ex.getMessage());
        return Result.error(400, "请求数据格式错误，请检查输入内容");
    }

    /**
     * 业务运行时异常（RuntimeException）
     * 项目中常通过 RuntimeException(message) 抛出业务错误
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException ex) {
        log.warn("业务异常: {}", ex.getMessage(), ex);
        return Result.error(500, ex.getMessage() != null ? ex.getMessage() : "系统内部错误");
    }

    /**
     * 兜底异常处理，防止异常泄露到前端
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex) {
        log.error("未处理异常: ", ex);
        return Result.error(500, "系统繁忙，请稍后重试");
    }

    private String formatFieldError(FieldError error) {
        String field = error.getField();
        String msg = error.getDefaultMessage();
        return field + ": " + msg;
    }
}

