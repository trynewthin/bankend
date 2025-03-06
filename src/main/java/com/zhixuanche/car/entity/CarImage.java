package com.zhixuanche.car.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 车辆图片实体类
 */
@Data
public class CarImage {
    
    private Integer imageId;        // 图片ID
    private Integer carId;          // 关联车辆ID
    private String imageType;       // 图片类型：缩略图、完整图1-5
    private String imageUrl;        // 图片URL
    private LocalDateTime uploadTime; // 上传时间
} 