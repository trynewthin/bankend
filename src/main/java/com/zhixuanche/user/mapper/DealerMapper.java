package com.zhixuanche.user.mapper;

import com.zhixuanche.user.entity.Dealer;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 经销商Mapper接口
 */
@Mapper
public interface DealerMapper {
    
    /**
     * 通过ID查询经销商
     */
    @Select("SELECT * FROM Dealers WHERE dealer_id = #{dealerId}")
    Dealer findById(Integer dealerId);
    
    /**
     * 通过用户ID查询经销商
     */
    @Select("SELECT * FROM Dealers WHERE user_id = #{userId}")
    Dealer findByUserId(Integer userId);
    
    /**
     * 插入新经销商
     */
    @Insert("INSERT INTO Dealers(user_id, dealer_name, address, business_license, " +
            "contact_person, contact_phone, status, description) " +
            "VALUES(#{userId}, #{dealerName}, #{address}, #{businessLicense}, " +
            "#{contactPerson}, #{contactPhone}, #{status}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "dealerId")
    int insert(Dealer dealer);
    
    /**
     * 更新经销商信息
     */
    @Update("UPDATE Dealers SET dealer_name = #{dealerName}, address = #{address}, " +
            "business_license = #{businessLicense}, contact_person = #{contactPerson}, " +
            "contact_phone = #{contactPhone}, description = #{description} " +
            "WHERE dealer_id = #{dealerId}")
    int update(Dealer dealer);
    
    /**
     * 更新经销商状态
     */
    @Update("UPDATE Dealers SET status = #{status} WHERE dealer_id = #{dealerId}")
    int updateStatus(@Param("dealerId") Integer dealerId, @Param("status") Integer status);
    
    /**
     * 获取待审核经销商列表
     */
    @Select("SELECT * FROM Dealers WHERE status = 0")
    List<Dealer> findPendingDealers();
    
    /**
     * 获取已审核经销商列表
     */
    @Select("SELECT * FROM Dealers WHERE status = 1")
    List<Dealer> findApprovedDealers();
    
    /**
     * 获取已拒绝经销商列表
     */
    @Select("SELECT * FROM Dealers WHERE status = 2")
    List<Dealer> findRejectedDealers();
    
    /**
     * 获取所有经销商列表
     */
    @Select("SELECT * FROM Dealers")
    List<Dealer> findAllDealers();
    
    /**
     * 统计经销商总数
     */
    @Select("SELECT COUNT(*) FROM Dealers")
    int countAllDealers();
    
    /**
     * 根据条件查询经销商
     * @param params 查询参数
     * @return 经销商列表
     */
    List<Dealer> findDealersByParams(Map<String, Object> params);
    
    /**
     * 根据条件统计经销商数量
     * @param params 查询参数
     * @return 经销商数量
     */
    int countDealersByParams(Map<String, Object> params);
    
    /**
     * 根据用户ID删除经销商
     * @param userId 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM Dealers WHERE user_id = #{userId}")
    int deleteByUserId(Integer userId);
    
    /**
     * 检查经销商是否有关联的车辆
     * @param dealerId 经销商ID
     * @return 关联车辆数量
     */
    @Select("SELECT COUNT(*) FROM Cars WHERE dealer_id = #{dealerId}")
    int countCarsByDealerId(Integer dealerId);
    
    /**
     * 根据ID删除经销商
     * @param dealerId 经销商ID
     * @return 影响行数
     */
    @Delete("DELETE FROM Dealers WHERE dealer_id = #{dealerId}")
    int deleteById(Integer dealerId);
} 