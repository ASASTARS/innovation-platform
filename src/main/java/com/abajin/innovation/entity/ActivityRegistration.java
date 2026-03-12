package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 活动报名实体类
 */
@Data
public class ActivityRegistration {
    private Long id;
    private Long activityId; // 活动ID
    private Long userId; // 报名用户ID
    private String userName; // 报名用户姓名
    private String contactPhone; // 联系电话
    private String email; // 邮箱
    private String remark; // 备注
    private String status; // 状态：PENDING-待审核, APPROVED-已通过, REJECTED-已拒绝, CANCELLED-已取消
    private String approvalStatus; // 审批状态：PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝
    private Long reviewerId; // 审批人ID
    private LocalDateTime reviewTime; // 审批时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联的活动信息（用于查询时关联）
    private String activityTitle; // 活动标题
    private LocalDateTime activityStartTime; // 活动开始时间
    private String activityLocation; // 活动地点
    private String activityStatus; // 活动状态
}
