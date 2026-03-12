package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.SpaceType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 空间类型Mapper接口
 */
@Mapper
public interface SpaceTypeMapper {

    /**
     * 查询所有空间类型
     */
    List<SpaceType> selectAll();

    /**
     * 根据ID查询
     */
    SpaceType selectById(Long id);
}
