package com.zhixuanche.admin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.zhixuanche.admin.dto.UserAdminDetailDTO;
import com.zhixuanche.admin.dto.UserStatusDTO;
import com.zhixuanche.admin.service.UserAdminService;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.message.service.NotificationService;
import com.zhixuanche.user.entity.Dealer;
import com.zhixuanche.user.entity.User;
import com.zhixuanche.user.entity.enums.UserType;
import com.zhixuanche.user.mapper.DealerMapper;
import com.zhixuanche.user.mapper.UserMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户管理服务实现
 */
@Service
public class UserAdminServiceImpl implements UserAdminService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private DealerMapper dealerMapper;
    
    @Autowired
    private NotificationService notificationService;
    
    @Override
    public PageResult<User> getUserList(String keyword, String status, String userType, 
                                      String startDate, String endDate, Integer page, Integer size) {
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            
            // 添加查询条件
            if (keyword != null && !keyword.isEmpty()) {
                params.put("keyword", keyword);
            }
            
            if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("ALL")) {
                if (status.equalsIgnoreCase("ACTIVE")) {
                    params.put("status", 1);
                } else if (status.equalsIgnoreCase("DISABLED")) {
                    params.put("status", 0);
                }
            }
            
            if (userType != null && !userType.isEmpty() && !userType.equalsIgnoreCase("ALL")) {
                params.put("userType", userType);
            }
            
            // 添加日期条件
            if (startDate != null && !startDate.isEmpty()) {
                params.put("startDate", startDate + " 00:00:00");
            }
            
            if (endDate != null && !endDate.isEmpty()) {
                params.put("endDate", endDate + " 23:59:59");
            }
            
            // 分页参数
            params.put("offset", (page - 1) * size);
            params.put("limit", size);
            
            // 执行查询
            List<User> users = userMapper.findByConditions(params);
            int total = userMapper.countByConditions(params);
            
            // 创建分页结果对象
            return new PageResult<>(total, page, size, users);
        } catch (Exception e) {
            // 发生异常时记录日志，并返回空数据
            System.err.println("查询用户列表失败: " + e.getMessage());
            e.printStackTrace();
            return new PageResult<>(0, page, size, new ArrayList<>());
        }
    }

    @Override
    public UserAdminDetailDTO getUserDetail(Integer userId) {
        // 获取用户基本信息
        User user = userMapper.findById(userId);
        if (user == null) {
            return null;
        }
        
        UserAdminDetailDTO detailDTO = new UserAdminDetailDTO();
        BeanUtils.copyProperties(user, detailDTO);
        
        // 如果是经销商，获取经销商信息
        if (user.getUserType() == UserType.DEALER) {
            Dealer dealer = dealerMapper.findByUserId(userId);
            
            if (dealer != null) {
                UserAdminDetailDTO.DealerInfoDTO dealerInfoDTO = new UserAdminDetailDTO.DealerInfoDTO();
                BeanUtils.copyProperties(dealer, dealerInfoDTO);
                
                // 手动处理status字段，将DealerStatus枚举转换为Integer
                if (dealer.getStatus() != null) {
                    dealerInfoDTO.setStatus(dealer.getStatus().getCode());
                }
                
                detailDTO.setDealerInfo(dealerInfoDTO);
            }
        }
        
        return detailDTO;
    }

    @Override
    @Transactional
    public boolean updateUserStatus(Integer userId, UserStatusDTO statusDTO) {
        // 获取当前管理员ID
        Integer adminId = StpUtil.getLoginIdAsInt();
        
        // 防止管理员禁用自己
        if (userId.equals(adminId)) {
            return false;
        }
        
        // 直接使用专用的方法更新状态，而不是创建不完整的User对象
        int result = userMapper.updateStatus(userId, statusDTO.getStatus());
        
        if (result > 0) {
            // 发送状态变更通知给用户
            sendStatusChangeNotification(userId, statusDTO);
        }
        
        return result > 0;
    }

    /**
     * 发送状态变更通知给用户
     */
    private void sendStatusChangeNotification(Integer userId, UserStatusDTO statusDTO) {
        String title = "账号状态变更通知";
        String content;
        String noticeType;
        
        if (statusDTO.getStatus() == 1) {
            content = "您的账号已被启用，现在可以正常使用系统功能。";
            noticeType = "ACCOUNT_ENABLED";
        } else {
            content = "您的账号已被禁用";
            if (statusDTO.getReason() != null && !statusDTO.getReason().isEmpty()) {
                content += "，原因：" + statusDTO.getReason();
            }
            noticeType = "ACCOUNT_DISABLED";
        }
        
        notificationService.sendSystemNotification(userId, title, content, noticeType);
    }

    @Override
    @Transactional
    public Map<String, Object> resetUserPassword(Integer userId, boolean notifyUser) {
        Map<String, Object> result = new HashMap<>();
        
        // 验证用户是否存在
        User user = userMapper.findById(userId);
        if (user == null) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        
        // 生成随机密码
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        
        // 更新用户密码
        int updateResult = userMapper.updatePassword(userId, newPassword);
        
        if (updateResult > 0) {
            result.put("success", true);
            result.put("message", "密码重置成功");
            
            // 如果不通知用户，则返回新密码
            if (!notifyUser) {
                result.put("new_password", newPassword);
            } else {
                // 发送密码重置通知
                sendPasswordResetNotification(userId, user.getUsername(), newPassword);
            }
        } else {
            result.put("success", false);
            result.put("message", "密码重置失败");
        }
        
        return result;
    }
    
    /**
     * 发送密码重置通知
     */
    private void sendPasswordResetNotification(Integer userId, String username, String newPassword) {
        String title = "密码重置通知";
        String content = String.format("您好，%s。您的账号密码已被管理员重置，新密码为：%s。请尽快登录并修改密码。", 
                                      username, newPassword);
        String noticeType = "PASSWORD_RESET";
        
        notificationService.sendSystemNotification(userId, title, content, noticeType);
    }

    @Override
    @Transactional
    public Map<String, Object> deleteUser(Integer userId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取当前管理员ID
        Integer adminId = StpUtil.getLoginIdAsInt();
        
        // 防止管理员删除自己
        if (userId.equals(adminId)) {
            result.put("success", false);
            result.put("message", "不能删除当前登录的管理员账号");
            return result;
        }
        
        // 检查用户是否存在
        User user = userMapper.findById(userId);
        if (user == null) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        
        try {
            // 检查用户是否有关联的预约
            int appointmentCount = userMapper.countAppointmentsByUserId(userId);
            if (appointmentCount > 0) {
                result.put("success", false);
                result.put("message", "该用户有关联的预约记录，无法删除");
                return result;
            }
            
            // 如果是经销商用户，需要检查并删除经销商相关数据
            if (user.getUserType() == UserType.DEALER) {
                Dealer dealer = dealerMapper.findByUserId(userId);
                if (dealer != null) {
                    // 检查经销商是否有关联的车辆
                    int carCount = dealerMapper.countCarsByDealerId(dealer.getDealerId());
                    if (carCount > 0) {
                        result.put("success", false);
                        result.put("message", "该经销商用户有关联的车辆信息，无法删除");
                        return result;
                    }
                    
                    // 删除经销商信息
                    dealerMapper.deleteByUserId(userId);
                }
            }
            
            // 删除用户
            int deleteResult = userMapper.deleteById(userId);
            
            if (deleteResult > 0) {
                result.put("success", true);
                result.put("message", "用户删除成功");
            } else {
                result.put("success", false);
                result.put("message", "用户删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除用户过程中发生错误：" + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
} 