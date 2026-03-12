package com.abajin.innovation.enums;

/**
 * 入驻状态枚举
 */
public enum EntryStatus {
    DRAFT("草稿"),
    PENDING("待审核"),
    APPROVED("已通过"),
    REJECTED("已驳回"),
    ENTERED("已入驻"),
    EXITED("已退出");

    private final String description;

    EntryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
