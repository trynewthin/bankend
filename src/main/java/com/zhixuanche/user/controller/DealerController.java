package com.zhixuanche.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.user.dto.DealerDTO;
import com.zhixuanche.user.entity.Dealer;
import com.zhixuanche.user.service.DealerService;
import com.zhixuanche.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * 经销商控制器
 */
@Tag(name = "经销商管理", description = "经销商信息管理接口")
@RestController
@RequestMapping("/dealers")
@SecurityRequirement(name = "Sa-Token")
public class DealerController {

    @Autowired
    private DealerService dealerService;

    /**
     * 提交经销商信息
     */
    @Operation(summary = "提交经销商信息", description = "提交经销商基本信息")
    @PostMapping("/info")
    public ApiResponse<Dealer> submitDealerInfo(@Valid @RequestBody DealerDTO dealerDTO) {
        Integer userId = StpUtil.getLoginIdAsInt();
        
        Dealer dealer = new Dealer();
        dealer.setUserId(userId);
        dealer.setDealerName(dealerDTO.getDealerName());
        dealer.setAddress(dealerDTO.getAddress());
        dealer.setBusinessLicense(dealerDTO.getBusinessLicense());
        dealer.setContactPerson(dealerDTO.getContactPerson());
        dealer.setContactPhone(dealerDTO.getContactPhone());
        dealer.setDescription(dealerDTO.getDescription());
        
        Dealer savedDealer = dealerService.submitDealerInfo(dealer);
        return ApiResponse.success("提交成功", savedDealer);
    }

    /**
     * 提交审核申请
     */
    @Operation(summary = "提交审核申请", description = "提交经销商审核申请")
    @PostMapping("/review")
    public ApiResponse<Dealer> submitDealerReview() {
        Integer userId = StpUtil.getLoginIdAsInt();
        
        Dealer dealer = dealerService.getDealerByUserId(userId);
        if (dealer == null) {
            return ApiResponse.error(400, "请先提交经销商信息");
        }
        
        dealerService.updateDealerStatus(dealer.getDealerId(), 0); // 0表示待审核状态
        return ApiResponse.success("提交审核成功", dealer);
    }

    /**
     * 获取待审核经销商列表（管理员接口）
     */
    @Operation(summary = "获取待审核列表", description = "获取待审核的经销商列表（管理员接口）")
    @GetMapping("/admin/pending")
    public ApiResponse<Object> getPendingDealers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        // 已通过Sa-Token配置验证管理员权限
        return ApiResponse.success("获取成功", dealerService.getPendingDealers());
    }
} 