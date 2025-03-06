package com.zhixuanche.common.model;

import lombok.Data;
import java.util.List;

/**
 * 分页查询结果
 */
@Data
public class PageResult<T> {
    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 当前页码
     */
    private int pageNum;
    
    /**
     * 每页大小
     */
    private int pageSize;
    
    /**
     * 总页数
     */
    private int pages;
    
    /**
     * 数据列表
     */
    private List<T> list;
    
    public PageResult() {}
    
    public PageResult(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }
    
    public PageResult(long total, int pageNum, int pageSize, List<T> list) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.list = list;
        this.pages = (int) Math.ceil((double) total / pageSize);
    }
}