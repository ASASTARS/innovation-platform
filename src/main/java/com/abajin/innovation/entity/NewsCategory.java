package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 新闻分类实体类
 */
@Data
public class NewsCategory {
    private Long id;
    private String name; // 分类名称
    private String code; // 分类代码
    private String description; // 分类描述
    private Integer sortOrder; // 排序顺序
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
