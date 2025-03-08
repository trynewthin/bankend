package com.zhixuanche.message.exception;

import com.zhixuanche.common.exception.BusinessException;

/**
 * 消息模块异常类
 */
public class MessageException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 消息不存在
     */
    public static final Integer ERROR_MESSAGE_NOT_FOUND = 10001;
    
    /**
     * 没有权限操作该消息
     */
    public static final Integer ERROR_NO_PERMISSION = 10002;
    
    /**
     * 消息发送失败
     */
    public static final Integer ERROR_SEND_FAILED = 10003;
    
    /**
     * 预约不存在
     */
    public static final Integer ERROR_APPOINTMENT_NOT_FOUND = 10101;
    
    /**
     * 预约状态不允许修改
     */
    public static final Integer ERROR_APPOINTMENT_STATUS = 10102;
    
    /**
     * 预约时间无效
     */
    public static final Integer ERROR_APPOINTMENT_TIME = 10103;
    
    /**
     * 构造方法
     * @param code 错误码
     * @param message 错误信息
     */
    public MessageException(Integer code, String message) {
        super(code, message);
    }
    
    /**
     * 消息不存在异常
     * @param messageId 消息ID
     * @return MessageException
     */
    public static MessageException messageNotFound(Integer messageId) {
        return new MessageException(ERROR_MESSAGE_NOT_FOUND, "消息不存在: " + messageId);
    }
    
    /**
     * 无权限操作消息异常
     * @param userId 用户ID
     * @param messageId 消息ID
     * @return MessageException
     */
    public static MessageException noPermission(Integer userId, Integer messageId) {
        return new MessageException(ERROR_NO_PERMISSION, "用户[" + userId + "]无权操作消息[" + messageId + "]");
    }
    
    /**
     * 消息发送失败异常
     * @param message 错误信息
     * @return MessageException
     */
    public static MessageException sendFailed(String message) {
        return new MessageException(ERROR_SEND_FAILED, "消息发送失败: " + message);
    }
    
    /**
     * 预约不存在异常
     * @param appointmentId 预约ID
     * @return MessageException
     */
    public static MessageException appointmentNotFound(Integer appointmentId) {
        return new MessageException(ERROR_APPOINTMENT_NOT_FOUND, "预约不存在: " + appointmentId);
    }
    
    /**
     * 预约状态不允许修改异常
     * @param currentStatus 当前状态
     * @param targetStatus 目标状态
     * @return MessageException
     */
    public static MessageException appointmentStatusError(String currentStatus, String targetStatus) {
        return new MessageException(ERROR_APPOINTMENT_STATUS, 
                "预约状态不允许从[" + currentStatus + "]变更为[" + targetStatus + "]");
    }
    
    /**
     * 预约时间无效异常
     * @param message 错误信息
     * @return MessageException
     */
    public static MessageException appointmentTimeError(String message) {
        return new MessageException(ERROR_APPOINTMENT_TIME, "预约时间无效: " + message);
    }
} 