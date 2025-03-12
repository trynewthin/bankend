package com.zhixuanche.user.service.impl;

import com.zhixuanche.user.entity.Dealer;
import com.zhixuanche.user.entity.enums.DealerStatus;
import com.zhixuanche.user.mapper.DealerMapper;
import com.zhixuanche.user.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 经销商服务实现类
 */
@Service
public class DealerServiceImpl implements DealerService {

    @Autowired
    private DealerMapper dealerMapper;

    @Override
    @Transactional
    public Dealer submitDealerInfo(Dealer dealer) {
        // 检查该用户是否已经提交过经销商信息
        Dealer existingDealer = dealerMapper.findByUserId(dealer.getUserId());
        
        if (existingDealer != null) {
            // 已存在记录，执行更新操作
            dealer.setDealerId(existingDealer.getDealerId());
            // 保留原有状态
            dealer.setStatus(existingDealer.getStatus());
            dealerMapper.update(dealer);
            return dealer;
        } else {
            // 不存在记录，执行插入操作
            // 设置初始状态为待审核
            dealer.setStatus(DealerStatus.PENDING);
            dealerMapper.insert(dealer);
            return dealer;
        }
    }

    @Override
    @Transactional
    public boolean updateDealerInfo(Dealer dealer) {
        return dealerMapper.update(dealer) > 0;
    }

    @Override
    public Dealer getDealerById(Integer dealerId) {
        return dealerMapper.findById(dealerId);
    }

    @Override
    public Dealer getDealerByUserId(Integer userId) {
        return dealerMapper.findByUserId(userId);
    }

    @Override
    @Transactional
    public boolean updateDealerStatus(Integer dealerId, Integer status) {
        return dealerMapper.updateStatus(dealerId, status) > 0;
    }

    @Override
    public List<Dealer> getPendingDealers() {
        return dealerMapper.findPendingDealers();
    }

    @Override
    public List<Dealer> getApprovedDealers() {
        return dealerMapper.findApprovedDealers();
    }
} 