package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.ProjectApplicationMember;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProjectApplicationMemberMapper {
    ProjectApplicationMember selectById(Long id);
    List<ProjectApplicationMember> selectByApplicationFormId(Long applicationFormId);
    int insert(ProjectApplicationMember member);
    int update(ProjectApplicationMember member);
    int deleteById(Long id);
    int deleteByApplicationFormId(Long applicationFormId);
}
