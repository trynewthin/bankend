package com.zhixuanche.user.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储服务接口
 */
public interface FileStorageService {
    /**
     * 存储文件
     * @param file 文件
     * @return 文件访问URL
     */
    String storeFile(MultipartFile file);

    /**
     * 删除文件
     * @param fileUrl 文件URL
     */
    void deleteFile(String fileUrl);
} 