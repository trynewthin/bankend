package com.zhixuanche.user.config;

import com.zhixuanche.user.entity.enums.DealerStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DealerStatus枚举类型处理器
 */
@MappedTypes(DealerStatus.class)
public class DealerStatusTypeHandler extends BaseTypeHandler<DealerStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DealerStatus parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public DealerStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName);
        return rs.wasNull() ? null : getEnumByCode(code);
    }

    @Override
    public DealerStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return rs.wasNull() ? null : getEnumByCode(code);
    }

    @Override
    public DealerStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);
        return cs.wasNull() ? null : getEnumByCode(code);
    }

    private DealerStatus getEnumByCode(int code) {
        for (DealerStatus status : DealerStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的经销商状态代码: " + code);
    }
} 