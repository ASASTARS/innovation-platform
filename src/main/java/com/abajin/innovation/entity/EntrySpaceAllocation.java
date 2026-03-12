package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 入驻空间分配实体类
 */
@Data
public class EntrySpaceAllocation {
    private Long id;
    private Long entryApplicationId; // 入驻申请ID
    private Long spaceId; // 分配的空间ID
    private LocalDate startDate; // 开始日期
    private LocalDate endDate; // 结束日期
    private String status; // 状态：ACTIVE-使用中, EXPIRED-已过期, RELEASED-已释放
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
