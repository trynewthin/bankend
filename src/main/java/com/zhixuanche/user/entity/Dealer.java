package com.zhixuanche.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhixuanche.user.entity.enums.DealerStatus;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 经销商实体类
 */
@Data
@TableName("Dealers")
public class Dealer {
    private Integer dealerId;          // 经销商ID

    @NotNull(message = "用户ID不能为空")
    private Integer userId;            // 关联用户ID

    @NotBlank(message = "经销商名称不能为空")
    @Size(max = 100, message = "经销商名称长度不能超过100个字符")
    private String dealerName;         // 经销商名称

    @Size(max = 200, message = "地址长度不能超过200个字符")
    private String address;            // 地址

    @Size(max = 100, message = "营业执照号长度不能超过100个字符")
    private String businessLicense;    // 营业执照号

    @Size(max = 50, message = "联系人姓名长度不能超过50个字符")
    private String contactPerson;      // 联系人

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;       // 联系电话

    private DealerStatus status;       // 状态：0-待审核, 1-已审核, 2-拒绝

    private String description;        // 描述
} 