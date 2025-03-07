package com.zhixuanche.car.exception;

import com.zhixuanche.common.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 车辆模块异常处理器
 */
@RestControllerAdvice("com.zhixuanche.car")  // 只处理车辆模块的异常
public class CarExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(CarExceptionHandler.class);

    /**
     * 处理车辆模块业务异常
     */
    @ExceptionHandler(CarException.class)
    public Result handleCarException(CarException e) {
        logger.warn("车辆模块异常：{}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }
} 