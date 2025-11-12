-- 创建数据库
CREATE DATABASE IF NOT EXISTS rbac_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE rbac_system;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `status` int(1) DEFAULT '1' COMMENT '状态 0:禁用 1:启用',
  `deleted` int(1) DEFAULT '0' COMMENT '删除标志 0:未删除 1:已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_key` varchar(50) NOT NULL COMMENT '角色标识',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `status` int(1) DEFAULT '1' COMMENT '状态 0:禁用 1:启用',
  `deleted` int(1) DEFAULT '0' COMMENT '删除标志 0:未删除 1:已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission_key` varchar(100) NOT NULL COMMENT '权限标识',
  `permission_name` varchar(50) NOT NULL COMMENT '权限名称',
  `description` varchar(200) DEFAULT NULL COMMENT '权限描述',
  `deleted` int(1) DEFAULT '0' COMMENT '删除标志 0:未删除 1:已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_key` (`permission_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 菜单表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `path` varchar(200) DEFAULT NULL COMMENT '路由路径',
  `component` varchar(200) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `sort` int(4) DEFAULT '0' COMMENT '显示顺序',
  `type` int(1) DEFAULT '1' COMMENT '菜单类型 0:目录 1:菜单 2:按钮',
  `permission` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `status` int(1) DEFAULT '1' COMMENT '状态 0:禁用 1:启用',
  `deleted` int(1) DEFAULT '0' COMMENT '删除标志 0:未删除 1:已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 角色菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 初始化数据
-- 密码: admin123 (BCrypt加密后)
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHCdBbZ6QvFR3qZpnRAkH2', '管理员', 'admin@example.com', '13800138000', NULL, 1, 0, NOW(), NOW());

INSERT INTO `sys_role` VALUES (1, 'admin', '超级管理员', '拥有所有权限', 1, 0, NOW(), NOW());
INSERT INTO `sys_role` VALUES (2, 'user', '普通用户', '普通用户权限', 1, 0, NOW(), NOW());

INSERT INTO `sys_permission` VALUES (1, 'user:view', '查看用户', '查看用户列表权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (2, 'user:add', '添加用户', '添加用户权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (3, 'user:edit', '编辑用户', '编辑用户权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (4, 'user:delete', '删除用户', '删除用户权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (5, 'role:view', '查看角色', '查看角色列表权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (6, 'role:add', '添加角色', '添加角色权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (7, 'role:edit', '编辑角色', '编辑角色权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (8, 'role:delete', '删除角色', '删除角色权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (9, 'permission:view', '查看权限', '查看权限列表权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (10, 'permission:add', '添加权限', '添加权限权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (11, 'permission:edit', '编辑权限', '编辑权限权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (12, 'permission:delete', '删除权限', '删除权限权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (13, 'menu:view', '查看菜单', '查看菜单列表权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (14, 'menu:add', '添加菜单', '添加菜单权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (15, 'menu:edit', '编辑菜单', '编辑菜单权限', 0, NOW(), NOW());
INSERT INTO `sys_permission` VALUES (16, 'menu:delete', '删除菜单', '删除菜单权限', 0, NOW(), NOW());

INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', '/system', 'Layout', 'el-icon-setting', 1, 0, NULL, 1, 0, NOW(), NOW());
INSERT INTO `sys_menu` VALUES (2, 1, '用户管理', '/system/user', 'system/user/index', 'el-icon-user', 1, 1, 'user:view', 1, 0, NOW(), NOW());
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', '/system/role', 'system/role/index', 'el-icon-s-custom', 2, 1, 'role:view', 1, 0, NOW(), NOW());
INSERT INTO `sys_menu` VALUES (4, 1, '权限管理', '/system/permission', 'system/permission/index', 'el-icon-lock', 3, 1, 'permission:view', 1, 0, NOW(), NOW());
INSERT INTO `sys_menu` VALUES (5, 1, '菜单管理', '/system/menu', 'system/menu/index', 'el-icon-menu', 4, 1, 'menu:view', 1, 0, NOW(), NOW());

INSERT INTO `sys_user_role` VALUES (1, 1, 1);

INSERT INTO `sys_role_permission` VALUES (1, 1, 1);
INSERT INTO `sys_role_permission` VALUES (2, 1, 2);
INSERT INTO `sys_role_permission` VALUES (3, 1, 3);
INSERT INTO `sys_role_permission` VALUES (4, 1, 4);
INSERT INTO `sys_role_permission` VALUES (5, 1, 5);
INSERT INTO `sys_role_permission` VALUES (6, 1, 6);
INSERT INTO `sys_role_permission` VALUES (7, 1, 7);
INSERT INTO `sys_role_permission` VALUES (8, 1, 8);
INSERT INTO `sys_role_permission` VALUES (9, 1, 9);
INSERT INTO `sys_role_permission` VALUES (10, 1, 10);
INSERT INTO `sys_role_permission` VALUES (11, 1, 11);
INSERT INTO `sys_role_permission` VALUES (12, 1, 12);
INSERT INTO `sys_role_permission` VALUES (13, 1, 13);
INSERT INTO `sys_role_permission` VALUES (14, 1, 14);
INSERT INTO `sys_role_permission` VALUES (15, 1, 15);
INSERT INTO `sys_role_permission` VALUES (16, 1, 16);

INSERT INTO `sys_role_menu` VALUES (1, 1, 1);
INSERT INTO `sys_role_menu` VALUES (2, 1, 2);
INSERT INTO `sys_role_menu` VALUES (3, 1, 3);
INSERT INTO `sys_role_menu` VALUES (4, 1, 4);
INSERT INTO `sys_role_menu` VALUES (5, 1, 5);