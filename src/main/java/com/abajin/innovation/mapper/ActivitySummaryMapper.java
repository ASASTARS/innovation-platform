package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.ActivitySummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动总结Mapper接口
 */
@Mapper
public interface ActivitySummaryMapper {
    /**
     * 根据ID查询总结
     */
    ActivitySummary selectById(@Param("id") Long id);

    /**
     * 根据活动ID查询总结
     */
    ActivitySummary selectByActivityId(@Param("activityId") Long activityId);

    /**
     * 插入总结
     */
    int insert(ActivitySummary summary);

    /**
     * 更新总结
     */
    int update(ActivitySummary summary);

    /**
     * 删除总结
     */
    int deleteById(@Param("id") Long id);

    /**
     * 分页查询所有活动总结（学院管理员用，关联活动标题）
     */
    List<ActivitySummary> selectPageForAdmin(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 统计活动总结总数
     */
    int countForAdmin();
}
