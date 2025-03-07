package com.zhixuanche.user.exception;

import com.zhixuanche.common.exception.ErrorCode;

/**
 * 经销商相关业务异常
 */
public class DealerException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public DealerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public DealerException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public int getCode() {
        return errorCode.getCode();
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
} 