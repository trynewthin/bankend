package com.zhixuanche.user.controller;

import com.zhixuanche.user.dto.UserProfileDTO;
import com.zhixuanche.user.entity.User;
import com.zhixuanche.user.service.UserService;
import com.zhixuanche.user.service.FileStorageService;
import com.zhixuanche.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

/**
 * 用户资料控制器
 */
@RestController
@RequestMapping("/users")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 获取用户资料
     */
    @GetMapping("/profile")
    public ApiResponse<User> getUserProfile() {
        // TODO: 从SecurityContext获取当前用户ID
        Integer userId = 1; // 临时写死，实际应该从token中获取
        User user = userService.getUserById(userId);
        return ApiResponse.success("获取成功", user);
    }

    /**
     * 更新用户资料
     */
    @PutMapping("/profile")
    public ApiResponse<User> updateUserProfile(@Valid @RequestBody UserProfileDTO profileDTO) {
        // TODO: 从SecurityContext获取当前用户ID
        Integer userId = 1; // 临时写死，实际应该从token中获取
        User user = userService.getUserById(userId);
        user.setEmail(profileDTO.getEmail());
        user.setPhone(profileDTO.getPhone());
        userService.updateUser(user);
        return ApiResponse.success("更新成功", user);
    }

    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    public ApiResponse<Object> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        // TODO: 从SecurityContext获取当前用户ID
        Integer userId = 1; // 临时写死，实际应该从token中获取
        String avatarUrl = fileStorageService.storeFile(file);
        User user = userService.getUserById(userId);
        user.setAvatar(avatarUrl);
        userService.updateUser(user);
        return ApiResponse.success("上传成功", avatarUrl);
    }
} 