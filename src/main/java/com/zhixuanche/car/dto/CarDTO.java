package com.zhixuanche.car.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 车辆数据传输对象
 */
@Data
public class CarDTO {
    
    private Integer carId;          // 车辆ID
    
    @NotNull(message = "经销商ID不能为空")
    private Integer dealerId;       // 经销商ID
    
    @NotBlank(message = "品牌不能为空")
    @Size(max = 50, message = "品牌长度不能超过50个字符")
    private String brand;           // 品牌
    
    @NotBlank(message = "型号不能为空")
    @Size(max = 100, message = "型号长度不能超过100个字符")
    private String model;           // 型号
    
    @NotNull(message = "年款不能为空")
    private Integer year;           // 年款
    
    @NotNull(message = "价格不能为空")
    @Min(value = 0, message = "价格不能小于0")
    private BigDecimal price;       // 价格
    
    @NotBlank(message = "类别不能为空")
    @Size(max = 50, message = "类别长度不能超过50个字符")
    private String category;        // 类别
    
    private Integer status;         // 状态：1-在售, 0-下架
    
    private CarDetailDTO detail;    // 车辆详情
} 