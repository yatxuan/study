/*
Navicat MySQL Data Transfer

Source Server         : 本地数据库
Source Server Version : 80015
Source Host           : localhost:3306
Source Database       : test_orders

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2020-04-24 16:28:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `user_id` int(16) DEFAULT NULL COMMENT '用户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单表';

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('1', '10');
INSERT INTO `orders` VALUES ('12', '10');
INSERT INTO `orders` VALUES ('13', '10');
INSERT INTO `orders` VALUES ('14', '10');
INSERT INTO `orders` VALUES ('15', '10');
INSERT INTO `orders` VALUES ('16', '20');
INSERT INTO `orders` VALUES ('17', '20');
