package com.zhixuanche.recommendation.exception;

import com.zhixuanche.common.exception.ErrorCode;

/**
 * 推荐模块异常
 */
public class RecommendationException extends RuntimeException {
    
    private final int code;
    
    public RecommendationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
    
    public RecommendationException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
} 