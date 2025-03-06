package com.zhixuanche.car.entity.enums;

/**
 * 车辆状态枚举
 */
public enum CarStatus {
    
    DOWN(0, "下架"),
    UP(1, "在售");
    
    private final int code;
    private final String description;
    
    CarStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static CarStatus getByCode(int code) {
        for (CarStatus status : CarStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
} 