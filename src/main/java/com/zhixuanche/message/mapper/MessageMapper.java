package com.zhixuanche.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhixuanche.message.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 消息数据访问层
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    
    /**
     * 获取用户接收的系统消息
     * @param page 分页参数
     * @param userId 用户ID
     * @param readStatus 读取状态
     * @param messageType 消息类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分页消息列表
     */
    IPage<Message> getSystemMessages(Page<Message> page, 
                                   @Param("userId") Integer userId,
                                   @Param("readStatus") Integer readStatus,
                                   @Param("messageType") String messageType,
                                   @Param("startDate") String startDate,
                                   @Param("endDate") String endDate);
    
    /**
     * 获取用户的互动消息
     * @param page 分页参数
     * @param userId 用户ID
     * @param interactionType 互动类型
     * @param targetType 目标类型
     * @param readStatus 读取状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分页消息列表
     */
    IPage<Message> getInteractionMessages(Page<Message> page,
                                        @Param("userId") Integer userId,
                                        @Param("interactionType") String interactionType,
                                        @Param("targetType") String targetType,
                                        @Param("readStatus") Integer readStatus,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);
    
    /**
     * 获取两个用户之间的消息记录
     * @param page 分页参数
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @param carId 关联车辆ID (可选)
     * @return 分页消息列表
     */
    IPage<Message> getMessagesBetweenUsers(Page<Message> page,
                                         @Param("userId1") Integer userId1,
                                         @Param("userId2") Integer userId2,
                                         @Param("carId") Integer carId);
    
    /**
     * 将消息标记为已读
     * @param messageId 消息ID
     * @param userId 用户ID (接收者)
     * @return 更新行数
     */
    @Update("UPDATE Messages SET read_status = 1 WHERE message_id = #{messageId} AND receiver_id = #{userId}")
    int markMessageAsRead(@Param("messageId") Integer messageId, @Param("userId") Integer userId);
    
    /**
     * 批量将消息标记为已读
     * @param messageIds 消息ID列表
     * @param userId 用户ID (接收者)
     * @return 更新行数
     */
    int batchMarkAsRead(@Param("messageIds") List<Integer> messageIds, @Param("userId") Integer userId);
    
    /**
     * 获取用户未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量，按消息类型和互动类型分组
     */
    List<Map<String, Object>> getUnreadMessageCount(@Param("userId") Integer userId);
    
    /**
     * 获取用户未读消息总数
     * @param userId 用户ID
     * @return 未读消息总数
     */
    int getTotalUnreadCount(@Param("userId") Integer userId);
} 