package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.ProjectApplicationForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectApplicationFormMapper {
    ProjectApplicationForm selectById(Long id);
    List<ProjectApplicationForm> selectAll();
    List<ProjectApplicationForm> selectByApplicantId(Long applicantId);
    List<ProjectApplicationForm> selectByStatus(String status);
    int insert(ProjectApplicationForm form);
    int update(ProjectApplicationForm form);
    int deleteById(Long id);
}
