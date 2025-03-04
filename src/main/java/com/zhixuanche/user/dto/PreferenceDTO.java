package com.zhixuanche.user.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 用户偏好设置DTO
 */
@Data
public class PreferenceDTO {
    @DecimalMin(value = "0", message = "最低价格不能小于0")
    private BigDecimal priceMin;

    @DecimalMin(value = "0", message = "最高价格不能小于0")
    private BigDecimal priceMax;

    private String preferredBrands;

    private String preferredCategories;

    private String otherPreferences;
} 