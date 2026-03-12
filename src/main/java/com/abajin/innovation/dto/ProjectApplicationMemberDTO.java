package com.abajin.innovation.dto;

import lombok.Data;

/**
 * 项目申请表单成员DTO
 */
@Data
public class ProjectApplicationMemberDTO {
    private Long id;
    private String studentId; // 学号
    private String name; // 姓名
    private String major; // 专业
    private String mainWork; // 主要工作
    private String signature; // 签名
    private Integer sortOrder; // 排序顺序
}
