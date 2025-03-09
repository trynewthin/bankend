package com.zhixuanche.admin.service.impl;

import com.zhixuanche.admin.dto.ContentAuditDTO;
import com.zhixuanche.admin.service.AuditLogService;
import com.zhixuanche.admin.service.ContentAuditService;
import com.zhixuanche.car.entity.Car;
import com.zhixuanche.car.entity.CarDetail;
import com.zhixuanche.car.entity.CarImage;
import com.zhixuanche.car.mapper.CarDetailMapper;
import com.zhixuanche.car.mapper.CarImageMapper;
import com.zhixuanche.car.mapper.CarMapper;
import com.zhixuanche.common.model.PageResult;
import com.zhixuanche.message.service.NotificationService;
import com.zhixuanche.user.entity.Dealer;
import com.zhixuanche.user.mapper.DealerMapper;
import cn.dev33.satoken.stp.StpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 内容审核服务实现
 */
@Service
public class ContentAuditServiceImpl implements ContentAuditService {

    @Autowired
    private CarMapper carMapper;
    
    @Autowired
    private CarDetailMapper carDetailMapper;
    
    @Autowired
    private CarImageMapper carImageMapper;
    
    @Autowired
    private DealerMapper dealerMapper;
    
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private AuditLogService auditLogService;
    
    @Override
    public PageResult<Car> getCarAuditList(String keyword, String auditStatus, Integer dealerId, 
                                          String startDate, String endDate, Integer page, Integer size) {
        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        
        // 添加关键词查询条件
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        
        // 添加审核状态查询条件
        if (StringUtils.isNotBlank(auditStatus)) {
            if ("PENDING".equalsIgnoreCase(auditStatus)) {
                params.put("status", 0);  // 待审核状态
            } else if ("APPROVED".equalsIgnoreCase(auditStatus)) {
                params.put("status", 1);  // 已通过状态
            } else if ("REJECTED".equalsIgnoreCase(auditStatus)) {
                params.put("status", 2);  // 已拒绝状态
            }
        }
        
        // 添加经销商ID查询条件
        if (dealerId != null) {
            params.put("dealerId", dealerId);
        }
        
        // 添加日期范围查询条件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(startDate)) {
            try {
                Date start = sdf.parse(startDate);
                params.put("startDate", start);
            } catch (ParseException e) {
                // 忽略无效的日期格式
            }
        }
        if (StringUtils.isNotBlank(endDate)) {
            try {
                Date end = sdf.parse(endDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(end);
                calendar.add(Calendar.DAY_OF_MONTH, 1); // 结束日期加1天，使查询包含当天
                params.put("endDate", calendar.getTime());
            } catch (ParseException e) {
                // 忽略无效的日期格式
            }
        }
        
        // 添加分页参数
        params.put("offset", (page - 1) * size);
        params.put("limit", size);
        
        // 查询总记录数
        int total = carMapper.countSearchResult(params);
        
        // 查询当前页数据
        List<Car> cars = carMapper.searchByKeyword(params);
        
        // 计算总页数
        int pages = (total + size - 1) / size;
        
        // 返回分页结果
        PageResult<Car> pageResult = new PageResult<>(total, page, size, cars);
        pageResult.setPages(pages);
        
        return pageResult;
    }

    @Override
    @Transactional
    public boolean auditCar(Integer carId, ContentAuditDTO auditDTO) {
        // 获取车辆信息
        Car car = carMapper.selectById(carId);
        if (car == null) {
            return false;
        }
        
        // 获取当前操作管理员ID
        Integer adminId = StpUtil.getLoginIdAsInt();
        
        // 获取经销商ID和经销商关联的用户ID
        Integer dealerId = car.getDealerId();
        Dealer dealer = dealerMapper.findById(dealerId);
        Integer dealerUserId = dealer != null ? dealer.getUserId() : null;
        
        // 更新车辆状态
        int result = carMapper.updateStatus(carId, auditDTO.getStatus());
        
        if (result > 0) {
            // 发送通知给经销商
            sendAuditNotificationToDealer(dealerUserId, car, auditDTO);
            
            // 记录审核日志
            logCarAuditOperation(carId, adminId, auditDTO);
        }
        
        return result > 0;
    }

    /**
     * 发送车辆审核通知给经销商
     */
    private void sendAuditNotificationToDealer(Integer dealerUserId, Car car, ContentAuditDTO auditDTO) {
        if (dealerUserId == null) {
            return;
        }
        
        String title = "车辆信息审核通知";
        String content;
        String noticeType;
        
        if (auditDTO.getStatus() == 1) {
            // 审核通过
            content = String.format("您的车辆 [%s %s] 信息已审核通过，现在可以在平台上展示了。", 
                    car.getBrand(), car.getModel());
            noticeType = "CAR_APPROVED";
        } else {
            // 审核拒绝
            content = String.format("您的车辆 [%s %s] 信息审核未通过。原因：%s", 
                    car.getBrand(), car.getModel(), 
                    StringUtils.isNotBlank(auditDTO.getRemarks()) ? auditDTO.getRemarks() : "信息不符合要求");
            noticeType = "CAR_REJECTED";
        }
        
        notificationService.sendSystemNotification(dealerUserId, title, content, noticeType);
    }
    
    /**
     * 记录车辆审核操作日志
     */
    private void logCarAuditOperation(Integer carId, Integer adminId, ContentAuditDTO auditDTO) {
        String content = String.format("审核车辆信息：%s", 
                auditDTO.getStatus() == 1 ? "通过" : "拒绝");
        
        if (StringUtils.isNotBlank(auditDTO.getRemarks())) {
            content += String.format("，备注：%s", auditDTO.getRemarks());
        }
        
        auditLogService.logCarAudit(carId, adminId, auditDTO.getStatus(), content);
    }

    @Override
    public Map<String, Object> getCarDetail(Integer carId) {
        // 获取车辆基本信息
        Car car = carMapper.selectById(carId);
        if (car == null) {
            return null;
        }
        
        // 获取车辆详情
        CarDetail detail = carDetailMapper.selectByCarId(carId);
        
        // 获取车辆图片
        List<CarImage> images = carImageMapper.selectByCarId(carId);
        
        // 获取经销商信息
        Dealer dealer = dealerMapper.findById(car.getDealerId());
        
        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("basic", car);
        result.put("detail", detail);
        result.put("images", images);
        result.put("dealer", dealer);
        
        return result;
    }
    
    @Override
    @Transactional
    public Map<String, Object> deleteCar(Integer carId) {
        Map<String, Object> result = new HashMap<>();
        
        // 检查车辆是否存在
        Car car = carMapper.selectById(carId);
        if (car == null) {
            result.put("success", false);
            result.put("message", "车辆不存在");
            return result;
        }
        
        // 检查车辆是否有关联的预约
        int appointmentCount = carMapper.countAppointmentsByCarId(carId);
        if (appointmentCount > 0) {
            result.put("success", false);
            result.put("message", "该车辆有关联的预约记录，无法删除");
            return result;
        }
        
        try {
            // 删除车辆图片
            carImageMapper.deleteByCarId(carId);
            
            // 删除车辆详情
            carDetailMapper.deleteByCarId(carId);
            
            // 删除车辆
            int deleteResult = carMapper.delete(carId);
            
            if (deleteResult > 0) {
                result.put("success", true);
                result.put("message", "车辆删除成功");
                
                // 获取经销商的用户ID，发送通知
                Integer dealerId = car.getDealerId();
                if (dealerId != null) {
                    Dealer dealer = dealerMapper.findById(dealerId);
                    if (dealer != null && dealer.getUserId() != null) {
                        sendCarDeletionNotification(dealer.getUserId(), car);
                    }
                }
            } else {
                result.put("success", false);
                result.put("message", "车辆删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "车辆删除过程中发生错误：" + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 发送车辆删除通知给经销商
     */
    private void sendCarDeletionNotification(Integer dealerUserId, Car car) {
        String title = "车辆信息删除通知";
        String content = String.format("您发布的车辆信息（%s %s）已被管理员删除", 
                                     car.getBrand(), car.getModel());
        String noticeType = "CAR_DELETED";
        
        notificationService.sendSystemNotification(dealerUserId, title, content, noticeType);
    }
} 