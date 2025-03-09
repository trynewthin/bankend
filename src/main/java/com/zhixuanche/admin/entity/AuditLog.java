package com.zhixuanche.admin.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审核日志实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    
    /**
     * 日志ID
     */
    private Integer logId;
    
    /**
     * 操作类型：CAR_AUDIT-车辆审核，DEALER_AUDIT-经销商审核
     */
    private String operationType;
    
    /**
     * 目标类型：CAR-车辆，DEALER-经销商
     */
    private String targetType;
    
    /**
     * 目标ID
     */
    private Integer targetId;
    
    /**
     * 操作者ID（管理员ID）
     */
    private Integer operatorId;
    
    /**
     * 操作内容
     */
    private String content;
    
    /**
     * 操作结果：1-通过，0-拒绝
     */
    private Integer result;
    
    /**
     * 操作时间
     */
    private LocalDateTime operationTime;
    
    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;
} 