package com.zhixuanche.car.service.impl;

import com.zhixuanche.car.entity.CarImage;
import com.zhixuanche.car.mapper.CarImageMapper;
import com.zhixuanche.car.service.CarImageService;
import com.zhixuanche.car.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 车辆图片服务实现类
 */
@Service
public class CarImageServiceImpl implements CarImageService {

    @Autowired
    private CarImageMapper carImageMapper;
    
    @Autowired
    @Qualifier("carFileStorageService")
    private FileStorageService fileStorageService;
    
    /**
     * 获取车辆所有图片
     * @param carId 车辆ID
     * @return 图片列表
     */
    @Override
    public List<CarImage> getCarImages(Integer carId) {
        return carImageMapper.selectByCarId(carId);
    }
    
    /**
     * 获取车辆缩略图
     * @param carId 车辆ID
     * @return 缩略图信息
     */
    @Override
    public CarImage getCarThumbnail(Integer carId) {
        return carImageMapper.selectThumbnail(carId);
    }
    
    /**
     * 上传车辆图片
     * @param carId 车辆ID
     * @param imageFile 图片文件
     * @param imageType 图片类型
     * @return 上传后的图片信息
     */
    @Override
    @Transactional
    public CarImage uploadCarImage(Integer carId, MultipartFile imageFile, String imageType) {
        // 检查同类型图片是否已存在
        CarImage existingImage = carImageMapper.selectByCarIdAndType(carId, imageType);
        if (existingImage != null) {
            // 删除已有图片
            fileStorageService.deleteFile(existingImage.getImageUrl());
            carImageMapper.delete(existingImage.getImageId());
        }
        
        // 存储新图片，使用改进的目录格式：cars/carId/imageType
        String directory = "cars/" + carId + "/" + imageType;
        String imageUrl = fileStorageService.storeFile(imageFile, directory);
        
        // 创建图片记录
        CarImage carImage = new CarImage();
        carImage.setCarId(carId);
        carImage.setImageType(imageType);
        carImage.setImageUrl(imageUrl);
        carImage.setUploadTime(LocalDateTime.now());
        
        // 保存到数据库
        carImageMapper.insert(carImage);
        
        return carImage;
    }
    
    /**
     * 删除车辆图片
     * @param imageId 图片ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteCarImage(Integer imageId) {
        // 获取图片信息
        CarImage image = carImageMapper.selectById(imageId);
        if (image == null) {
            return false;
        }
        
        // 删除文件
        fileStorageService.deleteFile(image.getImageUrl());
        
        // 删除数据库记录
        return carImageMapper.delete(imageId) > 0;
    }
    
    /**
     * 删除车辆所有图片
     * @param carId 车辆ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteAllCarImages(Integer carId) {
        // 获取所有图片
        List<CarImage> images = carImageMapper.selectByCarId(carId);
        
        // 删除所有图片文件
        for (CarImage image : images) {
            fileStorageService.deleteFile(image.getImageUrl());
        }
        
        // 删除数据库记录
        return carImageMapper.deleteByCarId(carId) > 0;
    }
    
    /**
     * 获取指定类型的车辆图片
     * @param carId 车辆ID
     * @param imageType 图片类型
     * @return 图片信息
     */
    @Override
    public CarImage getImageByType(Integer carId, String imageType) {
        return carImageMapper.selectByCarIdAndType(carId, imageType);
    }

    /**
     * 检查图片是否存在
     * @param imageId 图片ID
     * @return 是否存在
     */
    @Override
    public boolean exists(Integer imageId) {
        return carImageMapper.selectById(imageId) != null;
    }
} 