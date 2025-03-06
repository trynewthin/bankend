package com.zhixuanche.behavior.service;

import com.zhixuanche.behavior.dto.BrowseHistoryDTO;
import com.zhixuanche.behavior.dto.SearchHistoryDTO;
import com.zhixuanche.common.model.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 用户行为服务接口
 */
public interface BehaviorService {
    
    /**
     * 记录浏览行为
     */
    void recordBrowse(Integer userId, Integer carId, Integer duration);
    
    /**
     * 记录搜索行为
     */
    void recordSearch(Integer userId, String keywords);
    
    /**
     * 记录咨询行为
     */
    void recordConsult(Integer userId, Integer carId);
    
    /**
     * 分页获取浏览历史
     */
    PageResult<BrowseHistoryDTO> getBrowseHistory(Integer userId, Integer pageNum, Integer pageSize);
    
    /**
     * 分页获取搜索历史
     */
    PageResult<SearchHistoryDTO> getSearchHistory(Integer userId, Integer pageNum, Integer pageSize);
    
    /**
     * 删除单条浏览记录
     * @return 是否删除成功
     */
    boolean deleteBrowseRecord(Integer userId, Integer carId);
    
    /**
     * 删除单条搜索记录
     * @return 是否删除成功
     */
    boolean deleteSearchRecord(Integer userId, Integer searchId);
    
    /**
     * 清除浏览历史
     */
    void clearBrowseHistory(Integer userId);
    
    /**
     * 清除搜索历史
     */
    void clearSearchHistory(Integer userId);
    
    /**
     * 获取用户最常浏览的品牌
     */
    List<Map<String, Object>> getTopBrowsedBrands(Integer userId, Integer limit);
    
    /**
     * 获取用户最常搜索的关键词
     */
    List<Map<String, Object>> getTopSearchedKeywords(Integer userId, Integer limit);
    
    /**
     * 分析用户兴趣特征
     */
    Map<String, Object> analyzeUserInterests(Integer userId);
} 