package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.FundType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 基金类型Mapper接口
 */
@Mapper
public interface FundTypeMapper {
    /**
     * 查询所有基金类型
     */
    List<FundType> selectAll();

    /**
     * 根据ID查询
     */
    FundType selectById(Long id);
}
