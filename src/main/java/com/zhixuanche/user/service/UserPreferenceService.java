package com.zhixuanche.user.service;

import com.zhixuanche.user.entity.UserPreference;

/**
 * 用户偏好服务接口
 */
public interface UserPreferenceService {
    
    /**
     * 获取用户偏好设置
     */
    UserPreference getUserPreference(Integer userId);
    
    /**
     * 保存或更新用户偏好设置
     */
    boolean saveUserPreference(UserPreference preference);
    
    /**
     * 删除用户偏好设置
     */
    boolean deleteUserPreference(Integer userId);
} 