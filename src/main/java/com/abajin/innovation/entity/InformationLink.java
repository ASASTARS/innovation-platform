package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 信息对接记录实体类
 */
@Data
public class InformationLink {
    private Long id;
    private String sourceType; // 源类型：PROJECT-项目, TEAM-团队, FUND-基金申请
    private Long sourceId; // 源ID
    private String targetType; // 目标类型：PROJECT-项目, TEAM-团队, FUND-基金申请
    private Long targetId; // 目标ID
    private String linkType; // 关联类型（如：合作、引用、关联等）
    private String description; // 关联说明
    private Long creatorId; // 创建人ID
    private LocalDateTime createTime;
}
