package com.zhixuanche.message.service;

import com.zhixuanche.message.entity.Appointment;

/**
 * 通知服务接口
 * 负责处理各类系统通知的发送
 */
public interface NotificationService {
    
    /**
     * 发送预约状态变更通知
     * @param appointment 预约对象
     * @param oldStatus 旧状态
     * @param newStatus 新状态
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean sendAppointmentStatusChangeNotification(Appointment appointment, 
                                                  String oldStatus, 
                                                  String newStatus,
                                                  Integer operatorId);
    
    /**
     * 发送预约创建通知给经销商
     * @param appointment 预约对象
     * @return 是否成功
     */
    boolean sendAppointmentCreationNotificationToDealer(Appointment appointment);
    
    /**
     * 发送预约确认通知给用户
     * @param appointment 预约对象
     * @return 是否成功
     */
    boolean sendAppointmentConfirmationNotificationToUser(Appointment appointment);
    
    /**
     * 发送预约提醒通知
     * @param appointment 预约对象
     * @param hours 提前小时数
     * @return 是否成功
     */
    boolean sendAppointmentReminderNotification(Appointment appointment, int hours);
    
    /**
     * 发送系统通知给用户
     * @param userId 用户ID
     * @param title 通知标题
     * @param content 通知内容
     * @param noticeType 通知类型
     * @return 是否成功
     */
    boolean sendSystemNotification(Integer userId, String title, String content, String noticeType);
    
    /**
     * 发送营销通知给符合条件的用户
     * @param title 通知标题
     * @param content 通知内容
     * @param targetUserIds 目标用户ID列表
     * @param actionType 动作类型
     * @param actionValue 动作值
     * @return 发送成功的数量
     */
    int sendMarketingNotification(String title, String content, 
                               java.util.List<Integer> targetUserIds, 
                               String actionType, String actionValue);
} 