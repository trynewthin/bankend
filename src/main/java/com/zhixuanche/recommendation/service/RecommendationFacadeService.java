package com.zhixuanche.recommendation.service;

import com.zhixuanche.recommendation.model.RecommendationResult;
import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import java.util.List;

/**
 * 推荐门面服务接口
 */
public interface RecommendationFacadeService {
    
    /**
     * 获取首页推荐
     */
    RecommendationResult getHomePageRecommendations(Integer limit);
    
    /**
     * 获取基于行为的推荐
     */
    List<CarRecommendationDTO> getBehaviorRecommendations(Integer userId, Integer limit);
    
    /**
     * 获取基于偏好的推荐
     */
    List<CarRecommendationDTO> getPreferenceRecommendations(Integer userId, Integer limit);
    
    /**
     * 获取热门推荐
     */
    List<CarRecommendationDTO> getHotRecommendations(Integer limit, Integer days);
    
    /**
     * 获取新车推荐
     */
    List<CarRecommendationDTO> getNewCarRecommendations(Integer limit, Integer days);
} 