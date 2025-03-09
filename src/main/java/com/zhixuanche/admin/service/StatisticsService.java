package com.zhixuanche.admin.service;

import java.util.Map;

/**
 * 数据统计服务接口
 */
public interface StatisticsService {
    
    /**
     * 获取用户统计数据
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @param userType 用户类型
     * @param groupBy 分组方式
     * @return 统计数据
     */
    Map<String, Object> getUserStatistics(String startDate, String endDate, String userType, String groupBy);
    
    /**
     * 获取内容统计数据
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @param contentType 内容类型
     * @param groupBy 分组方式
     * @return 统计数据
     */
    Map<String, Object> getContentStatistics(String startDate, String endDate, String contentType, String groupBy);
    
    /**
     * 获取系统统计数据
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    Map<String, Object> getSystemStatistics(String startDate, String endDate);
} 