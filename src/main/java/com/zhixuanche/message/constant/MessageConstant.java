package com.zhixuanche.message.constant;

/**
 * 消息模块常量
 */
public class MessageConstant {
    
    /**
     * 消息类型
     */
    public static class MessageType {
        // 系统消息
        public static final String SYSTEM = "SYSTEM";
        // 营销消息
        public static final String MARKETING = "MARKETING";
        // 通知消息
        public static final String NOTICE = "NOTICE";
        // 活动消息
        public static final String ACTIVITY = "ACTIVITY";
        // 促销消息
        public static final String PROMOTION = "PROMOTION";
        // 更新消息
        public static final String UPDATE = "UPDATE";
        // 反馈消息
        public static final String FEEDBACK = "FEEDBACK";
    }
    
    /**
     * 交互类型
     */
    public static class InteractionType {
        // 评论
        public static final String COMMENT = "COMMENT";
        // 回复
        public static final String REPLY = "REPLY";
        // 问题
        public static final String QUESTION = "QUESTION";
        // 咨询
        public static final String CONSULTATION = "CONSULTATION";
    }
    
    /**
     * 目标类型
     */
    public static class TargetType {
        // 车辆
        public static final String VEHICLE = "VEHICLE";
        // 评论
        public static final String COMMENT = "COMMENT";
        // 帖子
        public static final String POST = "POST";
        // 经销商
        public static final String DEALER = "DEALER";
    }
    
    /**
     * 动作类型
     */
    public static class ActionType {
        // URL链接
        public static final String URL = "URL";
        // 活动
        public static final String ACTIVITY = "ACTIVITY";
        // 车辆
        public static final String VEHICLE = "VEHICLE";
        // 无动作
        public static final String NONE = "NONE";
    }
    
    /**
     * 消息读取状态
     */
    public static class ReadStatus {
        // 未读
        public static final int UNREAD = 0;
        // 已读
        public static final int READ = 1;
    }
    
    /**
     * 预约类型
     */
    public static class AppointmentType {
        // 看车
        public static final String VIEW = "看车";
        // 试驾
        public static final String TEST_DRIVE = "试驾";
    }
    
    /**
     * 预约状态
     */
    public static class AppointmentStatus {
        // 待确认
        public static final String PENDING = "待确认";
        // 已确认
        public static final String CONFIRMED = "已确认";
        // 已完成
        public static final String COMPLETED = "已完成";
        // 已取消
        public static final String CANCELLED = "已取消";
    }
} 