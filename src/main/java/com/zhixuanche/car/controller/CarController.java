package com.zhixuanche.car.controller;

import com.zhixuanche.car.dto.CarDTO;
import com.zhixuanche.car.dto.CarDetailDTO;
import com.zhixuanche.car.entity.Car;
import com.zhixuanche.car.entity.CarDetail;
import com.zhixuanche.car.entity.CarImage;
import com.zhixuanche.car.service.CarService;
import com.zhixuanche.car.service.CarDetailService;
import com.zhixuanche.car.service.CarImageService;
import com.zhixuanche.common.response.Result;
import com.zhixuanche.car.exception.CarException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆控制器
 */
@Tag(name = "车辆管理", description = "车辆基本信息的增删改查接口")
@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;
    
    @Autowired
    private CarDetailService carDetailService;
    
    @Autowired
    private CarImageService carImageService;
    
    /**
     * 获取车辆列表
     * @param page 页码
     * @param size 每页数量
     * @param status 状态
     * @param dealerId 经销商ID
     * @return 车辆列表和分页信息
     */
    @Operation(summary = "获取车辆列表", description = "分页获取车辆列表，支持按状态和经销商ID筛选")
    @GetMapping
    public Result getCars(
            @Parameter(description = "页码，默认1") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "每页数量，默认10") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "车辆状态：1-在售，0-下架") @RequestParam(value = "status", required = false) Integer status,
            @Parameter(description = "经销商ID") @RequestParam(value = "dealer_id", required = false) Integer dealerId) {
        
        Map<String, Object> params = new HashMap<>();
        if (status != null) {
            params.put("status", status);
        }
        if (dealerId != null) {
            params.put("dealerId", dealerId);
        }
        
        Map<String, Object> result = carService.getCars(params, page, size);
        return Result.success(result);
    }
    
    /**
     * 获取车辆详情
     * @param carId 车辆ID
     * @return 车辆详情
     */
    @Operation(summary = "获取车辆详情", description = "根据车辆ID获取完整的车辆信息，包括基本信息、详细配置和图片")
    @GetMapping("/{car_id}")
    public Result getCarDetail(
            @Parameter(description = "车辆ID") @PathVariable("car_id") Integer carId) {
        Car car = carService.getCarById(carId);
        if (car == null) {
            throw new CarException(404, "车辆不存在");
        }
        
        CarDetail detail = carDetailService.getDetailByCarId(carId);
        List<CarImage> images = carImageService.getCarImages(carId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("basic", car);
        result.put("detail", detail);
        result.put("images", images);
        
        return Result.success(result);
    }
    
    /**
     * 创建车辆
     * @param carDTO 车辆信息
     * @return 创建结果
     */
    @Operation(summary = "创建新车辆", description = "创建新的车辆信息，包括基本信息和详细配置")
    @PostMapping
    public Result createCar(@Valid @RequestBody CarDTO carDTO) {
        try {
            // 这里应该有权限验证，确认是经销商用户
            
            Car car = new Car();
            car.setBrand(carDTO.getBrand());
            car.setModel(carDTO.getModel());
            car.setYear(carDTO.getYear());
            car.setPrice(carDTO.getPrice());
            car.setCategory(carDTO.getCategory());
            car.setDealerId(carDTO.getDealerId());
            car.setStatus(1); // 默认上架状态
            car.setCreateTime(LocalDateTime.now());
            
            Car createdCar = carService.createCar(car);
            
            // 如果有详情信息，则保存详情
            if (carDTO.getDetail() != null) {
                CarDetailDTO detailDTO = carDTO.getDetail();
                detailDTO.setCarId(createdCar.getCarId());
                carDetailService.saveDetail(detailDTO);
            }
            
            return Result.success(createdCar);
        } catch (CarException ce) {
            // 业务异常直接抛出
            throw ce;
        } catch (Exception e) {
            // 其他异常包装
            throw new CarException(400, "创建车辆失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新车辆信息
     * @param carId 车辆ID
     * @param carDTO 车辆信息
     * @return 更新结果
     */
    @Operation(summary = "更新车辆信息", description = "更新指定车辆的基本信息和详细配置")
    @PutMapping("/{car_id}")
    public Result updateCar(
            @Parameter(description = "车辆ID") @PathVariable("car_id") Integer carId,
            @RequestBody CarDTO carDTO) {
        
        // 这里应该有权限验证，确认是车辆所属经销商
        
        Car car = carService.getCarById(carId);
        if (car == null) {
            return Result.error(404, "车辆不存在");
        }
        
        // 更新非null字段
        if (carDTO.getBrand() != null) car.setBrand(carDTO.getBrand());
        if (carDTO.getModel() != null) car.setModel(carDTO.getModel());
        if (carDTO.getYear() != null) car.setYear(carDTO.getYear());
        if (carDTO.getPrice() != null) car.setPrice(carDTO.getPrice());
        if (carDTO.getCategory() != null) car.setCategory(carDTO.getCategory());
        car.setUpdateTime(LocalDateTime.now());
        
        Car updatedCar = carService.updateCar(car);
        return Result.success(updatedCar);
    }
    
    /**
     * 更新车辆详细参数
     * @param carId 车辆ID
     * @param detailDTO 详细参数
     * @return 更新结果
     */
    @Operation(summary = "更新车辆详情", description = "更新指定车辆的详细配置信息")
    @PutMapping("/{car_id}/detail")
    public Result updateCarDetail(
            @Parameter(description = "车辆ID") @PathVariable("car_id") Integer carId,
            @RequestBody CarDetailDTO detailDTO) {
        
        // 这里应该有权限验证，确认是车辆所属经销商
        
        Car car = carService.getCarById(carId);
        if (car == null) {
            return Result.error(404, "车辆不存在");
        }
        
        detailDTO.setCarId(carId);
        CarDetail updatedDetail = carDetailService.saveDetail(detailDTO);
        
        return Result.success(updatedDetail);
    }
    
    /**
     * 更新车辆状态
     * @param carId 车辆ID
     * @param statusMap 状态信息
     * @return 更新结果
     */
    @Operation(summary = "修改车辆状态", description = "修改车辆的上架/下架状态")
    @PutMapping("/{car_id}/status")
    public Result changeCarStatus(
            @Parameter(description = "车辆ID") @PathVariable("car_id") Integer carId,
            @RequestBody Map<String, Integer> statusMap) {
        
        if (!statusMap.containsKey("status")) {
            throw new CarException(400, "缺少状态参数");
        }
        
        Integer status = statusMap.get("status");
        if (status != 0 && status != 1) {
            throw new CarException(400, "状态值无效，应为0或1");
        }
        
        Car car = carService.getCarById(carId);
        if (car == null) {
            throw new CarException(404, "车辆不存在");
        }
        
        boolean success = carService.changeStatus(carId, status);
        if (!success) {
            throw new CarException(500, "状态更新失败");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("car_id", carId);
        result.put("status", status);
        result.put("status_text", status == 1 ? "已上架" : "已下架");
        result.put("update_time", LocalDateTime.now());
        
        return Result.success(result);
    }
    
    /**
     * 删除车辆
     * @param carId 车辆ID
     * @return 删除结果
     */
    @Operation(summary = "删除车辆", description = "删除指定车辆及其所有相关信息（详情、图片等）")
    @DeleteMapping("/{car_id}")
    public Result deleteCar(
            @Parameter(description = "车辆ID") @PathVariable("car_id") Integer carId) {
        
        Car car = carService.getCarById(carId);
        if (car == null) {
            throw new CarException(404, "车辆不存在");
        }
        
        try {
            boolean success = carService.deleteCar(carId);
            if (!success) {
                throw new CarException(500, "删除失败");
            }
            return Result.success("删除成功");
        } catch (CarException ce) {
            // 直接抛出业务异常
            throw ce;
        } catch (Exception e) {
            // 将其他异常包装为CarException
            throw new CarException(500, "删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取品牌列表
     * @return 品牌列表
     */
    @Operation(summary = "获取品牌列表", description = "获取所有车辆品牌及其数量统计")
    @GetMapping("/brands")
    public Result getBrands() {
        List<Map<String, Object>> brands = carService.getBrands();
        return Result.success(brands);
    }
    
    /**
     * 获取车型类别列表
     * @return 类别列表
     */
    @Operation(summary = "获取车型类别", description = "获取所有车型类别（如轿车、SUV等）及其数量统计")
    @GetMapping("/categories")
    public Result getCategories() {
        List<Map<String, Object>> categories = carService.getCategories();
        return Result.success(categories);
    }
    
    /**
     * it按品牌查询车辆
     * @param brand 品牌名称
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Operation(summary = "按品牌查询车辆", description = "分页获取指定品牌的车辆列表")
    @GetMapping("/brand/{brand}")
    public Result getCarsByBrand(
            @Parameter(description = "品牌名称") @PathVariable("brand") String brand,
            @Parameter(description = "页码，默认1") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "每页数量，默认10") @RequestParam(value = "size", defaultValue = "10") int size) {
        
        Map<String, Object> result = carService.getCarsByBrand(brand, page, size);
        return Result.success(result);
    }
    
    /**
     * 按价格区间查询车辆
     * @param min 最低价格
     * @param max 最高价格
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Operation(summary = "按价格区间查询", description = "分页获取指定价格区间内的车辆列表")
    @GetMapping("/price")
    public Result getCarsByPriceRange(
            @Parameter(description = "最低价格") @RequestParam("min") double min,
            @Parameter(description = "最高价格") @RequestParam("max") double max,
            @Parameter(description = "页码，默认1") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "每页数量，默认10") @RequestParam(value = "size", defaultValue = "10") int size) {
        
        Map<String, Object> result = carService.getCarsByPriceRange(min, max, page, size);
        return Result.success(result);
    }
    
    /**
     * 按类别查询车辆
     * @param category 类别
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Operation(summary = "按类别查询车辆", description = "分页获取指定类别的车辆列表")
    @GetMapping("/category/{category}")
    public Result getCarsByCategory(
            @Parameter(description = "车型类别") @PathVariable("category") String category,
            @Parameter(description = "页码，默认1") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "每页数量，默认10") @RequestParam(value = "size", defaultValue = "10") int size) {
        
        try {
            // 防止中文编码问题
            if (category != null) {
                // 打印接收到的参数，用于调试
                System.out.println("接收到的类别参数: " + category);
            }
            
            Map<String, Object> result = carService.getCarsByCategory(category, page, size);
            return Result.success(result);
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
            throw new CarException(400, "查询车辆失败：" + e.getMessage());
        }
    }
    
    /**
     * 关键词搜索车辆
     * @param keyword 关键词
     * @param page 页码
     * @param size 每页数量
     * @return 车辆列表和分页信息
     */
    @Operation(summary = "搜索车辆", description = "根据关键词搜索车辆，支持品牌、型号、类别等字段")
    @GetMapping("/search")
    public Result searchCars(
            @Parameter(description = "搜索关键词") @RequestParam("keyword") String keyword,
            @Parameter(description = "页码，默认1") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "每页数量，默认10") @RequestParam(value = "size", defaultValue = "10") int size) {
        
        Map<String, Object> result = carService.searchCars(keyword, page, size);
        return Result.success(result);
    }
    
    /**
     * 多条件筛选车辆
     * @param filterParams 筛选条件
     * @return 车辆列表和分页信息
     */
    @Operation(summary = "多条件筛选车辆", description = "根据多个条件筛选车辆，支持品牌、类别、价格区间等组合查询")
    @PostMapping("/filter")
    public Result filterCars(
            @Parameter(description = "筛选条件") @RequestBody Map<String, Object> filterParams) {
        
        Integer page = 1;
        Integer size = 10;
        
        if (filterParams.containsKey("page")) {
            page = (Integer) filterParams.get("page");
        }
        
        if (filterParams.containsKey("size")) {
            size = (Integer) filterParams.get("size");
        }
        
        Map<String, Object> result = carService.filterCars(filterParams, page, size);
        return Result.success(result);
    }
} 