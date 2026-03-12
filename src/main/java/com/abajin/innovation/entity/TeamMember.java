package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 团队成员实体类
 */
@Data
public class TeamMember {
    private Long id;
    private Long teamId; // 团队ID
    private Long userId; // 用户ID
    private String userName; // 用户姓名
    private String role; // 角色：LEADER-队长, MEMBER-成员
    private String status; // 状态：ACTIVE-正常, INACTIVE-已退出
    private String approvalStatus; // 审批状态：PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝
    private LocalDateTime joinTime; // 加入时间
    // 申请时填写的个人信息（队长审核可见）
    private String studentId;      // 学号
    private String grade;         // 年级
    private String major;         // 专业
    private String competitionExperience; // 比赛经历
    private String awards;        // 获奖情况
    private String contactPhone;  // 手机号
    private String resumeAttachment; // 简历附件（URL或路径）
}
