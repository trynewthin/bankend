package com.zhixuanche.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户状态更新DTO
 */
@Data
public class UserStatusDTO {
    
    @NotNull(message = "状态不能为空")
    private Integer status; // 状态：1-正常, 0-禁用
    
    private String reason; // 操作原因
} 