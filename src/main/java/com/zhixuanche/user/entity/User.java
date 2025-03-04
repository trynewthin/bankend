package com.zhixuanche.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhixuanche.user.entity.enums.UserType;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;

/**
 * 用户实体类
 */
@Data
@TableName("Users")
public class User {
    private Integer userId;        // 用户ID

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;       // 用户名

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    private String password;       // 密码

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;          // 邮箱

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;          // 手机号

    private UserType userType;     // 用户类型：普通用户、经销商、管理员

    private Date registerTime;     // 注册时间

    private Date lastLoginTime;    // 最后登录时间

    private Integer status;        // 状态：1-正常, 0-禁用

    @Size(max = 200, message = "头像URL长度不能超过200个字符")
    private String avatar;         // 头像URL
} 