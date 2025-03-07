package com.zhixuanche.recommendation.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 推荐反馈实体
 */
@Data
public class RecommendationFeedback {
    private Integer feedbackId;
    private Integer userId;
    private Integer carId;
    private String recommendationType;
    private Boolean isClicked;
    private Boolean isViewed;
    private LocalDateTime createTime;
} 