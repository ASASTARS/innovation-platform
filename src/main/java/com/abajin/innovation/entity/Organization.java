package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 组织实体类（社团、工作室等）
 */
@Data
public class Organization {
    private Long id;
    private String name; // 组织名称
    private Long organizationTypeId; // 组织类型ID
    private String code; // 组织代码
    private String description; // 组织描述
    private Long leaderId; // 负责人ID
    private String leaderName; // 负责人姓名
    private Integer memberCount; // 成员数量
    private Long collegeId; // 所属学院ID
    private String collegeName; // 学院名称
    private String status; // 状态：ACTIVE-活跃, INACTIVE-非活跃, DISBANDED-已解散
    private String approvalStatus; // 审批状态：PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝
    private Long reviewerId; // 审批人ID
    private LocalDateTime reviewTime; // 审批时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
