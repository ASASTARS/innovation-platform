package com.abajin.innovation.enums;

import lombok.Getter;

/**
 * 成员状态枚举
 */
@Getter
public enum MemberStatus {
    INACTIVE("INACTIVE", "已退出"),
    ACTIVE("ACTIVE", "正常");

    private final String code;
    private final String description;

    MemberStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MemberStatus fromCode(String code) {
        for (MemberStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown member status code: " + code);
    }
}
