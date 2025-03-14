package com.zhixuanche.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhixuanche.common.exception.BusinessException;
import com.zhixuanche.common.exception.ErrorCode;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.message.constant.MessageConstant;
import com.zhixuanche.message.dto.MessageDTO;
import com.zhixuanche.message.dto.MessageQueryParam;
import com.zhixuanche.message.entity.Message;
import com.zhixuanche.message.mapper.MessageMapper;
import com.zhixuanche.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import cn.dev33.satoken.stp.StpUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息服务实现类
 */
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sendSystemMessage(Integer senderUserId, List<Integer> receiverUserIds, 
                              String title, String content, String messageType, 
                              Integer priority, String actionType, String actionValue) {
        // 参数校验
        if (senderUserId == null || CollectionUtils.isEmpty(receiverUserIds) || !StringUtils.hasText(content)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "发送系统消息参数不完整");
        }
        
        int successCount = 0;
        List<Message> messages = new ArrayList<>();
        
        // 构建消息对象
        for (Integer receiverId : receiverUserIds) {
            Message message = new Message();
            message.setSenderId(senderUserId);
            message.setReceiverId(receiverId);
            message.setTitle(title);
            message.setContent(content);
            message.setMessageType(messageType);
            message.setSendTime(LocalDateTime.now());
            message.setReadStatus(MessageConstant.ReadStatus.UNREAD);
            message.setPriority(priority != null ? priority : 3);
            message.setActionType(actionType);
            message.setActionValue(actionValue);
            
            messages.add(message);
        }
        
        // 批量保存消息
        try {
            if (saveBatch(messages)) {
                successCount = messages.size();
            }
        } catch (Exception e) {
            log.error("发送系统消息失败", e);
            throw new BusinessException(ErrorCode.MESSAGE_SEND_FAILED.getCode(), "发送系统消息失败: " + e.getMessage());
        }
        
        return successCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer sendInteractionMessage(Integer senderUserId, Integer receiverUserId, 
                                      Integer carId, String content, String interactionType, 
                                      String targetType, String targetId) {
        // 参数校验
        if (senderUserId == null || receiverUserId == null || !StringUtils.hasText(content)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "发送交互消息参数不完整");
        }
        
        // 构建消息对象
        Message message = new Message();
        message.setSenderId(senderUserId);
        message.setReceiverId(receiverUserId);
        message.setCarId(carId);
        message.setContent(content);
        message.setInteractionType(interactionType);
        message.setTargetType(targetType);
        message.setTargetId(targetId);
        message.setSendTime(LocalDateTime.now());
        message.setReadStatus(MessageConstant.ReadStatus.UNREAD);
        
        // 保存消息
        try {
            save(message);
            return message.getMessageId();
        } catch (Exception e) {
            log.error("发送交互消息失败", e);
            throw new BusinessException(ErrorCode.MESSAGE_SEND_FAILED.getCode(), "发送交互消息失败: " + e.getMessage());
        }
    }

    @Override
    public PageResult<MessageDTO> getSystemMessages(MessageQueryParam queryParam) {
        if (queryParam == null || queryParam.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "查询参数不完整");
        }
        
        // 构建分页对象
        Page<Message> page = new Page<>(queryParam.getPage(), queryParam.getSize());
        
        // 设置查询条件
        Integer readStatus = queryParam.getRead() != null ? 
                (queryParam.getRead() ? MessageConstant.ReadStatus.READ : MessageConstant.ReadStatus.UNREAD) : null;
        
        // 执行查询
        IPage<Message> messagePage = messageMapper.getSystemMessages(
                page, queryParam.getUserId(), readStatus, 
                queryParam.getMessageType(), queryParam.getStartDate(), queryParam.getEndDate());
        
        // 转换为DTO
        List<MessageDTO> dtoList = convertToDTO(messagePage.getRecords());
        
        // 构建分页结果
        return new PageResult<>(
                messagePage.getTotal(), 
                queryParam.getPage(), 
                queryParam.getSize(), 
                dtoList
        );
    }

    @Override
    public PageResult<MessageDTO> getInteractionMessages(MessageQueryParam queryParam) {
        if (queryParam == null || queryParam.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "查询参数不完整");
        }
        
        // 构建分页对象
        Page<Message> page = new Page<>(queryParam.getPage(), queryParam.getSize());
        
        // 设置查询条件
        Integer readStatus = queryParam.getRead() != null ? 
                (queryParam.getRead() ? MessageConstant.ReadStatus.READ : MessageConstant.ReadStatus.UNREAD) : null;
        
        // 执行查询
        IPage<Message> messagePage = messageMapper.getInteractionMessages(
                page, queryParam.getUserId(), queryParam.getInteractionType(), 
                queryParam.getTargetType(), readStatus, 
                queryParam.getStartDate(), queryParam.getEndDate());
        
        // 转换为DTO
        List<MessageDTO> dtoList = convertToDTO(messagePage.getRecords());
        
        // 构建分页结果
        return new PageResult<>(
                messagePage.getTotal(), 
                queryParam.getPage(), 
                queryParam.getSize(), 
                dtoList
        );
    }

    @Override
    public PageResult<MessageDTO> getMessagesBetweenUsers(Integer userId1, Integer userId2, 
                                                     Integer carId, Integer pageNum, Integer pageSize) {
        if (userId1 == null || userId2 == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "查询参数不完整");
        }
        
        // 构建分页对象
        Page<Message> page = new Page<Message>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 20);
        
        // 执行查询
        IPage<Message> messagePage = messageMapper.getMessagesBetweenUsers(page, userId1, userId2, carId);
        
        // 标记消息为已读（如果接收者是当前用户）
        List<Integer> messageIds = messagePage.getRecords().stream()
                .filter(m -> m.getReceiverId().equals(userId1) && m.getReadStatus() == MessageConstant.ReadStatus.UNREAD)
                .map(Message::getMessageId)
                .collect(Collectors.toList());
        
        if (!messageIds.isEmpty()) {
            messageMapper.batchMarkAsRead(messageIds, userId1);
        }
        
        // 转换为DTO
        List<MessageDTO> dtoList = convertToDTO(messagePage.getRecords());
        
        // 构建分页结果
        return new PageResult<MessageDTO>(
                messagePage.getTotal(), 
                pageNum != null ? pageNum : 1, 
                pageSize != null ? pageSize : 20, 
                dtoList
        );
    }

    @Override
    public boolean markMessageAsRead(Integer messageId, Integer userId) {
        if (messageId == null || userId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "参数不完整");
        }
        
        // 检查消息是否存在
        Message message = getById(messageId);
        if (message == null) {
            throw new BusinessException(ErrorCode.MESSAGE_NOT_FOUND.getCode(), "消息不存在");
        }
        
        // 检查权限
        if (!message.getReceiverId().equals(userId)) {
            throw new BusinessException(ErrorCode.MESSAGE_PERMISSION_DENIED.getCode(), "无权操作该消息");
        }
        
        // 已经是已读状态，直接返回成功
        if (message.getReadStatus() == MessageConstant.ReadStatus.READ) {
            return true;
        }
        
        // 更新为已读
        return messageMapper.markMessageAsRead(messageId, userId) > 0;
    }

    @Override
    public int batchMarkAsRead(List<Integer> messageIds, Integer userId) {
        if (CollectionUtils.isEmpty(messageIds) || userId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "参数不完整");
        }
        
        // 批量标记为已读
        return messageMapper.batchMarkAsRead(messageIds, userId);
    }

    @Override
    public Map<String, Integer> getUnreadMessageStats(Integer userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "参数不完整");
        }
        
        Map<String, Integer> result = new HashMap<>();
        
        // 获取未读消息总数
        int totalUnread = messageMapper.getTotalUnreadCount(userId);
        result.put("total", totalUnread);
        
        // 获取各类型未读消息数量
        List<Map<String, Object>> stats = messageMapper.getUnreadMessageCount(userId);
        
        // 初始化结果
        result.put("system", 0);
        result.put("interaction", 0);
        
        Map<String, Integer> bySystemType = new HashMap<>();
        Map<String, Integer> byInteractionType = new HashMap<>();
        
        // 处理统计结果
        for (Map<String, Object> stat : stats) {
            String type = (String) stat.get("type");
            Integer count = ((Number) stat.get("count")).intValue();
            
            if (type == null) continue;
            
            if (type.equals(MessageConstant.MessageType.SYSTEM) || 
                type.equals(MessageConstant.MessageType.NOTICE) || 
                type.equals(MessageConstant.MessageType.MARKETING) || 
                type.equals(MessageConstant.MessageType.ACTIVITY)) {
                
                // 系统类消息
                result.put("system", result.getOrDefault("system", 0) + count);
                bySystemType.put(type, count);
                
            } else if (type.equals(MessageConstant.InteractionType.COMMENT) || 
                       type.equals(MessageConstant.InteractionType.REPLY) || 
                       type.equals(MessageConstant.InteractionType.QUESTION) || 
                       type.equals(MessageConstant.InteractionType.CONSULTATION)) {
                
                // 交互类消息
                result.put("interaction", result.getOrDefault("interaction", 0) + count);
                byInteractionType.put(type, count);
            }
        }
        
        result.put("bySystemType", bySystemType.size());
        result.put("byInteractionType", byInteractionType.size());
        
        return result;
    }

    @Override
    public boolean deleteMessage(Integer messageId, Integer userId) {
        if (messageId == null || userId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "参数不完整");
        }
        
        // 检查消息是否存在
        Message message = getById(messageId);
        if (message == null) {
            throw new BusinessException(ErrorCode.MESSAGE_NOT_FOUND.getCode(), "消息不存在");
        }
        
        // 检查权限（只能删除自己收到的消息）
        if (!message.getReceiverId().equals(userId)) {
            throw new BusinessException(ErrorCode.MESSAGE_PERMISSION_DENIED.getCode(), "无权删除该消息");
        }
        
        // 删除消息
        return removeById(messageId);
    }

    @Override
    public int batchDeleteMessages(List<Integer> messageIds, Integer userId) {
        if (CollectionUtils.isEmpty(messageIds) || userId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR.getCode(), "参数不完整");
        }
        
        // 检查权限（只能批量删除自己收到的消息）
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Message::getMessageId, messageIds)
                   .eq(Message::getReceiverId, userId);
        
        return count(queryWrapper) > 0 ? baseMapper.delete(queryWrapper) : 0;
    }

    /**
     * 将Message实体列表转换为MessageDTO列表
     * @param messages 消息实体列表
     * @return DTO列表
     */
    private List<MessageDTO> convertToDTO(List<Message> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            return new ArrayList<>();
        }
        
        // 将消息转换为DTO，并设置发送者信息
        return messages.stream().map(message -> {
            MessageDTO dto = new MessageDTO();
            
            // 复制基本属性
            BeanUtils.copyProperties(message, dto);
            
            // 特殊处理一些属性
            dto.setId(message.getMessageId());
            dto.setFromUserId(message.getSenderId());
            dto.setToUserId(message.getReceiverId());
            dto.setRead(message.getReadStatus() == MessageConstant.ReadStatus.READ);
            
            // 设置发送者信息（系统用户或默认值）
            Integer senderId = message.getSenderId();
            if (senderId != null && senderId <= 4) { // 系统/营销/客服/AI助手
                switch (senderId) {
                    case 1:
                        dto.setFromUserName("系统");
                        dto.setFromUserAvatar("/images/avatars/system.png");
                        break;
                    case 2:
                        dto.setFromUserName("营销通知");
                        dto.setFromUserAvatar("/images/avatars/marketing.png");
                        break;
                    case 3:
                        dto.setFromUserName("客服中心");
                        dto.setFromUserAvatar("/images/avatars/customer_service.png");
                        break;
                    case 4:
                        dto.setFromUserName("AI助手");
                        dto.setFromUserAvatar("/images/avatars/ai_assistant.png");
                        break;
                    default:
                        dto.setFromUserName("系统通知");
                        dto.setFromUserAvatar("/images/avatars/system.png");
                }
            } else {
                // 对于普通用户消息，可以在前端根据用户ID查询显示
                // 或者后续添加批量查询用户信息的支持
                dto.setFromUserName("用户" + senderId);
                dto.setFromUserAvatar("/images/avatars/default.png");
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
} 