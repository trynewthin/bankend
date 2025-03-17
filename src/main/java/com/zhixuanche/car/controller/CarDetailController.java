package com.zhixuanche.car.controller;

import com.zhixuanche.car.dto.CarDetailDTO;
import com.zhixuanche.car.entity.Car;
import com.zhixuanche.car.entity.CarDetail;
import com.zhixuanche.car.entity.CarImage;
import com.zhixuanche.car.service.CarDetailService;
import com.zhixuanche.car.service.CarService;
import com.zhixuanche.common.response.Result;
import com.zhixuanche.car.exception.CarException;
import com.zhixuanche.common.security.LoginUser;
import com.zhixuanche.user.entity.Dealer;
import com.zhixuanche.user.service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 车辆详情控制器
 */
@Slf4j
@Tag(name = "车辆详情管理", description = "车辆详细配置信息的管理接口")
@RestController
@RequestMapping("/cars")
@SecurityRequirement(name = "Sa-Token")
public class CarDetailController {

    @Autowired
    private CarDetailService carDetailService;
    
    @Autowired
    private CarService carService;
    
    @Autowired
    private LoginUser loginUser;
    
    @Autowired
    private DealerService dealerService;
    
    @Operation(summary = "获取车辆详情", description = "获取指定车辆的完整信息，包括基本信息、详细配置和图片")
    @GetMapping("/{car_id}/detail")
    public Result getCarDetail(
            @Parameter(description = "车辆ID") @PathVariable("car_id") Integer carId) {
        
        try {
            // 获取车辆基本信息
            Car car = carService.getCarById(carId);
            if (car == null) {
                return Result.error(404, "车辆不存在");
            }
            
            // 获取车辆详细信息
            CarDetail detail = carDetailService.getDetailByCarId(carId);
            if (detail == null) {
                return Result.error(404, "车辆详情不存在");
            }
            
            // 获取车辆图片
            List<CarImage> carImages = carService.getCarImages(carId);
            List<String> images = carImages.stream()
                    .map(CarImage::getImageUrl)
                    .collect(Collectors.toList());
            
            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("basic", car);
            data.put("detail", detail);
            data.put("images", images);
            
            return Result.success(data);
        } catch (Exception e) {
            log.error("获取车辆详情失败", e);
            return Result.error(500, "系统异常，请稍后重试");
        }
    }
    
    @Operation(summary = "保存车辆详情", description = "保存或更新车辆的详细配置信息")
    @PostMapping("/{car_id}/detail")
    public Result saveCarDetail(
            @Parameter(description = "车辆ID") @PathVariable("car_id") Integer carId,
            @RequestBody CarDetailDTO detailDTO) {
        
        Car car = carService.getCarById(carId);
        if (car == null) {
            throw new CarException(404, "车辆不存在");
        }
        
        // 验证权限
        if (!loginUser.isAdmin()) {
            // 获取当前用户关联的经销商信息
            Dealer dealer = dealerService.getDealerByUserId(loginUser.getUserId());
            if (dealer == null) {
                throw new CarException(403, "无权限操作");
            }
            // 验证车辆是否属于该经销商
            if (!carService.isCarBelongToDealer(carId, dealer.getDealerId())) {
                throw new CarException(403, "无权操作此车辆");
            }
        }
        
        detailDTO.setCarId(carId);
        try {
            CarDetail savedDetail = carDetailService.saveDetail(detailDTO);
            return Result.success("保存成功", savedDetail);
        } catch (Exception e) {
            throw new CarException(500, "保存失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "删除车辆详情", description = "删除指定车辆的详细配置信息")
    @DeleteMapping("/{car_id}/detail")
    public Result deleteCarDetail(
            @Parameter(description = "车辆ID") @PathVariable("car_id") Integer carId) {
        
        Car car = carService.getCarById(carId);
        if (car == null) {
            throw new CarException(404, "车辆不存在");
        }
        
        // 验证权限
        if (!loginUser.isAdmin()) {
            // 获取当前用户关联的经销商信息
            Dealer dealer = dealerService.getDealerByUserId(loginUser.getUserId());
            if (dealer == null) {
                throw new CarException(403, "无权限操作");
            }
            // 验证车辆是否属于该经销商
            if (!carService.isCarBelongToDealer(carId, dealer.getDealerId())) {
                throw new CarException(403, "无权操作此车辆");
            }
        }
        
        boolean success = carDetailService.deleteDetail(carId);
        if (!success) {
            throw new CarException(404, "车辆详情不存在或删除失败");
        }
        
        return Result.success("删除成功");
    }
} 