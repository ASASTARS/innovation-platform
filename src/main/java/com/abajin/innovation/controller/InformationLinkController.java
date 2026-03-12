package com.abajin.innovation.controller;

import com.abajin.innovation.service.InformationLinkService;
import com.abajin.innovation.annotation.RequiresRole;
import com.abajin.innovation.common.Constants;
import com.abajin.innovation.common.Result;
import com.abajin.innovation.entity.FundApplication;
import com.abajin.innovation.entity.Project;
import com.abajin.innovation.entity.TeamMember;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 信息对接控制器
 */
@RestController
@RequestMapping("/information-link")
public class InformationLinkController {
    @Autowired
    private InformationLinkService informationLinkService;

    /**
     * 学生和教师申请加入团队
     * POST /api/information-link/teams/{teamId}/apply
     */
    @PostMapping("/teams/{teamId}/apply")
    @RequiresRole(value = {Constants.ROLE_STUDENT, Constants.ROLE_TEACHER})
    public Result<TeamMember> applyJoinTeam(
            @PathVariable Long teamId,
            @RequestAttribute("userId") Long userId,
            @RequestBody(required = false) Map<String, Object> body) {
        try {
            TeamMember member = informationLinkService.applyJoinTeam(teamId, userId, body);
            return Result.success("申请已提交，请等待队长审批", member);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 教师申请接管无人管理项目
     * POST /api/information-link/projects/{projectId}/takeover
     */
    @PostMapping("/projects/{projectId}/takeover")
    @RequiresRole(value = {Constants.ROLE_TEACHER})
    public Result<Project> applyTakeoverProject(
            @PathVariable Long projectId,
            @RequestAttribute("userId") Long userId) {
        try {
            // 这里简化处理，实际应该由学院管理员审核
            Project project = informationLinkService.applyTakeoverProject(projectId, userId, null);
            return Result.success("申请接管项目成功", project);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 项目负责人发起基金申请
     * POST /api/information-link/fund-applications
     */
    @PostMapping("/fund-applications")
    @RequiresRole(value = {Constants.ROLE_STUDENT, Constants.ROLE_TEACHER})
    public Result<FundApplication> createFundApplication(
            @Valid @RequestBody FundApplication application,
            @RequestAttribute("userId") Long userId) {
        try {
            FundApplication created = informationLinkService.createFundApplication(application, userId);
            return Result.success("基金申请提交成功", created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 学校管理员审核基金申请
     * POST /api/information-link/fund-applications/{id}/review
     */
    @PostMapping("/fund-applications/{id}/review")
    @RequiresRole(value = {Constants.ROLE_SCHOOL_ADMIN}, allowAdmin = false)
    public Result<FundApplication> reviewFundApplication(
            @PathVariable Long id,
            @RequestBody Map<String, String> reviewData,
            @RequestAttribute("userId") Long userId) {
        try {
            String approvalStatus = reviewData.get("approvalStatus");
            String reviewComment = reviewData.get("reviewComment");
            FundApplication reviewed = informationLinkService.reviewFundApplication(id, approvalStatus, reviewComment, userId);
            return Result.success("审核完成", reviewed);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 项目负责人招募成员
     * POST /api/information-link/projects/{projectId}/recruit
     */
    @PostMapping("/projects/{projectId}/recruit")
    @RequiresRole(value = {Constants.ROLE_STUDENT, Constants.ROLE_TEACHER})
    public Result<TeamMember> recruitMember(
            @PathVariable Long projectId,
            @RequestBody Map<String, Long> data,
            @RequestAttribute("userId") Long userId) {
        try {
            Long memberId = data.get("userId");
            TeamMember member = informationLinkService.recruitMember(projectId, memberId, userId);
            return Result.success("招募成员成功", member);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
