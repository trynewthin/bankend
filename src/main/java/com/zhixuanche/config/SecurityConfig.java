package com.zhixuanche.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.common.interceptor.UserIdInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

/**
 * 统一安全配置类
 * 整合认证、授权、跨域等安全相关配置
 */
@Configuration
@Order(1) // 确保安全配置优先级最高
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * 配置跨域访问
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 配置安全拦截器
     * 统一管理Sa-Token认证和用户ID拦截器
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 1. 注册Sa-Token认证拦截器
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 登录认证：除了白名单接口，其他都需要登录认证
            SaRouter.match("/**")
                    .notMatch(
                        // 用户认证接口
                        "/users/login", "/users/register", "/users/logout",
                        // 公开的推荐接口
                        "/recommendations/home",
                        "/recommendations/hot",
                        "/recommendations/new",
                        // 静态资源
                        "/static/**", "/favicon.ico",
                        // API文档相关
                        "/swagger-ui.html", "/swagger-ui/**",
                        "/v3/api-docs/**", "/swagger-resources/**",
                        "/doc.html", "/webjars/**",
                        // 健康检查
                        "/actuator/**"
                    )
                    .check(r -> StpUtil.checkLogin());

            // 角色认证：管理员接口需要管理员角色
            SaRouter.match("/dealers/admin/**", "/admin/**")
                    .check(r -> StpUtil.checkRole("admin"));
                    
            // 经销商接口权限
            SaRouter.match("/dealers/**")
                    .notMatch("/dealers/admin/**")
                    .check(r -> StpUtil.checkRole("dealer"));
        })).addPathPatterns("/**")
                .excludePathPatterns("/**", "OPTIONS") // 添加此行排除OPTIONS请求
                .order(1); // 认证拦截器优先级最高

        // 2. 注册用户ID拦截器
        registry.addInterceptor(new UserIdInterceptor())
                .addPathPatterns("/api/behavior/**", "/api/favorites/**")
                .order(2); // 用户ID拦截器次优先级
    }
} 