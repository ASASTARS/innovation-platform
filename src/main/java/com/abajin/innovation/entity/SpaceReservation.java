package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 空间预定实体类
 */
@Data
public class SpaceReservation {
    private Long id;
    private Long spaceId; // 空间ID，选“其他”时为 null
    private String customSpaceName; // 其他空间名称（当 spaceId 为空时使用）
    private Long applicantId; // 申请人ID
    private String applicantName; // 申请人姓名
    private LocalDate reservationDate; // 预定日期
    private LocalTime startTime; // 开始时间
    private LocalTime endTime; // 结束时间
    private String purpose; // 使用目的
    private Integer attendeeCount; // 预计参与人数
    private String contactPhone; // 联系电话
    private String status; // 状态：PENDING-待审核, APPROVED-已通过, REJECTED-已拒绝, CANCELLED-已取消, COMPLETED-已完成
    private String approvalStatus; // 审批状态：PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝
    private Long reviewerId; // 审批人ID
    private String reviewComment; // 审批意见
    private LocalDateTime reviewTime; // 审批时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
