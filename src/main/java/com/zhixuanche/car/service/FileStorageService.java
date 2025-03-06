package com.zhixuanche.car.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.lang.NonNull;

/**
 * 文件存储服务接口
 */
public interface FileStorageService {
    /**
     * 存储文件到指定目录
     * @param file 文件
     * @param directory 目录名称
     * @return 文件访问URL
     */
    String storeFile(@NonNull MultipartFile file, String directory);

    /**
     * 删除文件
     * @param fileUrl 文件URL
     * @return 是否删除成功
     */
    boolean deleteFile(String fileUrl);
} 