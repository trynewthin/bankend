package com.zhixuanche.admin.service.impl;

import com.zhixuanche.admin.service.StatisticsService;
import com.zhixuanche.user.entity.Dealer;
import com.zhixuanche.user.entity.enums.UserType;
import com.zhixuanche.user.mapper.UserMapper;
import com.zhixuanche.user.mapper.DealerMapper;
import com.zhixuanche.car.mapper.CarMapper;
import com.zhixuanche.message.mapper.AppointmentMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 数据统计服务实现
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private DealerMapper dealerMapper;
    
    @Autowired
    private CarMapper carMapper;
    
    @Autowired
    private AppointmentMapper appointmentMapper;
    
    @Override
    public Map<String, Object> getUserStatistics(String startDate, String endDate, String userType, String groupBy) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查日期参数
            if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
                log.warn("日期参数为空，使用默认日期范围");
                startDate = LocalDateTime.now().minusMonths(1).format(DateTimeFormatter.ISO_DATE);
                endDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
            }
            
            log.info("开始获取用户统计数据，时间范围：{} 至 {}", startDate, endDate);
            
            // 获取用户总数
            int totalUserCount = userMapper.countTotalUsers();
            log.info("用户总数：{}", totalUserCount);
            result.put("total_users", totalUserCount);
            
            // 获取用户类型分布
            Map<String, Integer> userTypeStats = userMapper.countUsersByType();
            log.info("用户类型分布：{}", userTypeStats);
            result.put("customer_count", userTypeStats.getOrDefault(UserType.NORMAL_USER.name(), 0));
            result.put("dealer_count", userTypeStats.getOrDefault(UserType.DEALER.name(), 0));
            result.put("admin_count", userTypeStats.getOrDefault(UserType.ADMIN.name(), 0));
            
            // 如果指定了用户类型，只返回该类型的统计
            if (StringUtils.isNotBlank(userType) && !"ALL".equalsIgnoreCase(userType)) {
                filterStatsByUserType(result, userType);
            }
            
            // 获取新增用户数
            int newUsers = userMapper.countNewUsers(startDate, endDate);
            log.info("新增用户数：{}", newUsers);
            result.put("new_users", newUsers);
            
            // 获取活跃用户数（通过预约记录统计）
            Map<String, Object> userActivityStats = userMapper.getUserActivityStatistics(startDate, endDate);
            log.info("用户活跃度统计：{}", userActivityStats);
            if (userActivityStats != null && userActivityStats.containsKey("active_users")) {
                int activeUsers = ((Number) userActivityStats.get("active_users")).intValue();
                result.put("active_users", activeUsers);
            } else {
                log.warn("用户活跃度统计数据为空");
                result.put("active_users", 0);
            }
            
            // 计算用户增长率
            double userGrowth = calculateUserGrowth(startDate, endDate);
            log.info("用户增长率：{}%", userGrowth);
            result.put("user_growth", userGrowth);
            
            // 获取用户趋势数据
            List<Map<String, Object>> userTrend = getUserTrendData(startDate, endDate, groupBy);
            log.info("用户趋势数据：{}", userTrend);
            result.put("user_trend", userTrend);
            
            // 获取经销商地区分布
            Map<String, Integer> regionDistribution = new HashMap<>();
            List<Dealer> dealers = dealerMapper.findAllDealers();
            log.info("经销商总数：{}", dealers.size());
            for (Dealer dealer : dealers) {
                String region = dealer.getAddress();
                if (region != null && !region.isEmpty()) {
                    // 从地址中提取地区信息（假设地址格式为：省份 城市 详细地址）
                    String[] parts = region.split(" ");
                    if (parts.length >= 2) {
                        String province = parts[0];
                        regionDistribution.merge(province, 1, Integer::sum);
                    }
                }
            }
            log.info("地区分布：{}", regionDistribution);
            result.put("user_region_distribution", regionDistribution);
            
        } catch (Exception e) {
            log.error("获取用户统计数据失败", e);
            // 发生异常时返回空数据
            result.put("total_users", 0);
            result.put("new_users", 0);
            result.put("active_users", 0);
            result.put("customer_count", 0);
            result.put("dealer_count", 0);
            result.put("user_growth", 0.0);
            result.put("user_trend", Collections.emptyList());
            result.put("user_region_distribution", Collections.emptyMap());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getContentStatistics(String startDate, String endDate, String contentType, String groupBy) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取车辆总数
            Map<String, Object> params = new HashMap<>();
            int totalCars = carMapper.selectCount(params);
            result.put("total_cars", totalCars);
            
            // 获取车辆状态分布
            Map<String, Integer> carStatusDistribution = carMapper.countCarsByStatus();
            result.put("car_status_distribution", carStatusDistribution);
            
            // 获取车辆品牌分布
            List<Map<String, Object>> brandDistribution = carMapper.countByBrand();
            result.put("car_brand_distribution", brandDistribution);
            
            // 获取新增车辆数
            int newCars = carMapper.countNewCars(startDate, endDate);
            result.put("new_cars", newCars);
            
            // 获取车辆趋势数据
            List<Map<String, Object>> carTrend = getCarTrendData(startDate, endDate, groupBy);
            result.put("car_trend", carTrend);
            
            // 获取车辆类别分布
            List<Map<String, Object>> categoryDistribution = carMapper.countByCategory();
            result.put("car_category_distribution", categoryDistribution);
            
            // 获取价格区间分布
            List<Map<String, Object>> priceRangeDistribution = new ArrayList<>();
            Map<String, Object> priceRanges = new HashMap<>();
            priceRanges.put("range", "0-10万");
            priceRanges.put("count", carMapper.selectByPriceRange(0, 100000, 0, Integer.MAX_VALUE).size());
            priceRangeDistribution.add(priceRanges);
            
            priceRanges = new HashMap<>();
            priceRanges.put("range", "10-20万");
            priceRanges.put("count", carMapper.selectByPriceRange(100000, 200000, 0, Integer.MAX_VALUE).size());
            priceRangeDistribution.add(priceRanges);
            
            priceRanges = new HashMap<>();
            priceRanges.put("range", "20-30万");
            priceRanges.put("count", carMapper.selectByPriceRange(200000, 300000, 0, Integer.MAX_VALUE).size());
            priceRangeDistribution.add(priceRanges);
            
            priceRanges = new HashMap<>();
            priceRanges.put("range", "30万以上");
            priceRanges.put("count", carMapper.selectByPriceRange(300000, Double.MAX_VALUE, 0, Integer.MAX_VALUE).size());
            priceRangeDistribution.add(priceRanges);
            
            result.put("car_price_distribution", priceRangeDistribution);
            
        } catch (Exception e) {
            // 发生异常时返回空数据
            result.put("total_cars", 0);
            result.put("car_status_distribution", Collections.emptyMap());
            result.put("car_brand_distribution", Collections.emptyList());
            result.put("new_cars", 0);
            result.put("car_trend", Collections.emptyList());
            result.put("car_category_distribution", Collections.emptyList());
            result.put("car_price_distribution", Collections.emptyList());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getSystemStatistics(String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取预约统计
            Map<String, Object> appointmentStats = new HashMap<>();
            appointmentStats.put("total_appointments", appointmentMapper.countTotalAppointments());
            appointmentStats.put("status_distribution", appointmentMapper.countAppointmentsByStatus());
            appointmentStats.put("new_appointments", appointmentMapper.countNewAppointments(startDate, endDate));
            appointmentStats.put("completion_stats", appointmentMapper.getAppointmentCompletionStatistics(startDate, endDate));
            result.put("appointment_stats", appointmentStats);
            
            // 获取经销商统计
            Map<String, Object> dealerStats = new HashMap<>();
            dealerStats.put("total_dealers", dealerMapper.countAllDealers());
            dealerStats.put("active_dealers", dealerMapper.findApprovedDealers().size());
            result.put("dealer_stats", dealerStats);
            
            // 获取车辆统计
            Map<String, Object> carStats = new HashMap<>();
            carStats.put("total_cars", carMapper.countTotalCars());
            carStats.put("status_distribution", carMapper.countCarsByStatus());
            carStats.put("brand_distribution", carMapper.countCarsByBrand());
            carStats.put("inventory_stats", carMapper.getCarInventoryStatistics(startDate, endDate));
            result.put("car_stats", carStats);
            
            // 获取用户活跃度统计
            Map<String, Object> userActivityStats = userMapper.getUserActivityStatistics(startDate, endDate);
            result.put("user_activity_stats", userActivityStats);
            
        } catch (Exception e) {
            // 发生异常时返回空数据
            result.put("appointment_stats", Collections.emptyMap());
            result.put("dealer_stats", Collections.emptyMap());
            result.put("car_stats", Collections.emptyMap());
            result.put("user_activity_stats", Collections.emptyMap());
        }
        
        return result;
    }
    
    /**
     * 计算用户增长率
     */
    private double calculateUserGrowth(String startDate, String endDate) {
        try {
            int oldUserCount = userMapper.countUsersBeforeDate(startDate);
            int newUserCount = userMapper.countNewUsers(startDate, endDate);
            
            if (oldUserCount == 0) {
                return newUserCount > 0 ? 100.0 : 0.0;
            }
            
            return ((double) newUserCount / oldUserCount) * 100;
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    /**
     * 获取用户趋势数据
     */
    private List<Map<String, Object>> getUserTrendData(String startDate, String endDate, String groupBy) {
        try {
            return userMapper.getUserTrendData(startDate, endDate, groupBy);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
    
    /**
     * 获取车辆趋势数据
     */
    private List<Map<String, Object>> getCarTrendData(String startDate, String endDate, String groupBy) {
        try {
            return carMapper.getCarTrendData(startDate, endDate, groupBy);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
    
    /**
     * 根据用户类型筛选统计结果
     */
    private void filterStatsByUserType(Map<String, Object> stats, String userType) {
        if ("CUSTOMER".equalsIgnoreCase(userType)) {
            stats.put("total_users", stats.get("customer_count"));
        } else if ("DEALER".equalsIgnoreCase(userType)) {
            stats.put("total_users", stats.get("dealer_count"));
        } else if ("ADMIN".equalsIgnoreCase(userType)) {
            stats.put("total_users", stats.get("admin_count"));
        }
    }
} 