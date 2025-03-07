package com.zhixuanche.recommendation.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.recommendation.service.RecommendationFacadeService;
import com.zhixuanche.recommendation.service.BehaviorRecommendationService;
import com.zhixuanche.recommendation.service.PreferenceRecommendationService;
import com.zhixuanche.recommendation.service.TrendingRecommendationService;
import com.zhixuanche.recommendation.model.RecommendationResult;
import com.zhixuanche.recommendation.model.CarRecommendationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 推荐门面服务实现类
 */
@Service
public class RecommendationFacadeServiceImpl implements RecommendationFacadeService {

    @Autowired
    private BehaviorRecommendationService behaviorRecommendationService;

    @Autowired
    private PreferenceRecommendationService preferenceRecommendationService;

    @Autowired
    private TrendingRecommendationService trendingRecommendationService;

    @Override
    public RecommendationResult getHomePageRecommendations(Integer limit) {
        RecommendationResult result = new RecommendationResult();
        
        // 获取热门推荐
        result.setHotRecommendations(trendingRecommendationService.getHotCars(limit, 7));
        
        // 获取新车推荐
        result.setNewRecommendations(trendingRecommendationService.getNewCars(limit, 30));
        
        // 如果用户已登录，获取个性化推荐
        try {
            Integer userId = StpUtil.getLoginIdAsInt();
            if (userId != null) {
                result.setPersonalRecommendations(
                    behaviorRecommendationService.getCombinedBehaviorRecommendations(userId, limit)
                );
            }
        } catch (Exception e) {
            // 用户未登录，返回空的个性化推荐列表
            result.setPersonalRecommendations(List.of());
        }
        
        return result;
    }

    @Override
    public List<CarRecommendationDTO> getBehaviorRecommendations(Integer userId, Integer limit) {
        return behaviorRecommendationService.getCombinedBehaviorRecommendations(userId, limit);
    }

    @Override
    public List<CarRecommendationDTO> getPreferenceRecommendations(Integer userId, Integer limit) {
        return preferenceRecommendationService.getRecommendationsByPreference(userId, limit);
    }

    @Override
    public List<CarRecommendationDTO> getHotRecommendations(Integer limit, Integer days) {
        return trendingRecommendationService.getHotCars(limit, days);
    }

    @Override
    public List<CarRecommendationDTO> getNewCarRecommendations(Integer limit, Integer days) {
        return trendingRecommendationService.getNewCars(limit, days);
    }
} 