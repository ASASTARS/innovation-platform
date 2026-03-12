package com.abajin.innovation.enums;

/**
 * 活动状态枚举
 */
public enum ActivityStatus {
    DRAFT("草稿"),
    SUBMITTED("已提交"),
    APPROVED("已通过"),
    REJECTED("已拒绝"),
    PUBLISHED("已发布"),
    ONGOING("进行中"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;

    ActivityStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
