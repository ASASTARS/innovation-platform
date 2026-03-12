package com.abajin.innovation.enums;

/**
 * 空间状态枚举
 */
public enum SpaceStatus {
    AVAILABLE("可用"),
    MAINTENANCE("维护中"),
    DISABLED("已禁用");

    private final String description;

    SpaceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
