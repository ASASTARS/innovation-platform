package com.abajin.innovation.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * 项目申请DTO
 */
@Data
public class ProjectApplicationDTO {
    @NotNull(message = "项目ID不能为空")
    private Long projectId;
    
    private String applicationContent; // 申请内容
    private String qualifications; // 资质说明
    private String contactPhone; // 联系电话
    private String contactEmail; // 联系邮箱
}
