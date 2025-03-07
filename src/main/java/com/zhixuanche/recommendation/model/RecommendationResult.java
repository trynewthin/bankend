package com.zhixuanche.recommendation.model;

import lombok.Data;
import java.util.List;

/**
 * 推荐结果封装
 */
@Data
public class RecommendationResult {
    private List<CarRecommendationDTO> personalRecommendations;
    private List<CarRecommendationDTO> hotRecommendations;
    private List<CarRecommendationDTO> newRecommendations;
    
    public static RecommendationResult empty() {
        RecommendationResult result = new RecommendationResult();
        result.setPersonalRecommendations(List.of());
        result.setHotRecommendations(List.of());
        result.setNewRecommendations(List.of());
        return result;
    }
} 