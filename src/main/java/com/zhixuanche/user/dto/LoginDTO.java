package com.zhixuanche.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录DTO
 */
@Data
public class LoginDTO {
    @NotBlank(message = "登录标识不能为空")
    private String loginIdentity;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String loginType;
} 