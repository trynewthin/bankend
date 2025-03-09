package com.zhixuanche.admin.controller;

import com.zhixuanche.admin.dto.DealerAuditDTO;
import com.zhixuanche.admin.service.DealerAdminService;
import com.zhixuanche.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 经销商管理控制器
 */
@Tag(name = "经销商管理", description = "系统管理员经销商管理接口")
@RestController
@RequestMapping("/admin/dealers")
public class DealerAdminController {

    @Autowired
    private DealerAdminService dealerAdminService;

    /**
     * 获取经销商列表
     */
    @Operation(summary = "获取经销商列表", description = "根据条件获取经销商列表，支持分页和条件筛选")
    @GetMapping
    public Result getDealerList(
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "认证状态") @RequestParam(required = false) String verifyStatus,
            @Parameter(description = "地区") @RequestParam(required = false) String region,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页记录数") @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(dealerAdminService.getDealerList(keyword, verifyStatus, region, page, size));
    }

    /**
     * 获取经销商详情
     */
    @Operation(summary = "获取经销商详情", description = "获取指定经销商的详细信息")
    @GetMapping("/{dealerId}")
    public Result getDealerDetail(
            @Parameter(description = "经销商ID") @PathVariable Integer dealerId) {
        return Result.success(dealerAdminService.getDealerDetail(dealerId));
    }

    /**
     * 审核经销商
     */
    @Operation(summary = "审核经销商", description = "审核经销商资质")
    @PostMapping("/{dealerId}/audit")
    public Result auditDealer(
            @Parameter(description = "经销商ID") @PathVariable Integer dealerId,
            @Valid @RequestBody DealerAuditDTO auditDTO) {
        boolean success = dealerAdminService.auditDealer(dealerId, auditDTO);
        if (success) {
            return Result.success("经销商审核成功");
        } else {
            return Result.error(500, "经销商审核失败");
        }
    }

    /**
     * 删除经销商
     */
    @Operation(summary = "删除经销商", description = "删除指定经销商及其关联信息")
    @DeleteMapping("/{dealerId}")
    public Result deleteDealer(
            @Parameter(description = "经销商ID") @PathVariable Integer dealerId) {
        Map<String, Object> result = dealerAdminService.deleteDealer(dealerId);
        if ((Boolean) result.get("success")) {
            return Result.success(result.get("message"));
        } else {
            return Result.error(500, (String) result.get("message"));
        }
    }
} 