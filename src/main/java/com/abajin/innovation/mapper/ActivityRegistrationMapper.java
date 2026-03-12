package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.ActivityRegistration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动报名Mapper接口
 */
@Mapper
public interface ActivityRegistrationMapper {
    /**
     * 根据ID查询报名
     */
    ActivityRegistration selectById(@Param("id") Long id);

    /**
     * 根据活动ID查询报名列表
     */
    List<ActivityRegistration> selectByActivityId(@Param("activityId") Long activityId);

    /**
     * 根据用户ID查询报名列表
     */
    List<ActivityRegistration> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询报名列表（包含活动信息）
     */
    List<ActivityRegistration> selectByUserIdWithActivity(@Param("userId") Long userId);

    /**
     * 查询用户是否已报名活动
     */
    ActivityRegistration selectByActivityIdAndUserId(
            @Param("activityId") Long activityId,
            @Param("userId") Long userId
    );

    /**
     * 插入报名
     */
    int insert(ActivityRegistration registration);

    /**
     * 更新报名
     */
    int update(ActivityRegistration registration);

    /**
     * 删除报名
     */
    int deleteById(@Param("id") Long id);
}
