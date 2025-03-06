package com.zhixuanche.behavior.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.behavior.dto.FavoriteDTO;
import com.zhixuanche.behavior.service.FavoriteService;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 收藏控制器
 */
@Tag(name = "收藏管理", description = "用于管理用户对车辆的收藏操作，提供添加、删除、查询收藏等功能")
@RestController
@RequestMapping("/favorites")
@SecurityRequirement(name = "Authorization")
public class FavoriteController {
    
    private static final Logger log = LoggerFactory.getLogger(FavoriteController.class);
    
    @Autowired
    private FavoriteService favoriteService;
    
    /**
     * 获取收藏列表
     */
    @Operation(
        summary = "获取收藏列表", 
        description = "获取当前用户的收藏车辆列表，包含车辆基本信息。支持分页查询。"
    )
    @Parameters({
        @Parameter(name = "page", description = "页码", example = "1"),
        @Parameter(name = "size", description = "每页条数", example = "10")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取收藏列表", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Result.class),
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"获取成功\",\n" +
                    "    \"data\": {\n" +
                    "        \"total\": 2,\n" +
                    "        \"current\": 1,\n" +
                    "        \"pages\": 1,\n" +
                    "        \"records\": [{\n" +
                    "            \"carId\": 1,\n" +
                    "            \"brand\": \"BMW\",\n" +
                    "            \"model\": \"宝马5系 530Li\",\n" +
                    "            \"price\": 479800.00,\n" +
                    "            \"createTime\": \"2025-03-02T08:12:16\",\n" +
                    "            \"thumbnailUrl\": \"/images/cars/bmw/5-series-thumb.jpg\"\n" +
                    "        }]\n" +
                    "    }\n" +
                    "}")
            )
        ),
        @ApiResponse(responseCode = "401", description = "未授权", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 401,\n" +
                    "    \"message\": \"请先登录\",\n" +
                    "    \"data\": null\n" +
                    "}")
            )
        )
    })
    @GetMapping
    public Result getFavoriteList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("获取收藏列表: userId={}, page={}, size={}", userId, page, size);
        
        PageResult<FavoriteDTO> pageResult = favoriteService.getFavoriteList(userId, page, size);
        Map<String, Object> data = new HashMap<>();
        data.put("total", pageResult.getTotal());
        data.put("pages", pageResult.getPages());
        data.put("current", page);
        data.put("records", pageResult.getList());
        return Result.success("获取成功", data);
    }
    
    /**
     * 添加收藏
     */
    @Operation(
        summary = "添加收藏", 
        description = "将指定车辆添加到当前用户的收藏列表中。如果已收藏，则返回409状态码。"
    )
    @Parameter(name = "car_id", description = "车辆ID", required = true, example = "2")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功添加收藏", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"操作成功\",\n" +
                    "    \"data\": {\n" +
                    "        \"favorite_id\": 6,\n" +
                    "        \"car_id\": 2,\n" +
                    "        \"create_time\": 1709714289123\n" +
                    "    }\n" +
                    "}")
            )
        ),
        @ApiResponse(responseCode = "400", description = "参数错误", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 400,\n" +
                    "    \"message\": \"参数错误：缺少car_id\",\n" +
                    "    \"data\": null\n" +
                    "}")
            )
        ),
        @ApiResponse(responseCode = "409", description = "已收藏该车辆", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 409,\n" +
                    "    \"message\": \"已收藏该车辆\",\n" +
                    "    \"data\": null\n" +
                    "}")
            )
        )
    })
    @PostMapping
    public Result addFavorite(
            @RequestBody Map<String, Integer> params) {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        Integer carId = params.get("car_id");
        if (carId == null) {
            return Result.badRequest("参数错误：缺少car_id");
        }
        
        log.info("添加收藏: userId={}, carId={}", userId, carId);
        
        // 检查是否已收藏
        if (favoriteService.isFavorite(userId, carId)) {
            return Result.error(409, "已收藏该车辆");
        }
        
        Integer favoriteId = favoriteService.addFavorite(userId, carId);
        if (favoriteId == null) {
            return Result.error(500, "收藏失败");
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("favorite_id", favoriteId);
        data.put("car_id", carId);
        data.put("create_time", System.currentTimeMillis());
        return Result.success("收藏成功", data);
    }
    
    /**
     * 取消收藏
     */
    @Operation(
        summary = "取消收藏", 
        description = "将指定车辆从当前用户的收藏列表中移除。如果未收藏，则返回404状态码。"
    )
    @Parameter(name = "carId", description = "车辆ID", required = true, example = "2")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功取消收藏", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"操作成功\",\n" +
                    "    \"data\": \"取消收藏成功\"\n" +
                    "}")
            )
        ),
        @ApiResponse(responseCode = "404", description = "未收藏该车辆", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 404,\n" +
                    "    \"message\": \"资源不存在\",\n" +
                    "    \"data\": null\n" +
                    "}")
            )
        )
    })
    @DeleteMapping("/{carId}")
    public Result removeFavorite(
            @PathVariable Integer carId) {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("取消收藏: userId={}, carId={}", userId, carId);
        
        // 检查是否已收藏
        if (!favoriteService.isFavorite(userId, carId)) {
            return Result.notFound();
        }
        
        favoriteService.removeFavorite(userId, carId);
        return Result.success("取消收藏成功");
    }
    
    /**
     * 检查是否收藏
     */
    @Operation(
        summary = "检查是否收藏", 
        description = "检查指定车辆是否已被当前用户收藏"
    )
    @Parameter(name = "carId", description = "车辆ID", required = true, example = "1")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取收藏状态", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"获取成功\",\n" +
                    "    \"data\": {\n" +
                    "        \"isFavorite\": true\n" +
                    "    }\n" +
                    "}")
            )
        )
    })
    @GetMapping("/check/{carId}")
    public Result checkIsFavorite(
            @PathVariable Integer carId) {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("检查是否收藏: userId={}, carId={}", userId, carId);
        
        boolean isFavorite = favoriteService.isFavorite(userId, carId);
        Map<String, Object> data = new HashMap<>();
        data.put("isFavorite", isFavorite);
        return Result.success("获取成功", data);
    }
    
    /**
     * 获取收藏数量
     */
    @Operation(
        summary = "获取收藏数量", 
        description = "获取当前用户的收藏总数"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取收藏数量", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"获取成功\",\n" +
                    "    \"data\": 2\n" +
                    "}")
            )
        )
    })
    @GetMapping("/count")
    public Result getFavoriteCount() {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("获取收藏数量: userId={}", userId);
        
        int count = favoriteService.getFavoriteCount(userId);
        return Result.success("获取成功", count);
    }
    
    /**
     * 获取车辆被收藏数量
     */
    @Operation(
        summary = "获取车辆被收藏数量", 
        description = "获取指定车辆被所有用户收藏的总次数"
    )
    @Parameter(name = "carId", description = "车辆ID", required = true, example = "1")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取车辆被收藏数量", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"获取成功\",\n" +
                    "    \"data\": 1\n" +
                    "}")
            )
        )
    })
    @GetMapping("/count/{carId}")
    public Result getCarFavoriteCount(@PathVariable Integer carId) {
        log.info("获取车辆被收藏数量: carId={}", carId);
        
        int count = favoriteService.getCarFavoriteCount(carId);
        return Result.success("获取成功", count);
    }
}