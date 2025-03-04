package com.zhixuanche.user.service;

import com.zhixuanche.user.entity.Dealer;
import java.util.List;

/**
 * 经销商服务接口
 */
public interface DealerService {
    
    /**
     * 提交经销商信息
     */
    Dealer submitDealerInfo(Dealer dealer);
    
    /**
     * 更新经销商信息
     */
    boolean updateDealerInfo(Dealer dealer);
    
    /**
     * 获取经销商信息
     */
    Dealer getDealerById(Integer dealerId);
    
    /**
     * 通过用户ID获取经销商信息
     */
    Dealer getDealerByUserId(Integer userId);
    
    /**
     * 更新经销商状态
     */
    boolean updateDealerStatus(Integer dealerId, Integer status);
    
    /**
     * 获取待审核经销商列表
     */
    List<Dealer> getPendingDealers();
    
    /**
     * 获取已审核经销商列表
     */
    List<Dealer> getApprovedDealers();
} 