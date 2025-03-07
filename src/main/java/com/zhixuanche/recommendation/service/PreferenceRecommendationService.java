package com.zhixuanche.recommendation.service;

import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import java.util.List;

/**
 * 基于偏好的推荐服务接口
 */
public interface PreferenceRecommendationService {
    
    /**
     * 根据用户偏好推荐
     */
    List<CarRecommendationDTO> getRecommendationsByPreference(Integer userId, Integer limit);
    
    /**
     * 计算偏好匹配分数
     */
    double calculatePreferenceMatchScore(Integer userId, Integer carId);
} 