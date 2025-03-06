package com.zhixuanche.behavior.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.behavior.dto.BrowseHistoryDTO;
import com.zhixuanche.behavior.dto.SearchHistoryDTO;
import com.zhixuanche.behavior.service.BehaviorService;
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
 * 用户行为控制器
 */
@Tag(name = "用户行为管理", description = "用于管理用户的浏览、搜索行为记录，以及分析用户兴趣")
@RestController
@RequestMapping("/behavior")
@SecurityRequirement(name = "Authorization")
public class BehaviorController {
    
    private static final Logger log = LoggerFactory.getLogger(BehaviorController.class);
    
    @Autowired
    private BehaviorService behaviorService;
    
    /**
     * 获取浏览历史
     */
    @Operation(
        summary = "获取浏览历史", 
        description = "获取当前用户的浏览历史记录列表，包含车辆基本信息和浏览时间、时长等信息。支持分页查询。"
    )
    @Parameters({
        @Parameter(name = "page", description = "页码", example = "1"),
        @Parameter(name = "size", description = "每页条数", example = "10")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取浏览历史", 
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
                    "            \"browseTime\": \"2025-03-06T07:43:57\",\n" +
                    "            \"duration\": 300,\n" +
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
    @GetMapping("/browse")
    public Result getBrowseHistory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("获取浏览历史: userId={}, page={}, size={}", userId, page, size);
        
        PageResult<BrowseHistoryDTO> pageResult = behaviorService.getBrowseHistory(userId, page, size);
        Map<String, Object> data = new HashMap<>();
        data.put("total", pageResult.getTotal());
        data.put("pages", pageResult.getPages());
        data.put("current", page);
        data.put("records", pageResult.getList());
        return Result.success("获取成功", data);
    }
    
    /**
     * 记录浏览行为
     */
    @Operation(
        summary = "记录浏览行为", 
        description = "记录用户对特定车辆的浏览行为。如果之前已有浏览记录，会更新时间和累加浏览时长。"
    )
    @Parameter(name = "car_id", description = "车辆ID", required = true, example = "1")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功记录浏览行为", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"操作成功\",\n" +
                    "    \"data\": \"记录成功\"\n" +
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
    @PostMapping("/browse")
    public Result recordBrowse(
            @RequestBody Map<String, Integer> params) {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        Integer carId = params.get("car_id");
        if (carId == null) {
            return Result.badRequest("参数错误：缺少car_id");
        }
        
        log.info("记录浏览行为: userId={}, carId={}", userId, carId);
        behaviorService.recordBrowse(userId, carId, 0);
        return Result.success("记录成功");
    }
    
    /**
     * 删除浏览记录
     */
    @Operation(
        summary = "删除浏览记录", 
        description = "删除当前用户特定车辆的浏览记录"
    )
    @Parameter(name = "carId", description = "车辆ID", required = true, example = "1")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功删除浏览记录", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"操作成功\",\n" +
                    "    \"data\": \"删除成功\"\n" +
                    "}")
            )
        ),
        @ApiResponse(responseCode = "404", description = "记录不存在", 
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
    @DeleteMapping("/browse/{carId}")
    public Result deleteBrowseRecord(
            @PathVariable Integer carId) {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("删除浏览记录: userId={}, carId={}", userId, carId);
        
        if (!behaviorService.deleteBrowseRecord(userId, carId)) {
            return Result.notFound();
        }
        return Result.success("删除成功");
    }
    
    /**
     * 清空浏览历史
     */
    @Operation(
        summary = "清空浏览历史", 
        description = "清空当前用户的所有浏览记录"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功清空浏览历史", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"操作成功\",\n" +
                    "    \"data\": \"清空成功\"\n" +
                    "}")
            )
        )
    })
    @DeleteMapping("/browse")
    public Result clearBrowseHistory() {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("清空浏览历史: userId={}", userId);
        
        behaviorService.clearBrowseHistory(userId);
        return Result.success("清空成功");
    }
    
    /**
     * 获取搜索历史
     */
    @Operation(
        summary = "获取搜索历史", 
        description = "获取当前用户的搜索历史记录列表，包含搜索关键词和搜索时间。支持分页查询。"
    )
    @Parameters({
        @Parameter(name = "page", description = "页码", example = "1"),
        @Parameter(name = "size", description = "每页条数", example = "10")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取搜索历史", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"获取成功\",\n" +
                    "    \"data\": {\n" +
                    "        \"total\": 1,\n" +
                    "        \"current\": 1,\n" +
                    "        \"pages\": 1,\n" +
                    "        \"records\": [{\n" +
                    "            \"behaviorId\": 10,\n" +
                    "            \"searchKeywords\": \"BMW 5系 北京\",\n" +
                    "            \"searchTime\": \"2025-03-04T08:12:15\"\n" +
                    "        }]\n" +
                    "    }\n" +
                    "}")
            )
        )
    })
    @GetMapping("/search")
    public Result getSearchHistory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("获取搜索历史: userId={}, page={}, size={}", userId, page, size);
        
        PageResult<SearchHistoryDTO> pageResult = behaviorService.getSearchHistory(userId, page, size);
        Map<String, Object> data = new HashMap<>();
        data.put("total", pageResult.getTotal());
        data.put("pages", pageResult.getPages());
        data.put("current", page);
        data.put("records", pageResult.getList());
        return Result.success("获取成功", data);
    }
    
    /**
     * 记录搜索行为
     */
    @Operation(
        summary = "记录搜索行为", 
        description = "记录用户的搜索关键词，每次搜索都会创建新的记录"
    )
    @Parameter(name = "keyword", description = "搜索关键词", required = true, example = "宝马 SUV")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功记录搜索行为", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"操作成功\",\n" +
                    "    \"data\": \"记录成功\"\n" +
                    "}")
            )
        ),
        @ApiResponse(responseCode = "400", description = "参数错误", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 400,\n" +
                    "    \"message\": \"参数错误：缺少keyword\",\n" +
                    "    \"data\": null\n" +
                    "}")
            )
        )
    })
    @PostMapping("/search")
    public Result recordSearch(
            @RequestBody Map<String, String> params) {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        
        String keyword = params.get("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            return Result.badRequest("参数错误：缺少keyword");
        }
        
        log.info("记录搜索行为: userId={}, keyword={}", userId, keyword);
        behaviorService.recordSearch(userId, keyword.trim());
        return Result.success("记录成功");
    }
    
    /**
     * 删除搜索记录
     */
    @Operation(
        summary = "删除搜索记录", 
        description = "删除特定的搜索记录"
    )
    @Parameter(name = "searchId", description = "搜索记录ID", required = true, example = "10")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功删除搜索记录", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"操作成功\",\n" +
                    "    \"data\": \"删除成功\"\n" +
                    "}")
            )
        ),
        @ApiResponse(responseCode = "404", description = "记录不存在", 
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
    @DeleteMapping("/search/{searchId}")
    public Result deleteSearchRecord(
            @PathVariable Integer searchId) {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("删除搜索记录: userId={}, searchId={}", userId, searchId);
        
        if (!behaviorService.deleteSearchRecord(userId, searchId)) {
            return Result.notFound();
        }
        return Result.success("删除成功");
    }
    
    /**
     * 清空搜索历史
     */
    @Operation(
        summary = "清空搜索历史", 
        description = "清空当前用户的所有搜索记录"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功清空搜索历史", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"操作成功\",\n" +
                    "    \"data\": \"清空成功\"\n" +
                    "}")
            )
        )
    })
    @DeleteMapping("/search")
    public Result clearSearchHistory() {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("清空搜索历史: userId={}", userId);
        
        behaviorService.clearSearchHistory(userId);
        return Result.success("清空成功");
    }
    
    /**
     * 获取用户兴趣分析
     */
    @Operation(
        summary = "获取用户兴趣分析", 
        description = "获取基于用户行为的兴趣分析数据，包括最常浏览的品牌、最常搜索的关键词、各类行为的数量等统计信息"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取用户兴趣分析", 
            content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(value = 
                    "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"message\": \"获取成功\",\n" +
                    "    \"data\": {\n" +
                    "        \"topBrands\": [{\n" +
                    "            \"brand\": \"BMW\",\n" +
                    "            \"count\": 1\n" +
                    "        }, {\n" +
                    "            \"brand\": \"Mercedes-Benz\",\n" +
                    "            \"count\": 1\n" +
                    "        }],\n" +
                    "        \"topKeywords\": [{\n" +
                    "            \"search_keywords\": \"BMW 5系 北京\",\n" +
                    "            \"count\": 1\n" +
                    "        }],\n" +
                    "        \"browseCount\": 2,\n" +
                    "        \"searchCount\": 1,\n" +
                    "        \"consultCount\": 1\n" +
                    "    }\n" +
                    "}")
            )
        )
    })
    @GetMapping("/interests")
    public Result getUserInterests() {
        // 直接使用StpUtil获取用户ID
        Integer userId = StpUtil.getLoginIdAsInt();
        log.info("获取用户兴趣分析: userId={}", userId);
        
        Map<String, Object> interests = behaviorService.analyzeUserInterests(userId);
        return Result.success("获取成功", interests);
    }
} 