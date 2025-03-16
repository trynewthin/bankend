package com.zhixuanche.message.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.common.exception.BusinessException;
import com.zhixuanche.common.exception.ErrorCode;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.common.response.Result;
import com.zhixuanche.message.dto.AppointmentDTO;
import com.zhixuanche.message.service.AppointmentService;
import com.zhixuanche.user.entity.Dealer;
import com.zhixuanche.user.entity.User;
import com.zhixuanche.user.entity.enums.UserType;
import com.zhixuanche.user.service.DealerService;
import com.zhixuanche.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约控制器
 */
@Tag(name = "预约管理", description = "预约相关接口，包括预约创建、查询、取消等操作")
@SecurityRequirement(name = "Sa-Token")
@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private DealerService dealerService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 创建预约
     */
    @Operation(
        summary = "创建预约",
        description = "创建一个新的预约，需要指定车辆ID、经销商ID、预约类型和时间",
        responses = {
            @ApiResponse(responseCode = "200", description = "创建成功", content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "401", description = "未授权"),
            @ApiResponse(responseCode = "500", description = "创建失败")
        }
    )
    @Parameters({
        @Parameter(name = "carId", description = "车辆ID", required = true),
        @Parameter(name = "dealerId", description = "经销商ID", required = true),
        @Parameter(name = "appointmentType", description = "预约类型：看车、试驾", required = true),
        @Parameter(name = "appointmentTime", description = "预约时间，格式：yyyy-MM-dd HH:mm:ss", required = true),
        @Parameter(name = "remarks", description = "备注信息")
    })
    @PostMapping("/create")
    public Result createAppointment(
            @RequestParam Integer carId,
            @RequestParam Integer dealerId,
            @RequestParam String appointmentType,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime appointmentTime,
            @RequestParam(required = false) String remarks) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 创建预约
        Integer appointmentId = appointmentService.createAppointment(
                userId, carId, dealerId, appointmentType, appointmentTime, remarks);
        
        return Result.success("创建预约成功", appointmentId);
    }
    
    /**
     * 获取用户预约列表
     */
    @Operation(
        summary = "获取用户预约列表",
        description = "获取当前用户的预约列表，可根据状态筛选",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @Parameters({
        @Parameter(name = "status", description = "预约状态：待确认、已确认、已完成、已取消"),
        @Parameter(name = "page", description = "当前页码，默认1"),
        @Parameter(name = "size", description = "每页大小，默认10")
    })
    @GetMapping("/user")
    public Result getUserAppointments(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 查询用户预约
        PageResult<AppointmentDTO> pageResult = appointmentService.getUserAppointments(
                userId, status, page, size);
        
        return Result.success("获取预约列表成功", pageResult);
    }
    
    /**
     * 获取经销商预约列表
     */
    @Operation(
        summary = "获取经销商预约列表",
        description = "获取当前经销商的预约列表，可根据状态筛选（仅经销商可用）",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权"),
            @ApiResponse(responseCode = "403", description = "权限不足")
        }
    )
    @Parameters({
        @Parameter(name = "status", description = "预约状态：待确认、已确认、已完成、已取消"),
        @Parameter(name = "page", description = "当前页码，默认1"),
        @Parameter(name = "size", description = "每页大小，默认10")
    })
    @GetMapping("/dealer")
    public Result getDealerAppointments(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 获取当前用户关联的经销商ID
        Integer dealerId = getDealerIdFromUser(userId);
        
        // 查询经销商预约
        PageResult<AppointmentDTO> pageResult = appointmentService.getDealerAppointments(
                dealerId, status, page, size);
        
        return Result.success("获取预约列表成功", pageResult);
    }
    
    /**
     * 更新预约状态（经销商操作）
     */
    @Operation(
        summary = "更新预约状态",
        description = "经销商更新预约的状态（仅经销商可用）",
        responses = {
            @ApiResponse(responseCode = "200", description = "更新成功"),
            @ApiResponse(responseCode = "401", description = "未授权"),
            @ApiResponse(responseCode = "403", description = "权限不足"),
            @ApiResponse(responseCode = "404", description = "预约不存在"),
            @ApiResponse(responseCode = "500", description = "更新失败")
        }
    )
    @Parameters({
        @Parameter(name = "appointmentId", description = "预约ID", required = true),
        @Parameter(name = "status", description = "新状态：已确认、已完成、已取消", required = true)
    })
    @PutMapping("/dealer/{appointmentId}/status")
    public Result updateStatusByDealer(
            @PathVariable Integer appointmentId,
            @RequestParam String status) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 获取当前用户关联的经销商ID
        Integer dealerId = getDealerIdFromUser(userId);
        
        // 更新状态
        boolean success = appointmentService.updateStatus(appointmentId, status, dealerId, "DEALER");
        
        return success ? Result.success("更新预约状态成功") : Result.error(5000, "更新预约状态失败");
    }
    
    /**
     * 取消预约（用户操作）
     */
    @Operation(
        summary = "取消预约",
        description = "用户取消自己的预约",
        responses = {
            @ApiResponse(responseCode = "200", description = "取消成功"),
            @ApiResponse(responseCode = "401", description = "未授权"),
            @ApiResponse(responseCode = "404", description = "预约不存在"),
            @ApiResponse(responseCode = "500", description = "取消失败")
        }
    )
    @Parameter(name = "appointmentId", description = "预约ID", required = true)
    @PutMapping("/{appointmentId}/cancel")
    public Result cancelAppointment(@PathVariable Integer appointmentId) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 取消预约
        boolean success = appointmentService.cancelAppointment(appointmentId, userId);
        
        return success ? Result.success("取消预约成功") : Result.error(5000, "取消预约失败");
    }
    
    /**
     * 获取即将到来的预约
     */
    @Operation(
        summary = "获取即将到来的预约",
        description = "获取当前用户即将到来的预约列表（状态为待确认或已确认）",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @Parameters({
        @Parameter(name = "page", description = "当前页码，默认1"),
        @Parameter(name = "size", description = "每页大小，默认10")
    })
    @GetMapping("/upcoming")
    public Result getUpcomingAppointments(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 查询即将到来的预约
        PageResult<AppointmentDTO> pageResult = appointmentService.getUpcomingAppointments(
                userId, page, size);
        
        return Result.success("获取即将到来的预约成功", pageResult);
    }
    
    /**
     * 获取指定车辆的预约
     */
    @Operation(
        summary = "获取指定车辆的预约",
        description = "获取当前用户关于指定车辆的所有预约",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @Parameter(name = "carId", description = "车辆ID", required = true)
    @GetMapping("/car/{carId}")
    public Result getCarAppointments(@PathVariable Integer carId) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 查询指定车辆的预约
        List<AppointmentDTO> appointments = appointmentService.getUserCarAppointments(userId, carId);
        
        return Result.success("获取车辆预约成功", appointments);
    }
    
    /**
     * 检查预约时间是否可用
     */
    @Operation(
        summary = "检查预约时间是否可用",
        description = "检查指定经销商在指定时间是否可以预约",
        responses = {
            @ApiResponse(responseCode = "200", description = "检查成功", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @Parameters({
        @Parameter(name = "dealerId", description = "经销商ID", required = true),
        @Parameter(name = "appointmentTime", description = "预约时间，格式：yyyy-MM-dd HH:mm:ss", required = true)
    })
    @GetMapping("/check-time")
    public Result checkAppointmentTime(
            @RequestParam Integer dealerId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime appointmentTime) {
        
        // 检查时间是否可用
        boolean available = appointmentService.isAppointmentTimeAvailable(dealerId, appointmentTime);
        
        return Result.success("检查预约时间成功", available);
    }
    
    /**
     * 获取待确认的预约数量（经销商视角）
     */
    @Operation(
        summary = "获取待确认的预约数量",
        description = "获取当前经销商待确认的预约数量（仅经销商可用）",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功", content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "401", description = "未授权"),
            @ApiResponse(responseCode = "403", description = "权限不足")
        }
    )
    @GetMapping("/pending/count")
    public Result getPendingCount() {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 获取当前用户关联的经销商ID
        Integer dealerId = getDealerIdFromUser(userId);
        
        // 获取待确认的预约数量
        int count = appointmentService.countPendingAppointments(dealerId);
        
        return Result.success("获取待确认预约数量成功", count);
    }
    
    /**
     * 从用户信息中获取经销商ID
     * @param userId 用户ID
     * @return 经销商ID
     * @throws BusinessException 如果用户不是经销商类型或获取经销商信息失败
     */
    private Integer getDealerIdFromUser(Integer userId) {
        // 获取用户信息
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND.getCode(), "用户不存在");
        }
        
        // 检查用户类型是否为经销商
        if (user.getUserType() != UserType.DEALER) {
            throw new BusinessException(ErrorCode.FORBIDDEN.getCode(), "只有经销商才能操作此功能");
        }
        
        // 获取经销商信息
        Dealer dealer = dealerService.getDealerByUserId(userId);
        if (dealer == null) {
            throw new BusinessException(ErrorCode.DEALER_NOT_FOUND.getCode(), "经销商信息不存在");
        }
        
        return dealer.getDealerId();
    }
} 