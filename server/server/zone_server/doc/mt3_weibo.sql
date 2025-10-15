/*
Navicat MySQL Data Transfer

Source Server         : 192.168.41.5
Source Server Version : 50537
Source Host           : 192.168.41.5:3306
Source Database       : mt3_weibo

Target Server Type    : MYSQL
Target Server Version : 50537
File Encoding         : 65001

Date: 2016-04-06 14:25:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `admin_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(60) CHARACTER SET latin1 NOT NULL,
  `password` varchar(30) CHARACTER SET latin1 NOT NULL,
  `role` tinyint(1) unsigned NOT NULL DEFAULT '9' COMMENT '角色',
  `last_login_time` int(11) unsigned NOT NULL DEFAULT '0',
  `last_login_device` varchar(255) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='管理员表';

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'admin@joyogame.com', '111111', '9', '1457916733', '', '0');
INSERT INTO `admin` VALUES ('4', 'ruixuejia@joyogame.com', '111111', '9', '0', '', '1459846335');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `name` varchar(60) NOT NULL DEFAULT '' COMMENT '角色名',
  `avatar` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '头像',
  `level` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '等级',
  PRIMARY KEY (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '月亮', '0', '0');
INSERT INTO `role` VALUES ('2', '雪花', '0', '0');

-- ----------------------------
-- Table structure for role_relation
-- ----------------------------
DROP TABLE IF EXISTS `role_relation`;
CREATE TABLE `role_relation` (
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `friendid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `relation` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '角色关系类型:1,好友 2,黑名单 3,待定',
  KEY `r` (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色关系表';

-- ----------------------------
-- Records of role_relation
-- ----------------------------
INSERT INTO `role_relation` VALUES ('1', '2', '1');
INSERT INTO `role_relation` VALUES ('1', '3', '1');
INSERT INTO `role_relation` VALUES ('12289', '4097', '1');
INSERT INTO `role_relation` VALUES ('8193', '12289', '1');
INSERT INTO `role_relation` VALUES ('12289', '8193', '1');
INSERT INTO `role_relation` VALUES ('110', '111', '1');
INSERT INTO `role_relation` VALUES ('111', '110', '1');
INSERT INTO `role_relation` VALUES ('110', '0', '1');

-- ----------------------------
-- Table structure for serverlist
-- ----------------------------
DROP TABLE IF EXISTS `serverlist`;
CREATE TABLE `serverlist` (
  `serverlist_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `serverkey` varchar(60) NOT NULL DEFAULT '' COMMENT '区服标志',
  `servername` varchar(60) NOT NULL DEFAULT '' COMMENT '服务器名',
  `weibo_hostname` varchar(60) NOT NULL DEFAULT '' COMMENT '朋友圈数据库连接信息',
  `weibo_port` varchar(60) NOT NULL DEFAULT '',
  `weibo_username` varchar(60) NOT NULL DEFAULT '',
  `weibo_password` varchar(60) NOT NULL DEFAULT '',
  `weibo_database` varchar(60) NOT NULL DEFAULT '',
  `role_hostname` varchar(60) NOT NULL DEFAULT '' COMMENT '用户好友关系数据库连接',
  `role_username` varchar(60) NOT NULL DEFAULT '',
  `role_password` varchar(60) NOT NULL DEFAULT '',
  `role_port` varchar(60) NOT NULL DEFAULT '',
  `role_database` varchar(60) NOT NULL DEFAULT '',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`serverlist_id`),
  KEY `sc` (`serverkey`,`create_time`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='区服连接信息表';

-- ----------------------------
-- Records of serverlist
-- ----------------------------
INSERT INTO `serverlist` VALUES ('1', '0000100001', '', '127.0.0.1', '3306', 'root', '123456', 'mt3_weibo', '192.168.41.5', 'root', 'locojoymysql', '3306', 'mt3_weibo', '0');
