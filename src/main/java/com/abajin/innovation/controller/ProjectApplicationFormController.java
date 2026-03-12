package com.abajin.innovation.controller;

import com.abajin.innovation.common.Result;
import com.abajin.innovation.dto.ProjectApplicationFormDTO;
import com.abajin.innovation.entity.ProjectApplicationForm;
import com.abajin.innovation.service.ProjectApplicationFormService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-application-forms")
public class ProjectApplicationFormController {
    @Autowired
    private ProjectApplicationFormService formService;

    @PostMapping
    public Result<ProjectApplicationForm> createForm(
            @Valid @RequestBody ProjectApplicationFormDTO dto,
            @RequestAttribute("userId") Long userId) {
        try {
            ProjectApplicationForm form = formService.createForm(dto, userId);
            return Result.success("创建成功", form);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<ProjectApplicationForm> updateForm(
            @PathVariable Long id,
            @Valid @RequestBody ProjectApplicationFormDTO dto,
            @RequestAttribute("userId") Long userId) {
        try {
            ProjectApplicationForm form = formService.updateForm(id, dto, userId);
            return Result.success("更新成功", form);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/submit")
    public Result<ProjectApplicationForm> submitForm(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId) {
        try {
            ProjectApplicationForm form = formService.submitForm(id, userId);
            return Result.success("提交成功", form);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<ProjectApplicationFormDTO> getFormById(@PathVariable Long id) {
        try {
            ProjectApplicationFormDTO dto = formService.getFormById(id);
            if (dto == null) {
                return Result.error("申请表单不存在");
            }
            return Result.success(dto);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<List<ProjectApplicationForm>> getMyForms(
            @RequestAttribute("userId") Long userId) {
        try {
            List<ProjectApplicationForm> forms = formService.getFormsByApplicantId(userId);
            return Result.success(forms);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    public Result<List<ProjectApplicationForm>> getAllForms(
            @RequestParam(required = false) String status) {
        try {
            List<ProjectApplicationForm> forms;
            if (status != null && !status.isEmpty()) {
                forms = formService.getFormsByStatus(status);
            } else {
                forms = formService.getAllForms();
            }
            return Result.success(forms);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
