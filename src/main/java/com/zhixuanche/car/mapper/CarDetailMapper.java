package com.zhixuanche.car.mapper;

import com.zhixuanche.car.entity.CarDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 车辆详情数据访问接口
 */
@Mapper
public interface CarDetailMapper {
    
    /**
     * 根据车辆ID查询详情
     * @param carId 车辆ID
     * @return 车辆详情
     */
    @Select("SELECT * FROM CarDetails WHERE car_id = #{carId}")
    CarDetail selectByCarId(Integer carId);
    
    /**
     * 根据详情ID查询
     * @param detailId 详情ID
     * @return 车辆详情
     */
    @Select("SELECT * FROM CarDetails WHERE detail_id = #{detailId}")
    CarDetail selectById(Integer detailId);
    
    /**
     * 插入车辆详情
     * @param carDetail 车辆详情实体
     * @return 影响行数
     */
    @Insert("INSERT INTO CarDetails (car_id, engine, transmission, fuel_type, fuel_consumption, seats, " +
            "color, body_size, wheelbase, features, warranty) " +
            "VALUES (#{carId}, #{engine}, #{transmission}, #{fuelType}, #{fuelConsumption}, #{seats}, " +
            "#{color}, #{bodySize}, #{wheelbase}, #{features}, #{warranty})")
    @Options(useGeneratedKeys = true, keyProperty = "detailId", keyColumn = "detail_id")
    int insert(CarDetail carDetail);
    
    /**
     * 更新车辆详情
     * @param carDetail 车辆详情实体
     * @return 影响行数
     */
    int update(CarDetail carDetail);
    
    /**
     * 删除车辆详情
     * @param carId 车辆ID
     * @return 影响行数
     */
    @Delete("DELETE FROM CarDetails WHERE car_id = #{carId}")
    int deleteByCarId(Integer carId);
    
    /**
     * 根据车辆ID和详情ID查询详情
     * @param carId 车辆ID
     * @param detailId 详情ID
     * @return 车辆详情
     */
    CarDetail selectByCarIdAndDetailId(@Param("carId") Integer carId, @Param("detailId") Integer detailId);
    
    /**
     * 批量查询车辆详情
     * @param carIds 车辆ID列表
     * @return 车辆详情列表
     */
    List<CarDetail> selectBatchByCarIds(@Param("carIds") List<Integer> carIds);
} 