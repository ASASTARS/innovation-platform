package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.ActivityType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 活动类型Mapper接口
 */
@Mapper
public interface ActivityTypeMapper {

    /**
     * 查询所有活动类型
     */
    List<ActivityType> selectAll();

    /**
     * 根据ID查询
     */
    ActivityType selectById(Long id);
}
