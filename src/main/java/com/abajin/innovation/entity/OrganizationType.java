package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 组织类型实体类
 */
@Data
public class OrganizationType {
    private Long id;
    private String name; // 组织类型名称
    private String code; // 组织类型代码
    private String description; // 组织类型描述
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
