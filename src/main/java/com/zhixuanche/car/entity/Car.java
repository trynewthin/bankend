package com.zhixuanche.car.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车辆实体类
 */
@Data
public class Car {
    
    private Integer carId;          // 车辆ID
    private Integer dealerId;       // 经销商ID
    private String brand;           // 品牌
    private String model;           // 型号
    private Integer year;           // 年款
    private BigDecimal price;       // 价格
    private String category;        // 类别：轿车/SUV/MPV等
    private Integer status;         // 状态：1-在售, 0-下架
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
} 