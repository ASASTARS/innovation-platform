package com.abajin.innovation.service;

import com.abajin.innovation.dto.InnovationTeamApplicationDTO;
import com.abajin.innovation.entity.InnovationTeamApplication;
import com.abajin.innovation.mapper.InnovationTeamApplicationMapper;
import com.abajin.innovation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InnovationTeamApplicationService {
    @Autowired
    private InnovationTeamApplicationMapper applicationMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public InnovationTeamApplication createApplication(InnovationTeamApplicationDTO dto, Long applicantId) {
        InnovationTeamApplication application = new InnovationTeamApplication();
        application.setTeamName(dto.getTeamName());
        application.setCooperativeEnterprise(dto.getCooperativeEnterprise());
        application.setApplicantStudentId(dto.getApplicantStudentId());
        application.setApplicantName(dto.getApplicantName());
        application.setApplicantContact(dto.getApplicantContact());
        application.setOnCampusMentorName(dto.getOnCampusMentorName());
        application.setOnCampusMentorContact(dto.getOnCampusMentorContact());
        application.setEnterpriseMentorName(dto.getEnterpriseMentorName());
        application.setEnterpriseMentorContact(dto.getEnterpriseMentorContact());
        application.setInnovationDirection(dto.getInnovationDirection());
        application.setPositioningAndIdeas(dto.getPositioningAndIdeas());
        application.setApplicantSignature(dto.getApplicantSignature());
        application.setApplicantSignDate(dto.getApplicantSignDate());
        application.setOnCampusMentorSignature(dto.getOnCampusMentorSignature());
        application.setOnCampusMentorSignDate(dto.getOnCampusMentorSignDate());
        application.setEnterpriseMentorSignature(dto.getEnterpriseMentorSignature());
        application.setEnterpriseMentorSignDate(dto.getEnterpriseMentorSignDate());
        application.setStatus("DRAFT");
        application.setApplicantId(applicantId);
        application.setCreateTime(LocalDateTime.now());
        application.setUpdateTime(LocalDateTime.now());

        applicationMapper.insert(application);
        return application;
    }

    @Transactional
    public InnovationTeamApplication updateApplication(Long id, InnovationTeamApplicationDTO dto, Long applicantId) {
        InnovationTeamApplication application = applicationMapper.selectById(id);
        if (application == null) {
            throw new RuntimeException("申请不存在");
        }
        if (!application.getApplicantId().equals(applicantId)) {
            throw new RuntimeException("无权修改此申请");
        }

        application.setTeamName(dto.getTeamName());
        application.setCooperativeEnterprise(dto.getCooperativeEnterprise());
        application.setApplicantStudentId(dto.getApplicantStudentId());
        application.setApplicantName(dto.getApplicantName());
        application.setApplicantContact(dto.getApplicantContact());
        application.setOnCampusMentorName(dto.getOnCampusMentorName());
        application.setOnCampusMentorContact(dto.getOnCampusMentorContact());
        application.setEnterpriseMentorName(dto.getEnterpriseMentorName());
        application.setEnterpriseMentorContact(dto.getEnterpriseMentorContact());
        application.setInnovationDirection(dto.getInnovationDirection());
        application.setPositioningAndIdeas(dto.getPositioningAndIdeas());
        application.setApplicantSignature(dto.getApplicantSignature());
        application.setApplicantSignDate(dto.getApplicantSignDate());
        application.setOnCampusMentorSignature(dto.getOnCampusMentorSignature());
        application.setOnCampusMentorSignDate(dto.getOnCampusMentorSignDate());
        application.setEnterpriseMentorSignature(dto.getEnterpriseMentorSignature());
        application.setEnterpriseMentorSignDate(dto.getEnterpriseMentorSignDate());
        application.setUpdateTime(LocalDateTime.now());

        applicationMapper.update(application);
        return application;
    }

    @Transactional
    public InnovationTeamApplication submitApplication(Long id, Long applicantId) {
        InnovationTeamApplication application = applicationMapper.selectById(id);
        if (application == null) {
            throw new RuntimeException("申请不存在");
        }
        if (!application.getApplicantId().equals(applicantId)) {
            throw new RuntimeException("无权提交此申请");
        }
        if (!"DRAFT".equals(application.getStatus())) {
            throw new RuntimeException("只能提交草稿状态的申请");
        }

        application.setStatus("SUBMITTED");
        application.setUpdateTime(LocalDateTime.now());
        applicationMapper.update(application);
        return application;
    }

    public InnovationTeamApplication getApplicationById(Long id) {
        return applicationMapper.selectById(id);
    }

    public List<InnovationTeamApplication> getApplicationsByApplicantId(Long applicantId) {
        return applicationMapper.selectByApplicantId(applicantId);
    }

    public List<InnovationTeamApplication> getApplicationsByStatus(String status) {
        return applicationMapper.selectByStatus(status);
    }

    public List<InnovationTeamApplication> getAllApplications() {
        return applicationMapper.selectAll();
    }
}
