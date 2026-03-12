package com.abajin.innovation.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 活动申报DTO
 */
@Data
public class ActivityDTO {
    private Long id;
    
    @NotBlank(message = "活动标题不能为空")
    private String title;
    
    private Long activityTypeId;
    
    private String activitySeries; // 先锋双创榜样、双创技术讲坛、企业家大讲堂
    private String activityTypeOther; // 其他活动类型（当类型为其他时）
    
    private String organizerType; // USER, TEAM, ORGANIZATION
    private Long organizerEntityId; // 组织者实体ID
    
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;
    
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
    
    private Long spaceId; // 关联预约空间ID；选“其他”时为 null
    private String location; // 活动地点（选空间时可为空/空间名，选“其他”时必填）
    private String description;
    private String content;
    private String registrationLink; // 报名链接
    private String qrCodeUrl; // 报名二维码URL
    private Long hostUnitId; // 主办单位ID
    private String coOrganizerIds; // 承办单位ID列表，逗号分隔
    private String otherUnits; // 其他单位
    private Integer maxParticipants;
    // private LocalDateTime registrationDeadline; // 已废弃，不再使用
}
