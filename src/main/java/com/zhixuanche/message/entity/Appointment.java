package com.zhixuanche.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预约实体类
 * 对应数据库 Appointments 表
 */
@Data
@TableName("Appointments")
public class Appointment {
    
    /**
     * 预约ID
     */
    @TableId(value = "appointment_id", type = IdType.AUTO)
    private Integer appointmentId;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;
    
    /**
     * 车辆ID
     */
    @TableField("car_id")
    private Integer carId;
    
    /**
     * 经销商ID
     */
    @TableField("dealer_id")
    private Integer dealerId;
    
    /**
     * 预约类型：看车/试驾
     */
    @TableField("appointment_type")
    private String appointmentType;
    
    /**
     * 预约时间
     */
    @TableField("appointment_time")
    private LocalDateTime appointmentTime;
    
    /**
     * 预约状态：待确认/已确认/已完成/已取消
     */
    @TableField("status")
    private String status;
    
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
} 