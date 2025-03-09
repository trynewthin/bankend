package com.zhixuanche.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 经销商审核DTO
 */
@Data
public class DealerAuditDTO {
    
    @NotNull(message = "审核状态不能为空")
    private Integer status; // 状态：1-通过, 0-拒绝
    
    private String remarks; // 审核备注
} 