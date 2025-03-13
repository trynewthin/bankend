package com.zhixuanche.admin.service.impl;

import com.zhixuanche.admin.dto.DealerAuditDTO;
import com.zhixuanche.admin.service.AuditLogService;
import com.zhixuanche.admin.service.DealerAdminService;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.message.service.NotificationService;
import com.zhixuanche.user.entity.Dealer;
import com.zhixuanche.user.entity.User;
import com.zhixuanche.user.mapper.DealerMapper;
import com.zhixuanche.user.mapper.UserMapper;
import cn.dev33.satoken.stp.StpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 经销商管理服务实现
 */
@Service
public class DealerAdminServiceImpl implements DealerAdminService {

    @Autowired
    private DealerMapper dealerMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private AuditLogService auditLogService;
    
    @Override
    public PageResult<Dealer> getDealerList(String keyword, String verifyStatus, String region, Integer page, Integer size) {
        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        
        // 添加关键词查询条件
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        
        // 添加认证状态查询条件
        if (StringUtils.isNotBlank(verifyStatus)) {
            if ("PENDING".equalsIgnoreCase(verifyStatus)) {
                params.put("status", 0);  // 待审核状态
            } else if ("APPROVED".equalsIgnoreCase(verifyStatus)) {
                params.put("status", 1);  // 已通过状态
            } else if ("REJECTED".equalsIgnoreCase(verifyStatus)) {
                params.put("status", 2);  // 已拒绝状态
            }
        }
        
        // 添加地区查询条件
        if (StringUtils.isNotBlank(region)) {
            params.put("region", region);
        }
        
        // 添加分页参数
        params.put("offset", (page - 1) * size);
        params.put("limit", size);
        
        // 查询总记录数和经销商列表
        List<Dealer> dealers = dealerMapper.findDealersByParams(params);
        int total = dealerMapper.countDealersByParams(params);
        
        // 计算总页数
        int pages = (total + size - 1) / size;
        
        // 返回分页结果
        PageResult<Dealer> pageResult = new PageResult<>(total, page, size, dealers);
        pageResult.setPages(pages);
        
        return pageResult;
    }

    @Override
    public Dealer getDealerDetail(Integer dealerId) {
        return dealerMapper.findById(dealerId);
    }

    @Override
    @Transactional
    public boolean auditDealer(Integer dealerId, DealerAuditDTO auditDTO) {
        Dealer dealer = dealerMapper.findById(dealerId);
        if (dealer == null) {
            return false;
        }
        
        // 获取当前操作管理员ID
        Integer adminId = StpUtil.getLoginIdAsInt();
        
        // 更新经销商状态
        int result = dealerMapper.updateStatus(dealerId, auditDTO.getStatus());
        
        // 如果审核通过，同时更新用户状态为正常
        if (auditDTO.getStatus() == 1) {
            User user = userMapper.findById(dealer.getUserId());
            if (user != null) {
                // 使用专用的updateStatus方法代替更新整个User对象
                userMapper.updateStatus(user.getUserId(), 1); // 设置为正常状态
            }
        }
        
        if (result > 0) {
            // 发送通知给经销商
            sendAuditNotificationToDealer(dealer.getUserId(), dealer, auditDTO);
            
            // 不再记录审核日志
            // 移除: logDealerAuditOperation(dealerId, adminId, auditDTO);
        }
        
        return result > 0;
    }
    
    /**
     * 发送经销商审核通知
     */
    private void sendAuditNotificationToDealer(Integer userId, Dealer dealer, DealerAuditDTO auditDTO) {
        if (userId == null) {
            return;
        }
        
        String title = "经销商资质审核通知";
        String content;
        String noticeType;
        
        if (auditDTO.getStatus() == 1) {
            // 审核通过
            content = String.format("恭喜！您的经销商资质 [%s] 已审核通过，您现在可以发布车辆信息了。", 
                    dealer.getDealerName());
            noticeType = "DEALER_APPROVED";
        } else {
            // 审核拒绝
            content = String.format("您的经销商资质 [%s] 审核未通过。原因：%s", 
                    dealer.getDealerName(), 
                    StringUtils.isNotBlank(auditDTO.getRemarks()) ? auditDTO.getRemarks() : "资质不符合要求");
            noticeType = "DEALER_REJECTED";
        }
        
        notificationService.sendSystemNotification(userId, title, content, noticeType);
    }
    
    @Override
    @Transactional
    public Map<String, Object> deleteDealer(Integer dealerId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取当前管理员ID
        Integer adminId = StpUtil.getLoginIdAsInt();
        
        // 检查经销商是否存在
        Dealer dealer = dealerMapper.findById(dealerId);
        if (dealer == null) {
            result.put("success", false);
            result.put("message", "经销商不存在");
            return result;
        }
        
        try {
            // 检查经销商是否有关联的车辆
            int carCount = dealerMapper.countCarsByDealerId(dealerId);
            if (carCount > 0) {
                result.put("success", false);
                result.put("message", "该经销商有关联的车辆信息，无法删除");
                return result;
            }
            
            // 获取用户ID，用于发送通知
            Integer userId = dealer.getUserId();
            
            // 删除经销商信息
            int deleteResult = dealerMapper.deleteById(dealerId);
            
            if (deleteResult > 0) {
                result.put("success", true);
                result.put("message", "经销商删除成功");
                
                // 发送通知给用户
                if (userId != null) {
                    sendDealerDeletionNotification(userId, dealer);
                }
            } else {
                result.put("success", false);
                result.put("message", "经销商删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除经销商过程中发生错误：" + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 发送经销商删除通知给用户
     */
    private void sendDealerDeletionNotification(Integer userId, Dealer dealer) {
        String title = "经销商信息删除通知";
        String content = String.format("您的经销商信息（%s）已被管理员删除", dealer.getDealerName());
        String noticeType = "DEALER_DELETED";
        
        notificationService.sendSystemNotification(userId, title, content, noticeType);
    }
} 