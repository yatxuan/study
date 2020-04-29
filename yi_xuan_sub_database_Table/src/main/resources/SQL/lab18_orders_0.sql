/*
Navicat MySQL Data Transfer

Source Server         : 本地数据库
Source Server Version : 80015
Source Host           : localhost:3306
Source Database       : lab18_orders_0

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2020-04-24 16:28:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for orders_0
-- ----------------------------
DROP TABLE IF EXISTS `orders_0`;
CREATE TABLE `orders_0` (
  `id` bigint(11) NOT NULL  COMMENT '订单编号',
  `user_id` int(16) DEFAULT NULL COMMENT '用户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单表';

-- ----------------------------
-- Records of orders_0
-- ----------------------------
INSERT INTO `orders_0` VALUES ('1', '123');

-- ----------------------------
-- Table structure for orders_2
-- ----------------------------
DROP TABLE IF EXISTS `orders_2`;
CREATE TABLE `orders_2` (
  `id` bigint(11) NOT NULL  COMMENT '订单编号',
  `user_id` int(16) DEFAULT NULL COMMENT '用户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单表';

-- ----------------------------
-- Records of orders_2
-- ----------------------------
INSERT INTO `orders_2` VALUES ('8', '10');
INSERT INTO `orders_2` VALUES ('9', '10');

-- ----------------------------
-- Table structure for orders_4
-- ----------------------------
DROP TABLE IF EXISTS `orders_4`;
CREATE TABLE `orders_4` (
  `id` bigint(11) NOT NULL  COMMENT '订单编号',
  `user_id` int(16) DEFAULT NULL COMMENT '用户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单表';

-- ----------------------------
-- Records of orders_4
-- ----------------------------

-- ----------------------------
-- Table structure for orders_6
-- ----------------------------
DROP TABLE IF EXISTS `orders_6`;
CREATE TABLE `orders_6` (
  `id` bigint(11) NOT NULL  COMMENT '订单编号',
  `user_id` int(16) DEFAULT NULL COMMENT '用户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单表';

-- ----------------------------
-- Records of orders_6
-- ----------------------------

-- ----------------------------
-- Table structure for order_config
-- ----------------------------
DROP TABLE IF EXISTS `order_config`;
CREATE TABLE `order_config` (
  `id` int(11) NOT NULL  COMMENT '编号',
  `pay_timeout` int(11) DEFAULT NULL COMMENT '支付超时时间;单位：分钟',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单配置表';

-- ----------------------------
-- Records of order_config
-- ----------------------------
INSERT INTO `order_config` VALUES ('1', '123');

DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ip_adder` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
