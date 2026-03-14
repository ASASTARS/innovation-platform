package com.abajin.innovation.service;

import com.abajin.innovation.entity.Team;
import com.abajin.innovation.entity.TeamMember;
import com.abajin.innovation.entity.User;
import com.abajin.innovation.mapper.TeamMapper;
import com.abajin.innovation.mapper.TeamMemberMapper;
import com.abajin.innovation.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 团队服务测试
 */
@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private TeamMemberMapper teamMemberMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private TeamService teamService;

    private User leader;
    private User member;
    private Team team;

    @BeforeEach
    void setUp() {
        leader = new User();
        leader.setId(1L);
        leader.setUsername("leader");
        leader.setRealName("队长");
        leader.setCollegeName("计算机学院");

        member = new User();
        member.setId(2L);
        member.setUsername("member");
        member.setRealName("队员");

        team = new Team();
        team.setId(1L);
        team.setName("创新团队");
        team.setDescription("这是一个创新团队");
        team.setLeaderId(1L);
        team.setLeaderName("队长");
        team.setMemberCount(1);
        team.setRecruiting(false);
        team.setIsPublic(true);
        team.setCollegeName("计算机学院");
    }

    @Test
    void createTeam_withValidData_returnsTeam() {
        // Arrange
        Team newTeam = new Team();
        newTeam.setName("新团队");
        newTeam.setDescription("描述");
        newTeam.setRecruiting(false);

        when(userMapper.selectById(1L)).thenReturn(leader);
        when(teamMapper.insert(any(Team.class))).thenReturn(1);
        when(teamMemberMapper.insert(any(TeamMember.class))).thenReturn(1);

        // Act
        Team result = teamService.createTeam(newTeam, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getLeaderId());
        assertEquals("队长", result.getLeaderName());
        assertEquals(1, result.getMemberCount());
        verify(teamMapper).insert(any(Team.class));
        verify(teamMemberMapper).insert(any(TeamMember.class));
    }

    @Test
    void createTeam_withRecruitingButNoRequirement_throwsException() {
        // Arrange
        Team newTeam = new Team();
        newTeam.setName("新团队");
        newTeam.setRecruiting(true);
        newTeam.setRecruitmentRequirement("  "); // 空白内容

        when(userMapper.selectById(1L)).thenReturn(leader);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teamService.createTeam(newTeam, 1L));
        assertEquals("开启招募时，招募内容不能为空", exception.getMessage());
    }

    @Test
    void createTeam_withNonExistentLeader_throwsException() {
        // Arrange
        when(userMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teamService.createTeam(team, 999L));
        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void updateTeam_withValidData_returnsUpdatedTeam() {
        // Arrange
        when(teamMapper.selectById(1L)).thenReturn(team);
        when(teamMapper.update(any(Team.class))).thenReturn(1);

        Team updateTeam = new Team();
        updateTeam.setName("更新后的名称");

        // Act
        Team result = teamService.updateTeam(1L, updateTeam, 1L);

        // Assert
        verify(teamMapper).update(any(Team.class));
    }

    @Test
    void updateTeam_withNonExistentTeam_throwsException() {
        // Arrange
        when(teamMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teamService.updateTeam(999L, team, 1L));
        assertEquals("团队不存在", exception.getMessage());
    }

    @Test
    void updateTeam_withUnauthorizedUser_throwsException() {
        // Arrange
        when(teamMapper.selectById(1L)).thenReturn(team);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teamService.updateTeam(1L, team, 999L));
        assertEquals("无权修改此团队", exception.getMessage());
    }

    @Test
    void addMember_withValidData_addsSuccessfully() {
        // Arrange
        when(teamMapper.selectById(1L)).thenReturn(team);
        when(teamMemberMapper.selectByTeamIdAndUserId(1L, 2L)).thenReturn(null);
        when(userMapper.selectById(2L)).thenReturn(member);
        when(teamMemberMapper.insert(any(TeamMember.class))).thenReturn(1);
        when(teamMapper.update(any(Team.class))).thenReturn(1);

        // Act
        teamService.addMember(1L, 2L, 1L);

        // Assert
        verify(teamMemberMapper).insert(any(TeamMember.class));
        verify(teamMapper).update(any(Team.class));
    }

    @Test
    void addMember_withExistingMember_throwsException() {
        // Arrange
        TeamMember existing = new TeamMember();
        when(teamMapper.selectById(1L)).thenReturn(team);
        when(teamMemberMapper.selectByTeamIdAndUserId(1L, 2L)).thenReturn(existing);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teamService.addMember(1L, 2L, 1L));
        assertEquals("该用户已经是团队成员", exception.getMessage());
    }

    @Test
    void addMember_withNonLeader_throwsException() {
        // Arrange
        when(teamMapper.selectById(1L)).thenReturn(team);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teamService.addMember(1L, 2L, 999L));
        assertEquals("只有队长可以添加成员", exception.getMessage());
    }

    @Test
    void removeMember_withValidData_removesSuccessfully() {
        // Arrange
        TeamMember teamMember = new TeamMember();
        teamMember.setId(2L);
        teamMember.setTeamId(1L);
        teamMember.setUserId(2L);
        teamMember.setRole("MEMBER");
        teamMember.setApprovalStatus("APPROVED");

        when(teamMapper.selectById(1L)).thenReturn(team);
        when(teamMemberMapper.selectById(2L)).thenReturn(teamMember);
        when(teamMemberMapper.deleteByTeamIdAndUserId(1L, 2L)).thenReturn(1);
        when(teamMapper.update(any(Team.class))).thenReturn(1);

        // Act
        teamService.removeMember(1L, 2L, 1L);

        // Assert
        verify(teamMemberMapper).deleteByTeamIdAndUserId(1L, 2L);
        verify(teamMapper).update(any(Team.class));
    }

    @Test
    void removeMember_withLeaderRole_throwsException() {
        // Arrange
        TeamMember leaderMember = new TeamMember();
        leaderMember.setId(1L);
        leaderMember.setTeamId(1L);
        leaderMember.setUserId(1L);
        leaderMember.setRole("LEADER");

        when(teamMapper.selectById(1L)).thenReturn(team);
        when(teamMemberMapper.selectById(1L)).thenReturn(leaderMember);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teamService.removeMember(1L, 1L, 1L));
        assertEquals("不能移除队长", exception.getMessage());
    }

    @Test
    void getTeamById_withExistingTeam_returnsTeam() {
        // Arrange
        when(teamMapper.selectById(1L)).thenReturn(team);

        // Act
        Team result = teamService.getTeamById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("创新团队", result.getName());
    }

    @Test
    void getTeamById_withNonExistingTeam_returnsNull() {
        // Arrange
        when(teamMapper.selectById(999L)).thenReturn(null);

        // Act
        Team result = teamService.getTeamById(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void isMember_withApprovedMember_returnsTrue() {
        // Arrange
        TeamMember member = new TeamMember();
        member.setApprovalStatus("APPROVED");
        when(teamMemberMapper.selectByTeamIdAndUserId(1L, 2L)).thenReturn(member);

        // Act
        boolean result = teamService.isMember(1L, 2L);

        // Assert
        assertTrue(result);
    }

    @Test
    void isMember_withPendingMember_returnsFalse() {
        // Arrange
        TeamMember member = new TeamMember();
        member.setApprovalStatus("PENDING");
        when(teamMemberMapper.selectByTeamIdAndUserId(1L, 2L)).thenReturn(member);

        // Act
        boolean result = teamService.isMember(1L, 2L);

        // Assert
        assertFalse(result);
    }

    @Test
    void isMember_withNonMember_returnsFalse() {
        // Arrange
        when(teamMemberMapper.selectByTeamIdAndUserId(1L, 999L)).thenReturn(null);

        // Act
        boolean result = teamService.isMember(1L, 999L);

        // Assert
        assertFalse(result);
    }

    @Test
    void reviewMemberApplication_withApproval_approvesMember() {
        // Arrange
        TeamMember application = new TeamMember();
        application.setId(2L);
        application.setTeamId(1L);
        application.setApprovalStatus("PENDING");

        when(teamMapper.selectById(1L)).thenReturn(team);
        when(teamMemberMapper.selectById(2L)).thenReturn(application);
        when(teamMemberMapper.update(any(TeamMember.class))).thenReturn(1);
        when(teamMapper.update(any(Team.class))).thenReturn(1);

        // Act
        TeamMember result = teamService.reviewMemberApplication(1L, 2L, "APPROVED", 1L);

        // Assert
        assertEquals("APPROVED", result.getApprovalStatus());
        verify(teamMapper).update(any(Team.class));
    }

    @Test
    void reviewMemberApplication_withRejection_rejectsMember() {
        // Arrange
        TeamMember application = new TeamMember();
        application.setId(2L);
        application.setTeamId(1L);
        application.setApprovalStatus("PENDING");

        when(teamMapper.selectById(1L)).thenReturn(team);
        when(teamMemberMapper.selectById(2L)).thenReturn(application);
        when(teamMemberMapper.update(any(TeamMember.class))).thenReturn(1);

        // Act
        TeamMember result = teamService.reviewMemberApplication(1L, 2L, "REJECTED", 1L);

        // Assert
        assertEquals("REJECTED", result.getApprovalStatus());
        verify(teamMapper, never()).update(any(Team.class));
    }

    @Test
    void reviewMemberApplication_withNonLeader_throwsException() {
        // Arrange
        when(teamMapper.selectById(1L)).thenReturn(team);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teamService.reviewMemberApplication(1L, 2L, "APPROVED", 999L));
        assertEquals("只有队长可以审批申请", exception.getMessage());
    }

    @Test
    void getTeamMembers_returnsApprovedMembersOnly() {
        // Arrange
        TeamMember approved1 = new TeamMember();
        approved1.setId(1L);
        approved1.setApprovalStatus("APPROVED");
        TeamMember pending = new TeamMember();
        pending.setId(2L);
        pending.setApprovalStatus("PENDING");
        TeamMember approved2 = new TeamMember();
        approved2.setId(3L);
        approved2.setApprovalStatus("APPROVED");

        List<TeamMember> allMembers = new ArrayList<>();
        allMembers.add(approved1);
        allMembers.add(pending);
        allMembers.add(approved2);

        when(teamMemberMapper.selectByTeamId(1L)).thenReturn(allMembers);

        // Act
        List<TeamMember> result = teamService.getTeamMembers(1L);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> "APPROVED".equals(m.getApprovalStatus())));
    }

    @Test
    void getAllTeams_withNullUserId_returnsPublicTeamsOnly() {
        // Arrange
        Team publicTeam = new Team();
        publicTeam.setId(1L);
        publicTeam.setIsPublic(true);
        Team privateTeam = new Team();
        privateTeam.setId(2L);
        privateTeam.setIsPublic(false);

        List<Team> allTeams = new ArrayList<>();
        allTeams.add(publicTeam);
        allTeams.add(privateTeam);

        when(teamMapper.selectAll()).thenReturn(allTeams);

        // Act
        List<Team> result = teamService.getAllTeams(null);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsPublic());
    }

    @Test
    void getAllTeams_withUserId_returnsAccessibleTeams() {
        // Arrange
        Team publicTeam = new Team();
        publicTeam.setId(1L);
        publicTeam.setIsPublic(true);
        publicTeam.setLeaderId(999L);
        Team privateTeam = new Team();
        privateTeam.setId(2L);
        privateTeam.setIsPublic(false);
        privateTeam.setLeaderId(1L);

        List<Team> allTeams = new ArrayList<>();
        allTeams.add(publicTeam);
        allTeams.add(privateTeam);

        when(teamMapper.selectAll()).thenReturn(allTeams);

        // Act
        List<Team> result = teamService.getAllTeams(1L);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getPendingApplications_withValidLeader_returnsPendingMembers() {
        // Arrange
        TeamMember pending1 = new TeamMember();
        pending1.setId(1L);
        pending1.setApprovalStatus("PENDING");
        TeamMember approved = new TeamMember();
        approved.setId(2L);
        approved.setApprovalStatus("APPROVED");
        TeamMember pending2 = new TeamMember();
        pending2.setId(3L);
        pending2.setApprovalStatus("PENDING");

        List<TeamMember> allMembers = new ArrayList<>();
        allMembers.add(pending1);
        allMembers.add(approved);
        allMembers.add(pending2);

        when(teamMapper.selectById(1L)).thenReturn(team);
        when(teamMemberMapper.selectByTeamId(1L)).thenReturn(allMembers);

        // Act
        List<TeamMember> result = teamService.getPendingApplications(1L, 1L);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getPendingApplications_withNonLeader_throwsException() {
        // Arrange
        when(teamMapper.selectById(1L)).thenReturn(team);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> teamService.getPendingApplications(1L, 999L));
        assertEquals("只有队长可以查看申请列表", exception.getMessage());
    }
}
