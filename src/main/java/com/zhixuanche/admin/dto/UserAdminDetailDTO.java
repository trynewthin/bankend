package com.zhixuanche.admin.dto;

import com.zhixuanche.user.entity.enums.UserType;
import lombok.Data;

import java.util.Date;

/**
 * 用户详情DTO
 */
@Data
public class UserAdminDetailDTO {
    
    private Integer userId;
    private String username;
    private String email;
    private String phone;
    private UserType userType;
    private Integer status;
    private Date registerTime;
    private Date lastLoginTime;
    private String avatar;
    
    // 经销商信息（仅当用户是经销商时有值）
    private DealerInfoDTO dealerInfo;
    
    @Data
    public static class DealerInfoDTO {
        private Integer dealerId;
        private String dealerName;
        private String address;
        private String businessLicense;
        private String contactPerson;
        private String contactPhone;
        private Integer status;
        private String description;
    }
} 