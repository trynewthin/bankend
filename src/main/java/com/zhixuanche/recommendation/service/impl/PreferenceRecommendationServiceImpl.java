package com.zhixuanche.recommendation.service.impl;

import com.zhixuanche.recommendation.service.PreferenceRecommendationService;
import com.zhixuanche.recommendation.mapper.RecommendationMapper;
import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenceRecommendationServiceImpl implements PreferenceRecommendationService {

    @Autowired
    private RecommendationMapper recommendationMapper;

    @Override
    public List<CarRecommendationDTO> getRecommendationsByPreference(Integer userId, Integer limit) {
        return recommendationMapper.findCarsByPreference(userId, limit);
    }

    @Override
    public double calculatePreferenceMatchScore(Integer userId, Integer carId) {
        // TODO: 实现偏好匹配分数计算
        return 0.0;
    }
} 