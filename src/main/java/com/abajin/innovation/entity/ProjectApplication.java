package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 项目申请实体类
 * 用于学生申请加入团队、教师申请接管项目
 */
@Data
public class ProjectApplication {
    private Long id;
    private String applicationNo; // 申请编号
    private Long projectId; // 项目ID
    private String projectTitle; // 项目标题（关联查询）
    private Long applicantId; // 申请人ID
    private String applicantName; // 申请人姓名
    private String applicantRole; // 申请人角色
    private String applicationType; // 申请类型：JOIN_TEAM-加入团队, TAKE_OVER-接管项目
    private String applicationContent; // 申请内容
    private String qualifications; // 资质说明
    private String contactPhone; // 联系电话
    private String contactEmail; // 联系邮箱
    private String approvalStatus; // 审批状态：PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝
    private String approverRole; // 审批人角色：PROJECT_LEADER-项目负责人, COLLEGE_ADMIN-学院管理员
    private Long approverId; // 审批人ID
    private String approverName; // 审批人姓名
    private LocalDateTime approvalTime; // 审批时间
    private String approvalComment; // 审批意见
    private Integer status; // 状态：1-有效, 0-无效
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
