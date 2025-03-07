package com.zhixuanche.user.exception;

import com.zhixuanche.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 用户模块异常处理器
 */
@RestControllerAdvice("com.zhixuanche.user")  // 只处理用户模块的异常
public class UserExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(UserExceptionHandler.class);

    /**
     * 处理用户模块业务异常
     */
    @ExceptionHandler(UserException.class)
    public ApiResponse<Void> handleUserException(UserException e) {
        logger.warn("用户模块异常：{}", e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理经销商相关异常
     */
    @ExceptionHandler(DealerException.class)
    public ApiResponse<Void> handleDealerException(DealerException e) {
        logger.warn("经销商模块异常：{}", e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage());
    }
} 