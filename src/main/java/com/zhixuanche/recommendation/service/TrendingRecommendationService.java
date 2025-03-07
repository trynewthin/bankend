package com.zhixuanche.recommendation.service;

import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import java.util.List;

/**
 * 热门趋势推荐服务接口
 */
public interface TrendingRecommendationService {
    
    /**
     * 获取热门车辆
     */
    List<CarRecommendationDTO> getHotCars(Integer limit, Integer days);
    
    /**
     * 获取新上架车辆
     */
    List<CarRecommendationDTO> getNewCars(Integer limit, Integer days);
    
    /**
     * 获取区域热门车辆
     */
    List<CarRecommendationDTO> getRegionalHotCars(String region, Integer limit);
} 