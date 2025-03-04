package com.zhixuanche.user.controller;

import com.zhixuanche.user.dto.LoginDTO;
import com.zhixuanche.user.dto.UserDTO;
import com.zhixuanche.user.entity.User;
import com.zhixuanche.user.service.UserService;
import com.zhixuanche.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.register(
            userDTO.getUsername(),
            userDTO.getPassword(),
            userDTO.getEmail(),
            userDTO.getPhone(),
            userDTO.getUserType()
        );
        return ApiResponse.success("注册成功", user);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<Object> login(@Valid @RequestBody LoginDTO loginDTO) {
        User user = userService.login(
            loginDTO.getLoginIdentity(),
            loginDTO.getPassword(),
            loginDTO.getLoginType()
        );
        // TODO: 生成token的逻辑需要在TokenService中实现
        return ApiResponse.success("登录成功", user);
    }
} 