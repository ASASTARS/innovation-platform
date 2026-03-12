package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 学院实体类
 */
@Data
public class College {
    private Long id;
    private String name; // 学院名称
    private String code; // 学院代码
    private String description; // 学院描述
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
