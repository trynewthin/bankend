package com.zhixuanche.car.service.impl;

import com.zhixuanche.car.entity.Car;
import com.zhixuanche.car.mapper.CarMapper;
import com.zhixuanche.car.service.CarDetailService;
import com.zhixuanche.car.service.CarImageService;
import com.zhixuanche.car.service.CarService;
import com.zhixuanche.car.exception.CarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆服务实现类
 */
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarMapper carMapper;
    
    @Autowired
    private CarDetailService carDetailService;
    
    @Autowired
    private CarImageService carImageService;
    
    /**
     * 获取车辆列表
     * @param params 查询参数
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Override
    public Map<String, Object> getCars(Map<String, Object> params, int page, int size) {
        // 计算分页偏移量
        int offset = (page - 1) * size;
        
        // 添加分页参数
        params.put("offset", offset);
        params.put("limit", size);
        
        // 查询车辆列表和总数
        List<Car> cars = carMapper.selectAll(params);
        int total = carMapper.selectCount(params);
        
        // 计算总页数
        int pages = (total + size - 1) / size;
        
        // 封装结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("pages", pages);
        result.put("current", page);
        result.put("cars", cars);
        
        return result;
    }
    
    /**
     * 根据ID获取车辆
     * @param carId 车辆ID
     * @return 车辆信息
     */
    @Override
    public Car getCarById(Integer carId) {
        return carMapper.selectById(carId);
    }
    
    /**
     * 创建车辆
     * @param car 车辆信息
     * @return 创建后的车辆
     */
    @Override
    @Transactional
    public Car createCar(Car car) {
        carMapper.insert(car);
        return car;
    }
    
    /**
     * 更新车辆
     * @param car 车辆信息
     * @return 更新后的车辆
     */
    @Override
    @Transactional
    public Car updateCar(Car car) {
        carMapper.update(car);
        return car;
    }
    
    /**
     * 更新车辆状态
     * @param carId 车辆ID
     * @param status 状态值
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean changeStatus(Integer carId, Integer status) {
        return carMapper.updateStatus(carId, status) > 0;
    }
    
    /**
     * 删除车辆
     * @param carId 车辆ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteCar(Integer carId) {
        // 检查车辆是否有关联预约
        int appointmentsCount = carMapper.countAppointmentsByCarId(carId);
        if (appointmentsCount > 0) {
            throw new CarException(400, "该车辆有相关预约记录，无法删除");
        }
        
        // 删除车辆详情
        carDetailService.deleteDetail(carId);
        
        // 删除车辆图片
        carImageService.deleteAllCarImages(carId);
        
        // 删除车辆
        return carMapper.delete(carId) > 0;
    }
    
    /**
     * 获取经销商的车辆列表
     * @param dealerId 经销商ID
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Override
    public Map<String, Object> getDealerCars(Integer dealerId, int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("dealerId", dealerId);
        return getCars(params, page, size);
    }
    
    /**
     * 按品牌获取车辆
     * @param brand 品牌
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Override
    public Map<String, Object> getCarsByBrand(String brand, int page, int size) {
        int offset = (page - 1) * size;
        List<Car> cars = carMapper.selectByBrand(brand, offset, size);
        
        Map<String, Object> params = new HashMap<>();
        params.put("brand", brand);
        params.put("status", 1); // 只查询在售车辆
        
        int total = carMapper.selectCount(params);
        int pages = (total + size - 1) / size;
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("pages", pages);
        result.put("current", page);
        result.put("cars", cars);
        
        return result;
    }
    
    /**
     * 按价格区间获取车辆
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Override
    public Map<String, Object> getCarsByPriceRange(double minPrice, double maxPrice, int page, int size) {
        int offset = (page - 1) * size;
        List<Car> cars = carMapper.selectByPriceRange(minPrice, maxPrice, offset, size);
        
        Map<String, Object> params = new HashMap<>();
        params.put("minPrice", minPrice);
        params.put("maxPrice", maxPrice);
        params.put("status", 1); // 只查询在售车辆
        
        int total = carMapper.selectCount(params);
        int pages = (total + size - 1) / size;
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("pages", pages);
        result.put("current", page);
        result.put("cars", cars);
        
        return result;
    }
    
    /**
     * 按类别获取车辆
     * @param category 类别
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Override
    public Map<String, Object> getCarsByCategory(String category, int page, int size) {
        int offset = (page - 1) * size;
        List<Car> cars = carMapper.selectByCategory(category, offset, size);
        
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("status", 1); // 只查询在售车辆
        
        int total = carMapper.selectCount(params);
        int pages = (total + size - 1) / size;
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("pages", pages);
        result.put("current", page);
        result.put("cars", cars);
        
        return result;
    }
    
    /**
     * 关键词搜索车辆
     * @param keyword 关键词
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Override
    public Map<String, Object> searchCars(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("status", 1); // 只查询在售车辆
        params.put("offset", offset);
        params.put("limit", size);
        
        List<Car> cars = carMapper.searchByKeyword(params);
        int total = carMapper.countSearchResult(params);
        
        int pages = (total + size - 1) / size;
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("pages", pages);
        result.put("current", page);
        result.put("cars", cars);
        
        return result;
    }
    
    /**
     * 获取所有品牌及其数量
     * @return 品牌列表
     */
    @Override
    public List<Map<String, Object>> getBrands() {
        return carMapper.countByBrand();
    }
    
    /**
     * 获取所有车型类别及其数量
     * @return 类别列表
     */
    @Override
    public List<Map<String, Object>> getCategories() {
        return carMapper.countByCategory();
    }
    
    /**
     * 多条件筛选车辆
     * @param filterParams 筛选条件
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Override
    public Map<String, Object> filterCars(Map<String, Object> filterParams, int page, int size) {
        int offset = (page - 1) * size;
        
        // 复制筛选参数，避免修改原始参数
        Map<String, Object> params = new HashMap<>(filterParams);
        
        // 设置分页参数
        params.put("offset", offset);
        params.put("limit", size);
        params.put("status", 1); // 只查询在售车辆
        
        // 查询符合条件的车辆
        List<Car> cars = carMapper.filterCars(params);
        int total = carMapper.countFilterResult(params);
        
        int pages = (total + size - 1) / size;
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("pages", pages);
        result.put("current", page);
        result.put("cars", cars);
        
        return result;
    }

    /**
     * 检查车辆是否存在
     * @param carId 车辆ID
     * @return 是否存在
     */
    @Override
    public boolean exists(Integer carId) {
        return carMapper.selectById(carId) != null;
    }

    /**
     * 检查车辆是否属于指定经销商
     * @param carId 车辆ID
     * @param dealerId 经销商ID
     * @return 是否属于该经销商
     */
    @Override
    public boolean isCarBelongToDealer(Integer carId, Integer dealerId) {
        Car car = carMapper.selectById(carId);
        return car != null && car.getDealerId().equals(dealerId);
    }
} 