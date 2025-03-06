package com.zhixuanche.car.service;

import com.zhixuanche.car.entity.Car;

import java.util.List;
import java.util.Map;

/**
 * 车辆服务接口
 */
public interface CarService {
    
    /**
     * 获取车辆列表
     * @param params 查询参数
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    Map<String, Object> getCars(Map<String, Object> params, int page, int size);
    
    /**
     * 根据ID获取车辆
     * @param carId 车辆ID
     * @return 车辆信息
     */
    Car getCarById(Integer carId);
    
    /**
     * 创建车辆
     * @param car 车辆信息
     * @return 创建后的车辆
     */
    Car createCar(Car car);
    
    /**
     * 更新车辆
     * @param car 车辆信息
     * @return 更新后的车辆
     */
    Car updateCar(Car car);
    
    /**
     * 更新车辆状态
     * @param carId 车辆ID
     * @param status 状态值
     * @return 是否成功
     */
    boolean changeStatus(Integer carId, Integer status);
    
    /**
     * 删除车辆
     * @param carId 车辆ID
     * @return 是否成功
     */
    boolean deleteCar(Integer carId);
    
    /**
     * 获取经销商的车辆列表
     * @param dealerId 经销商ID
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    Map<String, Object> getDealerCars(Integer dealerId, int page, int size);
    
    /**
     * 按品牌获取车辆
     * @param brand 品牌
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    Map<String, Object> getCarsByBrand(String brand, int page, int size);
    
    /**
     * 按价格区间获取车辆
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    Map<String, Object> getCarsByPriceRange(double minPrice, double maxPrice, int page, int size);
    
    /**
     * 按类别获取车辆
     * @param category 类别
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    Map<String, Object> getCarsByCategory(String category, int page, int size);
    
    /**
     * 关键词搜索车辆
     * @param keyword 关键词
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    Map<String, Object> searchCars(String keyword, int page, int size);
    
    /**
     * 获取所有品牌及其数量
     * @return 品牌列表
     */
    List<Map<String, Object>> getBrands();
    
    /**
     * 获取所有车型类别及其数量
     * @return 类别列表
     */
    List<Map<String, Object>> getCategories();
    
    /**
     * 多条件筛选车辆
     * @param filterParams 筛选条件
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    Map<String, Object> filterCars(Map<String, Object> filterParams, int page, int size);

    /**
     * 检查车辆是否存在
     * @param carId 车辆ID
     * @return 是否存在
     */
    boolean exists(Integer carId);

    /**
     * 检查车辆是否属于指定经销商
     * @param carId 车辆ID
     * @param dealerId 经销商ID
     * @return 是否属于该经销商
     */
    boolean isCarBelongToDealer(Integer carId, Integer dealerId);
} 