package com.zhixuanche.recommendation.service.impl;

import com.zhixuanche.recommendation.service.PreferenceRecommendationService;
import com.zhixuanche.recommendation.mapper.RecommendationMapper;
import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        // 获取用户偏好
        Map<String, Object> userPreference = recommendationMapper.getUserPreference(userId);
        if (userPreference == null) {
            return 0.0; // 用户没有设置偏好
        }
        
        // 获取车辆信息
        CarRecommendationDTO car = recommendationMapper.getCarById(carId);
        if (car == null) {
            return 0.0; // 车辆不存在
        }
        
        double score = 0.0;
        double totalWeight = 0.0;
        
        // 价格匹配度 (权重0.4)
        BigDecimal priceMin = (BigDecimal) userPreference.get("price_min");
        BigDecimal priceMax = (BigDecimal) userPreference.get("price_max");
        if (priceMin != null && priceMax != null && car.getPrice() != null) {
            if (car.getPrice().compareTo(priceMin) >= 0 && car.getPrice().compareTo(priceMax) <= 0) {
                // 价格在偏好范围内，满分
                score += 0.4;
            } else {
                // 价格不在范围内，根据距离计算分数
                BigDecimal distance = car.getPrice().compareTo(priceMin) < 0 ? 
                    priceMin.subtract(car.getPrice()) : car.getPrice().subtract(priceMax);
                BigDecimal range = priceMax.subtract(priceMin);
                if (range.compareTo(BigDecimal.ZERO) > 0) {
                    double priceScore = 0.4 * (1 - Math.min(distance.divide(range, 2, RoundingMode.HALF_UP).doubleValue(), 1.0));
                    score += priceScore;
                }
            }
            totalWeight += 0.4;
        }
        
        // 品牌匹配度 (权重0.3)
        String preferredBrands = (String) userPreference.get("preferred_brands");
        if (preferredBrands != null && car.getBrand() != null) {
            List<String> brands = Arrays.asList(preferredBrands.split(","));
            if (brands.contains(car.getBrand())) {
                score += 0.3;
            }
            totalWeight += 0.3;
        }
        
        // 类别匹配度 (权重0.3)
        String preferredCategories = (String) userPreference.get("preferred_categories");
        if (preferredCategories != null && car.getCategory() != null) {
            List<String> categories = Arrays.asList(preferredCategories.split(","));
            if (categories.contains(car.getCategory())) {
                score += 0.3;
            }
            totalWeight += 0.3;
        }
        
        // 归一化分数
        return totalWeight > 0 ? score / totalWeight : 0.0;
    }
} 