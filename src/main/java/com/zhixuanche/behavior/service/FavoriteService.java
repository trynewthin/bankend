package com.zhixuanche.behavior.service;

import com.zhixuanche.behavior.dto.FavoriteDTO;
import com.zhixuanche.common.model.PageResult;

/**
 * 收藏服务接口
 */
public interface FavoriteService {
    
    /**
     * 添加收藏
     * @return 收藏ID
     */
    Integer addFavorite(Integer userId, Integer carId);
    
    /**
     * 取消收藏
     */
    void removeFavorite(Integer userId, Integer carId);
    
    /**
     * 检查是否已收藏
     */
    boolean isFavorite(Integer userId, Integer carId);
    
    /**
     * 分页获取收藏列表
     */
    PageResult<FavoriteDTO> getFavoriteList(Integer userId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取用户收藏数量
     */
    int getFavoriteCount(Integer userId);
    
    /**
     * 获取车辆被收藏数量
     */
    int getCarFavoriteCount(Integer carId);
} 