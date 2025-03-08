package com.zhixuanche.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息数据传输对象
 */
@Schema(description = "消息数据传输对象", name = "MessageDTO")
@Data
public class MessageDTO {
    
    /**
     * 消息ID
     */
    @Schema(description = "消息ID", example = "1")
    private Integer id;
    
    /**
     * 发送者ID
     */
    @Schema(description = "发送者用户ID", example = "10")
    private Integer fromUserId;
    
    /**
     * 接收者ID
     */
    @Schema(description = "接收者用户ID", example = "12")
    private Integer toUserId;
    
    /**
     * 发送者用户名
     */
    @Schema(description = "发送者用户名", example = "张三")
    private String fromUserName;
    
    /**
     * 发送者头像
     */
    @Schema(description = "发送者头像URL", example = "/images/avatars/user10.jpg")
    private String fromUserAvatar;
    
    /**
     * 关联车辆ID
     */
    @Schema(description = "关联车辆ID", example = "5")
    private Integer carId;
    
    /**
     * 关联车辆信息（品牌+型号）
     */
    @Schema(description = "关联车辆信息", example = "宝马 3系")
    private String carInfo;
    
    /**
     * 消息标题
     */
    @Schema(description = "消息标题", example = "新优惠活动")
    private String title;
    
    /**
     * 消息内容
     */
    @Schema(description = "消息内容", example = "您关注的宝马3系降价5万，点击查看详情")
    private String content;
    
    /**
     * 消息类型
     */
    @Schema(description = "消息类型：SYSTEM/MARKETING/NOTICE/ACTIVITY等", example = "MARKETING")
    private String messageType;
    
    /**
     * 交互类型
     */
    @Schema(description = "交互类型：COMMENT/REPLY/QUESTION/CONSULTATION等", example = "CONSULTATION")
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
     * 是否已读
     */
    @Schema(description = "是否已读", example = "false")
    private Boolean read;
    
    /**
     * 发送时间
     */
    @Schema(description = "发送时间", example = "2025-03-01T10:15:30")
    private LocalDateTime sendTime;
    
    /**
     * 过期时间
     */
    @Schema(description = "过期时间", example = "2025-04-01T00:00:00")
    private LocalDateTime expireTime;
    
    /**
     * 优先级
     */
    @Schema(description = "优先级：1-低，2-中，3-高", example = "2")
    private Integer priority;
    
    /**
     * 动作类型
     */
    @Schema(description = "动作类型：URL/ACTIVITY/VEHICLE/NONE等", example = "URL")
    private String actionType;
    
    /**
     * 动作值
     */
    @Schema(description = "动作值，如URL链接等", example = "https://www.example.com/cars/5")
    private String actionValue;
} 