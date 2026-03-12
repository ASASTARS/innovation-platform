package com.abajin.innovation.controller;

import com.abajin.innovation.annotation.RequiresRole;
import com.abajin.innovation.common.Constants;
import com.abajin.innovation.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限控制示例控制器
 * 展示如何使用 @RequiresRole 注解进行权限控制
 */
@RestController
@RequestMapping("/examples")
public class PermissionExampleController {

    /**
     * 示例1：学生和教师都可以访问
     * GET /api/examples/student-teacher
     */
    @GetMapping("/student-teacher")
    @RequiresRole(value = {Constants.ROLE_STUDENT, Constants.ROLE_TEACHER})
    public Result<Map<String, String>> studentTeacherExample(@RequestAttribute("userId") Long userId) {
        Map<String, String> data = new HashMap<>();
        data.put("message", "学生和教师都可以访问此接口");
        data.put("userId", userId.toString());
        return Result.success(data);
    }

    /**
     * 示例2：只有学生可以访问
     * GET /api/examples/student-only
     */
    @GetMapping("/student-only")
    @RequiresRole(value = {Constants.ROLE_STUDENT}, allowAdmin = false)
    public Result<Map<String, String>> studentOnlyExample(@RequestAttribute("userId") Long userId) {
        Map<String, String> data = new HashMap<>();
        data.put("message", "只有学生可以访问此接口");
        data.put("userId", userId.toString());
        return Result.success(data);
    }

    /**
     * 示例3：只有教师可以访问
     * GET /api/examples/teacher-only
     */
    @GetMapping("/teacher-only")
    @RequiresRole(value = {Constants.ROLE_TEACHER}, allowAdmin = false)
    public Result<Map<String, String>> teacherOnlyExample(@RequestAttribute("userId") Long userId) {
        Map<String, String> data = new HashMap<>();
        data.put("message", "只有教师可以访问此接口");
        data.put("userId", userId.toString());
        return Result.success(data);
    }

    /**
     * 示例4：学院管理员可以访问（不允许学校管理员）
     * GET /api/examples/college-admin-only
     */
    @GetMapping("/college-admin-only")
    @RequiresRole(value = {Constants.ROLE_COLLEGE_ADMIN}, allowAdmin = false)
    public Result<Map<String, String>> collegeAdminOnlyExample(@RequestAttribute("userId") Long userId) {
        Map<String, String> data = new HashMap<>();
        data.put("message", "只有学院管理员可以访问此接口");
        data.put("userId", userId.toString());
        return Result.success(data);
    }

    /**
     * 示例5：学校管理员可以访问（不允许学院管理员）
     * GET /api/examples/school-admin-only
     */
    @GetMapping("/school-admin-only")
    @RequiresRole(value = {Constants.ROLE_SCHOOL_ADMIN}, allowAdmin = false)
    public Result<Map<String, String>> schoolAdminOnlyExample(@RequestAttribute("userId") Long userId) {
        Map<String, String> data = new HashMap<>();
        data.put("message", "只有学校管理员可以访问此接口");
        data.put("userId", userId.toString());
        return Result.success(data);
    }

    /**
     * 示例6：管理员都可以访问（学院管理员和学校管理员）
     * GET /api/examples/admin-all
     */
    @GetMapping("/admin-all")
    @RequiresRole(value = {}, allowAdmin = true)
    public Result<Map<String, String>> adminAllExample(@RequestAttribute("userId") Long userId) {
        Map<String, String> data = new HashMap<>();
        data.put("message", "所有管理员都可以访问此接口");
        data.put("userId", userId.toString());
        return Result.success(data);
    }

    /**
     * 示例7：学生、教师和管理员都可以访问
     * GET /api/examples/all-roles
     */
    @GetMapping("/all-roles")
    @RequiresRole(value = {Constants.ROLE_STUDENT, Constants.ROLE_TEACHER}, allowAdmin = true)
    public Result<Map<String, String>> allRolesExample(@RequestAttribute("userId") Long userId) {
        Map<String, String> data = new HashMap<>();
        data.put("message", "学生、教师和管理员都可以访问此接口");
        data.put("userId", userId.toString());
        return Result.success(data);
    }

    /**
     * 示例8：无需权限验证（但需要登录）
     * GET /api/examples/public
     */
    @GetMapping("/public")
    public Result<Map<String, String>> publicExample(@RequestAttribute("userId") Long userId) {
        Map<String, String> data = new HashMap<>();
        data.put("message", "所有登录用户都可以访问此接口");
        data.put("userId", userId.toString());
        return Result.success(data);
    }
}
