package com.zhixuanche.user.service.impl;

import com.zhixuanche.user.entity.User;
import com.zhixuanche.user.entity.enums.UserType;
import com.zhixuanche.user.mapper.UserMapper;
import com.zhixuanche.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public User register(String username, String password, String email, String phone, String userType) {
        // 检查邮箱和手机号是否已存在
        if (checkEmailExists(email)) {
            throw new RuntimeException("邮箱已被注册");
        }
        if (phone != null && checkPhoneExists(phone)) {
            throw new RuntimeException("手机号已被注册");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 直接使用明文密码
        user.setEmail(email);
        user.setPhone(phone);
        user.setUserType(UserType.valueOf(userType)); // 将String转换为UserType枚举
        user.setRegisterTime(new Date());
        user.setStatus(1);

        // 保存用户
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String loginIdentity, String password, String loginType) {
        User user = null;
        
        // 根据登录类型查询用户
        if ("email".equals(loginType)) {
            user = userMapper.findByEmail(loginIdentity);
        } else if ("phone".equals(loginType)) {
            user = userMapper.findByPhone(loginIdentity);
        }

        // 验证用户是否存在和密码是否正确
        if (user == null || !password.equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        // 更新最后登录时间
        user.setLastLoginTime(new Date());
        userMapper.updateLastLoginTime(user.getUserId(), user.getLastLoginTime());

        return user;
    }

    @Override
    public User getUserById(Integer userId) {
        return userMapper.findById(userId);
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        return userMapper.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean updatePassword(Integer userId, String oldPassword, String newPassword) {
        // 获取用户信息
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!oldPassword.equals(user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }

        // 更新密码
        return userMapper.updatePassword(userId, newPassword) > 0;
    }

    @Override
    @Transactional
    public boolean updateAvatar(Integer userId, String avatarUrl) {
        User user = new User();
        user.setUserId(userId);
        user.setAvatar(avatarUrl);
        return userMapper.update(user) > 0;
    }

    @Override
    public boolean checkEmailExists(String email) {
        return userMapper.checkEmailExists(email) > 0;
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        return userMapper.checkPhoneExists(phone) > 0;
    }
} 