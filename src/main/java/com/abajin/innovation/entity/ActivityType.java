package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 活动类型实体类
 */
@Data
public class ActivityType {
    private Long id;
    private String name; // 活动类型名称
    private String code; // 活动类型代码
    private String description; // 活动类型描述
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
