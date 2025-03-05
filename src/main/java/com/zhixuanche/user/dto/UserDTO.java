package com.zhixuanche.user.dto;

import com.zhixuanche.user.entity.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册DTO
 */
@Data
public class UserDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "用户类型不能为空")
    private String userType;

    /**
     * 获取用户类型枚举值
     * @return UserType枚举值
     * @throws RuntimeException 当用户类型无效时抛出异常
     */
    public UserType getUserTypeEnum() {
        try {
            return UserType.valueOf(userType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("无效的用户类型");
        }
    }
} 