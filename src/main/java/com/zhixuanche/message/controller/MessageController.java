package com.zhixuanche.message.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.common.response.Result;
import com.zhixuanche.message.constant.MessageConstant;
import com.zhixuanche.message.dto.MessageDTO;
import com.zhixuanche.message.dto.MessageQueryParam;
import com.zhixuanche.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息控制器
 */
@Tag(name = "消息管理", description = "消息相关接口，包括系统消息、交互消息、聊天记录等")
@SecurityRequirement(name = "Sa-Token")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 获取系统消息
     */
    @Operation(
        summary = "获取系统消息", 
        description = "获取当前用户的系统消息列表，支持已读/未读过滤和分页",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @Parameters({
        @Parameter(name = "read", description = "是否已读：true-已读，false-未读，null-全部"),
        @Parameter(name = "messageType", description = "消息类型：SYSTEM/MARKETING/NOTICE/ACTIVITY等"),
        @Parameter(name = "startDate", description = "开始日期，格式：yyyy-MM-dd"),
        @Parameter(name = "endDate", description = "结束日期，格式：yyyy-MM-dd"),
        @Parameter(name = "page", description = "当前页码，默认1"),
        @Parameter(name = "size", description = "每页大小，默认20")
    })
    @GetMapping("/system")
    public Result getSystemMessages(
            @RequestParam(required = false) Boolean read,
            @RequestParam(required = false) String messageType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 构建查询参数
        MessageQueryParam queryParam = new MessageQueryParam();
        queryParam.setUserId(userId);
        queryParam.setRead(read);
        queryParam.setMessageType(messageType);
        queryParam.setStartDate(startDate);
        queryParam.setEndDate(endDate);
        queryParam.setPage(page);
        queryParam.setSize(size);
        
        // 查询系统消息
        PageResult<MessageDTO> pageResult = messageService.getSystemMessages(queryParam);
        
        return Result.success("获取系统消息成功", pageResult);
    }

    /**
     * 获取交互消息
     */
    @Operation(
        summary = "获取交互消息",
        description = "获取当前用户的交互消息列表，支持已读/未读过滤、交互类型过滤和分页",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @Parameters({
        @Parameter(name = "read", description = "是否已读：true-已读，false-未读，null-全部"),
        @Parameter(name = "interactionType", description = "交互类型：COMMENT/REPLY/QUESTION/CONSULTATION等"),
        @Parameter(name = "targetType", description = "目标类型：VEHICLE/COMMENT/POST/DEALER等"),
        @Parameter(name = "startDate", description = "开始日期，格式：yyyy-MM-dd"),
        @Parameter(name = "endDate", description = "结束日期，格式：yyyy-MM-dd"),
        @Parameter(name = "page", description = "当前页码，默认1"),
        @Parameter(name = "size", description = "每页大小，默认20")
    })
    @GetMapping("/interaction")
    public Result getInteractionMessages(
            @RequestParam(required = false) Boolean read,
            @RequestParam(required = false) String interactionType,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 构建查询参数
        MessageQueryParam queryParam = new MessageQueryParam();
        queryParam.setUserId(userId);
        queryParam.setRead(read);
        queryParam.setInteractionType(interactionType);
        queryParam.setTargetType(targetType);
        queryParam.setStartDate(startDate);
        queryParam.setEndDate(endDate);
        queryParam.setPage(page);
        queryParam.setSize(size);
        
        // 查询交互消息
        PageResult<MessageDTO> pageResult = messageService.getInteractionMessages(queryParam);
        
        return Result.success("获取交互消息成功", pageResult);
    }

    /**
     * 获取与指定用户的消息记录
     */
    @Operation(
        summary = "获取聊天记录",
        description = "获取当前用户与指定联系人的聊天记录，支持关联车辆和分页",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @Parameters({
        @Parameter(name = "contactId", description = "联系人ID", required = true),
        @Parameter(name = "carId", description = "关联车辆ID，可选"),
        @Parameter(name = "page", description = "当前页码，默认1"),
        @Parameter(name = "size", description = "每页大小，默认20")
    })
    @GetMapping("/chat/{contactId}")
    public Result getChatMessages(
            @PathVariable Integer contactId,
            @RequestParam(required = false) Integer carId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 查询聊天记录
        PageResult<MessageDTO> pageResult = messageService.getMessagesBetweenUsers(
                userId, contactId, carId, page, size);
        
        return Result.success("获取聊天记录成功", pageResult);
    }

    /**
     * 发送消息
     */
    @Operation(
        summary = "发送消息",
        description = "向指定用户发送消息，可选关联车辆",
        responses = {
            @ApiResponse(responseCode = "200", description = "发送成功", content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "401", description = "未授权"),
            @ApiResponse(responseCode = "500", description = "消息发送失败")
        }
    )
    @Parameters({
        @Parameter(name = "receiverId", description = "接收者ID", required = true),
        @Parameter(name = "carId", description = "关联车辆ID"),
        @Parameter(name = "content", description = "消息内容", required = true)
    })
    @PostMapping("/send")
    public Result sendMessage(
            @RequestParam Integer receiverId,
            @RequestParam(required = false) Integer carId,
            @RequestParam String content) {
        
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 发送消息
        Integer messageId = messageService.sendInteractionMessage(
                userId, receiverId, carId, content, 
                MessageConstant.InteractionType.CONSULTATION, null, null);
        
        return Result.success("发送消息成功", messageId);
    }

    /**
     * 标记消息为已读
     */
    @Operation(
        summary = "标记消息为已读",
        description = "将指定消息标记为已读状态",
        responses = {
            @ApiResponse(responseCode = "200", description = "标记成功"),
            @ApiResponse(responseCode = "401", description = "未授权"),
            @ApiResponse(responseCode = "500", description = "标记失败")
        }
    )
    @Parameter(name = "messageId", description = "消息ID", required = true)
    @PutMapping("/{messageId}/read")
    public Result markMessageRead(@PathVariable Integer messageId) {
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 标记为已读
        boolean success = messageService.markMessageAsRead(messageId, userId);
        
        return success ? Result.success("标记已读成功") : Result.error(5100, "标记已读失败");
    }

    /**
     * 批量标记消息为已读
     */
    @Operation(
        summary = "批量标记消息为已读",
        description = "将多条消息批量标记为已读状态",
        responses = {
            @ApiResponse(responseCode = "200", description = "标记成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "消息ID列表",
        required = true,
        content = @Content(schema = @Schema(implementation = List.class))
    )
    @PutMapping("/read/batch")
    public Result batchMarkRead(@RequestBody List<Integer> messageIds) {
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 批量标记为已读
        int count = messageService.batchMarkAsRead(messageIds, userId);
        
        return Result.success("批量标记已读成功", count);
    }

    /**
     * 删除消息
     */
    @Operation(
        summary = "删除消息",
        description = "删除指定的消息",
        responses = {
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "401", description = "未授权"),
            @ApiResponse(responseCode = "500", description = "删除失败")
        }
    )
    @Parameter(name = "messageId", description = "消息ID", required = true)
    @DeleteMapping("/{messageId}")
    public Result deleteMessage(@PathVariable Integer messageId) {
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 删除消息
        boolean success = messageService.deleteMessage(messageId, userId);
        
        return success ? Result.success("删除消息成功") : Result.error(5100, "删除消息失败");
    }

    /**
     * 批量删除消息
     */
    @Operation(
        summary = "批量删除消息",
        description = "批量删除多条消息",
        responses = {
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "消息ID列表",
        required = true,
        content = @Content(schema = @Schema(implementation = List.class))
    )
    @DeleteMapping("/batch")
    public Result batchDeleteMessages(@RequestBody List<Integer> messageIds) {
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 批量删除消息
        int count = messageService.batchDeleteMessages(messageIds, userId);
        
        return Result.success("批量删除消息成功", count);
    }

    /**
     * 获取未读消息统计
     */
    @Operation(
        summary = "获取未读消息统计",
        description = "获取当前用户未读消息的数量统计，按消息类型分组",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @GetMapping("/unread/count")
    public Result getUnreadCount() {
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 获取未读消息统计
        Map<String, Integer> stats = messageService.getUnreadMessageStats(userId);
        
        return Result.success("获取未读消息统计成功", stats);
    }

    /**
     * 获取聊天联系人列表
     */
    @Operation(
        summary = "获取聊天联系人",
        description = "获取与当前登录用户有消息往来的其他用户ID列表",
        responses = {
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
        }
    )
    @GetMapping("/contacts")
    public Result getChatContacts() {
        // 获取当前登录用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        // 获取聊天联系人列表
        List<Integer> contactIds = messageService.getChatContactIds(userId);
        
        return Result.success("获取聊天联系人成功", contactIds);
    }
} 