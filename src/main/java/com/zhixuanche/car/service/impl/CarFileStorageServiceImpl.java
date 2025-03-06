package com.zhixuanche.car.service.impl;

import com.zhixuanche.car.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 车辆文件存储服务实现类
 */
@Service("carFileStorageService")
public class CarFileStorageServiceImpl implements FileStorageService {

    @Value("${resource.static-locations}")
    private String resourceLocation;
    
    @Value("${resource.car-path}")
    private String carPath;
    
    // 基础存储路径，与用户模块保持一致
    private static final String BASE_STORAGE_PATH = "database/public";
    
    /**
     * 存储文件到指定目录
     * @param file 要存储的文件，不能为null
     * @param directory 目录名称，对于车辆图片可以是 "cars/carId/imageType" 格式
     * @return 文件访问URL
     */
    @Override
    public String storeFile(@NonNull MultipartFile file, String directory) {
        try {
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            
            // 生成新文件名
            String newFilename = generateFilename(directory, fileExtension);
            
            // 确定目录路径
            String dirPath = getDirectoryPath(directory);
            
            // 确保目录存在
            createDirectoryIfNotExists(dirPath);
            
            // 保存文件
            Path targetLocation = Paths.get(dirPath).resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // 返回文件URL
            return getFileUrl(directory, newFilename);
        } catch (IOException ex) {
            throw new RuntimeException("无法存储文件: " + ex.getMessage(), ex);
        }
    }
    
    /**
     * 生成文件名
     * @param directory 目录信息
     * @param fileExtension 文件扩展名
     * @return 生成的文件名
     */
    private String generateFilename(String directory, String fileExtension) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        // 处理车辆图片的特殊命名
        if (directory.startsWith("cars")) {
            // 尝试从directory中提取carId和imageType
            // 格式可能是：cars, cars/carId, cars/carId/imageType
            String[] parts = directory.split("/");
            String carId = (parts.length > 1) ? parts[1] : "unknown";
            String imageType = (parts.length > 2) ? parts[2] : "general";
            
            // 返回格式：car_carId_imageType_timestamp.ext
            return "car_" + carId + "_" + imageType + "_" + timestamp + fileExtension;
        }
        
        // 其他类型的文件使用UUID
        return UUID.randomUUID().toString() + fileExtension;
    }
    
    /**
     * 删除文件
     * @param fileUrl 文件URL
     * @return 是否删除成功
     */
    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            // 从URL中提取文件路径
            String filePath = getFilePathFromUrl(fileUrl);
            
            // 删除文件
            Path targetLocation = Paths.get(filePath);
            return Files.deleteIfExists(targetLocation);
        } catch (IOException ex) {
            throw new RuntimeException("无法删除文件: " + ex.getMessage(), ex);
        }
    }
    
    /**
     * 获取文件扩展名
     * @param filename 文件名
     * @return 扩展名（包含点号）
     */
    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }
    
    /**
     * 确保目录存在，不存在则创建
     * @param dirPath 目录路径
     */
    private void createDirectoryIfNotExists(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("无法创建目录: " + dirPath);
            }
        }
    }
    
    /**
     * 获取目录路径
     * @param directory 目录名称
     * @return 完整目录路径
     */
    private String getDirectoryPath(String directory) {
        // 如果是车辆图片
        if (directory.startsWith("cars")) {
            // 提取基本目录，忽略额外信息
            return BASE_STORAGE_PATH + "/images" + carPath;
        }
        
        // 默认返回根目录
        return BASE_STORAGE_PATH + "/images/" + directory;
    }
    
    /**
     * 获取文件URL
     * @param directory 目录名称
     * @param filename 文件名
     * @return 文件URL
     */
    private String getFileUrl(String directory, String filename) {
        // 如果是车辆图片
        if (directory.startsWith("cars")) {
            return resourceLocation + carPath + filename;
        }
        
        // 默认返回根目录URL
        return resourceLocation + "/" + directory + "/" + filename;
    }
    
    /**
     * 从URL中提取文件路径
     * @param fileUrl 文件URL
     * @return 文件路径
     */
    private String getFilePathFromUrl(String fileUrl) {
        // 移除基础URL部分
        String relativePath = fileUrl.replace(resourceLocation, "");
        
        // 返回完整文件路径
        return BASE_STORAGE_PATH + "/images" + relativePath;
    }
}