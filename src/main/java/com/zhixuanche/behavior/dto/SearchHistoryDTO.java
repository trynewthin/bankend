package com.zhixuanche.behavior.dto;

import lombok.Data;
import java.util.Date;

/**
 * 搜索历史数据传输对象
 */
@Data
public class SearchHistoryDTO {
    /**
     * 行为ID
     */
    private Integer behaviorId;
    
    /**
     * 搜索关键词
     */
    private String searchKeywords;
    
    /**
     * 搜索时间
     */
    private Date searchTime;
} 