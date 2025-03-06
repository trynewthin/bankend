package com.zhixuanche.car.entity.enums;

/**
 * 车辆图片类型枚举
 */
public enum CarImageType {
    
    THUMBNAIL("缩略图"),
    FULL_IMAGE_1("完整图1"),
    FULL_IMAGE_2("完整图2"),
    FULL_IMAGE_3("完整图3"),
    FULL_IMAGE_4("完整图4"),
    FULL_IMAGE_5("完整图5");
    
    private final String description;
    
    CarImageType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 