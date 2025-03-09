package com.zhixuanche.admin.service;

import com.zhixuanche.admin.dto.DealerAuditDTO;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.user.entity.Dealer;

import java.util.Map;

/**
 * 经销商管理服务接口
 */
public interface DealerAdminService {
    
    /**
     * 获取经销商列表
     * @param keyword 关键词
     * @param verifyStatus 认证状态
     * @param region 地区
     * @param page 页码
     * @param size 每页记录数
     * @return 经销商列表和分页信息
     */
    PageResult<Dealer> getDealerList(String keyword, String verifyStatus, String region, Integer page, Integer size);
    
    /**
     * 获取经销商详情
     * @param dealerId 经销商ID
     * @return 经销商详情
     */
    Dealer getDealerDetail(Integer dealerId);
    
    /**
     * 审核经销商
     * @param dealerId 经销商ID
     * @param auditDTO 审核信息
     * @return 是否成功
     */
    boolean auditDealer(Integer dealerId, DealerAuditDTO auditDTO);
    
    /**
     * 删除经销商
     * @param dealerId 经销商ID
     * @return 删除结果信息
     */
    Map<String, Object> deleteDealer(Integer dealerId);
} 