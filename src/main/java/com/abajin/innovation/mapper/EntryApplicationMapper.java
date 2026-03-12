package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.EntryApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入驻申请Mapper接口
 */
@Mapper
public interface EntryApplicationMapper {
    /**
     * 根据ID查询入驻申请
     */
    EntryApplication selectById(@Param("id") Long id);

    /**
     * 分页查询入驻申请
     */
    List<EntryApplication> selectPage(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("teamName") String teamName,
            @Param("status") String status,
            @Param("approvalStatus") String approvalStatus,
            @Param("applicationType") String applicationType,
            @Param("applicantId") Long applicantId
    );

    /**
     * 统计入驻申请总数
     */
    int count(
            @Param("teamName") String teamName,
            @Param("status") String status,
            @Param("approvalStatus") String approvalStatus,
            @Param("applicationType") String applicationType,
            @Param("applicantId") Long applicantId
    );

    /**
     * 插入入驻申请
     */
    int insert(EntryApplication entryApplication);

    /**
     * 更新入驻申请
     */
    int update(EntryApplication entryApplication);

    /**
     * 删除入驻申请
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据申请人ID查询申请列表
     */
    List<EntryApplication> selectByApplicantId(@Param("applicantId") Long applicantId);
}
