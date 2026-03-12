package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.FundApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基金申请Mapper接口
 */
@Mapper
public interface FundApplicationMapper {
    /**
     * 根据ID查询基金申请
     */
    FundApplication selectById(@Param("id") Long id);

    /**
     * 根据申请人ID查询申请列表
     */
    List<FundApplication> selectByApplicantId(@Param("applicantId") Long applicantId);

    /**
     * 根据申请人ID + 审批状态查询申请列表
     */
    List<FundApplication> selectByApplicantIdAndApprovalStatus(@Param("applicantId") Long applicantId,
                                                               @Param("approvalStatus") String approvalStatus);

    /**
     * 根据项目ID查询申请列表
     */
    List<FundApplication> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据状态查询申请列表
     */
    List<FundApplication> selectByStatus(@Param("status") String status);

    /**
     * 根据审批状态查询申请列表
     */
    List<FundApplication> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus);

    List<FundApplication> selectByStatusAndApprovalStatus(@Param("status") String status,
                                                          @Param("approvalStatus") String approvalStatus);

    /**
     * 查询所有基金申请
     */
    List<FundApplication> selectAll();

    /**
     * 插入申请
     */
    int insert(FundApplication application);

    /**
     * 更新申请
     */
    int update(FundApplication application);

    /**
     * 删除申请
     */
    int deleteById(@Param("id") Long id);
}
