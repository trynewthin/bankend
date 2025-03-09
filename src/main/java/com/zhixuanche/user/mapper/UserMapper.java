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
} 