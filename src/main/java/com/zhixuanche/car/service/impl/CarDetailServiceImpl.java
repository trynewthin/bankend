package com.zhixuanche.car.service.impl;

import com.zhixuanche.car.dto.CarDetailDTO;
import com.zhixuanche.car.entity.CarDetail;
import com.zhixuanche.car.mapper.CarDetailMapper;
import com.zhixuanche.car.service.CarDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 车辆详情服务实现类
 */
@Service
public class CarDetailServiceImpl implements CarDetailService {

    @Autowired
    private CarDetailMapper carDetailMapper;

    /**
     * 根据车辆ID获取详情
     * @param carId 车辆ID
     * @return 车辆详情
     */
    @Override
    public CarDetail getDetailByCarId(Integer carId) {
        return carDetailMapper.selectByCarId(carId);
    }

    /**
     * 创建车辆详情
     * @param carDetail 车辆详情
     * @return 创建后的详情
     */
    @Override
    @Transactional
    public CarDetail createDetail(CarDetail carDetail) {
        int rows = carDetailMapper.insert(carDetail);
        if (rows > 0) {
            return carDetail; // MyBatis会自动设置生成的主键
        }
        return null;
    }

    /**
     * 更新车辆详情
     * @param carDetail 车辆详情
     * @return 更新后的详情
     */
    @Override
    @Transactional
    public CarDetail updateDetail(CarDetail carDetail) {
        int rows = carDetailMapper.update(carDetail);
        if (rows > 0) {
            return getDetailByCarId(carDetail.getCarId());
        }
        return null;
    }

    /**
     * 根据DTO创建或更新车辆详情
     * @param detailDTO 详情DTO
     * @return 创建或更新后的详情
     */
    @Override
    @Transactional
    public CarDetail saveDetail(CarDetailDTO detailDTO) {
        // 转换DTO为实体
        CarDetail detail = new CarDetail();
        detail.setCarId(detailDTO.getCarId());
        detail.setEngine(detailDTO.getEngine());
        detail.setTransmission(detailDTO.getTransmission());
        detail.setFuelType(detailDTO.getFuelType());
        detail.setFuelConsumption(detailDTO.getFuelConsumption());
        detail.setSeats(detailDTO.getSeats());
        detail.setColor(detailDTO.getColor());
        detail.setBodySize(detailDTO.getBodySize());
        detail.setWheelbase(detailDTO.getWheelbase());
        detail.setFeatures(detailDTO.getFeatures());
        detail.setWarranty(detailDTO.getWarranty());
        
        // 检查是否存在该车辆的详情
        CarDetail existingDetail = getDetailByCarId(detailDTO.getCarId());
        if (existingDetail != null) {
            // 已存在，执行更新
            detail.setDetailId(existingDetail.getDetailId());
            return updateDetail(detail);
        } else {
            // 不存在，执行创建
            return createDetail(detail);
        }
    }

    /**
     * 删除车辆详情
     * @param carId 车辆ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteDetail(Integer carId) {
        return carDetailMapper.deleteByCarId(carId) > 0;
    }
} 