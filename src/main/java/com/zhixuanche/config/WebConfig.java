package com.zhixuanche.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web通用配置类
 * 处理编码、静态资源等基础配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * 字符编码过滤器，确保所有请求和响应都使用UTF-8编码
     * 这有助于解决中文参数编码问题
     */
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
} 