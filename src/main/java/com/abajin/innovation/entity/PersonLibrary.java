package com.abajin.innovation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 人员库实体类（校内导师、校外导师、行业专家、优秀学生）
 */
@Data
public class PersonLibrary {
    private Long id;
    private Long personTypeId; // 人员类型ID
    private String personTypeName; // 人员类型名称（关联查询）
    private Long userId; // 关联用户ID（如果是校内人员）
    private String name; // 姓名
    private String gender; // 性别：MALE-男, FEMALE-女
    private String phone; // 联系电话
    private String email; // 邮箱
    private String avatar; // 头像URL
    private String title; // 职称/头衔
    private String organization; // 所属单位/企业
    private String position; // 职位
    private String researchDirection; // 研究方向/专业领域
    private String achievements; // 主要成就/荣誉
    private String introduction; // 个人简介
    private String expertiseAreas; // 专业领域（JSON数组）
    private String status; // 状态：ACTIVE-活跃, INACTIVE-非活跃
    private String approvalStatus; // 审批状态：PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝
    private Long reviewerId; // 审批人ID
    private LocalDateTime reviewTime; // 审批时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
