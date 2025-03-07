package com.zhixuanche.recommendation.mapper;

import com.zhixuanche.recommendation.model.RecommendationFeedback;
import org.apache.ibatis.annotations.*;

/**
 * 推荐反馈数据访问接口
 */
@Mapper
public interface RecommendationFeedbackMapper {

    /**
     * 保存推荐反馈
     */
    @Insert("""
        INSERT INTO RecommendationFeedbacks(
            user_id, car_id, recommendation_type, 
            is_clicked, is_viewed, create_time
        ) VALUES (
            #{userId}, #{carId}, #{recommendationType},
            #{isClicked}, #{isViewed}, #{createTime}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "feedbackId")
    void saveRecommendationFeedback(RecommendationFeedback feedback);

    /**
     * 获取用户对特定车辆的反馈
     */
    @Select("""
        SELECT * FROM RecommendationFeedbacks
        WHERE user_id = #{userId} 
        AND car_id = #{carId}
        ORDER BY create_time DESC
        LIMIT 1
    """)
    RecommendationFeedback findLatestFeedback(
        @Param("userId") Integer userId,
        @Param("carId") Integer carId
    );
} 