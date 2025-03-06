package com.zhixuanche.car.service;

import com.zhixuanche.car.entity.CarDetail;
import com.zhixuanche.car.dto.CarDetailDTO;

/**
 * 车辆详情服务接口
 */
public interface CarDetailService {
    
    /**
     * 根据车辆ID获取详情
     * @param carId 车辆ID
     * @return 车辆详情
     */
    CarDetail getDetailByCarId(Integer carId);
    
    /**
     * 创建车辆详情
     * @param carDetail 车辆详情
     * @return 创建后的详情
     */
    CarDetail createDetail(CarDetail carDetail);
    
    /**
     * 更新车辆详情
     * @param carDetail 车辆详情
     * @return 更新后的详情
     */
    CarDetail updateDetail(CarDetail carDetail);
    
    /**
     * 根据DTO创建或更新车辆详情
     * @param detailDTO 详情DTO
     * @return 创建或更新后的详情
     */
    CarDetail saveDetail(CarDetailDTO detailDTO);
    
    /**
     * 删除车辆详情
     * @param carId 车辆ID
     * @return 是否成功
     */
    boolean deleteDetail(Integer carId);
} 