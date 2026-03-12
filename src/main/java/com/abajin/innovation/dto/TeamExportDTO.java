package com.abajin.innovation.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 团队导出 Excel 行 DTO
 */
@Data
public class TeamExportDTO {

    @ExcelProperty("团队名称")
    private String name;

    @ExcelProperty("团队类型")
    private String teamType;

    @ExcelProperty("团队简介")
    private String description;

    @ExcelProperty("负责人姓名")
    private String leaderName;

    @ExcelProperty("负责人ID")
    private Long leaderId;

    @ExcelProperty("负责人学号")
    private String leaderStudentId;

    @ExcelProperty("学院名")
    private String collegeName;

    @ExcelProperty("指导老师")
    private String instructorName;

    @ExcelProperty("成员数量")
    private Integer memberCount;

    @ExcelProperty("是否招募")
    private String recruiting;

    @ExcelProperty("是否公开")
    private String isPublic;

    @ExcelProperty("招募要求")
    private String recruitmentRequirement;

    @ExcelProperty("历史荣誉")
    private String honors;

    @ExcelProperty("项目标签")
    private String tags;

    @ExcelProperty("创建时间")
    private String createTime;
}
