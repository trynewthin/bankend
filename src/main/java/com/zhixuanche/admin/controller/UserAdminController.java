package com.zhixuanche.admin.controller;

import com.zhixuanche.admin.dto.UserAdminDetailDTO;
import com.zhixuanche.admin.dto.UserStatusDTO;
import com.zhixuanche.admin.service.UserAdminService;
import com.zhixuanche.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理", description = "系统管理员用户管理接口")
@RestController
@RequestMapping("/admin/users")
public class UserAdminController {

    @Autowired
    private UserAdminService userAdminService;

    /**
     * 获取用户列表
     */
    @Operation(summary = "获取用户列表", description = "根据条件获取用户列表，支持分页和条件筛选")
    @GetMapping
    public Result getUserList(
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "用户状态") @RequestParam(required = false) String status,
            @Parameter(description = "用户类型") @RequestParam(required = false) String userType,
            @Parameter(description = "注册起始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "注册结束日期") @RequestParam(required = false) String endDate,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页记录数") @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(userAdminService.getUserList(keyword, status, userType, startDate, endDate, page, size));
    }

    /**
     * 获取用户详情
     */
    @Operation(summary = "获取用户详情", description = "获取指定用户的详细信息")
    @GetMapping("/{userId}")
    public Result getUserDetail(
            @Parameter(description = "用户ID") @PathVariable Integer userId) {
        UserAdminDetailDTO userDetail = userAdminService.getUserDetail(userId);
        if (userDetail == null) {
            return Result.error(404, "用户不存在");
        }
        return Result.success(userDetail);
    }

    /**
     * 更新用户状态
     */
    @Operation(summary = "更新用户状态", description = "启用或禁用用户账号")
    @PutMapping("/{userId}/status")
    public Result updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Integer userId,
            @Valid @RequestBody UserStatusDTO statusDTO) {
        boolean success = userAdminService.updateUserStatus(userId, statusDTO);
        if (success) {
            return Result.success("用户状态更新成功");
        } else {
            return Result.error(500, "用户状态更新失败");
        }
    }

    /**
     * 重置用户密码
     */
    @Operation(summary = "重置用户密码", description = "重置用户密码为随机密码")
    @PostMapping("/{userId}/reset-password")
    public Result resetUserPassword(
            @Parameter(description = "用户ID") @PathVariable Integer userId,
            @Parameter(description = "是否通知用户") @RequestParam(defaultValue = "true") Boolean notifyUser) {
        return Result.success(userAdminService.resetUserPassword(userId, notifyUser));
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "删除指定用户及其关联信息")
    @DeleteMapping("/{userId}")
    public Result deleteUser(
            @Parameter(description = "用户ID") @PathVariable Integer userId) {
        Map<String, Object> result = userAdminService.deleteUser(userId);
        if ((Boolean) result.get("success")) {
            return Result.success(result.get("message"));
        } else {
            return Result.error(500, (String) result.get("message"));
        }
    }
} 