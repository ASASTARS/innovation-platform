package com.abajin.innovation.controller;

import com.abajin.innovation.common.Result;
import com.abajin.innovation.dto.InnovationTeamApplicationDTO;
import com.abajin.innovation.entity.InnovationTeamApplication;
import com.abajin.innovation.service.InnovationTeamApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/innovation-team-applications")
public class InnovationTeamApplicationController {
    @Autowired
    private InnovationTeamApplicationService applicationService;

    @PostMapping
    public Result<InnovationTeamApplication> createApplication(
            @Valid @RequestBody InnovationTeamApplicationDTO dto,
            @RequestAttribute("userId") Long userId) {
        try {
            InnovationTeamApplication application = applicationService.createApplication(dto, userId);
            return Result.success("创建成功", application);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<InnovationTeamApplication> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody InnovationTeamApplicationDTO dto,
            @RequestAttribute("userId") Long userId) {
        try {
            InnovationTeamApplication application = applicationService.updateApplication(id, dto, userId);
            return Result.success("更新成功", application);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/submit")
    public Result<InnovationTeamApplication> submitApplication(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId) {
        try {
            InnovationTeamApplication application = applicationService.submitApplication(id, userId);
            return Result.success("提交成功", application);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<InnovationTeamApplication> getApplicationById(@PathVariable Long id) {
        try {
            InnovationTeamApplication application = applicationService.getApplicationById(id);
            if (application == null) {
                return Result.error("申请不存在");
            }
            return Result.success(application);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<List<InnovationTeamApplication>> getMyApplications(
            @RequestAttribute("userId") Long userId) {
        try {
            List<InnovationTeamApplication> applications = applicationService.getApplicationsByApplicantId(userId);
            return Result.success(applications);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    public Result<List<InnovationTeamApplication>> getAllApplications(
            @RequestParam(required = false) String status) {
        try {
            List<InnovationTeamApplication> applications;
            if (status != null && !status.isEmpty()) {
                applications = applicationService.getApplicationsByStatus(status);
            } else {
                applications = applicationService.getAllApplications();
            }
            return Result.success(applications);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
