package com.zhixuanche.user.service;

import com.zhixuanche.user.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    User register(String username, String password, String email, String phone, String userType);
    
    /**
     * 用户登录
     */
    User login(String loginIdentity, String password, String loginType);
    
    /**
     * 获取用户信息
     */
    User getUserById(Integer userId);
    
    /**
     * 更新用户信息
     */
    boolean updateUser(User user);
    
    /**
     * 更新用户密码
     */
    boolean updatePassword(Integer userId, String oldPassword, String newPassword);
    
    /**
     * 更新用户头像
     */
    boolean updateAvatar(Integer userId, String avatarUrl);
    
    /**
     * 检查邮箱是否存在
     */
    boolean checkEmailExists(String email);
    
    /**
     * 检查手机号是否存在
     */
    boolean checkPhoneExists(String phone);
} 