package com.zhixuanche.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 经销商信息DTO
 */
@Data
public class DealerDTO {
    @NotBlank(message = "经销商名称不能为空")
    @Size(max = 100, message = "经销商名称长度不能超过100个字符")
    private String dealerName;

    @Size(max = 200, message = "地址长度不能超过200个字符")
    private String address;

    @Size(max = 100, message = "营业执照号长度不能超过100个字符")
    private String businessLicense;

    @Size(max = 50, message = "联系人姓名长度不能超过50个字符")
    private String contactPerson;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;

    private String description;
}