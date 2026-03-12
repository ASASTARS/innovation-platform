package com.abajin.innovation.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 校企联合创新团队申请表DTO
 */
@Data
public class InnovationTeamApplicationDTO {
    private Long id;
    
    @NotBlank(message = "创新团队名称不能为空")
    private String teamName; // 创新团队名称
    
    private String cooperativeEnterprise; // 合作企业
    
    private String applicantStudentId; // 发起人学号
    
    @NotBlank(message = "发起人姓名不能为空")
    private String applicantName; // 发起人姓名
    
    private String applicantContact; // 发起人联系方式
    
    private String onCampusMentorName; // 校内导师姓名
    
    private String onCampusMentorContact; // 校内导师联系方式
    
    private String enterpriseMentorName; // 企业导师姓名
    
    private String enterpriseMentorContact; // 企业导师联系方式
    
    private String innovationDirection; // 创新方向
    
    private String positioningAndIdeas; // 创新团队定位与建设思路
    
    private String applicantSignature; // 发起人签字
    
    private LocalDate applicantSignDate; // 发起人签字日期
    
    private String onCampusMentorSignature; // 校内导师签字（学院盖章）
    
    private LocalDate onCampusMentorSignDate; // 校内导师签字日期
    
    private String enterpriseMentorSignature; // 企业导师签字（企业盖章）
    
    private LocalDate enterpriseMentorSignDate; // 企业导师签字日期
}
