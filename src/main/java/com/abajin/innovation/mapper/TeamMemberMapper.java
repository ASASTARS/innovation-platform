package com.abajin.innovation.mapper;

import com.abajin.innovation.entity.TeamMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeamMemberMapper {
    TeamMember selectById(Long id);
    List<TeamMember> selectByTeamId(Long teamId);
    List<TeamMember> selectByUserId(Long userId);
    TeamMember selectByTeamIdAndUserId(@Param("teamId") Long teamId, @Param("userId") Long userId);
    int insert(TeamMember teamMember);
    int update(TeamMember teamMember);
    int deleteById(Long id);
    int deleteByTeamIdAndUserId(@Param("teamId") Long teamId, @Param("userId") Long userId);
}
