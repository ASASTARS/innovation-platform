package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 人员类型实体类
 */
@Data
public class PersonType {
    private Long id;
    private String name; // 人员类型名称
    private String code; // 人员类型代码
    private String description; // 人员类型描述
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
