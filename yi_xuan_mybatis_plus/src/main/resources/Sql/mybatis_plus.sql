SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1263001984569470977', 'name:1', '1', 'email:1', '2020-05-20 07:01:53', null, '1', '0');
INSERT INTO `tb_user` VALUES ('1263001985496412162', 'name:2', '2', 'email:2', '2020-05-20 07:01:53', null, '1', '0');
INSERT INTO `tb_user` VALUES ('1263001985802596354', 'name:3', '3', 'email:3', '2020-05-20 07:01:54', null, '1', '0');
INSERT INTO `tb_user` VALUES ('1263001986112974850', 'name:4', '4', 'email:4', '2020-05-20 07:01:54', null, '1', '0');
INSERT INTO `tb_user` VALUES ('1263001986419159042', 'name:5', '5', 'email:5', '2020-05-20 07:01:54', null, '1', '0');
INSERT INTO `tb_user` VALUES ('1263001986733731841', 'name:6', '6', 'email:6', '2020-05-20 07:01:54', null, '1', '0');
INSERT INTO `tb_user` VALUES ('1263001987048304642', 'name:7', '7', 'email:7', '2020-05-20 07:01:54', null, '1', '0');
INSERT INTO `tb_user` VALUES ('1263001987358683137', 'name:8', '8', 'email:8', '2020-05-20 07:01:54', null, '1', '0');
INSERT INTO `tb_user` VALUES ('1263001987660673026', 'name:9', '9', 'email:9', '2020-05-20 07:01:54', null, '1', '0');
