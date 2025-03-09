package com.zhixuanche.admin.mapper;

import com.zhixuanche.admin.entity.AuditLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 审核日志Mapper接口
 */
@Mapper
public interface AuditLogMapper {
    
    /**
     * 插入审核日志
     * @param auditLog 审核日志实体
     * @return 影响行数
     */
    @Insert("INSERT INTO AuditLogs (operation_type, target_type, target_id, operator_id, content, result, operation_time, extra_info) " +
            "VALUES (#{operationType}, #{targetType}, #{targetId}, #{operatorId}, #{content}, #{result}, #{operationTime}, #{extraInfo})")
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    int insert(AuditLog auditLog);
    
    /**
     * 查询目标相关的审核日志
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 审核日志列表
     */
    @Select("SELECT * FROM AuditLogs WHERE target_type = #{targetType} AND target_id = #{targetId} ORDER BY operation_time DESC")
    List<AuditLog> findByTarget(@Param("targetType") String targetType, @Param("targetId") Integer targetId);
    
    /**
     * 查询操作者的审核日志
     * @param operatorId 操作者ID
     * @param limit 限制数量
     * @return 审核日志列表
     */
    @Select("SELECT * FROM AuditLogs WHERE operator_id = #{operatorId} ORDER BY operation_time DESC LIMIT #{limit}")
    List<AuditLog> findByOperator(@Param("operatorId") Integer operatorId, @Param("limit") Integer limit);
} 