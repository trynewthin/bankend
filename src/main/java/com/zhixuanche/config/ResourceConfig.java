package com.zhixuanche.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 资源配置类
 * 用于管理静态资源的访问路径
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "resource")
public class ResourceConfig {
    /**
     * 静态资源基础URL
     * 例如：http://localhost:8090/images/
     */
    private String staticLocations;

    /**
     * 头像存储路径
     * 例如：/avatars/
     */
    private String avatarPath;

    /**
     * 汽车图片存储路径
     * 例如：/cars/
     */
    private String carPath;

    /**
     * 获取头像完整URL
     * @param filename 文件名
     * @return 完整的头像访问URL
     */
    public String getAvatarUrl(String filename) {
        if (!StringUtils.hasText(filename)) {
            return null;
        }
        return staticLocations + "avatars/" + filename;
    }

    /**
     * 获取汽车图片完整URL
     * @param filename 文件名
     * @return 完整的汽车图片访问URL
     */
    public String getCarImageUrl(String filename) {
        if (!StringUtils.hasText(filename)) {
            return null;
        }
        return staticLocations + "cars/" + filename;
    }

    /**
     * 获取头像存储的物理路径
     * @param filename 文件名
     * @return 头像文件的物理存储路径
     */
    public String getAvatarStoragePath(String filename) {
        if (!StringUtils.hasText(filename)) {
            return null;
        }
        return avatarPath + filename;
    }

    /**
     * 获取汽车图片存储的物理路径
     * @param filename 文件名
     * @return 汽车图片的物理存储路径
     */
    public String getCarImageStoragePath(String filename) {
        if (!StringUtils.hasText(filename)) {
            return null;
        }
        return carPath + filename;
    }
} 