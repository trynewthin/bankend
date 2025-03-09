package com.zhixuanche.admin.controller;

import com.zhixuanche.admin.dto.ContentAuditDTO;
import com.zhixuanche.admin.service.ContentAuditService;
import com.zhixuanche.car.entity.Car;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 内容审核控制器
 */
@Tag(name = "内容审核", description = "系统内容审核接口")
@RestController
@RequestMapping("/admin/audit")
public class ContentAuditController {

    @Autowired
    private ContentAuditService contentAuditService;

    /**
     * 获取待审核车辆列表
     */
    @Operation(summary = "获取待审核车辆", description = "获取待审核的车辆信息列表")
    @GetMapping("/cars")
    public Result getCarAuditList(
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "审核状态") @RequestParam(required = false) String auditStatus,
            @Parameter(description = "经销商ID") @RequestParam(required = false) Integer dealerId,
            @Parameter(description = "提交起始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "提交结束日期") @RequestParam(required = false) String endDate,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页记录数") @RequestParam(defaultValue = "20") Integer size) {
        PageResult<Car> pageResult = contentAuditService.getCarAuditList(keyword, auditStatus, dealerId, 
                                                              startDate, endDate, page, size);
        return Result.success(pageResult);
    }

    /**
     * 审核车辆信息
     */
    @Operation(summary = "审核车辆信息", description = "对车辆信息进行审核")
    @PostMapping("/cars/{carId}")
    public Result auditCar(
            @Parameter(description = "车辆ID") @PathVariable Integer carId,
            @Valid @RequestBody ContentAuditDTO auditDTO) {
        boolean success = contentAuditService.auditCar(carId, auditDTO);
        if (success) {
            return Result.success("车辆审核成功");
        } else {
            return Result.error(500, "车辆审核失败");
        }
    }

    /**
     * 获取车辆详情
     */
    @Operation(summary = "获取车辆详情", description = "获取待审核车辆的详细信息")
    @GetMapping("/cars/{carId}/detail")
    public Result getCarDetail(
            @Parameter(description = "车辆ID") @PathVariable Integer carId) {
        return Result.success(contentAuditService.getCarDetail(carId));
    }

    /**
     * 删除车辆
     */
    @Operation(summary = "删除车辆", description = "删除指定的车辆及其关联信息")
    @DeleteMapping("/cars/{carId}")
    public Result deleteCar(
            @Parameter(description = "车辆ID") @PathVariable Integer carId) {
        Map<String, Object> result = contentAuditService.deleteCar(carId);
        if ((Boolean) result.get("success")) {
            return Result.success(result.get("message"));
        } else {
            return Result.error(500, (String) result.get("message"));
        }
    }
} 