package com.zhixuanche.admin.service;

import com.zhixuanche.admin.dto.ContentAuditDTO;
import com.zhixuanche.car.entity.Car;
import com.zhixuanche.common.model.PageResult;

import java.util.Map;

/**
 * 内容审核服务接口
 */
public interface ContentAuditService {
    
    /**
     * 获取待审核车辆列表
     * @param keyword 关键词
     * @param auditStatus 审核状态
     * @param dealerId 经销商ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param page 页码
     * @param size 每页记录数
     * @return 车辆列表和分页信息
     */
    PageResult<Car> getCarAuditList(String keyword, String auditStatus, Integer dealerId, 
                                     String startDate, String endDate, Integer page, Integer size);
    
    /**
     * 审核车辆信息
     * @param carId 车辆ID
     * @param auditDTO 审核信息
     * @return 是否成功
     */
    boolean auditCar(Integer carId, ContentAuditDTO auditDTO);
    
    /**
     * 获取车辆详情
     * @param carId 车辆ID
     * @return 车辆详情
     */
    Map<String, Object> getCarDetail(Integer carId);
    
    /**
     * 删除车辆
     * @param carId 车辆ID
     * @return 是否成功（包含删除结果和原因）
     */
    Map<String, Object> deleteCar(Integer carId);
} 