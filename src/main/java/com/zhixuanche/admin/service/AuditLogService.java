package com.zhixuanche.admin.service;

import com.zhixuanche.admin.entity.AuditLog;

import java.util.List;

/**
 * 审核日志服务接口
 */
public interface AuditLogService {
    
    /**
     * 记录车辆审核日志
     * @param carId 车辆ID
     * @param operatorId 操作者ID
     * @param result 审核结果
     * @param content 审核内容
     * @return 是否成功
     */
    boolean logCarAudit(Integer carId, Integer operatorId, Integer result, String content);
    
    /**
     * 记录经销商审核日志
     * @param dealerId 经销商ID
     * @param operatorId 操作者ID
     * @param result 审核结果
     * @param content 审核内容
     * @return 是否成功
     */
    boolean logDealerAudit(Integer dealerId, Integer operatorId, Integer result, String content);
    
    /**
     * 获取车辆的审核日志
     * @param carId 车辆ID
     * @return 审核日志列表
     */
    List<AuditLog> getCarAuditLogs(Integer carId);
    
    /**
     * 获取经销商的审核日志
     * @param dealerId 经销商ID
     * @return 审核日志列表
     */
    List<AuditLog> getDealerAuditLogs(Integer dealerId);
} 