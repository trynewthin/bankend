package com.zhixuanche.car.service;

import com.zhixuanche.car.entity.CarImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 车辆图片服务接口
 */
public interface CarImageService {
    
    /**
     * 获取车辆所有图片
     * @param carId 车辆ID
     * @return 图片列表
     */
    List<CarImage> getCarImages(Integer carId);
    
    /**
     * 获取车辆缩略图
     * @param carId 车辆ID
     * @return 缩略图信息
     */
    CarImage getCarThumbnail(Integer carId);
    
    /**
     * 上传车辆图片
     * @param carId 车辆ID
     * @param imageFile 图片文件
     * @param imageType 图片类型
     * @return 上传后的图片信息
     */
    CarImage uploadCarImage(Integer carId, MultipartFile imageFile, String imageType);
    
    /**
     * 删除车辆图片
     * @param imageId 图片ID
     * @return 是否成功
     */
    boolean deleteCarImage(Integer imageId);
    
    /**
     * 删除车辆所有图片
     * @param carId 车辆ID
     * @return 是否成功
     */
    boolean deleteAllCarImages(Integer carId);
    
    /**
     * 获取指定类型的车辆图片
     * @param carId 车辆ID
     * @param imageType 图片类型
     * @return 图片信息
     */
    CarImage getImageByType(Integer carId, String imageType);
    
    /**
     * 检查图片是否存在
     * @param imageId 图片ID
     * @return 是否存在
     */
    boolean exists(Integer imageId);
}