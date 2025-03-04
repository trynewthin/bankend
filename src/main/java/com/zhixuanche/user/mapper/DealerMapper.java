package com.zhixuanche.user.mapper;

import com.zhixuanche.user.entity.Dealer;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
} 