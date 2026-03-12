package com.abajin.innovation.common;

import lombok.Data;

import java.util.List;

/**
 * 分页结果封装
 * 统一使用 pageNum / pageSize 作为分页参数
 */
@Data
public class PageResult<T> {
    /**
     * 当前页码（从1开始）
     */
    private Integer pageNum;
    
    /**
     * 每页大小
     */
    private Integer pageSize;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 数据列表
     */
    private List<T> list;

    public PageResult() {
    }

    public PageResult(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
        this.list = list;
    }

    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        return new PageResult<>(pageNum, pageSize, total, list);
    }
}
