package com.abajin.innovation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 空间占用时段（用于空间预约与活动共用时的冲突展示）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OccupiedSlotDTO {
    private String startTime; // HH:mm:ss
    private String endTime;   // HH:mm:ss
}
