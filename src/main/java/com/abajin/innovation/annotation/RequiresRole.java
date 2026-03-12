package com.abajin.innovation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色权限注解
 * 用于标记需要特定角色才能访问的接口
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    /**
     * 允许访问的角色列表
     */
    String[] value() default {};

    /**
     * 是否允许管理员访问（学校管理员和学院管理员）
     */
    boolean allowAdmin() default false;
}
