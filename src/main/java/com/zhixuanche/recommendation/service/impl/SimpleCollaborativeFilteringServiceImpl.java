package com.zhixuanche.recommendation.service.impl;

import com.zhixuanche.recommendation.service.CollaborativeFilteringService;
import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import com.zhixuanche.recommendation.mapper.RecommendationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 简单协同过滤实现
 * 当前使用基础的相似度计算,后续可以替换为更复杂的协同过滤算法
 */
@Service
public class SimpleCollaborativeFilteringServiceImpl implements CollaborativeFilteringService {

    @Autowired
    private RecommendationMapper recommendationMapper;
    
    @Override
    public List<CarRecommendationDTO> getUserBasedRecommendations(Integer userId, Integer limit) {
        // 暂时使用简单的基于品牌和类别的相似推荐
        return recommendationMapper.findSimilarCars(userId, limit);
    }
    
    @Override
    public List<CarRecommendationDTO> getItemBasedRecommendations(Integer userId, Integer limit) {
        // 暂时使用相同实现
        return recommendationMapper.findSimilarCars(userId, limit);
    }
} 