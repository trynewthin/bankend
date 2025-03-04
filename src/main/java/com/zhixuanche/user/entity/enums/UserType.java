package com.zhixuanche.user.entity.enums;

/**
 * 用户类型枚举
 */
public enum UserType {
    NORMAL_USER("普通用户"),
    DEALER("经销商"),
    ADMIN("管理员");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 