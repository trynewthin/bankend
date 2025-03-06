package com.zhixuanche.car.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 车辆详情数据传输对象
 */
@Data
public class CarDetailDTO {
    
    private Integer detailId;       // 详情ID
    private Integer carId;          // 关联车辆ID
    private String engine;          // 发动机
    private String transmission;    // 变速箱
    private String fuelType;        // 燃料类型
    private BigDecimal fuelConsumption; // 油耗(L/100km)
    private Integer seats;          // 座位数
    private String color;           // 可选颜色，逗号分隔
    private String bodySize;        // 车身尺寸
    private BigDecimal wheelbase;   // 轴距(mm)
    private String features;        // 配置特点
    private String warranty;        // 保修信息
} 