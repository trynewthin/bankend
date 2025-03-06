package com.zhixuanche.behavior.mapper;

import com.zhixuanche.behavior.entity.Favorite;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 收藏数据访问接口
 */
@Mapper
public interface FavoriteMapper {
    
    /**
     * 根据ID查询收藏记录
     */
    @Select("SELECT * FROM Favorites WHERE favorite_id = #{favoriteId}")
    Favorite findById(Integer favoriteId);
    
    /**
     * 根据用户ID分页查询收藏记录
     */
    @Select("SELECT * FROM Favorites WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<Favorite> findByUserId(@Param("userId") Integer userId, 
                               @Param("offset") Integer offset,
                               @Param("limit") Integer limit);
    
    /**
     * 根据用户ID和车辆ID查询收藏记录
     */
    @Select("SELECT * FROM Favorites WHERE user_id = #{userId} AND car_id = #{carId}")
    Favorite findByUserIdAndCarId(@Param("userId") Integer userId, @Param("carId") Integer carId);
    
    /**
     * 检查是否已收藏
     */
    @Select("SELECT COUNT(*) FROM Favorites WHERE user_id = #{userId} AND car_id = #{carId}")
    int existsByUserIdAndCarId(@Param("userId") Integer userId, @Param("carId") Integer carId);
    
    /**
     * 插入收藏记录
     */
    @Insert("INSERT INTO Favorites(user_id, car_id, create_time) VALUES(#{userId}, #{carId}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "favoriteId")
    int insert(Favorite favorite);
    
    /**
     * 删除收藏记录
     */
    @Delete("DELETE FROM Favorites WHERE user_id = #{userId} AND car_id = #{carId}")
    int deleteByUserIdAndCarId(@Param("userId") Integer userId, @Param("carId") Integer carId);
    
    /**
     * 统计用户收藏数量
     */
    @Select("SELECT COUNT(*) FROM Favorites WHERE user_id = #{userId}")
    int countByUserId(Integer userId);
    
    /**
     * 统计车辆被收藏数量
     */
    @Select("SELECT COUNT(*) FROM Favorites WHERE car_id = #{carId}")
    int countByCarId(Integer carId);
    
    /**
     * 获取带车辆信息的收藏列表
     */
    @Select("SELECT f.*, c.brand, c.model, c.price, ci.image_url as thumbnail_url " +
            "FROM Favorites f " +
            "JOIN Cars c ON f.car_id = c.car_id " +
            "LEFT JOIN CarImages ci ON c.car_id = ci.car_id AND ci.image_type = 'thumbnail' " +
            "WHERE f.user_id = #{userId} " +
            "ORDER BY f.create_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<Map<String, Object>> getFavoriteListWithCarInfo(@Param("userId") Integer userId,
                                                        @Param("offset") Integer offset,
                                                        @Param("limit") Integer limit);
} 