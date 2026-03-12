package com.abajin.innovation.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 基金类型实体类
 */
@Data
public class FundType {
    private Long id;
    private String name; // 基金类型名称
    private String code; // 基金类型代码
    private String description; // 基金类型描述
    private BigDecimal maxAmount; // 最大申请金额
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
