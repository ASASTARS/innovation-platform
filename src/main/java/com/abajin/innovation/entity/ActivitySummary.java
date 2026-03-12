package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 活动总结实体类
 */
@Data
public class ActivitySummary {
    private Long id;
    private Long activityId; // 活动ID
    private Integer actualParticipants; // 实际参与人数
    private String summaryContent; // 活动总结内容
    private String achievements; // 活动成果
    private String photos; // 活动照片（JSON数组，存储文件路径）
    private String attachments; // 附件（JSON数组，存储文件路径）
    private String status; // 状态：DRAFT-草稿, SUBMITTED-已提交, APPROVED-已通过
    private String approvalStatus; // 审批状态：PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝
    private Long reviewerId; // 审批人ID
    private String reviewComment; // 审批意见
    private LocalDateTime reviewTime; // 审批时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /** 关联查询：活动标题（列表展示用，非表字段） */
    private String activityTitle;
}
