package com.zhixuanche.recommendation.mapper;

import org.apache.ibatis.annotations.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 推荐统计数据访问接口
 */
@Mapper
public interface RecommendationStatsMapper {

    /**
     * 获取推荐效果统计数据
     */
    @Select("""
        SELECT 
            DATE(behavior_time) as date,
            COUNT(DISTINCT user_id) as user_count,
            COUNT(*) as view_count
        FROM UserBehaviors
        WHERE behavior_type = 'browse'
        AND behavior_time BETWEEN #{startDate} AND #{endDate}
        GROUP BY DATE(behavior_time)
    """)
    List<Map<String, Object>> getRecommendationStats(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
} 