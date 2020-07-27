/*
Navicat MySQL Data Transfer

Source Server         : 本地数据库
Source Server Version : 80015
Source Host           : localhost:3306
Source Database       : shiro_test

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2020-07-27 10:47:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission` (
  `id` bigint(20) NOT NULL,
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '权限名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '权限说明',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` int(2) DEFAULT '0' COMMENT '删除标志  -1-删除 0-未删除 默认0',
  `version` int(10) DEFAULT '0' COMMENT '乐观锁：版本标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统权限表';

-- ----------------------------
-- Records of tb_permission
-- ----------------------------

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `id` bigint(20) NOT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '角色名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '角色备注',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` int(2) DEFAULT '0' COMMENT '删除标志  -1-删除 0-未删除 默认0',
  `version` int(10) DEFAULT '0' COMMENT '乐观锁：版本标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色表';

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES ('1', 'user:save', '测试', 'admin', '2020-06-23 16:24:05', '', null, '0', '0');
INSERT INTO `tb_role` VALUES ('2', 'user:info', '测试2', 'admin', '2020-06-23 16:24:05', '', null, '0', '0');

-- ----------------------------
-- Table structure for tb_role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_permissions`;
CREATE TABLE `tb_role_permissions` (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色-权限关系表';

-- ----------------------------
-- Records of tb_role_permissions
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '密码',
  `nick_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '昵称',
  `enabled` int(2) DEFAULT '1' COMMENT '状态：1启用、-1禁用',
  `create_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `log_number` int(11) DEFAULT '1' COMMENT '该账户最大允许几个客户端同时登陆',
  `deleted` int(2) DEFAULT '0' COMMENT '删除标志  -1-删除 0-未删除 默认0',
  `version` int(10) DEFAULT '0' COMMENT '乐观锁：版本标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户表';

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1272819601889124351', 'user', 'ef453e0d2a71905aebc57f5718d0fd8e34162a113753ddbac5d5a018ff79b694', '管理员', '1', 'admin', '2020-06-16 09:13:36', 'admin', '2020-06-16 10:07:15', '1', '0', '1');
INSERT INTO `tb_user` VALUES ('1272819601889124353', 'admin', 'd82494f05d6917ba02f7aaa29689ccb444bb73f20380876cb05d1f37537b7892', '管理员', '1', 'admin', '2020-06-16 09:13:36', 'admin', '2020-06-16 10:07:15', '1', '0', '1');

-- ----------------------------
-- Table structure for tb_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_roles`;
CREATE TABLE `tb_user_roles` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户-角色关系表';

-- ----------------------------
-- Records of tb_user_roles
-- ----------------------------
INSERT INTO `tb_user_roles` VALUES ('1', '1272819601889124353', '2');
INSERT INTO `tb_user_roles` VALUES ('2', '1272819601889124351', '2');
