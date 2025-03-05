package com.zhixuanche.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.user.dto.LoginDTO;
import com.zhixuanche.user.dto.UserDTO;
import com.zhixuanche.user.entity.User;
import com.zhixuanche.user.service.UserService;
import com.zhixuanche.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@Tag(name = "用户管理", description = "用户注册、登录、登出等接口")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "新用户注册接口")
    @SecurityRequirements  // 不需要token
    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.register(
            userDTO.getUsername(),
            userDTO.getPassword(),
            userDTO.getEmail(),
            userDTO.getPhone(),
            userDTO.getUserTypeEnum().name()  // 使用枚举的name值
        );
        return ApiResponse.success("注册成功", user);
    }

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户登录接口，支持邮箱和手机号登录")
    @SecurityRequirements  // 不需要token
    @PostMapping("/login")
    public ApiResponse<Object> login(@Valid @RequestBody LoginDTO loginDTO) {
        User user = userService.login(
            loginDTO.getLoginIdentity(),
            loginDTO.getPassword(),
            loginDTO.getLoginType()
        );
        
        // 登录成功，使用Sa-Token记录登录状态
        StpUtil.login(user.getUserId());
        
        // 返回token等信息
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("tokenInfo", StpUtil.getTokenInfo());
        
        return ApiResponse.success("登录成功", result);
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出", description = "用户登出接口")
    @SecurityRequirement(name = "Sa-Token")
    @PostMapping("/logout")
    public ApiResponse<Object> logout() {
        StpUtil.logout();
        return ApiResponse.success("登出成功", null);
    }
} 