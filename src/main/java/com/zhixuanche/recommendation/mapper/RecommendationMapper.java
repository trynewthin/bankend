package com.zhixuanche.recommendation.mapper;

import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 推荐数据访问接口
 */
@Mapper
public interface RecommendationMapper {

    /**
     * 获取热门车辆
     */
    @Select("""
        SELECT c.*, 
            COUNT(DISTINCT ub.user_id) as view_count,
            COUNT(DISTINCT f.user_id) as favorite_count,
            ci.image_url as main_image
        FROM Cars c
        LEFT JOIN UserBehaviors ub ON c.car_id = ub.car_id 
            AND ub.behavior_type = '浏览'
            AND ub.behavior_time >= #{startTime}
        LEFT JOIN Favorites f ON c.car_id = f.car_id
            AND f.create_time >= #{startTime}
        LEFT JOIN CarImages ci ON c.car_id = ci.car_id 
            AND ci.image_type = '缩略图'
        WHERE c.status = 1
        GROUP BY c.car_id, ci.image_url
        ORDER BY (COUNT(DISTINCT ub.user_id) * 0.6 + COUNT(DISTINCT f.user_id) * 0.4) DESC
        LIMIT #{limit}
    """)
    List<CarRecommendationDTO> findHotCars(@Param("startTime") String startTime, 
                                          @Param("limit") Integer limit);

    /**
     * 获取新上架车辆
     */
    @Select("""
        SELECT c.*, 
            COUNT(DISTINCT ub.user_id) as view_count,
            COUNT(DISTINCT f.user_id) as favorite_count,
            ci.image_url as main_image
        FROM Cars c
        LEFT JOIN UserBehaviors ub ON c.car_id = ub.car_id 
            AND ub.behavior_type = '浏览'
        LEFT JOIN Favorites f ON c.car_id = f.car_id
        LEFT JOIN CarImages ci ON c.car_id = ci.car_id 
            AND ci.image_type = '缩略图'
        WHERE c.status = 1
        AND c.create_time >= #{startTime}
        GROUP BY c.car_id, ci.image_url
        ORDER BY c.create_time DESC
        LIMIT #{limit}
    """)
    List<CarRecommendationDTO> findNewCars(@Param("startTime") String startTime,
                                          @Param("limit") Integer limit);

    /**
     * 根据用户浏览历史获取相似车辆
     */
    @Select("""
        SELECT c.*, 
            COUNT(DISTINCT ub.user_id) as view_count,
            COUNT(DISTINCT f.user_id) as favorite_count,
            ci.image_url as main_image
        FROM Cars c
        LEFT JOIN UserBehaviors ub ON c.car_id = ub.car_id 
            AND ub.behavior_type = '浏览'
        LEFT JOIN Favorites f ON c.car_id = f.car_id
        LEFT JOIN CarImages ci ON c.car_id = ci.car_id 
            AND ci.image_type = '缩略图'
        WHERE c.status = 1
        AND (
            c.brand IN (
                SELECT DISTINCT c2.brand 
                FROM Cars c2
                INNER JOIN UserBehaviors ub2 ON c2.car_id = ub2.car_id
                WHERE ub2.user_id = #{userId} 
                AND ub2.behavior_type = '浏览'
            )
            OR c.category IN (
                SELECT DISTINCT c2.category
                FROM Cars c2
                INNER JOIN UserBehaviors ub2 ON c2.car_id = ub2.car_id
                WHERE ub2.user_id = #{userId}
                AND ub2.behavior_type = '浏览'
            )
        )
        AND c.car_id NOT IN (
            SELECT car_id 
            FROM UserBehaviors 
            WHERE user_id = #{userId}
        )
        GROUP BY c.car_id, ci.image_url
        ORDER BY c.create_time DESC
        LIMIT #{limit}
    """)
    List<CarRecommendationDTO> findSimilarCars(@Param("userId") Integer userId,
                                              @Param("limit") Integer limit);

    /**
     * 根据用户偏好获取推荐车辆
     */
    @Select("""
        SELECT c.*, 
            COUNT(DISTINCT ub.user_id) as view_count,
            COUNT(DISTINCT f.user_id) as favorite_count,
            ci.image_url as main_image
        FROM Cars c
        LEFT JOIN UserBehaviors ub ON c.car_id = ub.car_id 
            AND ub.behavior_type = '浏览'
        LEFT JOIN Favorites f ON c.car_id = f.car_id
        LEFT JOIN CarImages ci ON c.car_id = ci.car_id 
            AND ci.image_type = '缩略图'
        INNER JOIN UserPreferences up ON up.user_id = #{userId}
        WHERE c.status = 1
        AND c.price BETWEEN up.price_min AND up.price_max
        AND (
            FIND_IN_SET(c.brand, up.preferred_brands)
            OR FIND_IN_SET(c.category, up.preferred_categories)
        )
        GROUP BY c.car_id, ci.image_url
        ORDER BY c.create_time DESC
        LIMIT #{limit}
    """)
    List<CarRecommendationDTO> findCarsByPreference(@Param("userId") Integer userId,
                                                   @Param("limit") Integer limit);

    /**
     * 预留: 获取用户相似度数据
     * 后续实现协同过滤时使用
     */
    @Select("""
        SELECT 
            ub1.user_id as user_id1,
            ub2.user_id as user_id2,
            COUNT(DISTINCT ub1.car_id) as common_views
        FROM UserBehaviors ub1
        JOIN UserBehaviors ub2 ON ub1.car_id = ub2.car_id
        WHERE ub1.user_id < ub2.user_id
        AND ub1.behavior_type = 'browse'
        AND ub2.behavior_type = 'browse'
        GROUP BY ub1.user_id, ub2.user_id
        HAVING common_views >= #{minCommonViews}
    """)
    List<Map<String, Object>> getUserSimilarityData(@Param("minCommonViews") Integer minCommonViews);

    /**
     * 预留: 获取车辆相似度数据
     * 后续实现协同过滤时使用
     */
    @Select("""
        SELECT 
            ub1.car_id as car_id1,
            ub2.car_id as car_id2,
            COUNT(DISTINCT ub1.user_id) as co_viewed_count
        FROM UserBehaviors ub1
        JOIN UserBehaviors ub2 ON ub1.user_id = ub2.user_id
        WHERE ub1.car_id < ub2.car_id
        AND ub1.behavior_type = 'browse'
        AND ub2.behavior_type = 'browse'
        GROUP BY ub1.car_id, ub2.car_id
        HAVING co_viewed_count >= #{minCoViews}
    """)
    List<Map<String, Object>> getCarSimilarityData(@Param("minCoViews") Integer minCoViews);

    /**
     * 获取区域热门车辆
     */
    @Select("""
        SELECT c.*, 
            COUNT(DISTINCT ub.user_id) as view_count,
            COUNT(DISTINCT f.user_id) as favorite_count
        FROM Cars c
        LEFT JOIN UserBehaviors ub ON c.car_id = ub.car_id 
            AND ub.behavior_type = 'browse'
            AND ub.behavior_time >= #{startTime}
        LEFT JOIN Favorites f ON c.car_id = f.car_id
            AND f.create_time >= #{startTime}
        INNER JOIN Dealers d ON c.dealer_id = d.dealer_id
        WHERE c.status = 1
        AND d.region = #{region}
        GROUP BY c.car_id
        ORDER BY (COUNT(DISTINCT ub.user_id) * 0.6 + COUNT(DISTINCT f.user_id) * 0.4) DESC
        LIMIT #{limit}
    """)
    List<CarRecommendationDTO> findRegionalHotCars(
        @Param("region") String region,
        @Param("startTime") String startTime, 
        @Param("limit") Integer limit);

    /**
     * 获取用户最近的搜索关键词
     */
    @Select("""
        SELECT DISTINCT search_keywords, MAX(behavior_time) as latest_time
        FROM UserBehaviors
        WHERE user_id = #{userId}
        AND behavior_type = '搜索'
        AND search_keywords IS NOT NULL
        GROUP BY search_keywords
        ORDER BY latest_time DESC
        LIMIT #{limit}
    """)
    List<String> findRecentSearchKeywords(@Param("userId") Integer userId, @Param("limit") Integer limit);

    /**
     * 根据搜索关键词获取相关车辆
     */
    @Select("""
        <script>
        SELECT c.*, 
            COUNT(DISTINCT ub.user_id) as view_count,
            COUNT(DISTINCT f.user_id) as favorite_count
        FROM Cars c
        LEFT JOIN UserBehaviors ub ON c.car_id = ub.car_id 
            AND ub.behavior_type = '浏览'
        LEFT JOIN Favorites f ON c.car_id = f.car_id
        WHERE c.status = 1
        AND (
            <foreach item="keyword" collection="keywords" separator=" OR ">
            c.brand LIKE CONCAT('%', #{keyword}, '%')
            OR c.model LIKE CONCAT('%', #{keyword}, '%')
            OR c.category LIKE CONCAT('%', #{keyword}, '%')
            </foreach>
        )
        GROUP BY c.car_id
        ORDER BY c.create_time DESC
        LIMIT #{limit}
        </script>
    """)
    List<CarRecommendationDTO> findCarsBySearchKeywords(@Param("keywords") List<String> keywords, @Param("limit") Integer limit);

    /**
     * 获取用户偏好
     */
    @Select("""
        SELECT * FROM UserPreferences
        WHERE user_id = #{userId}
    """)
    Map<String, Object> getUserPreference(@Param("userId") Integer userId);

    /**
     * 根据ID获取车辆
     */
    @Select("""
        SELECT * FROM Cars
        WHERE car_id = #{carId}
    """)
    CarRecommendationDTO getCarById(@Param("carId") Integer carId);
} 