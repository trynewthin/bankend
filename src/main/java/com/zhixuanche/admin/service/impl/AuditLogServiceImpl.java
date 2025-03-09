package com.zhixuanche.admin.service.impl;

import com.zhixuanche.admin.entity.AuditLog;
import com.zhixuanche.admin.mapper.AuditLogMapper;
import com.zhixuanche.admin.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 审核日志服务实现类
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogMapper auditLogMapper;
    
    @Override
    public boolean logCarAudit(Integer carId, Integer operatorId, Integer result, String content) {
        AuditLog auditLog = AuditLog.builder()
                .operationType("CAR_AUDIT")
                .targetType("CAR")
                .targetId(carId)
                .operatorId(operatorId)
                .content(content)
                .result(result)
                .operationTime(LocalDateTime.now())
                .build();
        
        return auditLogMapper.insert(auditLog) > 0;
    }
    
    @Override
    public boolean logDealerAudit(Integer dealerId, Integer operatorId, Integer result, String content) {
        AuditLog auditLog = AuditLog.builder()
                .operationType("DEALER_AUDIT")
                .targetType("DEALER")
                .targetId(dealerId)
                .operatorId(operatorId)
                .content(content)
                .result(result)
                .operationTime(LocalDateTime.now())
                .build();
        
        return auditLogMapper.insert(auditLog) > 0;
    }
    
    @Override
    public List<AuditLog> getCarAuditLogs(Integer carId) {
        return auditLogMapper.findByTarget("CAR", carId);
    }
    
    @Override
    public List<AuditLog> getDealerAuditLogs(Integer dealerId) {
        return auditLogMapper.findByTarget("DEALER", dealerId);
    }
} 