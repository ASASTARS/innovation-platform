package com.abajin.innovation.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 团队导入 Excel 行 DTO
 *
 * 适配当前使用的 Excel 模板（见前端“导入团队”弹窗）：
 * A: 团队名称
 * B: 队伍类型
 * C: 团队简介
 * D: 负责人姓名（使用真实姓名匹配系统用户）
 * E: 负责人ID（可选，用作学号等）
 * F: 学院名
 * G: 指导老师
 * H: 成员数量（当前导入逻辑中不使用）
 * I: 是否招募
 * J: 是否公开
 * K: 招募要求
 * L: 历史荣誉
 * M: 项目标签
 * N: 创建时间（当前导入逻辑中不使用）
 */
@Data
public class TeamImportDTO {

    @ExcelProperty(value = "团队名称", index = 0)
    private String name;

    @ExcelProperty(value = "队伍类型", index = 1)
    private String teamType;

    @ExcelProperty(value = "团队简介", index = 2)
    private String description;

    @ExcelProperty(value = "负责人姓名", index = 3)
    private String leaderRealName;

    @ExcelProperty(value = "负责人ID", index = 4)
    private String leaderStudentId;

    @ExcelProperty(value = "学院名", index = 5)
    private String collegeName;

    @ExcelProperty(value = "指导老师", index = 6)
    private String instructorName;

    @ExcelProperty(value = "成员数量", index = 7)
    private String memberCount;

    @ExcelProperty(value = "是否招募", index = 8)
    private String recruiting;

    @ExcelProperty(value = "是否公开", index = 9)
    private String isPublic;

    @ExcelProperty(value = "招募要求", index = 10)
    private String recruitmentRequirement;

    @ExcelProperty(value = "历史荣誉", index = 11)
    private String honors;

    @ExcelProperty(value = "项目标签", index = 12)
    private String tags;

    @ExcelProperty(value = "创建时间", index = 13)
    private String createTime;
}
