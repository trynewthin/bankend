package com.zhixuanche.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.zhixuanche.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public ApiResponse<Void> handleNotLoginException(NotLoginException e) {
        logger.debug("未登录：{}", e.getMessage());
        return ApiResponse.error(401, "请先登录");
    }
    
    /**
     * 处理无权限异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public ApiResponse<Void> handleNotPermissionException(NotPermissionException e) {
        logger.debug("无权限：{}", e.getMessage());
        return ApiResponse.error(403, "无权限访问");
    }
    
    /**
     * 处理角色异常
     */
    @ExceptionHandler(NotRoleException.class)
    public ApiResponse<Void> handleNotRoleException(NotRoleException e) {
        logger.debug("角色不匹配：{}", e.getMessage());
        return ApiResponse.error(403, "无权限访问");
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        logger.error("系统异常", e);
        return ApiResponse.error(500, "系统异常，请稍后重试");
    }
} 