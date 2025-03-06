package com.zhixuanche.car.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 车辆图片数据传输对象
 */
@Data
public class CarImageDTO {
    
    private Integer imageId;        // 图片ID
    private Integer carId;          // 关联车辆ID
    private String imageType;       // 图片类型
    private String imageUrl;        // 图片URL
    private LocalDateTime uploadTime; // 上传时间
} 