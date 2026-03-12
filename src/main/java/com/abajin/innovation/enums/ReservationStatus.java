package com.abajin.innovation.enums;

/**
 * 空间预定状态枚举
 */
public enum ReservationStatus {
    PENDING("待审核"),
    APPROVED("已通过"),
    REJECTED("已拒绝"),
    CANCELLED("已取消"),
    COMPLETED("已完成");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
