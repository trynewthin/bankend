package com.zhixuanche.admin.controller;

import com.zhixuanche.admin.service.StatisticsService;
import com.zhixuanche.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据统计控制器
 */
@Tag(name = "数据统计", description = "系统数据统计接口")
@RestController
@RequestMapping("/admin/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取用户统计数据
     */
    @Operation(summary = "获取用户统计", description = "获取用户相关统计数据")
    @GetMapping("/users")
    public Result getUserStatistics(
            @Parameter(description = "起始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate,
            @Parameter(description = "用户类型") @RequestParam(required = false, defaultValue = "ALL") String userType,
            @Parameter(description = "分组方式") @RequestParam(required = false, defaultValue = "DAY") String groupBy) {
        return Result.success(statisticsService.getUserStatistics(startDate, endDate, userType, groupBy));
    }

    /**
     * 获取内容统计数据
     */
    @Operation(summary = "获取内容统计", description = "获取内容相关统计数据")
    @GetMapping("/content")
    public Result getContentStatistics(
            @Parameter(description = "起始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate,
            @Parameter(description = "内容类型") @RequestParam(required = false, defaultValue = "ALL") String contentType,
            @Parameter(description = "分组方式") @RequestParam(required = false, defaultValue = "DAY") String groupBy) {
        return Result.success(statisticsService.getContentStatistics(startDate, endDate, contentType, groupBy));
    }

    /**
     * 获取系统统计数据
     */
    @Operation(summary = "获取系统统计", description = "获取系统相关统计数据")
    @GetMapping("/system")
    public Result getSystemStatistics(
            @Parameter(description = "起始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate) {
        return Result.success(statisticsService.getSystemStatistics(startDate, endDate));
    }
} 