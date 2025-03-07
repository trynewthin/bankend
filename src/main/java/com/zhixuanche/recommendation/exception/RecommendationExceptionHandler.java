package com.zhixuanche.recommendation.exception;

import com.zhixuanche.common.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 推荐模块异常处理器
 */
@RestControllerAdvice(basePackages = "com.zhixuanche.recommendation")
public class RecommendationExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationExceptionHandler.class);
    
    /**
     * 处理推荐相关异常
     */
    @ExceptionHandler(RecommendationException.class)
    public Result handleRecommendationException(RecommendationException e) {
        logger.error("推荐异常", e);
        return Result.error(e.getCode(), e.getMessage());
    }
} 