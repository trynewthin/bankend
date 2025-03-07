package com.zhixuanche.recommendation.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车辆推荐数据传输对象
 */
@Data
public class CarRecommendationDTO {
    private Integer carId;
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    private String category;
    private String mainImage;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 推荐相关字段
    private Integer viewCount;
    private Integer favoriteCount;
    private String recommendReason;
    private Double relevanceScore;
} 