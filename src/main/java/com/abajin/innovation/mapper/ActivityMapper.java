package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动Mapper接口
 */
@Mapper
public interface ActivityMapper {
    /**
     * 根据ID查询活动
     */
    Activity selectById(@Param("id") Long id);

    /**
     * 分页查询活动
     */
    List<Activity> selectPage(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("status") String status,
            @Param("approvalStatus") String approvalStatus,
            @Param("activityTypeId") Long activityTypeId,
            @Param("keyword") String keyword
    );

    /**
     * 分页查询活动（非管理员可见范围：已通过 + 自己创建 + 自己报名）
     */
    List<Activity> selectPageVisibleToUser(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("status") String status,
            @Param("activityTypeId") Long activityTypeId,
            @Param("keyword") String keyword,
            @Param("viewerUserId") Long viewerUserId
    );

    /**
     * 统计总数
     */
    int count(
            @Param("status") String status,
            @Param("approvalStatus") String approvalStatus,
            @Param("activityTypeId") Long activityTypeId,
            @Param("keyword") String keyword
    );

    int countVisibleToUser(
            @Param("status") String status,
            @Param("activityTypeId") Long activityTypeId,
            @Param("keyword") String keyword,
            @Param("viewerUserId") Long viewerUserId
    );

    /**
     * 根据组织者ID查询活动列表
     */
    List<Activity> selectByOrganizerId(@Param("organizerId") Long organizerId);

    /**
     * 根据状态查询活动列表
     */
    List<Activity> selectByStatus(@Param("status") String status);

    /**
     * 插入活动
     */
    int insert(Activity activity);

    /**
     * 更新活动
     */
    int update(Activity activity);

    /**
     * 删除活动
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询某空间在指定时间范围内有占用的活动（用于冲突检测与占用展示）
     * 排除已取消；可选排除指定活动ID（编辑时）
     */
    List<Activity> selectBySpaceIdAndDateTimeOverlap(
            @Param("spaceId") Long spaceId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("excludeActivityId") Long excludeActivityId
    );
}
