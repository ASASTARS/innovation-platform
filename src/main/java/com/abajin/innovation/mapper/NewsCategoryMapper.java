package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.NewsCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 新闻分类Mapper接口
 */
@Mapper
public interface NewsCategoryMapper {

    /**
     * 查询所有分类
     */
    List<NewsCategory> selectAll();

    /**
     * 根据ID查询
     */
    NewsCategory selectById(Long id);
}
