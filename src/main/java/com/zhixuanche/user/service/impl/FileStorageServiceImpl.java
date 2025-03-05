package com.zhixuanche.user.service.impl;

import com.zhixuanche.config.ResourceConfig;
import com.zhixuanche.user.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

/**
 * 文件存储服务实现类
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private ResourceConfig resourceConfig;

    // 允许的图片格式
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
        "image/jpeg",
        "image/png",
        "image/gif"
    );

    // 文件大小限制：2MB
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    // 基础存储路径
    private static final String BASE_STORAGE_PATH = "database/public";

    @Override
    public String storeFile(@NonNull MultipartFile file) {
        try {
            // 1. 基础验证
            if (file.isEmpty()) {
                throw new RuntimeException("文件为空");
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new RuntimeException("文件大小不能超过2MB");
            }

            // 2. 获取并验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
                throw new RuntimeException("只支持JPG、PNG、GIF格式的图片");
            }

            // 3. 获取原始文件名和扩展名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new RuntimeException("无效的文件名");
            }
            
            String fileExtension = getFileExtension(originalFilename).toLowerCase();
            if (!isValidImageExtension(fileExtension)) {
                throw new RuntimeException("不支持的图片格式");
            }

            // 4. 生成新的文件名：avatar_timestamp.extension
            String timestamp = String.valueOf(System.currentTimeMillis());
            String newFilename = "avatar_" + timestamp + fileExtension;
            
            // 5. 确保目录存在
            Path uploadDir = Paths.get(BASE_STORAGE_PATH, "images", "avatars");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // 6. 保存文件
            Path targetLocation = uploadDir.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // 7. 返回可访问的URL
            return resourceConfig.getAvatarUrl(newFilename);
            
        } catch (IOException ex) {
            throw new RuntimeException("文件存储失败: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            if (StringUtils.hasText(fileUrl)) {
                // 从URL中提取文件名
                String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
                
                // 验证文件名格式
                if (!filename.startsWith("avatar_")) {
                    throw new RuntimeException("无效的头像文件");
                }
                
                // 获取文件路径
                Path filePath = Paths.get(BASE_STORAGE_PATH, "images", "avatars", filename);
                
                // 删除文件
                Files.deleteIfExists(filePath);
            }
        } catch (IOException ex) {
            throw new RuntimeException("文件删除失败", ex);
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex);
        }
        throw new RuntimeException("无效的文件格式");
    }

    /**
     * 验证图片扩展名
     */
    private boolean isValidImageExtension(String extension) {
        return ".jpg".equals(extension) ||
               ".jpeg".equals(extension) ||
               ".png".equals(extension) ||
               ".gif".equals(extension);
    }
} 