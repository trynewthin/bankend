package com.zhixuanche.car.exception;

/**
 * 车辆模块业务异常
 */
public class CarException extends RuntimeException {
    
    private final int code;
    
    public CarException(String message) {
        super(message);
        this.code = 400;
    }
    
    public CarException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
} 