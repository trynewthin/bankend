package com.zhixuanche.behavior.entity;

import lombok.Data;
import java.util.Date;

/**
 * 收藏实体类
 */
@Data
public class Favorite {
    /**
     * 收藏ID
     */
    private Integer favoriteId;
    
    /**
     * 关联用户ID
     */
    private Integer userId;
    
    /**
     * 关联车辆ID
     */
    private Integer carId;
    
    /**
     * 收藏时间
     */
    private Date createTime;
} 