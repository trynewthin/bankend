package com.zhixuanche.user.mapper;

import com.zhixuanche.user.entity.UserPreference;
import org.apache.ibatis.annotations.*;

/**
 * 用户偏好Mapper接口
 */
@Mapper
public interface UserPreferenceMapper {
    
    /**
     * 通过用户ID查询偏好设置
     */
    @Select("SELECT * FROM UserPreferences WHERE user_id = #{userId}")
    UserPreference findByUserId(Integer userId);
    
    /**
     * 插入新的偏好设置
     */
    @Insert("INSERT INTO UserPreferences(user_id, price_min, price_max, preferred_brands, " +
            "preferred_categories, other_preferences, update_time) " +
            "VALUES(#{userId}, #{priceMin}, #{priceMax}, #{preferredBrands}, " +
            "#{preferredCategories}, #{otherPreferences}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "preferenceId")
    int insert(UserPreference preference);
    
    /**
     * 更新偏好设置
     */
    @Update("UPDATE UserPreferences SET price_min = #{priceMin}, price_max = #{priceMax}, " +
            "preferred_brands = #{preferredBrands}, preferred_categories = #{preferredCategories}, " +
            "other_preferences = #{otherPreferences}, update_time = #{updateTime} " +
            "WHERE user_id = #{userId}")
    int update(UserPreference preference);
    
    /**
     * 删除用户偏好设置
     */
    @Delete("DELETE FROM UserPreferences WHERE user_id = #{userId}")
    int deleteByUserId(Integer userId);
} 