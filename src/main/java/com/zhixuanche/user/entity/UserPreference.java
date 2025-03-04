package com.zhixuanche.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户偏好实体类
 */
@Data
@TableName("UserPreferences")
public class UserPreference {
    private Integer preferenceId;       // 偏好ID

    @NotNull(message = "用户ID不能为空")
    private Integer userId;             // 用户ID

    @DecimalMin(value = "0", message = "最低价格不能小于0")
    private BigDecimal priceMin;        // 最低价格

    @DecimalMin(value = "0", message = "最高价格不能小于0")
    private BigDecimal priceMax;        // 最高价格

    @Size(max = 200, message = "偏好品牌长度不能超过200个字符")
    private String preferredBrands;     // 偏好品牌，逗号分隔

    @Size(max = 200, message = "偏好车型长度不能超过200个字符")
    private String preferredCategories; // 偏好车型，逗号分隔

    private String otherPreferences;    // 其他偏好

    @NotNull(message = "更新时间不能为空")
    private Date updateTime;            // 更新时间
} 