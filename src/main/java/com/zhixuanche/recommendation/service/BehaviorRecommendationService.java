package com.zhixuanche.recommendation.service;

import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import java.util.List;

/**
 * 基于行为的推荐服务接口
 */
public interface BehaviorRecommendationService {
    
    /**
     * 根据浏览历史推荐
     */
    List<CarRecommendationDTO> getRecommendationsByBrowseHistory(Integer userId, Integer limit);
    
    /**
     * 根据搜索历史推荐
     */
    List<CarRecommendationDTO> getRecommendationsBySearchHistory(Integer userId, Integer limit);
    
    /**
     * 获取综合行为推荐
     */
    List<CarRecommendationDTO> getCombinedBehaviorRecommendations(Integer userId, Integer limit);
} 