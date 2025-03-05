package com.zhixuanche.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
<<<<<<< HEAD
import com.zhixuanche.car.exception.CarException;
import com.zhixuanche.common.response.ApiResponse;
import com.zhixuanche.common.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.stream.Collectors;
=======
import com.zhixuanche.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
>>>>>>> 50c7cad (全局异常管理，mybais，satoken添加)

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
<<<<<<< HEAD

    /**
     * 处理车辆模块业务异常
     */
    @ExceptionHandler(CarException.class)
    public Result handleCarException(CarException e) {
        logger.warn("车辆模块异常：{}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        
        String message = String.join("; ", errors);
        logger.warn("参数校验失败：{}", message);
        return Result.error(400, message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        
        String message = String.join("; ", errors);
        logger.warn("参数绑定失败：{}", message);
        return Result.error(400, message);
    }

    /**
     * 处理请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingParamException(MissingServletRequestParameterException e) {
        logger.warn("请求参数缺失：{}", e.getMessage());
        return Result.error(400, "缺少必要的请求参数：" + e.getParameterName());
    }

    /**
     * 处理请求体解析异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.warn("请求体解析失败：{}", e.getMessage());
        return Result.error(400, "请求体格式错误");
    }

    /**
     * 处理文件上传超出大小限制异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        logger.warn("文件上传超出大小限制：{}", e.getMessage());
        return Result.error(400, "上传文件过大，请压缩后重试");
    }
=======
>>>>>>> 50c7cad (全局异常管理，mybais，satoken添加)
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        logger.error("系统异常", e);
        return ApiResponse.error(500, "系统异常，请稍后重试");
    }
} 