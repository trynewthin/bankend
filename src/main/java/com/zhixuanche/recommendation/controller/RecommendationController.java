package com.zhixuanche.recommendation.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.common.response.Result;
import com.zhixuanche.recommendation.model.RecommendationResult;
import com.zhixuanche.recommendation.service.RecommendationFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 推荐控制器
 */
@Tag(name = "推荐接口", description = "提供车辆推荐相关的接口")
@RestController
@RequestMapping("/recommendations")
@Validated
public class RecommendationController {

    @Autowired
    private RecommendationFacadeService recommendationService;

    /**
     * 获取首页推荐 - 公开接口
     */
    @Operation(summary = "获取首页推荐", description = "获取首页的个性化推荐和热门推荐")
    @ApiResponse(responseCode = "200", description = "成功获取推荐列表")
    @GetMapping("/home")
    @SaIgnore  // 标记为不需要认证
    public Result getHomeRecommendations(
            @Parameter(description = "推荐数量限制", example = "10")
            @RequestParam(required = false, defaultValue = "10") 
            @Min(1) @Max(50) Integer limit) {
        RecommendationResult result = recommendationService.getHomePageRecommendations(limit);
        return Result.success(result);
    }

    /**
     * 获取基于行为的推荐 - 需要登录
     */
    @Operation(summary = "获取行为推荐", description = "根据用户浏览历史推荐相似车辆")
    @ApiResponse(responseCode = "200", description = "成功获取推荐列表")
    @ApiResponse(responseCode = "401", description = "未登录")
    @GetMapping("/behavior")
    public Result getBehaviorRecommendations(
            @Parameter(description = "推荐数量限制", example = "10")
            @RequestParam(required = false, defaultValue = "10") 
            @Min(1) @Max(50) Integer limit) {
        Integer userId = StpUtil.getLoginIdAsInt();
        return Result.success(recommendationService.getBehaviorRecommendations(userId, limit));
    }

    /**
     * 获取基于偏好的推荐 - 需要登录
     */
    @Operation(summary = "获取偏好推荐", description = "根据用户设置的购车偏好推荐车辆")
    @ApiResponse(responseCode = "200", description = "成功获取推荐列表")
    @ApiResponse(responseCode = "401", description = "未登录")
    @GetMapping("/preference")
    public Result getPreferenceRecommendations(
            @Parameter(description = "推荐数量限制", example = "10")
            @RequestParam(required = false, defaultValue = "10") 
            @Min(1) @Max(50) Integer limit) {
        Integer userId = StpUtil.getLoginIdAsInt();
        return Result.success(recommendationService.getPreferenceRecommendations(userId, limit));
    }

    /**
     * 获取热门推荐 - 公开接口
     */
    @Operation(summary = "获取热门推荐", description = "获取系统内热门车辆推荐")
    @ApiResponse(responseCode = "200", description = "成功获取推荐列表")
    @GetMapping("/hot")
    @SaIgnore  // 标记为不需要认证
    public Result getHotRecommendations(
            @Parameter(description = "车辆类别", example = "SUV")
            @RequestParam(required = false) String category,
            @Parameter(description = "推荐数量限制", example = "10")
            @RequestParam(required = false, defaultValue = "10") 
            @Min(1) @Max(50) Integer limit,
            @Parameter(description = "统计天数", example = "7")
            @RequestParam(required = false, defaultValue = "7") 
            @Min(1) @Max(30) Integer days) {
        return Result.success(recommendationService.getHotRecommendations(limit, days));
    }

    /**
     * 获取新车推荐 - 公开接口
     */
    @Operation(summary = "获取新车推荐", description = "获取最新上架的车辆推荐")
    @ApiResponse(responseCode = "200", description = "成功获取推荐列表")
    @GetMapping("/new")
    @SaIgnore  // 标记为不需要认证
    public Result getNewCarRecommendations(
            @Parameter(description = "车辆类别", example = "轿车")
            @RequestParam(required = false) String category,
            @Parameter(description = "推荐数量限制", example = "10")
            @RequestParam(required = false, defaultValue = "10") 
            @Min(1) @Max(50) Integer limit,
            @Parameter(description = "统计天数", example = "30")
            @RequestParam(required = false, defaultValue = "30") 
            @Min(1) @Max(90) Integer days) {
        return Result.success(recommendationService.getNewCarRecommendations(limit, days));
    }
} 