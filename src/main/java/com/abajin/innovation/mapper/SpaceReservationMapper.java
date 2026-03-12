package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.SpaceReservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 空间预定Mapper接口
 */
@Mapper
public interface SpaceReservationMapper {
    /**
     * 根据ID查询预定
     */
    SpaceReservation selectById(@Param("id") Long id);

    /**
     * 根据申请人ID查询预定列表
     */
    List<SpaceReservation> selectByApplicantId(@Param("applicantId") Long applicantId);

    /**
     * 根据空间ID查询预定列表
     */
    List<SpaceReservation> selectBySpaceId(@Param("spaceId") Long spaceId);

    /**
     * 查询指定日期和时间的预定（用于检查冲突）
     */
    List<SpaceReservation> selectBySpaceAndTime(
            @Param("spaceId") Long spaceId,
            @Param("reservationDate") LocalDate reservationDate,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

    /**
     * 根据状态查询预定列表
     */
    List<SpaceReservation> selectByStatus(@Param("status") String status);

    /**
     * 查询所有预定
     */
    List<SpaceReservation> selectAll();

    /**
     * 插入预定
     */
    int insert(SpaceReservation reservation);

    /**
     * 更新预定
     */
    int update(SpaceReservation reservation);

    /**
     * 删除预定
     */
    int deleteById(@Param("id") Long id);
}
