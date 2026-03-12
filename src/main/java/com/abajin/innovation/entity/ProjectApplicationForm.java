package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目申请表单实体类（指导教师申请表）
 */
@Data
public class ProjectApplicationForm {
    private Long id;
    private String instructorName; // 指导教师姓名
    private String leaderName; // 团队负责人姓名
    private String leaderPhone; // 团队负责人联系电话
    private Integer competitionRegistered; // 是否已报名参加竞赛：0-否，1-是
    private String competitionName; // 竞赛名称（拟报或已报竞赛）
    private String projectName; // 项目名称
    private String projectIntroduction; // 项目简介
    private String projectAchievements; // 项目成绩
    private String expectedOutcomes; // 预期成果
    private String leaderDeclaration; // 团队负责人声明内容
    private String leaderSignature; // 团队负责人签字
    private LocalDate leaderSignDate; // 团队负责人签字日期
    private String memberDeclaration; // 团队成员声明内容
    private String memberSignature; // 团队成员签字
    private LocalDate memberSignDate; // 团队成员签字日期
    private String instructorOpinion; // 指导教师意见
    private String instructorSignature; // 指导教师签字（学院盖章）
    private LocalDate instructorSignDate; // 指导教师签字日期
    private String status; // 状态：DRAFT-草稿, SUBMITTED-已提交, APPROVED-已通过, REJECTED-已拒绝
    private Long applicantId; // 申请人ID
    private String applicantName; // 申请人姓名
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
