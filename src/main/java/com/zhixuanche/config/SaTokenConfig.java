package com.zhixuanche.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    
    // 注册Sa-Token的拦截器
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 注册路由拦截器，自定义认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 登录认证：除了登录注册接口和文档接口，其他都需要登录认证
            SaRouter.match("/**")
                    .notMatch(
                        // 用户认证接口
                        "/auth/login", "/auth/register", "/auth/logout",
                        // 静态资源
                        "/static/**", "/favicon.ico",
                        // Swagger UI 相关
                        "/swagger-ui.html", "/swagger-ui/**",
                        // OpenAPI 文档相关
                        "/v3/api-docs/**", "/swagger-resources/**",
                        // Knife4j UI 相关（如果使用）
                        "/doc.html", "/webjars/**",
                        // 健康检查
                        "/actuator/**"
                    )
                    .check(r -> StpUtil.checkLogin());

            // 角色认证：管理员接口需要管理员角色
            SaRouter.match("/dealers/admin/**")
                    .check(r -> StpUtil.checkRole("admin"));
        })).addPathPatterns("/**");
    }
} 