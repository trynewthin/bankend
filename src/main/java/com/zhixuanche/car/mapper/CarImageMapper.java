package com.zhixuanche.car.mapper;

import com.zhixuanche.car.entity.CarImage;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 车辆图片数据访问接口
 */
@Mapper
public interface CarImageMapper {
    
    /**
     * 根据车辆ID查询图片
     * @param carId 车辆ID
     * @return 图片列表
     */
    @Select("SELECT * FROM CarImages WHERE car_id = #{carId}")
    List<CarImage> selectByCarId(Integer carId);
    
    /**
     * 根据图片ID查询
     * @param imageId 图片ID
     * @return 图片信息
     */
    @Select("SELECT * FROM CarImages WHERE image_id = #{imageId}")
    CarImage selectById(Integer imageId);
    
    /**
     * 查询车辆缩略图
     * @param carId 车辆ID
     * @return 缩略图信息
     */
    @Select("SELECT * FROM CarImages WHERE car_id = #{carId} AND image_type = '缩略图' LIMIT 1")
    CarImage selectThumbnail(Integer carId);
    
    /**
     * 插入车辆图片
     * @param carImage 车辆图片实体
     * @return 影响行数
     */
    @Insert("INSERT INTO CarImages (car_id, image_type, image_url, upload_time) " +
            "VALUES (#{carId}, #{imageType}, #{imageUrl}, #{uploadTime})")
    @Options(useGeneratedKeys = true, keyProperty = "imageId", keyColumn = "image_id")
    int insert(CarImage carImage);
    
    /**
     * 删除图片
     * @param imageId 图片ID
     * @return 影响行数
     */
    @Delete("DELETE FROM CarImages WHERE image_id = #{imageId}")
    int delete(Integer imageId);
    
    /**
     * 删除车辆所有图片
     * @param carId 车辆ID
     * @return 影响行数
     */
    @Delete("DELETE FROM CarImages WHERE car_id = #{carId}")
    int deleteByCarId(Integer carId);
    
    /**
     * 查询车辆图片数量
     * @param carId 车辆ID
     * @return 图片数量
     */
    @Select("SELECT COUNT(*) FROM CarImages WHERE car_id = #{carId}")
    int countByCarId(Integer carId);
    
    /**
     * 根据车辆ID和图片类型查询图片
     * @param carId 车辆ID
     * @param imageType 图片类型
     * @return 图片信息
     */
    @Select("SELECT * FROM CarImages WHERE car_id = #{carId} AND image_type = #{imageType} LIMIT 1")
    CarImage selectByCarIdAndType(@Param("carId") Integer carId, @Param("imageType") String imageType);
    
    /**
     * 批量查询多个车辆的图片
     * @param carIds 车辆ID列表
     * @return 图片列表
     */
    List<CarImage> selectBatchByCarIds(@Param("carIds") List<Integer> carIds);
    
    /**
     * 查询指定类型和数量的图片
     * @param carId 车辆ID
     * @param types 图片类型列表
     * @param orderBy 排序方式
     * @param limit 限制数量
     * @return 图片列表
     */
    List<CarImage> selectByCarIdAndTypes(
        @Param("carId") Integer carId, 
        @Param("types") List<String> types,
        @Param("orderBy") String orderBy,
        @Param("limit") Integer limit
    );
    
    /**
     * 查询车辆全部图片并按类型分组
     * @param carId 车辆ID
     * @return 分组统计结果
     */
    List<Map<String, Object>> selectGroupByType(@Param("carId") Integer carId);
    
    /**
     * 批量插入图片
     * @param images 图片列表
     * @return 影响行数
     */
    int batchInsert(List<CarImage> images);
    
    /**
     * 更新图片信息
     * @param carImage 图片信息
     * @return 影响行数
     */
    int update(CarImage carImage);
} 