package com.zhixuanche.user.service.impl;

import com.zhixuanche.user.entity.UserPreference;
import com.zhixuanche.user.mapper.UserPreferenceMapper;
import com.zhixuanche.user.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户偏好服务实现类
 */
@Service
public class UserPreferenceServiceImpl implements UserPreferenceService {

    @Autowired
    private UserPreferenceMapper preferenceMapper;

    @Override
    public UserPreference getUserPreference(Integer userId) {
        return preferenceMapper.findByUserId(userId);
    }

    @Override
    @Transactional
    public boolean saveUserPreference(UserPreference preference) {
        preference.setUpdateTime(new Date());
        UserPreference existingPreference = preferenceMapper.findByUserId(preference.getUserId());
        if (existingPreference == null) {
            return preferenceMapper.insert(preference) > 0;
        } else {
            return preferenceMapper.update(preference) > 0;
        }
    }

    @Override
    @Transactional
    public boolean deleteUserPreference(Integer userId) {
        return preferenceMapper.deleteByUserId(userId) > 0;
    }
} 