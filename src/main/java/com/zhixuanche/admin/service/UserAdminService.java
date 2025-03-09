package com.zhixuanche.admin.service;

import com.zhixuanche.admin.dto.UserAdminDetailDTO;
import com.zhixuanche.admin.dto.UserStatusDTO;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.user.entity.User;

import java.util.Map;

/**
 * 用户管理服务接口
 */
public interface UserAdminService {
    
    /**
     * 获取用户列表
     * @param keyword 关键词
     * @param status 状态
     * @param userType 用户类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param page 页码
     * @param size 每页记录数
     * @return 用户列表和分页信息
     */
    PageResult<User> getUserList(String keyword, String status, String userType, 
                                 String startDate, String endDate, Integer page, Integer size);
    
    /**
     * 获取用户详情
     * @param userId 用户ID
     * @return 用户详情
     */
    UserAdminDetailDTO getUserDetail(Integer userId);
    
    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param statusDTO 状态信息
     * @return 是否成功
     */
    boolean updateUserStatus(Integer userId, UserStatusDTO statusDTO);
    
    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param notifyUser 是否通知用户
     * @return 结果信息
     */
    Map<String, Object> resetUserPassword(Integer userId, boolean notifyUser);
    
    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除结果信息
     */
    Map<String, Object> deleteUser(Integer userId);
} 