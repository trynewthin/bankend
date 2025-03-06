package com.zhixuanche.behavior.dto;

import lombok.Data;
import java.util.Date;

/**
 * 收藏数据传输对象
 */
@Data
public class FavoriteDTO {
    /**
     * 车辆ID
     */
    private Integer carId;
    
    /**
     * 品牌
     */
    private String brand;
    
    /**
     * 型号
     */
    private String model;
    
    /**
     * 价格
     */
    private Double price;
    
    /**
     * 收藏时间
     */
    private Date createTime;
    
    /**
     * 缩略图URL
     */
    private String thumbnailUrl;
} 