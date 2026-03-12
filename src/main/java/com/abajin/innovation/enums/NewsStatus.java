package com.abajin.innovation.enums;

/**
 * 新闻状态枚举
 */
public enum NewsStatus {
    DRAFT("草稿"),
    PENDING("待审核"),
    PUBLISHED("已发布"),
    REJECTED("已驳回");

    private final String description;

    NewsStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
