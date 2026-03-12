package com.abajin.innovation.service;

import com.abajin.innovation.dto.ProjectApplicationFormDTO;
import com.abajin.innovation.dto.ProjectApplicationMemberDTO;
import com.abajin.innovation.entity.ProjectApplicationForm;
import com.abajin.innovation.entity.ProjectApplicationMember;
import com.abajin.innovation.mapper.ProjectApplicationFormMapper;
import com.abajin.innovation.mapper.ProjectApplicationMemberMapper;
import com.abajin.innovation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectApplicationFormService {
    @Autowired
    private ProjectApplicationFormMapper formMapper;
    
    @Autowired
    private ProjectApplicationMemberMapper memberMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public ProjectApplicationForm createForm(ProjectApplicationFormDTO dto, Long applicantId) {
        // 验证成员数量（最多4人，不包括负责人）
        if (dto.getMembers() != null && dto.getMembers().size() > 4) {
            throw new RuntimeException("项目组成员不能超过4人（不包括团队负责人）");
        }

        ProjectApplicationForm form = new ProjectApplicationForm();
        form.setInstructorName(dto.getInstructorName());
        form.setLeaderName(dto.getLeaderName());
        form.setLeaderPhone(dto.getLeaderPhone());
        form.setCompetitionRegistered(dto.getCompetitionRegistered() != null && dto.getCompetitionRegistered() ? 1 : 0);
        form.setCompetitionName(dto.getCompetitionName());
        form.setProjectName(dto.getProjectName());
        form.setProjectIntroduction(dto.getProjectIntroduction());
        form.setProjectAchievements(dto.getProjectAchievements());
        form.setExpectedOutcomes(dto.getExpectedOutcomes());
        form.setLeaderDeclaration(dto.getLeaderDeclaration());
        form.setLeaderSignature(dto.getLeaderSignature());
        form.setLeaderSignDate(dto.getLeaderSignDate());
        form.setMemberDeclaration(dto.getMemberDeclaration());
        form.setMemberSignature(dto.getMemberSignature());
        form.setMemberSignDate(dto.getMemberSignDate());
        form.setInstructorOpinion(dto.getInstructorOpinion());
        form.setInstructorSignature(dto.getInstructorSignature());
        form.setInstructorSignDate(dto.getInstructorSignDate());
        form.setStatus("DRAFT");
        form.setApplicantId(applicantId);
        form.setApplicantName(userMapper.selectById(applicantId).getRealName());
        form.setCreateTime(LocalDateTime.now());
        form.setUpdateTime(LocalDateTime.now());

        formMapper.insert(form);

        // 保存成员信息
        if (dto.getMembers() != null && !dto.getMembers().isEmpty()) {
            for (int i = 0; i < dto.getMembers().size(); i++) {
                ProjectApplicationMemberDTO memberDTO = dto.getMembers().get(i);
                ProjectApplicationMember member = new ProjectApplicationMember();
                member.setApplicationFormId(form.getId());
                member.setStudentId(memberDTO.getStudentId());
                member.setName(memberDTO.getName());
                member.setMajor(memberDTO.getMajor());
                member.setMainWork(memberDTO.getMainWork());
                member.setSignature(memberDTO.getSignature());
                member.setSortOrder(i);
                member.setCreateTime(LocalDateTime.now());
                memberMapper.insert(member);
            }
        }

        return form;
    }

    @Transactional
    public ProjectApplicationForm updateForm(Long id, ProjectApplicationFormDTO dto, Long applicantId) {
        ProjectApplicationForm form = formMapper.selectById(id);
        if (form == null) {
            throw new RuntimeException("申请表单不存在");
        }
        if (!form.getApplicantId().equals(applicantId)) {
            throw new RuntimeException("无权修改此申请表单");
        }

        // 验证成员数量
        if (dto.getMembers() != null && dto.getMembers().size() > 4) {
            throw new RuntimeException("项目组成员不能超过4人（不包括团队负责人）");
        }

        form.setInstructorName(dto.getInstructorName());
        form.setLeaderName(dto.getLeaderName());
        form.setLeaderPhone(dto.getLeaderPhone());
        form.setCompetitionRegistered(dto.getCompetitionRegistered() != null && dto.getCompetitionRegistered() ? 1 : 0);
        form.setCompetitionName(dto.getCompetitionName());
        form.setProjectName(dto.getProjectName());
        form.setProjectIntroduction(dto.getProjectIntroduction());
        form.setProjectAchievements(dto.getProjectAchievements());
        form.setExpectedOutcomes(dto.getExpectedOutcomes());
        form.setLeaderDeclaration(dto.getLeaderDeclaration());
        form.setLeaderSignature(dto.getLeaderSignature());
        form.setLeaderSignDate(dto.getLeaderSignDate());
        form.setMemberDeclaration(dto.getMemberDeclaration());
        form.setMemberSignature(dto.getMemberSignature());
        form.setMemberSignDate(dto.getMemberSignDate());
        form.setInstructorOpinion(dto.getInstructorOpinion());
        form.setInstructorSignature(dto.getInstructorSignature());
        form.setInstructorSignDate(dto.getInstructorSignDate());
        form.setUpdateTime(LocalDateTime.now());

        formMapper.update(form);

        // 删除旧成员，插入新成员
        memberMapper.deleteByApplicationFormId(id);
        if (dto.getMembers() != null && !dto.getMembers().isEmpty()) {
            for (int i = 0; i < dto.getMembers().size(); i++) {
                ProjectApplicationMemberDTO memberDTO = dto.getMembers().get(i);
                ProjectApplicationMember member = new ProjectApplicationMember();
                member.setApplicationFormId(form.getId());
                member.setStudentId(memberDTO.getStudentId());
                member.setName(memberDTO.getName());
                member.setMajor(memberDTO.getMajor());
                member.setMainWork(memberDTO.getMainWork());
                member.setSignature(memberDTO.getSignature());
                member.setSortOrder(i);
                member.setCreateTime(LocalDateTime.now());
                memberMapper.insert(member);
            }
        }

        return form;
    }

    @Transactional
    public ProjectApplicationForm submitForm(Long id, Long applicantId) {
        ProjectApplicationForm form = formMapper.selectById(id);
        if (form == null) {
            throw new RuntimeException("申请表单不存在");
        }
        if (!form.getApplicantId().equals(applicantId)) {
            throw new RuntimeException("无权提交此申请表单");
        }
        if (!"DRAFT".equals(form.getStatus())) {
            throw new RuntimeException("只能提交草稿状态的申请表单");
        }

        form.setStatus("SUBMITTED");
        form.setUpdateTime(LocalDateTime.now());
        formMapper.update(form);
        return form;
    }

    public ProjectApplicationFormDTO getFormById(Long id) {
        ProjectApplicationForm form = formMapper.selectById(id);
        if (form == null) {
            return null;
        }

        ProjectApplicationFormDTO dto = new ProjectApplicationFormDTO();
        dto.setId(form.getId());
        dto.setInstructorName(form.getInstructorName());
        dto.setLeaderName(form.getLeaderName());
        dto.setLeaderPhone(form.getLeaderPhone());
        dto.setCompetitionRegistered(form.getCompetitionRegistered() != null && form.getCompetitionRegistered() == 1);
        dto.setCompetitionName(form.getCompetitionName());
        dto.setProjectName(form.getProjectName());
        dto.setProjectIntroduction(form.getProjectIntroduction());
        dto.setProjectAchievements(form.getProjectAchievements());
        dto.setExpectedOutcomes(form.getExpectedOutcomes());
        dto.setLeaderDeclaration(form.getLeaderDeclaration());
        dto.setLeaderSignature(form.getLeaderSignature());
        dto.setLeaderSignDate(form.getLeaderSignDate());
        dto.setMemberDeclaration(form.getMemberDeclaration());
        dto.setMemberSignature(form.getMemberSignature());
        dto.setMemberSignDate(form.getMemberSignDate());
        dto.setInstructorOpinion(form.getInstructorOpinion());
        dto.setInstructorSignature(form.getInstructorSignature());
        dto.setInstructorSignDate(form.getInstructorSignDate());

        // 获取成员列表
        List<ProjectApplicationMember> members = memberMapper.selectByApplicationFormId(id);
        List<ProjectApplicationMemberDTO> memberDTOs = members.stream().map(m -> {
            ProjectApplicationMemberDTO memberDTO = new ProjectApplicationMemberDTO();
            memberDTO.setId(m.getId());
            memberDTO.setStudentId(m.getStudentId());
            memberDTO.setName(m.getName());
            memberDTO.setMajor(m.getMajor());
            memberDTO.setMainWork(m.getMainWork());
            memberDTO.setSignature(m.getSignature());
            memberDTO.setSortOrder(m.getSortOrder());
            return memberDTO;
        }).collect(Collectors.toList());
        dto.setMembers(memberDTOs);

        return dto;
    }

    public List<ProjectApplicationForm> getFormsByApplicantId(Long applicantId) {
        return formMapper.selectByApplicantId(applicantId);
    }

    public List<ProjectApplicationForm> getFormsByStatus(String status) {
        return formMapper.selectByStatus(status);
    }

    public List<ProjectApplicationForm> getAllForms() {
        return formMapper.selectAll();
    }
}
