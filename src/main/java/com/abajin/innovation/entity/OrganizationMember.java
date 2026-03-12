package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 组织成员实体类
 */
@Data
public class OrganizationMember {
    private Long id;
    private Long organizationId; // 组织ID
    private Long userId; // 用户ID
    private String userName; // 用户姓名
    private String role; // 角色：LEADER-负责人, MEMBER-成员, ADVISOR-顾问
    private LocalDateTime joinTime; // 加入时间
    private String status; // 状态：ACTIVE-活跃, INACTIVE-非活跃
}
