package com.zhixuanche.behavior.constant;

/**
 * 用户行为类型枚举
 */
public enum BehaviorType {
    BROWSE("browse", "浏览"),
    SEARCH("search", "搜索"),
    CONSULT("consult", "咨询");
    
    private String code;
    private String desc;
    
    BehaviorType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    /**
     * 根据code获取枚举值
     */
    public static BehaviorType getByCode(String code) {
        for (BehaviorType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
} 