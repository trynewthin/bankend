package com.zhixuanche.behavior.service.impl;

import com.zhixuanche.behavior.dto.FavoriteDTO;
import com.zhixuanche.behavior.entity.Favorite;
import com.zhixuanche.behavior.mapper.FavoriteMapper;
import com.zhixuanche.behavior.service.FavoriteService;
import com.zhixuanche.common.model.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 收藏服务实现类
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {
    
    private static final Logger log = LoggerFactory.getLogger(FavoriteServiceImpl.class);
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Override
    @Transactional
    public Integer addFavorite(Integer userId, Integer carId) {
        // 检查是否已收藏
        if (favoriteMapper.existsByUserIdAndCarId(userId, carId) > 0) {
            return null;
        }
        
        // 添加收藏记录
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setCarId(carId);
        favorite.setCreateTime(new Date());
        favoriteMapper.insert(favorite);
        
        return favorite.getFavoriteId();
    }
    
    @Override
    @Transactional
    public void removeFavorite(Integer userId, Integer carId) {
        favoriteMapper.deleteByUserIdAndCarId(userId, carId);
    }
    
    @Override
    public boolean isFavorite(Integer userId, Integer carId) {
        return favoriteMapper.existsByUserIdAndCarId(userId, carId) > 0;
    }
    
    @Override
    public PageResult<FavoriteDTO> getFavoriteList(Integer userId, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Map<String, Object>> records = favoriteMapper.getFavoriteListWithCarInfo(userId, offset, pageSize);
        
        int total = favoriteMapper.countByUserId(userId);
        
        List<FavoriteDTO> dtoList = new ArrayList<>();
        for (Map<String, Object> record : records) {
            try {
                FavoriteDTO dto = new FavoriteDTO();
                dto.setCarId((Integer) record.get("car_id"));
                dto.setBrand((String) record.get("brand"));
                dto.setModel((String) record.get("model"));
                
                // 处理价格字段，支持BigDecimal到Double的安全转换
                Object priceObj = record.get("price");
                if (priceObj instanceof BigDecimal) {
                    dto.setPrice(((BigDecimal) priceObj).doubleValue());
                } else if (priceObj instanceof Double) {
                    dto.setPrice((Double) priceObj);
                } else if (priceObj != null) {
                    // 其他类型尝试转换为Double
                    dto.setPrice(Double.valueOf(priceObj.toString()));
                }
                
                dto.setCreateTime((Date) record.get("create_time"));
                dto.setThumbnailUrl((String) record.get("thumbnail_url"));
                dtoList.add(dto);
            } catch (Exception e) {
                log.error("转换收藏记录时出错: {}", e.getMessage(), e);
                // 继续处理下一条记录
            }
        }
        
        return new PageResult<>(total, pageNum, pageSize, dtoList);
    }
    
    @Override
    public int getFavoriteCount(Integer userId) {
        return favoriteMapper.countByUserId(userId);
    }
    
    @Override
    public int getCarFavoriteCount(Integer carId) {
        return favoriteMapper.countByCarId(carId);
    }
} 