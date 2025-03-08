package com.zhixuanche.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.message.dto.AppointmentDTO;
import com.zhixuanche.message.entity.Appointment;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约服务接口
 */
public interface AppointmentService extends IService<Appointment> {
    
    /**
     * 创建预约
     * @param userId 用户ID
     * @param carId 车辆ID
     * @param dealerId 经销商ID
     * @param appointmentType 预约类型（看车/试驾）
     * @param appointmentTime 预约时间
     * @param remarks 备注
     * @return 预约ID
     */
    Integer createAppointment(Integer userId, Integer carId, Integer dealerId, 
                            String appointmentType, LocalDateTime appointmentTime, String remarks);
    
    /**
     * 获取用户的预约列表
     * @param userId 用户ID
     * @param status 预约状态（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页预约列表
     */
    PageResult<AppointmentDTO> getUserAppointments(Integer userId, String status, 
                                                Integer pageNum, Integer pageSize);
    
    /**
     * 获取经销商的预约列表
     * @param dealerId 经销商ID
     * @param status 预约状态（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页预约列表
     */
    PageResult<AppointmentDTO> getDealerAppointments(Integer dealerId, String status, 
                                                  Integer pageNum, Integer pageSize);
    
    /**
     * 更新预约状态
     * @param appointmentId 预约ID
     * @param status 新状态
     * @param operatorId 操作人ID
     * @param operatorType 操作人类型（用户/经销商）
     * @return 是否成功
     */
    boolean updateStatus(Integer appointmentId, String status, Integer operatorId, String operatorType);
    
    /**
     * 取消预约
     * @param appointmentId 预约ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean cancelAppointment(Integer appointmentId, Integer userId);
    
    /**
     * 获取用户即将到来的预约
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页预约列表
     */
    PageResult<AppointmentDTO> getUpcomingAppointments(Integer userId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取用户指定车辆的预约
     * @param userId 用户ID
     * @param carId 车辆ID
     * @return 预约列表
     */
    List<AppointmentDTO> getUserCarAppointments(Integer userId, Integer carId);
    
    /**
     * 判断预约时间是否可用
     * @param dealerId 经销商ID
     * @param appointmentTime 预约时间
     * @return 是否可用
     */
    boolean isAppointmentTimeAvailable(Integer dealerId, LocalDateTime appointmentTime);
    
    /**
     * 统计经销商待确认的预约数量
     * @param dealerId 经销商ID
     * @return 待确认预约数量
     */
    int countPendingAppointments(Integer dealerId);
} 