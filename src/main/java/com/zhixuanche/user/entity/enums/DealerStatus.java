package com.zhixuanche.user.entity.enums;

/**
 * 经销商状态枚举
 */
public enum DealerStatus {
    PENDING(0, "待审核"),
    APPROVED(1, "已审核"),
    REJECTED(2, "已拒绝");

    private final Integer code;
    private final String description;

    DealerStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
} 