package com.abajin.innovation.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * 项目申请表单DTO
 */
@Data
public class ProjectApplicationFormDTO {
    private Long id;
    
    private String instructorName; // 指导教师姓名
    
    @NotBlank(message = "团队负责人姓名不能为空")
    private String leaderName; // 团队负责人姓名
    
    private String leaderPhone; // 团队负责人联系电话
    
    private Boolean competitionRegistered; // 是否已报名参加竞赛
    
    private String competitionName; // 竞赛名称
    
    @NotBlank(message = "项目名称不能为空")
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
    
    private String instructorSignature; // 指导教师签字
    
    private LocalDate instructorSignDate; // 指导教师签字日期
    
    @Valid
    private List<ProjectApplicationMemberDTO> members; // 项目组成员（最多4人）
}

