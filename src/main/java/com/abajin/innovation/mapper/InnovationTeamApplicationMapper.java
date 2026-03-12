package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.InnovationTeamApplication;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InnovationTeamApplicationMapper {
    InnovationTeamApplication selectById(Long id);
    List<InnovationTeamApplication> selectAll();
    List<InnovationTeamApplication> selectByApplicantId(Long applicantId);
    List<InnovationTeamApplication> selectByStatus(String status);
    int insert(InnovationTeamApplication application);
    int update(InnovationTeamApplication application);
    int deleteById(Long id);
}
