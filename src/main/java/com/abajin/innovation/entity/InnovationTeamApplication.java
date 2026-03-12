package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 校企联合创新团队申请表实体类
 */
@Data
public class InnovationTeamApplication {
    private Long id;
    private String teamName; // 创新团队名称
    private String cooperativeEnterprise; // 合作企业
    private String applicantStudentId; // 发起人学号
    private String applicantName; // 发起人姓名
    private String applicantContact; // 发起人联系方式
    private String onCampusMentorName; // 校内导师姓名
    private String onCampusMentorContact; // 校内导师联系方式
    private String enterpriseMentorName; // 企业导师姓名
    private String enterpriseMentorContact; // 企业导师联系方式
    private String innovationDirection; // 创新方向
    private String positioningAndIdeas; // 创新团队定位与建设思路
    private String applicantSignature; // 发起人签字
    private LocalDate applicantSignDate; // 发起人签字日期
    private String onCampusMentorSignature; // 校内导师签字（学院盖章）
    private LocalDate onCampusMentorSignDate; // 校内导师签字日期
    private String enterpriseMentorSignature; // 企业导师签字（企业盖章）
    private LocalDate enterpriseMentorSignDate; // 企业导师签字日期
    private String status; // 状态：DRAFT-草稿, SUBMITTED-已提交, APPROVED-已通过, REJECTED-已拒绝
    private Long applicantId; // 申请人ID
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
