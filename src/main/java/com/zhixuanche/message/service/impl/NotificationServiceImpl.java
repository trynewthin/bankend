package com.zhixuanche.message.service.impl;

import com.zhixuanche.common.exception.BusinessException;
import com.zhixuanche.common.exception.ErrorCode;
import com.zhixuanche.message.constant.MessageConstant;
import com.zhixuanche.message.entity.Appointment;
import com.zhixuanche.message.entity.Message;
import com.zhixuanche.message.service.MessageService;
import com.zhixuanche.message.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通知服务实现类
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private MessageService messageService;
    
    // 系统用户ID
    private static final Integer SYSTEM_USER_ID = 1;      // 系统通知账号
    private static final Integer MARKETING_USER_ID = 2;   // 营销账号
    private static final Integer CUSTOMER_SERVICE_ID = 3; // 客服账号
    private static final Integer AI_ASSISTANT_ID = 4;     // AI助手账号
    
    @Override
    public boolean sendAppointmentStatusChangeNotification(Appointment appointment, 
                                                        String oldStatus, 
                                                        String newStatus,
                                                        Integer operatorId) {
        if (appointment == null || oldStatus == null || newStatus == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "发送预约状态变更通知参数不完整");
        }
        
        try {
            String title = "预约状态已更新";
            String content = generateStatusChangeContent(appointment, oldStatus, newStatus);
            Integer receiverId = null;
            
            // 发送给谁取决于谁操作的
            if (operatorId.equals(appointment.getUserId())) {
                // 用户操作，通知经销商
                receiverId = appointment.getDealerId();
            } else {
                // 经销商操作，通知用户
                receiverId = appointment.getUserId();
            }
            
            // 发送通知
            messageService.sendSystemMessage(
                    CUSTOMER_SERVICE_ID,
                    Collections.singletonList(receiverId),
                    title,
                    content,
                    MessageConstant.MessageType.NOTICE,
                    3, // 普通优先级
                    null,
                    null
            );
            
            return true;
        } catch (Exception e) {
            log.error("发送预约状态变更通知失败", e);
            return false;
        }
    }
    
    @Override
    public boolean sendAppointmentCreationNotificationToDealer(Appointment appointment) {
        if (appointment == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "发送预约创建通知参数不完整");
        }
        
        try {
            String title = "新预约通知";
            String content = generateCreationNoticeContent(appointment);
            
            // 发送通知给经销商
            messageService.sendSystemMessage(
                    CUSTOMER_SERVICE_ID,
                    Collections.singletonList(appointment.getDealerId()),
                    title,
                    content,
                    MessageConstant.MessageType.NOTICE,
                    4, // 较高优先级
                    null,
                    null
            );
            
            return true;
        } catch (Exception e) {
            log.error("发送预约创建通知失败", e);
            return false;
        }
    }
    
    @Override
    public boolean sendAppointmentConfirmationNotificationToUser(Appointment appointment) {
        if (appointment == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "发送预约确认通知参数不完整");
        }
        
        try {
            String title = "预约已确认";
            String content = generateConfirmationNoticeContent(appointment);
            
            // 发送通知给用户
            messageService.sendSystemMessage(
                    CUSTOMER_SERVICE_ID,
                    Collections.singletonList(appointment.getUserId()),
                    title,
                    content,
                    MessageConstant.MessageType.NOTICE,
                    3, // 普通优先级
                    null,
                    null
            );
            
            return true;
        } catch (Exception e) {
            log.error("发送预约确认通知失败", e);
            return false;
        }
    }
    
    @Override
    public boolean sendAppointmentReminderNotification(Appointment appointment, int hours) {
        if (appointment == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "发送预约提醒通知参数不完整");
        }
        
        try {
            String title = "预约即将开始";
            String content = generateReminderNoticeContent(appointment, hours);
            
            // 发送通知给用户
            messageService.sendSystemMessage(
                    CUSTOMER_SERVICE_ID,
                    Collections.singletonList(appointment.getUserId()),
                    title,
                    content,
                    MessageConstant.MessageType.NOTICE,
                    4, // 较高优先级
                    null,
                    null
            );
            
            return true;
        } catch (Exception e) {
            log.error("发送预约提醒通知失败", e);
            return false;
        }
    }
    
    @Override
    public boolean sendSystemNotification(Integer userId, String title, String content, String noticeType) {
        if (userId == null || title == null || content == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "发送系统通知参数不完整");
        }
        
        try {
            // 发送通知给用户
            messageService.sendSystemMessage(
                    SYSTEM_USER_ID,
                    Collections.singletonList(userId),
                    title,
                    content,
                    noticeType != null ? noticeType : MessageConstant.MessageType.NOTICE,
                    3, // 普通优先级
                    null,
                    null
            );
            
            return true;
        } catch (Exception e) {
            log.error("发送系统通知失败", e);
            return false;
        }
    }
    
    @Override
    public int sendMarketingNotification(String title, String content, List<Integer> targetUserIds, 
                                     String actionType, String actionValue) {
        if (title == null || content == null || CollectionUtils.isEmpty(targetUserIds)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "发送营销通知参数不完整");
        }
        
        try {
            // 发送营销通知给目标用户
            return messageService.sendSystemMessage(
                    MARKETING_USER_ID,
                    targetUserIds,
                    title,
                    content,
                    MessageConstant.MessageType.MARKETING,
                    3, // 普通优先级
                    actionType,
                    actionValue
            );
        } catch (Exception e) {
            log.error("发送营销通知失败", e);
            return 0;
        }
    }
    
    /**
     * 生成预约状态变更通知内容
     */
    private String generateStatusChangeContent(Appointment appointment, String oldStatus, String newStatus) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        String formattedTime = appointment.getAppointmentTime().format(formatter);
        String appointmentType = appointment.getAppointmentType();
        
        StringBuilder content = new StringBuilder();
        content.append("您好，您的");
        content.append(formattedTime);
        content.append("的");
        content.append(appointmentType);
        content.append("预约状态已从\"");
        content.append(oldStatus);
        content.append("\"变更为\"");
        content.append(newStatus);
        content.append("\"。");
        
        if (newStatus.equals(MessageConstant.AppointmentStatus.CONFIRMED)) {
            content.append("经销商已确认您的预约，请准时到店。");
        } else if (newStatus.equals(MessageConstant.AppointmentStatus.CANCELLED)) {
            content.append("预约已取消。");
        } else if (newStatus.equals(MessageConstant.AppointmentStatus.COMPLETED)) {
            content.append("预约已完成，感谢您的到访。");
        }
        
        return content.toString();
    }
    
    /**
     * 生成预约创建通知内容
     */
    private String generateCreationNoticeContent(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        String formattedTime = appointment.getAppointmentTime().format(formatter);
        String appointmentType = appointment.getAppointmentType();
        
        StringBuilder content = new StringBuilder();
        content.append("您有一个新的预约申请。用户（ID:");
        content.append(appointment.getUserId());
        content.append("）预约了");
        content.append(formattedTime);
        content.append("的");
        content.append(appointmentType);
        content.append("服务，请及时处理。");
        
        if (appointment.getRemarks() != null && !appointment.getRemarks().isEmpty()) {
            content.append("用户备注：");
            content.append(appointment.getRemarks());
        }
        
        return content.toString();
    }
    
    /**
     * 生成预约确认通知内容
     */
    private String generateConfirmationNoticeContent(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        String formattedTime = appointment.getAppointmentTime().format(formatter);
        String appointmentType = appointment.getAppointmentType();
        
        StringBuilder content = new StringBuilder();
        content.append("您好，您的");
        content.append(formattedTime);
        content.append("的");
        content.append(appointmentType);
        content.append("预约已被经销商确认。请准时到店，如有变动请提前联系。");
        
        return content.toString();
    }
    
    /**
     * 生成预约提醒通知内容
     */
    private String generateReminderNoticeContent(Appointment appointment, int hours) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        String formattedTime = appointment.getAppointmentTime().format(formatter);
        String appointmentType = appointment.getAppointmentType();
        
        StringBuilder content = new StringBuilder();
        content.append("温馨提醒：您的");
        content.append(appointmentType);
        content.append("预约将在");
        content.append(hours);
        content.append("小时后（");
        content.append(formattedTime);
        content.append("）开始，请准时到店。");
        
        return content.toString();
    }
} 