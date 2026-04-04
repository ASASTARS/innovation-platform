-- ============================================
-- 数据库迁移脚本：添加CAS统一身份认证支持
-- 日期：2026-04-04
-- 说明：添加认证方式、CAS用户ID、资料完善标记字段
-- ============================================

-- 1. 添加认证方式字段
ALTER TABLE `user`
ADD COLUMN `auth_type` VARCHAR(20) DEFAULT 'LOCAL' COMMENT '认证方式：LOCAL-本地密码, CAS-CAS统一认证, BOTH-双认证' AFTER `status`;

-- 2. 添加CAS用户ID字段
ALTER TABLE `user`
ADD COLUMN `cas_uid` VARCHAR(50) NULL COMMENT 'CAS用户唯一标识（学号/工号）' AFTER `auth_type`;

-- 3. 添加资料完善标记字段
ALTER TABLE `user`
ADD COLUMN `is_profile_complete` TINYINT(1) DEFAULT 1 COMMENT '资料是否完善：0-未完善，1-已完善' AFTER `cas_uid`;

-- 4. 添加唯一索引
ALTER TABLE `user`
ADD UNIQUE INDEX `idx_cas_uid` (`cas_uid`);

-- ============================================
-- 验证脚本（执行后可检查）
-- ============================================
-- 检查列是否添加成功
-- DESC user;

-- 检查现有用户的auth_type默认值
-- SELECT auth_type, COUNT(*) FROM user GROUP BY auth_type;

-- 检查CAS用户
-- SELECT id, username, real_name, cas_uid, auth_type, is_profile_complete
-- FROM user WHERE auth_type IN ('CAS', 'BOTH');
