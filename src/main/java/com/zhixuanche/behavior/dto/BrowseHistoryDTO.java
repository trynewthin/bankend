package com.zhixuanche.behavior.dto;

import lombok.Data;
import java.util.Date;

/**
 * 浏览历史数据传输对象
 */
@Data
public class BrowseHistoryDTO {
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
     * 浏览时间
     */
    private Date browseTime;
    
    /**
     * 浏览时长(秒)
     */
    private Integer duration;
    
    /**
     * 缩略图URL
     */
    private String thumbnailUrl;
} 