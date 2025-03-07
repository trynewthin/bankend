package com.zhixuanche.recommendation.service;

import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import java.util.List;

/**
 * 协同过滤推荐服务接口
 * 预留接口以便后续实现更复杂的协同过滤算法
 */
public interface CollaborativeFilteringService {
    
    /**
     * 基于用户的协同过滤推荐
     */
    List<CarRecommendationDTO> getUserBasedRecommendations(Integer userId, Integer limit);
    
    /**
     * 基于物品的协同过滤推荐
     */
    List<CarRecommendationDTO> getItemBasedRecommendations(Integer userId, Integer limit);
} 