package com.zhixuanche.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息实体类
 * 对应数据库 Messages 表
 */
@Data
@TableName("Messages")
public class Message {
    
    /**
     * 消息ID
     */
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;
    
    /**
     * 发送者ID
     */
    @TableField("sender_id")
    private Integer senderId;
    
    /**
     * 接收者ID
     */
    @TableField("receiver_id")
    private Integer receiverId;
    
    /**
     * 关联车辆ID（可为空）
     */
    @TableField("car_id")
    private Integer carId;
    
    /**
     * 消息内容
     */
    @TableField("content")
    private String content;
    
    /**
     * 消息标题
     */
    @TableField("title")
    private String title;
    
    /**
     * 消息类型：SYSTEM/MARKETING/NOTICE等
     */
    @TableField("message_type")
    private String messageType;
    
    /**
     * 交互类型：COMMENT/REPLY/QUESTION/CONSULTATION等
     */
    @TableField("interaction_type")
    private String interactionType;
    
    /**
     * 目标类型：VEHICLE/COMMENT/POST/DEALER等
     */
    @TableField("target_type")
    private String targetType;
    
    /**
     * 目标ID
     */
    @TableField("target_id")
    private String targetId;
    
    /**
     * 发送时间
     */
    @TableField("send_time")
    private LocalDateTime sendTime;
    
    /**
     * 读取状态：0-未读, 1-已读
     */
    @TableField("read_status")
    private Integer readStatus;
    
    /**
     * 过期时间
     */
    @TableField("expire_time")
    private LocalDateTime expireTime;
    
    /**
     * 优先级（默认3）
     */
    @TableField("priority")
    private Integer priority;
    
    /**
     * 动作类型：URL/ACTIVITY/VEHICLE/NONE等
     */
    @TableField("action_type")
    private String actionType;
    
    /**
     * 动作值，如URL链接
     */
    @TableField("action_value")
    private String actionValue;
} 