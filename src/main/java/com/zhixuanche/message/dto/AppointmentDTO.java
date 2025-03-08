package com.zhixuanche.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预约数据传输对象
 */
@Schema(description = "预约数据传输对象", name = "AppointmentDTO")
@Data
public class AppointmentDTO {
    
    /**
     * 预约ID
     */
    @Schema(description = "预约ID", example = "1")
    private Integer id;
    
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "14")
    private Integer userId;
    
    /**
     * 用户名称
     */
    @Schema(description = "用户名称", example = "李四")
    private String userName;
    
    /**
     * 用户电话
     */
    @Schema(description = "用户电话", example = "13812345678")
    private String userPhone;
    
    /**
     * 车辆ID
     */
    @Schema(description = "车辆ID", example = "5")
    private Integer carId;
    
    /**
     * 车辆信息（品牌+型号）
     */
    @Schema(description = "车辆信息", example = "奔驰 E级")
    private String carInfo;
    
    /**
     * 经销商ID
     */
    @Schema(description = "经销商ID", example = "3")
    private Integer dealerId;
    
    /**
     * 经销商名称
     */
    @Schema(description = "经销商名称", example = "北京星驰汽车销售有限公司")
    private String dealerName;
    
    /**
     * 预约类型
     */
    @Schema(description = "预约类型：看车、试驾", example = "看车")
    private String appointmentType;
    
    /**
     * 预约时间
     */
    @Schema(description = "预约时间", example = "2025-03-15T10:00:00")
    private LocalDateTime appointmentTime;
    
    /**
     * 预约状态
     */
    @Schema(description = "预约状态：待确认、已确认、已完成、已取消", example = "待确认")
    private String status;
    
    /**
     * 备注
     */
    @Schema(description = "备注信息", example = "希望有销售顾问专门介绍")
    private String remarks;
    
    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2025-03-08T21:33:53")
    private LocalDateTime createTime;
} 