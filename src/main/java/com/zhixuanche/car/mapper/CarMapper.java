package com.zhixuanche.car.mapper;

import com.zhixuanche.car.entity.Car;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 车辆数据访问接口
 */
@Mapper
public interface CarMapper {
    
    /**
     * 查询所有车辆
     * @param params 查询参数
     * @return 车辆列表
     */
    List<Car> selectAll(Map<String, Object> params);
    
    /**
     * 查询车辆总数
     * @param params 查询参数
     * @return 车辆总数
     */
    int selectCount(Map<String, Object> params);
    
    /**
     * 根据ID查询车辆
     * @param carId 车辆ID
     * @return 车辆信息
     */
    @Select("SELECT * FROM Cars WHERE car_id = #{carId}")
    Car selectById(Integer carId);
    
    /**
     * 根据经销商ID查询车辆
     * @param dealerId 经销商ID
     * @return 车辆列表
     */
    @Select("SELECT * FROM Cars WHERE dealer_id = #{dealerId}")
    List<Car> selectByDealerId(Integer dealerId);
    
    /**
     * 插入车辆
     * @param car 车辆实体
     * @return 影响行数
     */
    @Insert("INSERT INTO Cars (dealer_id, brand, model, year, price, category, status, create_time) " +
            "VALUES (#{dealerId}, #{brand}, #{model}, #{year}, #{price}, #{category}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "carId", keyColumn = "car_id")
    int insert(Car car);
    
    /**
     * 更新车辆
     * @param car 车辆实体
     * @return 影响行数
     */
    int update(Car car);
    
    /**
     * 更新车辆状态
     * @param carId 车辆ID
     * @param status 状态值
     * @return 影响行数
     */
    @Update("UPDATE Cars SET status = #{status}, update_time = NOW() WHERE car_id = #{carId}")
    int updateStatus(@Param("carId") Integer carId, @Param("status") Integer status);
    
    /**
     * 删除车辆
     * @param carId 车辆ID
     * @return 影响行数
     */
    @Delete("DELETE FROM Cars WHERE car_id = #{carId}")
    int delete(Integer carId);
    
    /**
     * 查询所有品牌
     * @return 品牌列表
     */
    @Select("SELECT DISTINCT brand FROM Cars WHERE status = 1")
    List<String> findAllBrands();
    
    /**
     * 查询所有车型类别
     * @return 类别列表
     */
    @Select("SELECT DISTINCT category FROM Cars WHERE status = 1")
    List<String> findAllCategories();
    
    /**
     * 按品牌统计车辆数量
     * @return 品牌-数量映射
     */
    @Select("SELECT brand, COUNT(*) as count FROM Cars WHERE status = 1 GROUP BY brand")
    List<Map<String, Object>> countByBrand();
    
    /**
     * 按类别统计车辆数量
     * @return 类别-数量映射
     */
    @Select("SELECT category, COUNT(*) as count FROM Cars WHERE status = 1 GROUP BY category")
    List<Map<String, Object>> countByCategory();
    
    /**
     * 根据品牌查询车辆
     * @param brand 品牌名称
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 车辆列表
     */
    @Select("SELECT * FROM Cars WHERE brand = #{brand} AND status = 1 LIMIT #{limit} OFFSET #{offset}")
    List<Car> selectByBrand(@Param("brand") String brand, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 根据价格区间查询车辆
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 车辆列表
     */
    @Select("SELECT * FROM Cars WHERE price BETWEEN #{minPrice} AND #{maxPrice} AND status = 1 LIMIT #{limit} OFFSET #{offset}")
    List<Car> selectByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, 
                               @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 根据类别查询车辆
     * @param category 类别
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 车辆列表
     */
    @Select("SELECT * FROM Cars WHERE category = #{category} AND status = 1 LIMIT #{limit} OFFSET #{offset}")
    List<Car> selectByCategory(@Param("category") String category, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 根据关键词搜索车辆
     * @param params 包含关键词和分页参数
     * @return 车辆列表
     */
    List<Car> searchByKeyword(Map<String, Object> params);
    
    /**
     * 统计搜索结果总数
     * @param params 包含关键词的参数
     * @return 总数
     */
    int countSearchResult(Map<String, Object> params);
    
    /**
     * 多条件筛选车辆
     * @param params 筛选条件和分页参数
     * @return 车辆列表
     */
    List<Car> filterCars(Map<String, Object> params);
    
    /**
     * 统计筛选结果总数
     * @param params 筛选条件
     * @return 总数
     */
    int countFilterResult(Map<String, Object> params);
    
    /**
     * 检查车辆是否有相关预约
     * @param carId 车辆ID
     * @return 关联预约数量
     */
    @Select("SELECT COUNT(*) FROM Appointments WHERE car_id = #{carId}")
    int countAppointmentsByCarId(Integer carId);
} 