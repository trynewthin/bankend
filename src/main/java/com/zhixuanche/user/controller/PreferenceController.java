package com.zhixuanche.user.controller;

import com.zhixuanche.user.dto.PreferenceDTO;
import com.zhixuanche.user.entity.UserPreference;
import com.zhixuanche.user.service.UserPreferenceService;
import com.zhixuanche.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * 用户偏好设置控制器
 */
@RestController
@RequestMapping("/users")
public class PreferenceController {

    @Autowired
    private UserPreferenceService preferenceService;

    /**
     * 获取用户偏好设置
     */
    @GetMapping("/preference")
    public ApiResponse<UserPreference> getUserPreference() {
        // TODO: 从SecurityContext获取当前用户ID
        Integer userId = 1; // 临时写死，实际应该从token中获取
        UserPreference preference = preferenceService.getUserPreference(userId);
        return ApiResponse.success("获取成功", preference);
    }

    /**
     * 更新用户偏好设置
     */
    @PutMapping("/preference")
    public ApiResponse<UserPreference> updateUserPreference(@Valid @RequestBody PreferenceDTO preferenceDTO) {
        // TODO: 从SecurityContext获取当前用户ID
        Integer userId = 1; // 临时写死，实际应该从token中获取
        
        UserPreference preference = new UserPreference();
        preference.setUserId(userId);
        preference.setPriceMin(preferenceDTO.getPriceMin());
        preference.setPriceMax(preferenceDTO.getPriceMax());
        preference.setPreferredBrands(preferenceDTO.getPreferredBrands());
        preference.setPreferredCategories(preferenceDTO.getPreferredCategories());
        preference.setOtherPreferences(preferenceDTO.getOtherPreferences());
        
        preferenceService.saveUserPreference(preference);
        return ApiResponse.success("更新成功", preference);
    }
} 