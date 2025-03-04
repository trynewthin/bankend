package com.zhixuanche.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "resource")
public class ResourceConfig {
    private String staticLocations;
    private String avatarPath;
    private String carPath;

    public String getAvatarUrl(String filename) {
        return staticLocations + avatarPath + filename;
    }

    public String getCarImageUrl(String filename) {
        return staticLocations + carPath + filename;
    }
} 