package com.zhixuanche.common.exception;

/**
 * 系统错误码枚举
 * 错误码设计：
 * - 1xxx: 用户模块错误
 * - 2xxx: 车辆模块错误
 * - 3xxx: 行为记录模块错误
 * - 4xxx: 智能推荐模块错误
 * - 5xxx: 消息模块错误
 * - 6xxx: 系统管理模块错误
 * - 7xxx: 系统错误
 */
public enum ErrorCode {
    
    // 通用错误码 (5xxx)
    SYSTEM_ERROR(5000, "系统错误"),
    PARAM_ERROR(5001, "参数错误"),
    UNAUTHORIZED(5002, "未授权"),
    FORBIDDEN(5003, "无权限访问"),
    
    // 用户模块错误码 (1xxx)
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    ACCOUNT_LOCKED(1004, "账号已被锁定"),
    INVALID_VERIFICATION_CODE(1005, "验证码无效"),
    
    // 经销商相关错误码 (11xx)
    DEALER_NOT_FOUND(1101, "经销商不存在"),
    DEALER_ALREADY_EXISTS(1102, "经销商已存在"),
    DEALER_AUDIT_REJECTED(1103, "经销商审核未通过"),
    DEALER_NOT_VERIFIED(1104, "经销商未认证"),
    
    // 车辆模块错误码 (2xxx)
    CAR_NOT_FOUND(2001, "车辆不存在"),
    CAR_ALREADY_EXISTS(2002, "车辆已存在"),
    CAR_AUDIT_REJECTED(2003, "车辆审核未通过"),
    CAR_ALREADY_SOLD(2004, "车辆已售出"),
    CAR_OPERATION_FORBIDDEN(2005, "无权操作该车辆"),
    
    // 推荐模块错误码 (4xxx)
    RECOMMENDATION_ERROR(4000, "推荐系统异常"),
    GET_HOME_RECOMMENDATIONS_ERROR(4001, "获取首页推荐失败"),
    GET_BEHAVIOR_RECOMMENDATIONS_ERROR(4002, "获取行为推荐失败"),
    GET_PREFERENCE_RECOMMENDATIONS_ERROR(4003, "获取偏好推荐失败"),
    GET_HOT_RECOMMENDATIONS_ERROR(4004, "获取热门推荐失败"),
    GET_NEW_RECOMMENDATIONS_ERROR(4005, "获取新车推荐失败"),
    INVALID_RECOMMENDATION_PARAMS(4006, "无效的推荐参数"),
    
    // 消息模块错误码 (5xxx)
    MESSAGE_ERROR(5100, "消息系统异常"),
    MESSAGE_NOT_FOUND(5101, "消息不存在"),
    MESSAGE_PERMISSION_DENIED(5102, "无权限操作该消息"),
    MESSAGE_SEND_FAILED(5103, "消息发送失败"),
    
    // 预约相关错误码 (51xx)
    APPOINTMENT_ERROR(5110, "预约系统异常"),
    APPOINTMENT_NOT_FOUND(5111, "预约不存在"),
    APPOINTMENT_STATUS_ERROR(5112, "预约状态不允许修改"),
    APPOINTMENT_TIME_INVALID(5113, "预约时间无效"),
    APPOINTMENT_CONFLICT(5114, "预约时间冲突"),
    APPOINTMENT_PERMISSION_DENIED(5115, "无权限操作该预约");
    
    private final int code;
    private final String message;
    
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
} 