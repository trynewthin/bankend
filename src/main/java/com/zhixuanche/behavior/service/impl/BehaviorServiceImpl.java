package com.zhixuanche.behavior.service.impl;

import com.zhixuanche.behavior.constant.BehaviorType;
import com.zhixuanche.behavior.dto.BrowseHistoryDTO;
import com.zhixuanche.behavior.dto.SearchHistoryDTO;
import com.zhixuanche.behavior.entity.UserBehavior;
import com.zhixuanche.behavior.mapper.UserBehaviorMapper;
import com.zhixuanche.behavior.service.BehaviorService;
import com.zhixuanche.common.model.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 用户行为服务实现类
 */
@Service
public class BehaviorServiceImpl implements BehaviorService {
    
    private static final Logger log = LoggerFactory.getLogger(BehaviorServiceImpl.class);
    
    @Autowired
    private UserBehaviorMapper behaviorMapper;
    
    @Override
    @Transactional
    public void recordBrowse(Integer userId, Integer carId, Integer duration) {
        log.info("记录浏览行为: userId={}, carId={}, duration={}", userId, carId, duration);
        
        UserBehavior behavior = behaviorMapper.findByUserIdAndCarIdAndBehaviorType(
            userId, carId, BehaviorType.BROWSE);
            
        if (behavior == null) {
            behavior = new UserBehavior();
            behavior.setUserId(userId);
            behavior.setCarId(carId);
            behavior.setBehaviorType(BehaviorType.BROWSE);
            behavior.setBehaviorTime(new Date());
            behavior.setDuration(duration);
            behaviorMapper.insert(behavior);
            log.info("新增浏览记录: behaviorId={}", behavior.getBehaviorId());
        } else {
            behavior.setBehaviorTime(new Date());
            behavior.setDuration(behavior.getDuration() + duration);
            behaviorMapper.update(behavior);
            log.info("更新浏览记录: behaviorId={}", behavior.getBehaviorId());
        }
    }
    
    @Override
    @Transactional
    public void recordSearch(Integer userId, String keywords) {
        log.info("记录搜索行为: userId={}, keywords={}", userId, keywords);
        
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setBehaviorType(BehaviorType.SEARCH);
        behavior.setBehaviorTime(new Date());
        behavior.setSearchKeywords(keywords);
        behavior.setCarId(1);
        behaviorMapper.insert(behavior);
        log.info("新增搜索记录: behaviorId={}", behavior.getBehaviorId());
    }
    
    @Override
    @Transactional
    public void recordConsult(Integer userId, Integer carId) {
        log.info("记录咨询行为: userId={}, carId={}", userId, carId);
        
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setCarId(carId);
        behavior.setBehaviorType(BehaviorType.CONSULT);
        behavior.setBehaviorTime(new Date());
        behaviorMapper.insert(behavior);
        log.info("新增咨询记录: behaviorId={}", behavior.getBehaviorId());
    }
    
    @Override
    public PageResult<BrowseHistoryDTO> getBrowseHistory(Integer userId, Integer pageNum, Integer pageSize) {
        log.info("获取浏览历史: userId={}, pageNum={}, pageSize={}", userId, pageNum, pageSize);
        
        int offset = (pageNum - 1) * pageSize;
        List<Map<String, Object>> records = behaviorMapper.getBrowseHistoryWithCarInfo(
            userId, BehaviorType.BROWSE, offset, pageSize);
            
        int total = behaviorMapper.countByUserIdAndBehaviorType(userId, BehaviorType.BROWSE);
        
        List<BrowseHistoryDTO> dtoList = new ArrayList<>();
        for (Map<String, Object> record : records) {
            try {
                BrowseHistoryDTO dto = new BrowseHistoryDTO();
                dto.setCarId((Integer) record.get("car_id"));
                dto.setBrand((String) record.get("brand"));
                dto.setModel((String) record.get("model"));
                
                // 处理价格字段的类型转换
                Object priceObj = record.get("price");
                if (priceObj != null) {
                    if (priceObj instanceof Double) {
                        dto.setPrice((Double) priceObj);
                    } else {
                        dto.setPrice(Double.parseDouble(priceObj.toString()));
                    }
                }
                
                // 修复日期类型转换问题
                Object browseTimeObj = record.get("behavior_time");
                if (browseTimeObj instanceof LocalDateTime) {
                    // 将LocalDateTime转换为Date
                    LocalDateTime ldt = (LocalDateTime) browseTimeObj;
                    Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                    dto.setBrowseTime(date);
                } else if (browseTimeObj instanceof Date) {
                    dto.setBrowseTime((Date) browseTimeObj);
                } else if (browseTimeObj != null) {
                    log.warn("浏览时间字段类型未知: {}", browseTimeObj.getClass().getName());
                }
                
                dto.setDuration((Integer) record.get("duration"));
                dto.setThumbnailUrl((String) record.get("thumbnail_url"));
                dtoList.add(dto);
            } catch (Exception e) {
                log.error("转换浏览记录时出错: {}", e.getMessage(), e);
            }
        }
        
        return new PageResult<>(total, pageNum, pageSize, dtoList);
    }
    
    @Override
    public PageResult<SearchHistoryDTO> getSearchHistory(Integer userId, Integer pageNum, Integer pageSize) {
        log.info("获取搜索历史: userId={}, pageNum={}, pageSize={}", userId, pageNum, pageSize);
        
        int offset = (pageNum - 1) * pageSize;
        List<UserBehavior> records = behaviorMapper.findByUserIdAndBehaviorType(
            userId, BehaviorType.SEARCH, offset, pageSize);
            
        int total = behaviorMapper.countByUserIdAndBehaviorType(userId, BehaviorType.SEARCH);
        
        List<SearchHistoryDTO> dtoList = new ArrayList<>();
        for (UserBehavior record : records) {
            SearchHistoryDTO dto = new SearchHistoryDTO();
            dto.setBehaviorId(record.getBehaviorId());
            dto.setSearchKeywords(record.getSearchKeywords());
            
            // 处理日期转换问题
            Object searchTimeObj = record.getBehaviorTime();
            if (searchTimeObj instanceof LocalDateTime) {
                // 将LocalDateTime转换为Date
                LocalDateTime ldt = (LocalDateTime) searchTimeObj;
                Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                dto.setSearchTime(date);
            } else if (searchTimeObj instanceof Date) {
                dto.setSearchTime((Date) searchTimeObj);
            } else if (searchTimeObj != null) {
                log.warn("搜索时间字段类型未知: {}", searchTimeObj.getClass().getName());
            }
            
            dtoList.add(dto);
        }
        
        return new PageResult<>(total, pageNum, pageSize, dtoList);
    }
    
    @Override
    @Transactional
    public void clearBrowseHistory(Integer userId) {
        log.info("清空浏览历史: userId={}", userId);
        
        // 获取所有浏览记录
        List<UserBehavior> records = behaviorMapper.findByUserIdAndBehaviorType(
            userId, BehaviorType.BROWSE, 0, Integer.MAX_VALUE);
        
        // 逐条删除
        for (UserBehavior record : records) {
            behaviorMapper.delete(record.getBehaviorId());
        }
        
        log.info("清空浏览历史完成，共删除{}条记录", records.size());
    }
    
    @Override
    @Transactional
    public void clearSearchHistory(Integer userId) {
        log.info("清空搜索历史: userId={}", userId);
        
        // 获取所有搜索记录
        List<UserBehavior> records = behaviorMapper.findByUserIdAndBehaviorType(
            userId, BehaviorType.SEARCH, 0, Integer.MAX_VALUE);
        
        // 逐条删除
        for (UserBehavior record : records) {
            behaviorMapper.delete(record.getBehaviorId());
        }
        
        log.info("清空搜索历史完成，共删除{}条记录", records.size());
    }
    
    @Override
    public List<Map<String, Object>> getTopBrowsedBrands(Integer userId, Integer limit) {
        return behaviorMapper.getTopBrowsedBrands(userId, BehaviorType.BROWSE, limit);
    }
    
    @Override
    public List<Map<String, Object>> getTopSearchedKeywords(Integer userId, Integer limit) {
        return behaviorMapper.getTopSearchedKeywords(userId, BehaviorType.SEARCH, limit);
    }
    
    @Override
    public Map<String, Object> analyzeUserInterests(Integer userId) {
        log.info("分析用户兴趣: userId={}", userId);
        
        Map<String, Object> interests = new HashMap<>();
        
        // 1. 获取最常浏览的品牌
        interests.put("topBrands", getTopBrowsedBrands(userId, 5));
        
        // 2. 获取最常搜索的关键词
        interests.put("topKeywords", getTopSearchedKeywords(userId, 10));
        
        // 3. 统计行为数量
        interests.put("browseCount", 
            behaviorMapper.countByUserIdAndBehaviorType(userId, BehaviorType.BROWSE));
        interests.put("searchCount", 
            behaviorMapper.countByUserIdAndBehaviorType(userId, BehaviorType.SEARCH));
        interests.put("consultCount", 
            behaviorMapper.countByUserIdAndBehaviorType(userId, BehaviorType.CONSULT));
        
        return interests;
    }
    
    @Override
    @Transactional
    public boolean deleteBrowseRecord(Integer userId, Integer carId) {
        log.info("删除浏览记录: userId={}, carId={}", userId, carId);
        
        UserBehavior behavior = behaviorMapper.findByUserIdAndCarIdAndBehaviorType(
            userId, carId, BehaviorType.BROWSE);
        if (behavior == null) {
            log.info("未找到要删除的浏览记录");
            return false;
        }
        
        boolean result = behaviorMapper.delete(behavior.getBehaviorId()) > 0;
        log.info("删除浏览记录结果: {}", result);
        return result;
    }
    
    @Override
    @Transactional
    public boolean deleteSearchRecord(Integer userId, Integer searchId) {
        log.info("删除搜索记录: userId={}, searchId={}", userId, searchId);
        
        UserBehavior behavior = behaviorMapper.findById(searchId);
        if (behavior == null || !behavior.getUserId().equals(userId) || 
            behavior.getBehaviorType() != BehaviorType.SEARCH) {
            log.info("未找到要删除的搜索记录或记录不属于当前用户");
            return false;
        }
        
        boolean result = behaviorMapper.delete(searchId) > 0;
        log.info("删除搜索记录结果: {}", result);
        return result;
    }
} 