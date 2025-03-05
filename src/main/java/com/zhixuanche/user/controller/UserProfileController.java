package com.zhixuanche.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.user.dto.UserProfileDTO;
import com.zhixuanche.user.entity.User;
import com.zhixuanche.user.service.UserService;
import com.zhixuanche.user.service.FileStorageService;
import com.zhixuanche.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

/**
 * 用户资料控制器
 */
@Tag(name = "用户资料", description = "用户资料管理接口")
@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Sa-Token")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 获取用户资料
     */
    @Operation(summary = "获取用户资料", description = "获取当前登录用户的资料信息")
    @GetMapping("/profile")
    public ApiResponse<User> getUserProfile() {
        Integer userId = StpUtil.getLoginIdAsInt();
        User user = userService.getUserById(userId);
        return ApiResponse.success("获取成功", user);
    }

    /**
     * 更新用户资料
     */
    @Operation(summary = "更新用户资料", description = "更新当前登录用户的资料信息")
    @PutMapping("/profile")
    public ApiResponse<User> updateUserProfile(@Valid @RequestBody UserProfileDTO profileDTO) {
        Integer userId = StpUtil.getLoginIdAsInt();
        User user = userService.getUserById(userId);
        user.setEmail(profileDTO.getEmail());
        user.setPhone(profileDTO.getPhone());
        userService.updateUser(user);
        return ApiResponse.success("更新成功", user);
    }

    /**
     * 上传头像
     */
    @Operation(
        summary = "上传头像", 
        description = """
            上传当前登录用户的头像
            - 支持的格式：JPG、PNG、GIF
            - 文件大小限制：2MB
            - 文件命名规则：avatar_timestamp.extension
            """,
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "上传成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ApiResponse.class,
                        example = """
                        {
                            "code": 200,
                            "message": "上传成功",
                            "data": "http://localhost:8090/images/avatars/avatar_1679012345678.jpg"
                        }
                        """
                    )
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "请求错误",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ApiResponse.class,
                        example = """
                        {
                            "code": 400,
                            "message": "文件大小不能超过2MB"
                        }
                        """
                    )
                )
            )
        }
    )
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Object> uploadAvatar(
        @Parameter(
            description = """
                头像文件
                - 支持的格式：JPG、PNG、GIF
                - 最大大小：2MB
                """,
            required = true,
            content = @Content(mediaType = "image/*")
        )
        @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ApiResponse.error(400, "请选择要上传的文件");
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ApiResponse.error(400, "只能上传图片文件");
        }
        
        Integer userId = StpUtil.getLoginIdAsInt();
        String avatarUrl = fileStorageService.storeFile(file);
        userService.updateAvatar(userId, avatarUrl);
        return ApiResponse.success("上传成功", avatarUrl);
    }
} 