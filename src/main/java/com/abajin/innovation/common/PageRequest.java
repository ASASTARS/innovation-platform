package com.abajin.innovation.common;

import lombok.Data;

/**
 * 分页查询参数基类
 */
@Data
public class PageRequest {
    /**
     * 当前页码（从1开始），默认1
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小，默认10
     */
    private Integer pageSize = 10;

    /**
     * 获取偏移量（用于数据库查询）
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
