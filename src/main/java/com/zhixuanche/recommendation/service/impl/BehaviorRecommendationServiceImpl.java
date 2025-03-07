package com.zhixuanche.recommendation.service.impl;

import com.zhixuanche.recommendation.service.BehaviorRecommendationService;
import com.zhixuanche.recommendation.mapper.RecommendationMapper;
import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

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
        // 获取用户最近的搜索关键词
        List<String> recentSearchKeywords = recommendationMapper.findRecentSearchKeywords(userId, 5);
        
        if (recentSearchKeywords.isEmpty()) {
            return List.of(); // 如果没有搜索历史，返回空列表
        }
        
        // 基于搜索关键词推荐车辆
        return recommendationMapper.findCarsBySearchKeywords(recentSearchKeywords, limit);
    }

    @Override
    public List<CarRecommendationDTO> getCombinedBehaviorRecommendations(Integer userId, Integer limit) {
        // 获取浏览历史推荐和搜索历史推荐
        List<CarRecommendationDTO> browseRecommendations = getRecommendationsByBrowseHistory(userId, limit / 2);
        List<CarRecommendationDTO> searchRecommendations = getRecommendationsBySearchHistory(userId, limit / 2);
        
        // 合并两种推荐结果，去重
        Map<Integer, CarRecommendationDTO> combinedMap = new LinkedHashMap<>();
        
        // 先添加浏览历史推荐
        for (CarRecommendationDTO car : browseRecommendations) {
            combinedMap.put(car.getCarId(), car);
        }
        
        // 再添加搜索历史推荐
        for (CarRecommendationDTO car : searchRecommendations) {
            if (!combinedMap.containsKey(car.getCarId())) {
                combinedMap.put(car.getCarId(), car);
                if (combinedMap.size() >= limit) {
                    break;
                }
            }
        }
        
        return new ArrayList<>(combinedMap.values());
    }
} 