package com.zhixuanche.user.exception;

import com.zhixuanche.common.exception.ErrorCode;

/**
 * 用户模块业务异常
 */
public class UserException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public UserException(ErrorCode errorCode, String message) {
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