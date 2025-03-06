package com.zhixuanche.common.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户ID拦截器
 * 用于从Sa-Token中提取用户ID，并设置为请求属性
 */
public class UserIdInterceptor implements HandlerInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(UserIdInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("UserIdInterceptor被调用: URI={}", request.getRequestURI());
        
        // 检查用户是否已登录
        boolean isLogin = StpUtil.isLogin();
        log.info("用户登录状态: isLogin={}", isLogin);
        
        if (isLogin) {
            // 从Sa-Token中获取用户ID
            Integer userId = StpUtil.getLoginIdAsInt();
            // 设置为请求属性
            request.setAttribute("userId", userId);
            log.info("设置用户ID请求属性: userId={}", userId);
        }
        
        // 总是返回true，让请求继续处理
        // 如果用户未登录，会在后续的认证拦截器中被拦截
        return true;
    }
} 