package com.zhixuanche.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.user.dto.PreferenceDTO;
import com.zhixuanche.user.entity.UserPreference;
import com.zhixuanche.user.service.UserPreferenceService;
import com.zhixuanche.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * 用户偏好设置控制器
 */
@Tag(name = "用户偏好", description = "用户偏好设置管理接口")
@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Sa-Token")
public class PreferenceController {

    @Autowired
    private UserPreferenceService preferenceService;

    /**
     * 获取用户偏好设置
     */
    @Operation(summary = "获取偏好设置", description = "获取当前登录用户的偏好设置")
    @GetMapping("/preference")
    public ApiResponse<UserPreference> getUserPreference() {
        Integer userId = StpUtil.getLoginIdAsInt();
        UserPreference preference = preferenceService.getUserPreference(userId);
        return ApiResponse.success("获取成功", preference);
    }

    /**
     * 更新用户偏好设置
     */
    @Operation(summary = "更新偏好设置", description = "更新当前登录用户的偏好设置")
    @PutMapping("/preference")
    public ApiResponse<UserPreference> updateUserPreference(@Valid @RequestBody PreferenceDTO preferenceDTO) {
        Integer userId = StpUtil.getLoginIdAsInt();
        
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