package com.zhixuanche.recommendation.service.impl;

import com.zhixuanche.recommendation.service.BehaviorRecommendationService;
import com.zhixuanche.recommendation.mapper.RecommendationMapper;
import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BehaviorRecommendationServiceImpl implements BehaviorRecommendationService {

    @Autowired
    private RecommendationMapper recommendationMapper;

    @Override
    public List<CarRecommendationDTO> getRecommendationsByBrowseHistory(Integer userId, Integer limit) {
        return recommendationMapper.findSimilarCars(userId, limit);
    }

    @Override
    public List<CarRecommendationDTO> getRecommendationsBySearchHistory(Integer userId, Integer limit) {
        // TODO: 实现基于搜索历史的推荐
        return List.of();
    }

    @Override
    public List<CarRecommendationDTO> getCombinedBehaviorRecommendations(Integer userId, Integer limit) {
        // 目前只返回浏览历史推荐
        return getRecommendationsByBrowseHistory(userId, limit);
    }
} 