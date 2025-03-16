package com.zhixuanche.user.mapper;

import com.zhixuanche.user.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 通过ID查询用户
     */
    @Select("SELECT * FROM Users WHERE user_id = #{userId}")
    User findById(Integer userId);
    
    /**
     * 通过邮箱查询用户
     */
    @Select("SELECT * FROM Users WHERE email = #{email}")
    User findByEmail(String email);
    
    /**
     * 通过手机号查询用户
     */
    @Select("SELECT * FROM Users WHERE phone = #{phone}")
    User findByPhone(String phone);
    
    /**
     * 插入新用户
     */
    @Insert("INSERT INTO Users(username, password, email, phone, user_type, register_time, status) " +
            "VALUES(#{username}, #{password}, #{email}, #{phone}, #{userType}, #{registerTime}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);
    
    /**
     * 更新用户信息
     */
    @Update("UPDATE Users SET username = #{username}, email = #{email}, phone = #{phone}, " +
            "avatar = #{avatar}, status = #{status} WHERE user_id = #{userId}")
    int update(User user);
    
    /**
     * 更新最后登录时间
     */
    @Update("UPDATE Users SET last_login_time = #{lastLoginTime} WHERE user_id = #{userId}")
    int updateLastLoginTime(@Param("userId") Integer userId, @Param("lastLoginTime") Date lastLoginTime);
    
    /**
     * 更新用户密码
     */
    @Update("UPDATE Users SET password = #{newPassword} WHERE user_id = #{userId}")
    int updatePassword(@Param("userId") Integer userId, @Param("newPassword") String newPassword);
    
    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(*) FROM Users WHERE email = #{email}")
    int checkEmailExists(String email);
    
    /**
     * 检查手机号是否存在
     */
    @Select("SELECT COUNT(*) FROM Users WHERE phone = #{phone}")
    int checkPhoneExists(String phone);
    
    /**
     * 根据条件分页查询用户列表
     * @param conditions 查询条件
     * @return 用户列表
     */
    List<User> findByConditions(Map<String, Object> conditions);
    
    /**
     * 根据条件统计用户数量
     * @param conditions 查询条件
     * @return 用户数量
     */
    int countByConditions(Map<String, Object> conditions);
    
    /**
     * 删除用户
     * @param userId 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM Users WHERE user_id = #{userId}")
    int deleteById(Integer userId);
    
    /**
     * 检查用户是否有关联的预约
     * @param userId 用户ID
     * @return 关联预约数量
     */
    @Select("SELECT COUNT(*) FROM Appointments WHERE user_id = #{userId}")
    int countAppointmentsByUserId(Integer userId);
    
    /**
     * 仅更新用户状态
     * @param userId 用户ID
     * @param status 状态值
     * @return 影响行数
     */
    @Update("UPDATE Users SET status = #{status} WHERE user_id = #{userId}")
    int updateStatus(@Param("userId") Integer userId, @Param("status") Integer status);
    
    /**
     * 统计用户总数
     */
    @Select("SELECT COUNT(*) FROM Users WHERE status = 1")
    int countTotalUsers();
    
    /**
     * 统计各类型用户数量
     */
    @Select("SELECT user_type, COUNT(*) as count FROM Users WHERE status = 1 GROUP BY user_type")
    List<Map<String, Object>> countUsersByType();
    
    /**
     * 统计新增用户数量
     */
    @Select("""
        SELECT COUNT(*) 
        FROM Users 
        WHERE status = 1 
        AND DATE(register_time) BETWEEN STR_TO_DATE(#{startDate}, '%Y-%m-%d') 
        AND STR_TO_DATE(#{endDate}, '%Y-%m-%d')
    """)
    int countNewUsers(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    /**
     * 统计指定日期之前的用户数量
     */
    @Select("""
        SELECT COUNT(*) 
        FROM Users 
        WHERE status = 1 
        AND DATE(register_time) < STR_TO_DATE(#{date}, '%Y-%m-%d')
    """)
    int countUsersBeforeDate(@Param("date") String date);
    
    /**
     * 获取用户趋势数据
     */
    @Select("""
        SELECT 
            DATE(register_time) as date,
            COUNT(*) as new_users,
            COUNT(DISTINCT CASE WHEN last_login_time >= STR_TO_DATE(#{startDate}, '%Y-%m-%d') THEN user_id END) as active_users
        FROM Users
        WHERE status = 1
        AND DATE(register_time) BETWEEN STR_TO_DATE(#{startDate}, '%Y-%m-%d') 
        AND STR_TO_DATE(#{endDate}, '%Y-%m-%d')
        GROUP BY DATE(register_time)
        ORDER BY date
    """)
    List<Map<String, Object>> getUserTrendData(@Param("startDate") String startDate, 
                                              @Param("endDate") String endDate, 
                                              @Param("groupBy") String groupBy);
    
    /**
     * 获取用户活跃度统计
     */
    @Select("""
        SELECT 
            COUNT(*) as total_users,
            COUNT(DISTINCT CASE WHEN last_login_time >= STR_TO_DATE(#{startDate}, '%Y-%m-%d') THEN user_id END) as active_users,
            COUNT(DISTINCT CASE WHEN last_login_time >= STR_TO_DATE(#{startDate}, '%Y-%m-%d') AND user_type = 'NORMAL_USER' THEN user_id END) as active_customers,
            COUNT(DISTINCT CASE WHEN last_login_time >= STR_TO_DATE(#{startDate}, '%Y-%m-%d') AND user_type = 'DEALER' THEN user_id END) as active_dealers
        FROM Users
        WHERE status = 1
        AND DATE(register_time) BETWEEN STR_TO_DATE(#{startDate}, '%Y-%m-%d') 
        AND STR_TO_DATE(#{endDate}, '%Y-%m-%d')
    """)
    Map<String, Object> getUserActivityStatistics(@Param("startDate") String startDate, 
                                                @Param("endDate") String endDate);
} 