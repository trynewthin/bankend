package com.zhixuanche.behavior.mapper;

import com.zhixuanche.behavior.constant.BehaviorType;
import com.zhixuanche.behavior.entity.UserBehavior;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 用户行为数据访问接口
 */
@Mapper
public interface UserBehaviorMapper {
    
    /**
     * 根据ID查询行为记录
     */
    @Select("SELECT * FROM UserBehaviors WHERE behavior_id = #{behaviorId}")
    UserBehavior findById(Integer behaviorId);
    
    /**
     * 根据用户ID和行为类型分页查询行为记录
     */
    @Select("SELECT * FROM UserBehaviors WHERE user_id = #{userId} AND behavior_type = #{behaviorType} " +
            "ORDER BY behavior_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<UserBehavior> findByUserIdAndBehaviorType(@Param("userId") Integer userId, 
                                                  @Param("behaviorType") BehaviorType behaviorType,
                                                  @Param("offset") Integer offset,
                                                  @Param("limit") Integer limit);
    
    /**
     * 根据用户ID和车辆ID查询特定行为
     */
    @Select("SELECT * FROM UserBehaviors WHERE user_id = #{userId} AND car_id = #{carId} " +
            "AND behavior_type = #{behaviorType}")
    UserBehavior findByUserIdAndCarIdAndBehaviorType(@Param("userId") Integer userId,
                                                    @Param("carId") Integer carId,
                                                    @Param("behaviorType") BehaviorType behaviorType);
    
    /**
     * 插入行为记录
     */
    @Insert("INSERT INTO UserBehaviors(user_id, car_id, behavior_type, behavior_time, duration, search_keywords) " +
            "VALUES(#{userId}, #{carId}, #{behaviorType}, #{behaviorTime}, #{duration}, #{searchKeywords})")
    @Options(useGeneratedKeys = true, keyProperty = "behaviorId")
    int insert(UserBehavior behavior);
    
    /**
     * 更新行为记录
     */
    @Update("UPDATE UserBehaviors SET behavior_time = #{behaviorTime}, duration = #{duration} " +
            "WHERE behavior_id = #{behaviorId}")
    int update(UserBehavior behavior);
    
    /**
     * 删除行为记录
     */
    @Delete("DELETE FROM UserBehaviors WHERE behavior_id = #{behaviorId}")
    int delete(Integer behaviorId);
    
    /**
     * 获取用户最常浏览的品牌
     */
    @Select("SELECT c.brand, COUNT(*) as count FROM UserBehaviors u " +
            "JOIN Cars c ON u.car_id = c.car_id " +
            "WHERE u.user_id = #{userId} AND u.behavior_type = #{behaviorType} " +
            "GROUP BY c.brand ORDER BY count DESC LIMIT #{limit}")
    List<Map<String, Object>> getTopBrowsedBrands(@Param("userId") Integer userId, 
                                                 @Param("behaviorType") BehaviorType behaviorType,
                                                 @Param("limit") Integer limit);
    
    /**
     * 获取用户最常搜索的关键词
     */
    @Select("SELECT search_keywords, COUNT(*) as count FROM UserBehaviors " +
            "WHERE user_id = #{userId} AND behavior_type = #{behaviorType} AND search_keywords IS NOT NULL " +
            "GROUP BY search_keywords ORDER BY count DESC LIMIT #{limit}")
    List<Map<String, Object>> getTopSearchedKeywords(@Param("userId") Integer userId, 
                                                    @Param("behaviorType") BehaviorType behaviorType,
                                                    @Param("limit") Integer limit);
    
    /**
     * 获取带车辆信息的浏览历史
     */
    @Select("SELECT u.*, c.brand, c.model, c.price, ci.image_url as thumbnail_url " +
            "FROM UserBehaviors u " +
            "JOIN Cars c ON u.car_id = c.car_id " +
            "LEFT JOIN CarImages ci ON c.car_id = ci.car_id AND ci.image_type = '缩略图' " +
            "WHERE u.user_id = #{userId} AND u.behavior_type = #{behaviorType} " +
            "ORDER BY u.behavior_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<Map<String, Object>> getBrowseHistoryWithCarInfo(@Param("userId") Integer userId,
                                                         @Param("behaviorType") BehaviorType behaviorType,
                                                         @Param("offset") Integer offset,
                                                         @Param("limit") Integer limit);
    
    /**
     * 统计用户行为数量
     */
    @Select("SELECT COUNT(*) FROM UserBehaviors WHERE user_id = #{userId} AND behavior_type = #{behaviorType}")
    int countByUserIdAndBehaviorType(@Param("userId") Integer userId, @Param("behaviorType") BehaviorType behaviorType);
} 