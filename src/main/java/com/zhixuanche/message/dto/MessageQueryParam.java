package com.zhixuanche.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 消息查询参数
 */
@Schema(description = "消息查询参数", name = "MessageQueryParam")
@Data
public class MessageQueryParam {
    
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "14")
    private Integer userId;
    
    /**
     * 联系人ID (如发送者或接收者)
     */
    @Schema(description = "联系人ID (如发送者或接收者)", example = "10")
    private Integer contactId;
    
    /**
     * 是否已读
     */
    @Schema(description = "是否已读：true-已读，false-未读，null-全部", example = "false")
    private Boolean read;
    
    /**
     * 消息类型
     */
    @Schema(description = "消息类型：SYSTEM/MARKETING/NOTICE等", example = "SYSTEM")
    private String messageType;
    
    /**
     * 交互类型
     */
    @Schema(description = "交互类型：COMMENT/REPLY/QUESTION等", example = "CONSULTATION")
    private String interactionType;
    
    /**
     * 目标类型
     */
    @Schema(description = "目标类型：VEHICLE/COMMENT/POST/DEALER等", example = "VEHICLE")
    private String targetType;
    
    /**
     * 目标ID
     */
    @Schema(description = "目标ID", example = "5")
    private String targetId;
    
    /**
     * 起始日期
     */
    @Schema(description = "起始日期，格式：yyyy-MM-dd", example = "2025-01-01")
    private String startDate;
    
    /**
     * 结束日期
     */
    @Schema(description = "结束日期，格式：yyyy-MM-dd", example = "2025-03-31")
    private String endDate;
    
    /**
     * 页码
     */
    @Schema(description = "页码，默认1", example = "1")
    private Integer page = 1;
    
    /**
     * 每页数量
     */
    @Schema(description = "每页数量，默认20", example = "20")
    private Integer size = 20;
} 