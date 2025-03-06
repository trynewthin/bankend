package com.zhixuanche.car.controller;

import com.zhixuanche.car.entity.CarImage;
import com.zhixuanche.car.service.CarImageService;
import com.zhixuanche.car.service.CarService;
import com.zhixuanche.common.response.ApiResponse;
import com.zhixuanche.car.exception.CarException;
import com.zhixuanche.common.security.LoginUser;
import com.zhixuanche.user.entity.Dealer;
import com.zhixuanche.user.service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import cn.dev33.satoken.annotation.SaIgnore;

import java.util.List;

/**
 * 车辆图片控制器
 */
@Tag(name = "车辆图片管理", description = "车辆图片的上传、查询和删除接口")
@RestController
@RequestMapping("/cars")
public class CarImageController {

    @Autowired
    private CarImageService carImageService;
    
    @Autowired
    private CarService carService;
    
    @Autowired
    private LoginUser loginUser;
    
    @Autowired
    private DealerService dealerService;
    
    @Operation(summary = "获取车辆图片", description = "获取指定车辆的所有图片列表")
    @GetMapping("/{carId}/images")
    @SaIgnore
    public ApiResponse<?> getCarImages(@PathVariable Integer carId) {
        // 验证车辆是否存在
        if (!carService.exists(carId)) {
            throw new CarException(404, "车辆不存在");
        }
        
        List<CarImage> images = carImageService.getCarImages(carId);
        return ApiResponse.success("获取成功", images);
    }
    
    @Operation(summary = "上传车辆图片", description = "上传车辆图片，支持缩略图和完整图，每种类型最多一张")
    @PostMapping("/{carId}/images")
    @SecurityRequirement(name = "Sa-Token")
    public ApiResponse<?> uploadCarImage(
            @PathVariable Integer carId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {
        
        // 验证车辆是否存在
        if (!carService.exists(carId)) {
            throw new CarException(404, "车辆不存在");
        }
        
        // 权限验证
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
        
        if (file.isEmpty()) {
            throw new CarException(400, "请选择要上传的图片");
        }
        
        // 验证图片类型
        if (!isValidImageType(type)) {
            throw new CarException(400, "不支持的图片类型");
        }
        
        try {
            CarImage carImage = carImageService.uploadCarImage(carId, file, type);
            return ApiResponse.success("上传成功", carImage);
        } catch (Exception e) {
            throw new CarException(500, "图片上传失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "删除车辆图片", description = "删除指定的车辆图片")
    @DeleteMapping("/{carId}/images/{imageId}")
    @SecurityRequirement(name = "Sa-Token")
    public ApiResponse<?> deleteCarImage(
            @PathVariable Integer carId,
            @PathVariable Integer imageId) {
        
        // 验证图片是否存在
        if (!carImageService.exists(imageId)) {
            throw new CarException(404, "图片不存在");
        }
        
        // 权限验证
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
        
        try {
            carImageService.deleteCarImage(imageId);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            throw new CarException(500, "删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 验证图片类型是否有效
     * @param type 图片类型
     * @return 是否有效
     */
    private boolean isValidImageType(String type) {
        return type != null && (
                "缩略图".equals(type) ||
                "完整图1".equals(type) ||
                "完整图2".equals(type) ||
                "完整图3".equals(type) ||
                "完整图4".equals(type) ||
                "完整图5".equals(type) ||
                "外观图".equals(type) ||
                "内饰图".equals(type) ||
                "细节图".equals(type)
        );
    }
} 