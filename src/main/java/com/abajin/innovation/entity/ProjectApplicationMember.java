package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 项目申请表单成员实体类
 */
@Data
public class ProjectApplicationMember {
    private Long id;
    private Long applicationFormId; // 申请表单ID
    private String studentId; // 学号
    private String name; // 姓名
    private String major; // 专业
    private String mainWork; // 主要工作
    private String signature; // 签名
    private Integer sortOrder; // 排序顺序
    private LocalDateTime createTime;
}
