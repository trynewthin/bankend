package com.zhixuanche.recommendation.constant;

/**
 * 推荐类型枚举
 */
public enum RecommendationType {
    BEHAVIOR("behavior", "基于行为"),
    PREFERENCE("preference", "基于偏好"),
    HOT("hot", "热门推荐"),
    NEW("new", "新车推荐");
    
    private final String code;
    private final String desc;
    
    RecommendationType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
} 