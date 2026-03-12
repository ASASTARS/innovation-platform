package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 空间类型实体类
 */
@Data
public class SpaceType {
    private Long id;
    private String name; // 空间类型名称
    private String code; // 空间类型代码
    private String description; // 空间类型描述
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
