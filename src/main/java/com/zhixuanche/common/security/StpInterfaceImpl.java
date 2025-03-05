package com.zhixuanche.common.security;

import cn.dev33.satoken.stp.StpInterface;
import com.zhixuanche.user.entity.User;
import com.zhixuanche.user.entity.enums.UserType;
import com.zhixuanche.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    
    @Autowired
    private UserService userService;
    
    /**
     * 返回一个用户所拥有的权限码集合 
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
<<<<<<< HEAD
        // 安全地转换loginId为Integer
        Integer userId;
        if (loginId instanceof Integer) {
            userId = (Integer) loginId;
        } else {
            userId = Integer.parseInt(loginId.toString());
        }
        
        User user = userService.getUserById(userId);
=======
        User user = userService.getUserById((Integer) loginId);
>>>>>>> 50c7cad (全局异常管理，mybais，satoken添加)
        List<String> permissionList = new ArrayList<>();
        
        if (user != null) {
            // 所有用户都有的基础权限
            permissionList.add("user:profile:view");     // 查看个人资料
            permissionList.add("user:profile:edit");     // 编辑个人资料
            
            // 根据用户类型添加特定权限
            if (user.getUserType() == UserType.ADMIN) {
                // 管理员权限
                permissionList.add("user:*");            // 用户管理所有权限
                permissionList.add("dealer:*");          // 经销商管理所有权限
                permissionList.add("car:*");             // 车辆管理所有权限
                permissionList.add("system:*");          // 系统管理所有权限
            } 
            else if (user.getUserType() == UserType.DEALER) {
                // 经销商权限
                permissionList.add("dealer:info:edit");  // 编辑经销商信息
                permissionList.add("car:add");           // 添加车辆
                permissionList.add("car:edit");          // 编辑车辆
                permissionList.add("car:delete");        // 删除车辆
                permissionList.add("order:view");        // 查看订单
                permissionList.add("order:process");     // 处理订单
            }
            else {
                // 普通用户权限
                permissionList.add("car:view");          // 查看车辆
                permissionList.add("car:favorite");      // 收藏车辆
                permissionList.add("order:create");      // 创建订单
                permissionList.add("order:cancel");      // 取消订单
                permissionList.add("review:add");        // 添加评价
            }
        }
        
        return permissionList;
    }
    
    /**
     * 返回一个用户所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
<<<<<<< HEAD
        // 安全地转换loginId为Integer
        Integer userId;
        if (loginId instanceof Integer) {
            userId = (Integer) loginId;
        } else {
            userId = Integer.parseInt(loginId.toString());
        }
        
        User user = userService.getUserById(userId);
=======
        User user = userService.getUserById((Integer) loginId);
>>>>>>> 50c7cad (全局异常管理，mybais，satoken添加)
        List<String> roleList = new ArrayList<>();
        if (user != null) {
            // 添加用户类型对应的角色
            roleList.add(user.getUserType().name().toLowerCase());
            
            // 如果是管理员，额外添加admin角色
            if (user.getUserType() == UserType.ADMIN) {
                roleList.add("admin");
            }
        }
        return roleList;
    }
} 