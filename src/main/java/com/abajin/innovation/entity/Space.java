package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 空间信息实体类
 */
@Data
public class Space {
    private Long id;
    private String name; // 空间名称
    private Long spaceTypeId; // 空间类型ID
    private String location; // 位置
    private Integer capacity; // 容量（人数）
    private String facilities; // 设施设备（JSON格式）
    private String description; // 空间描述
    private String status; // 状态：AVAILABLE-可用, MAINTENANCE-维护中, DISABLED-已禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
