package com.abajin.innovation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * CAS账号合并DTO
 */
@Data
public class CasMergeDTO {
    /**
     * CAS用户ID（学号/工号）
     */
    @NotBlank(message = "CAS用户ID不能为空")
    private String casUid;

    /**
     * 本地账号密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
