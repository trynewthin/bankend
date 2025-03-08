package com.zhixuanche.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhixuanche.common.exception.BusinessException;
import com.zhixuanche.common.exception.ErrorCode;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.message.constant.MessageConstant;
import com.zhixuanche.message.dto.AppointmentDTO;
import com.zhixuanche.message.entity.Appointment;
import com.zhixuanche.message.mapper.AppointmentMapper;
import com.zhixuanche.message.service.AppointmentService;
import com.zhixuanche.message.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预约服务实现类
 */
@Service
@Slf4j
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {
    
    @Autowired
    private AppointmentMapper appointmentMapper;
    
    @Autowired
    private NotificationService notificationService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createAppointment(Integer userId, Integer carId, Integer dealerId, 
                                  String appointmentType, LocalDateTime appointmentTime, String remarks) {
        // 参数校验
        if (userId == null || carId == null || dealerId == null || 
            appointmentTime == null || !StringUtils.hasText(appointmentType)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "创建预约参数不完整");
        }
        
        // 检查预约类型是否合法
        if (!appointmentType.equals(MessageConstant.AppointmentType.VIEW) && 
            !appointmentType.equals(MessageConstant.AppointmentType.TEST_DRIVE)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "无效的预约类型");
        }
        
        // 检查预约时间是否合法（不能是过去的时间）
        if (appointmentTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.APPOINTMENT_TIME_INVALID.getCode(), "预约时间不能是过去的时间");
        }
        
        // 检查预约时间是否可用
        if (!isAppointmentTimeAvailable(dealerId, appointmentTime)) {
            throw new BusinessException(ErrorCode.APPOINTMENT_CONFLICT.getCode(), "该时间段已被预约，请选择其他时间");
        }
        
        // 创建预约对象
        Appointment appointment = new Appointment();
        appointment.setUserId(userId);
        appointment.setCarId(carId);
        appointment.setDealerId(dealerId);
        appointment.setAppointmentType(appointmentType);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setStatus(MessageConstant.AppointmentStatus.PENDING);
        appointment.setRemarks(remarks);
        appointment.setCreateTime(LocalDateTime.now());
        
        // 保存预约
        try {
            save(appointment);
            
            // 发送预约创建通知给经销商
            notificationService.sendAppointmentCreationNotificationToDealer(appointment);
            
            return appointment.getAppointmentId();
        } catch (Exception e) {
            log.error("创建预约失败", e);
            throw new BusinessException(ErrorCode.APPOINTMENT_ERROR.getCode(), "创建预约失败: " + e.getMessage());
        }
    }
    
    @Override
    public PageResult<AppointmentDTO> getUserAppointments(Integer userId, String status, 
                                                      Integer pageNum, Integer pageSize) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "查询参数不完整");
        }
        
        // 构建分页对象
        Page<Appointment> page = new Page<Appointment>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
        
        // 执行查询
        IPage<Appointment> appointmentPage = appointmentMapper.getUserAppointments(page, userId, status);
        
        // 转换为DTO
        List<AppointmentDTO> dtoList = convertToDTO(appointmentPage.getRecords());
        
        // 构建分页结果
        return new PageResult<AppointmentDTO>(
                appointmentPage.getTotal(),
                pageNum != null ? pageNum : 1,
                pageSize != null ? pageSize : 10,
                dtoList
        );
    }
    
    @Override
    public PageResult<AppointmentDTO> getDealerAppointments(Integer dealerId, String status, 
                                                        Integer pageNum, Integer pageSize) {
        if (dealerId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "查询参数不完整");
        }
        
        // 构建分页对象
        Page<Appointment> page = new Page<Appointment>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
        
        // 执行查询
        IPage<Appointment> appointmentPage = appointmentMapper.getDealerAppointments(page, dealerId, status);
        
        // 转换为DTO
        List<AppointmentDTO> dtoList = convertToDTO(appointmentPage.getRecords());
        
        // 构建分页结果
        return new PageResult<AppointmentDTO>(
                appointmentPage.getTotal(),
                pageNum != null ? pageNum : 1,
                pageSize != null ? pageSize : 10,
                dtoList
        );
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Integer appointmentId, String status, Integer operatorId, String operatorType) {
        if (appointmentId == null || !StringUtils.hasText(status) || operatorId == null || !StringUtils.hasText(operatorType)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "更新状态参数不完整");
        }
        
        // 检查预约是否存在
        Appointment appointment = getById(appointmentId);
        if (appointment == null) {
            throw new BusinessException(ErrorCode.APPOINTMENT_NOT_FOUND.getCode(), "预约不存在");
        }
        
        // 检查权限
        if (operatorType.equals("USER") && !appointment.getUserId().equals(operatorId)) {
            throw new BusinessException(ErrorCode.APPOINTMENT_PERMISSION_DENIED.getCode(), "无权修改该预约");
        } else if (operatorType.equals("DEALER") && !appointment.getDealerId().equals(operatorId)) {
            throw new BusinessException(ErrorCode.APPOINTMENT_PERMISSION_DENIED.getCode(), "无权修改该预约");
        }
        
        // 检查状态变更是否合法
        String oldStatus = appointment.getStatus();
        if (oldStatus.equals(MessageConstant.AppointmentStatus.COMPLETED) || 
            oldStatus.equals(MessageConstant.AppointmentStatus.CANCELLED)) {
            throw new BusinessException(ErrorCode.APPOINTMENT_STATUS_ERROR.getCode(), 
                    "已完成或已取消的预约不能再次修改状态");
        }
        
        // 更新状态
        try {
            boolean result = appointmentMapper.updateStatus(appointmentId, status) > 0;
            
            if (result) {
                // 发送状态变更通知
                notificationService.sendAppointmentStatusChangeNotification(appointment, oldStatus, status, operatorId);
                
                // 如果是确认预约，发送通知给用户
                if (status.equals(MessageConstant.AppointmentStatus.CONFIRMED) && 
                    operatorType.equals("DEALER")) {
                    notificationService.sendAppointmentConfirmationNotificationToUser(appointment);
                }
            }
            
            return result;
        } catch (Exception e) {
            log.error("更新预约状态失败", e);
            throw new BusinessException(ErrorCode.APPOINTMENT_ERROR.getCode(), "更新预约状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean cancelAppointment(Integer appointmentId, Integer userId) {
        return updateStatus(appointmentId, MessageConstant.AppointmentStatus.CANCELLED, userId, "USER");
    }
    
    @Override
    public PageResult<AppointmentDTO> getUpcomingAppointments(Integer userId, Integer pageNum, Integer pageSize) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "查询参数不完整");
        }
        
        // 构建分页对象
        Page<Appointment> page = new Page<Appointment>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
        
        // 执行查询
        IPage<Appointment> appointmentPage = appointmentMapper.getUpcomingAppointments(page, userId);
        
        // 转换为DTO
        List<AppointmentDTO> dtoList = convertToDTO(appointmentPage.getRecords());
        
        // 构建分页结果
        return new PageResult<AppointmentDTO>(
                appointmentPage.getTotal(),
                pageNum != null ? pageNum : 1,
                pageSize != null ? pageSize : 10,
                dtoList
        );
    }
    
    @Override
    public List<AppointmentDTO> getUserCarAppointments(Integer userId, Integer carId) {
        if (userId == null || carId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "查询参数不完整");
        }
        
        // 执行查询
        List<Appointment> appointments = appointmentMapper.getUserCarAppointments(userId, carId);
        
        // 转换为DTO
        return convertToDTO(appointments);
    }
    
    @Override
    public boolean isAppointmentTimeAvailable(Integer dealerId, LocalDateTime appointmentTime) {
        if (dealerId == null || appointmentTime == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "检查预约时间参数不完整");
        }
        
        // 获取当天该经销商的所有预约
        LocalDateTime startTime = appointmentTime.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = startTime.plusDays(1);
        
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Appointment::getDealerId, dealerId)
                   .between(Appointment::getAppointmentTime, startTime, endTime)
                   .in(Appointment::getStatus, MessageConstant.AppointmentStatus.PENDING, MessageConstant.AppointmentStatus.CONFIRMED);
        
        List<Appointment> existingAppointments = list(queryWrapper);
        
        // 检查是否有时间冲突（前后30分钟内已有预约）
        if (!CollectionUtils.isEmpty(existingAppointments)) {
            LocalDateTime requestStart = appointmentTime.minusMinutes(30);
            LocalDateTime requestEnd = appointmentTime.plusMinutes(30);
            
            for (Appointment existing : existingAppointments) {
                LocalDateTime existingTime = existing.getAppointmentTime();
                
                // 检查时间是否重叠
                if ((existingTime.isEqual(appointmentTime) || existingTime.isAfter(requestStart)) && 
                    existingTime.isBefore(requestEnd)) {
                    return false;
                }
                
                // 每个预约占用1小时（前后30分钟）
                LocalDateTime existingStart = existingTime.minusMinutes(30);
                LocalDateTime existingEnd = existingTime.plusMinutes(30);
                
                if ((appointmentTime.isEqual(existingStart) || appointmentTime.isAfter(existingStart)) && 
                    appointmentTime.isBefore(existingEnd)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public int countPendingAppointments(Integer dealerId) {
        if (dealerId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "查询参数不完整");
        }
        
        return appointmentMapper.countPendingAppointments(dealerId);
    }
    
    /**
     * 将Appointment实体列表转换为AppointmentDTO列表
     * @param appointments 预约实体列表
     * @return DTO列表
     */
    private List<AppointmentDTO> convertToDTO(List<Appointment> appointments) {
        if (CollectionUtils.isEmpty(appointments)) {
            return new ArrayList<>();
        }
        
        return appointments.stream().map(appointment -> {
            AppointmentDTO dto = new AppointmentDTO();
            
            // 复制基本属性
            BeanUtils.copyProperties(appointment, dto);
            
            // 特殊处理一些属性
            dto.setId(appointment.getAppointmentId());
            
            return dto;
        }).collect(Collectors.toList());
    }
} 