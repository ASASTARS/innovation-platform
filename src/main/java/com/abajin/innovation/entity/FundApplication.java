package com.abajin.innovation.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 基金申请实体类
 */
@Data
public class FundApplication {
    private Long id;
    private String title; // 申请标题
    private Long fundTypeId; // 基金类型ID
    private Long applicantId; // 申请人ID
    private String applicantName; // 申请人姓名
    private String applicantType; // 申请人类型：USER-个人, TEAM-团队, PROJECT-项目
    private Long applicantEntityId; // 申请人实体ID
    private Long projectId; // 关联项目ID
    private Long teamId; // 关联团队ID
    private BigDecimal applicationAmount; // 申请金额
    private String applicationReason; // 申请理由
    private String applicationContent; // 申请内容
    private String expectedOutcomes; // 预期成果
    private String attachments; // 附件（JSON数组，存储文件路径）
    private String status; // 状态：DRAFT-草稿, SUBMITTED-已提交, APPROVED-已通过, REJECTED-已拒绝, FUNDED-已资助
    private String approvalStatus; // 审批状态：PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝
    private Long reviewerId; // 审批人ID
    private String reviewComment; // 审批意见
    private LocalDateTime reviewTime; // 审批时间
    private BigDecimal approvedAmount; // 批准金额
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
