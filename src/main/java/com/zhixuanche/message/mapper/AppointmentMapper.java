package com.zhixuanche.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhixuanche.message.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 预约数据访问层
 */
@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
    
    /**
     * 获取用户的预约列表
     * @param page 分页参数
     * @param userId 用户ID
     * @param status 预约状态
     * @return 分页预约列表
     */
    IPage<Appointment> getUserAppointments(Page<Appointment> page,
                                         @Param("userId") Integer userId,
                                         @Param("status") String status);
    
    /**
     * 获取经销商的预约列表
     * @param page 分页参数
     * @param dealerId 经销商ID
     * @param status 预约状态
     * @return 分页预约列表
     */
    IPage<Appointment> getDealerAppointments(Page<Appointment> page,
                                           @Param("dealerId") Integer dealerId,
                                           @Param("status") String status);
    
    /**
     * 更新预约状态
     * @param appointmentId 预约ID
     * @param status 新状态
     * @return 更新行数
     */
    @Update("UPDATE Appointments SET status = #{status} WHERE appointment_id = #{appointmentId}")
    int updateStatus(@Param("appointmentId") Integer appointmentId, @Param("status") String status);
    
    /**
     * 获取待确认的预约数量（经销商）
     * @param dealerId 经销商ID
     * @return 待确认预约数量
     */
    int countPendingAppointments(@Param("dealerId") Integer dealerId);
    
    /**
     * 获取用户指定车辆的预约
     * @param userId 用户ID
     * @param carId 车辆ID
     * @return 预约列表
     */
    List<Appointment> getUserCarAppointments(@Param("userId") Integer userId, @Param("carId") Integer carId);
    
    /**
     * 获取即将到来的预约
     * @param page 分页参数
     * @param userId 用户ID
     * @return 分页预约列表
     */
    IPage<Appointment> getUpcomingAppointments(Page<Appointment> page, @Param("userId") Integer userId);
    
    /**
     * 统计预约总数
     */
    int countTotalAppointments();
    
    /**
     * 统计各状态预约数量
     */
    List<Map<String, Object>> countAppointmentsByStatus();
    
    /**
     * 统计新增预约数量
     */
    int countNewAppointments(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    /**
     * 获取预约趋势数据
     */
    List<Map<String, Object>> getAppointmentTrendData(@Param("startDate") String startDate, 
                                                    @Param("endDate") String endDate, 
                                                    @Param("groupBy") String groupBy);
    
    /**
     * 获取预约完成率统计
     */
    Map<String, Object> getAppointmentCompletionStatistics(@Param("startDate") String startDate, 
                                                         @Param("endDate") String endDate);
} 