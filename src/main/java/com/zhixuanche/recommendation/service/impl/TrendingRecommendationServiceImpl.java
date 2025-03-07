package com.zhixuanche.recommendation.service.impl;

import com.zhixuanche.recommendation.service.TrendingRecommendationService;
import com.zhixuanche.recommendation.mapper.RecommendationMapper;
import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TrendingRecommendationServiceImpl implements TrendingRecommendationService {

    @Autowired
    private RecommendationMapper recommendationMapper;

    @Override
    public List<CarRecommendationDTO> getHotCars(Integer limit, Integer days) {
        String startTime = LocalDateTime.now().minusDays(days)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return recommendationMapper.findHotCars(startTime, limit);
    }

    @Override
    public List<CarRecommendationDTO> getNewCars(Integer limit, Integer days) {
        String startTime = LocalDateTime.now().minusDays(days)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return recommendationMapper.findNewCars(startTime, limit);
    }

    @Override
    public List<CarRecommendationDTO> getRegionalHotCars(String region, Integer limit) {
        // TODO: 实现区域热门车辆推荐
        return List.of();
    }
} 