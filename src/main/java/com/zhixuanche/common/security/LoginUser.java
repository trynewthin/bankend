package com.zhixuanche.common.security;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.user.entity.User;
import com.zhixuanche.user.service.UserService;
import org.springframework.stereotype.Component;

/**
 * 当前登录用户信息
 */
@Component
public class LoginUser {
    
    private final UserService userService;
    
    public LoginUser(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * 获取当前登录用户ID
     */
    public Integer getUserId() {
        return StpUtil.getLoginIdAsInt();
    }
    
    /**
     * 获取当前登录用户信息
     */
    public User getUser() {
        return userService.getUserById(getUserId());
    }
    
    /**
     * 判断当前用户是否为管理员
     */
    public boolean isAdmin() {
        return StpUtil.hasRole("admin");
    }
    
    /**
     * 判断当前用户是否为经销商
     */
    public boolean isDealer() {
        return StpUtil.hasRole("dealer");
    }
} 