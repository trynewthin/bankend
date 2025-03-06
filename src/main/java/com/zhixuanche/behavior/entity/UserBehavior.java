package com.zhixuanche.behavior.entity;

import com.zhixuanche.behavior.constant.BehaviorType;
import lombok.Data;
import java.util.Date;

/**
 * 用户行为记录实体类
 */
@Data
public class UserBehavior {
    /**
     * 行为ID
     */
    private Integer behaviorId;
    
    /**
     * 关联用户ID
     */
    private Integer userId;
    
    /**
     * 关联车辆ID
     */
    private Integer carId;
    
    /**
     * 行为类型：浏览、搜索、咨询
     */
    private BehaviorType behaviorType;
    
    /**
     * 行为时间
     */
    private Date behaviorTime;
    
    /**
     * 浏览时长(秒)
     */
    private Integer duration;
    
    /**
     * 搜索关键词
     */
    private String searchKeywords;
    
    /**
     * 获取行为类型编码
     */
    public String getBehaviorTypeCode() {
        return behaviorType != null ? behaviorType.getCode() : null;
    }
    
    /**
     * 设置行为类型编码
     */
    public void setBehaviorTypeCode(String code) {
        this.behaviorType = BehaviorType.getByCode(code);
    }
    
    /**
     * 获取行为类型中文描述（用于数据库存储）
     */
    public String getBehaviorTypeDesc() {
        return behaviorType != null ? behaviorType.getDesc() : null;
    }
} 