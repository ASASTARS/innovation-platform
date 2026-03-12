package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 入驻申请实体类
 * 根据实际申请表设计
 */
@Data
public class EntryApplication {
    private Long id;
    
    // ========== 申请人信息 ==========
    private Long applicantId; // 申请人ID（用户ID）
    private String applicantName; // 申请人姓名
    private String applicantStudentId; // 发起人学号
    private String applicantPhone; // 发起人联系方式
    
    // ========== 团队信息 ==========
    private String teamName; // 创新团队名称
    private String teamType; // 团队类型：INNOVATION-创新团队, STARTUP-创业团队, RESEARCH-科研团队
    private String teamDescription; // 团队简介
    private String innovationDirection; // 创新方向
    private String teamPositioning; // 创新团队定位与建设思路（详细描述）
    private Integer teamSize; // 团队规模（总人数）
    
    /** 招募人员的要求（必填，入驻通过后展示在团队卡片） */
    private String recruitmentRequirements;
    
    // ========== 导师信息 ==========
    private String instructorName; // 指导教师姓名
    private String instructorContact; // 指导教师联系方式
    private String campusMentorName; // 校内导师姓名
    private String campusMentorContact; // 校内导师联系方式
    private String enterpriseMentorName; // 企业导师姓名
    private String enterpriseMentorContact; // 企业导师联系方式
    
    // ========== 企业信息 ==========
    private String partnerCompany; // 合作企业
    
    // ========== 项目信息 ==========
    private String projectName; // 项目名称
    private String projectDescription; // 项目简介
    private String projectAchievements; // 项目成绩（AB类赛事参赛所获最高荣誉奖项等）
    private String expectedOutcomes; // 预期成果
    
    // ========== 竞赛信息 ==========
    private Integer isCompetitionRegistered; // 是否已报名参加竞赛：0-否，1-是
    private String competitionName; // 竞赛名称（拟报或已报竞赛）
    
    // ========== 团队成员信息（JSON格式存储） ==========
    private String teamMembers; // 项目组成员（JSON数组，包含学号、姓名、专业、主要工作）
    
    // ========== 联系方式 ==========
    private String contactPhone; // 联系电话
    private String contactEmail; // 联系邮箱
    
    // ========== 附件 ==========
    private String attachments; // 附件（JSON数组，存储文件路径）
    
    // ========== 状态信息 ==========
    private String status; // 状态：DRAFT-草稿, PENDING-待审核, APPROVED-已通过, REJECTED-已驳回, ENTERED-已入驻, EXITED-已退出
    private String approvalStatus; // 审批状态：PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝
    
    // ========== 审核信息 ==========
    private Long reviewerId; // 审批人ID
    private String reviewerName; // 审批人姓名（关联查询）
    private String reviewComment; // 审批意见
    private LocalDateTime reviewTime; // 审批时间
    
    // ========== 入驻信息 ==========
    private LocalDateTime entryTime; // 入驻时间
    private LocalDateTime exitTime; // 退出时间
    private String exitReason; // 退出原因
    
    // ========== 时间戳 ==========
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
