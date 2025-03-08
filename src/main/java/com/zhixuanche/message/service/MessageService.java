package com.zhixuanche.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.message.dto.MessageDTO;
import com.zhixuanche.message.dto.MessageQueryParam;
import com.zhixuanche.message.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * 消息服务接口
 */
public interface MessageService extends IService<Message> {
    
    /**
     * 发送系统消息
     * @param senderUserId 发送者用户ID（通常是系统用户ID）
     * @param receiverUserIds 接收者用户ID列表
     * @param title 消息标题
     * @param content 消息内容
     * @param messageType 消息类型
     * @param priority 优先级
     * @param actionType 动作类型
     * @param actionValue 动作值
     * @return 发送成功的消息数量
     */
    int sendSystemMessage(Integer senderUserId, List<Integer> receiverUserIds, 
                         String title, String content, String messageType, 
                         Integer priority, String actionType, String actionValue);
    
    /**
     * 发送用户间交互消息
     * @param senderUserId 发送者用户ID
     * @param receiverUserId 接收者用户ID
     * @param carId 关联车辆ID
     * @param content 消息内容
     * @param interactionType 交互类型
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 消息ID
     */
    Integer sendInteractionMessage(Integer senderUserId, Integer receiverUserId, 
                                 Integer carId, String content, String interactionType, 
                                 String targetType, String targetId);
    
    /**
     * 获取系统消息
     * @param queryParam 查询参数
     * @return 分页消息列表
     */
    PageResult<MessageDTO> getSystemMessages(MessageQueryParam queryParam);
    
    /**
     * 获取交互消息
     * @param queryParam 查询参数
     * @return 分页消息列表
     */
    PageResult<MessageDTO> getInteractionMessages(MessageQueryParam queryParam);
    
    /**
     * 获取两个用户之间的消息
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @param carId 关联车辆ID (可选)
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页消息列表
     */
    PageResult<MessageDTO> getMessagesBetweenUsers(Integer userId1, Integer userId2, 
                                                Integer carId, Integer pageNum, Integer pageSize);
    
    /**
     * 标记消息为已读
     * @param messageId 消息ID
     * @param userId 用户ID (接收者)
     * @return 是否成功
     */
    boolean markMessageAsRead(Integer messageId, Integer userId);
    
    /**
     * 批量标记消息为已读
     * @param messageIds 消息ID列表
     * @param userId 用户ID (接收者)
     * @return 更新的消息数量
     */
    int batchMarkAsRead(List<Integer> messageIds, Integer userId);
    
    /**
     * 获取用户的未读消息统计
     * @param userId 用户ID
     * @return 未读消息统计Map
     */
    Map<String, Integer> getUnreadMessageStats(Integer userId);
    
    /**
     * 删除消息
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteMessage(Integer messageId, Integer userId);
    
    /**
     * 批量删除消息
     * @param messageIds 消息ID列表
     * @param userId 用户ID
     * @return 删除的消息数量
     */
    int batchDeleteMessages(List<Integer> messageIds, Integer userId);
} 