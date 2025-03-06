package com.zhixuanche.behavior.config;

import com.zhixuanche.behavior.constant.BehaviorType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 行为类型枚举转换器
 * 负责Java枚举BehaviorType和数据库ENUM('浏览', '搜索', '咨询')之间的转换
 */
@MappedTypes(BehaviorType.class)
public class BehaviorTypeHandler extends BaseTypeHandler<BehaviorType> {
    
    private static final Logger log = LoggerFactory.getLogger(BehaviorTypeHandler.class);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BehaviorType parameter, JdbcType jdbcType) throws SQLException {
        // 存入数据库时，使用描述(中文)，而不是代码(英文)
        log.debug("设置行为类型参数: index={}, type={}, desc={}", i, parameter, parameter.getDesc());
        ps.setString(i, parameter.getDesc());
    }

    @Override
    public BehaviorType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        }
        log.debug("获取行为类型结果: columnName={}, value={}", columnName, value);
        return getBehaviorTypeByDesc(value);
    }

    @Override
    public BehaviorType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        log.debug("获取行为类型结果: columnIndex={}, value={}", columnIndex, value);
        return getBehaviorTypeByDesc(value);
    }

    @Override
    public BehaviorType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        log.debug("获取行为类型结果: columnIndex={}, value={}", columnIndex, value);
        return getBehaviorTypeByDesc(value);
    }
    
    /**
     * 根据中文描述获取对应的行为类型枚举
     */
    private BehaviorType getBehaviorTypeByDesc(String desc) {
        if (desc == null) {
            return null;
        }
        
        for (BehaviorType type : BehaviorType.values()) {
            if (type.getDesc().equals(desc)) {
                return type;
            }
        }
        log.warn("未找到匹配的行为类型: desc={}", desc);
        return null;
    }
} 