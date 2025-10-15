/*
Navicat MySQL Data Transfer

Source Server         : 42.62.47.222
Source Server Version : 50173
Source Host           : 42.62.47.222:3306
Source Database       : mt3_weibo

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2016-05-04 17:27:34
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
-- Table structure for bbs
-- ----------------------------
DROP TABLE IF EXISTS `bbs`;
CREATE TABLE `bbs` (
  `bbs_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '留言者',
  `to_roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '被留言者',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态0正常,1删除',
  `gift_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '礼物id,包括玫瑰花,金兰花,同心结',
  `gift_count` int(11) unsigned NOT NULL DEFAULT '0',
  `reply_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '回复id',
  `reply_roleid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`bbs_id`),
  KEY `tsc` (`to_roleid`,`status`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='留言板表';

-- ----------------------------
-- Records of bbs
-- ----------------------------
INSERT INTO `bbs` VALUES ('1', '1', '2', '0', '0', '0', '0', '0', '1459862023');
INSERT INTO `bbs` VALUES ('2', '1', '2', '1', '0', '0', '0', '0', '1459862276');
INSERT INTO `bbs` VALUES ('3', '1', '2', '0', '0', '0', '0', '0', '1459862316');
INSERT INTO `bbs` VALUES ('4', '1', '2', '0', '0', '0', '0', '0', '1459862321');
INSERT INTO `bbs` VALUES ('5', '1', '1', '0', '0', '0', '0', '0', '1459862401');
INSERT INTO `bbs` VALUES ('6', '1', '2', '0', '0', '0', '0', '0', '1460468224');
INSERT INTO `bbs` VALUES ('7', '1', '2', '0', '0', '0', '0', '0', '1460468430');
INSERT INTO `bbs` VALUES ('8', '1', '2', '0', '0', '0', '0', '0', '1460468475');
INSERT INTO `bbs` VALUES ('9', '1', '2', '0', '0', '0', '0', '0', '1460468478');
INSERT INTO `bbs` VALUES ('10', '1', '2', '0', '0', '0', '0', '0', '1460468672');
INSERT INTO `bbs` VALUES ('11', '1', '2', '0', '0', '0', '0', '0', '1460468691');
INSERT INTO `bbs` VALUES ('12', '1', '2', '0', '0', '0', '0', '0', '1460468692');
INSERT INTO `bbs` VALUES ('13', '1', '2', '0', '0', '0', '0', '0', '1460468693');
INSERT INTO `bbs` VALUES ('14', '1', '2', '0', '0', '0', '0', '0', '1460468694');
INSERT INTO `bbs` VALUES ('15', '1', '2', '0', '0', '0', '0', '0', '1460468695');
INSERT INTO `bbs` VALUES ('16', '1', '2', '0', '0', '0', '0', '0', '1460468695');
INSERT INTO `bbs` VALUES ('17', '1', '2', '0', '0', '0', '0', '0', '1460468695');
INSERT INTO `bbs` VALUES ('18', '1', '2', '0', '0', '0', '0', '0', '1460468696');
INSERT INTO `bbs` VALUES ('19', '1', '2', '0', '0', '0', '0', '0', '1460468696');
INSERT INTO `bbs` VALUES ('20', '1', '2', '0', '0', '0', '0', '0', '1460468696');
INSERT INTO `bbs` VALUES ('21', '1', '2', '0', '0', '0', '0', '0', '1460468696');
INSERT INTO `bbs` VALUES ('22', '1', '2', '0', '0', '0', '0', '0', '1460468697');
INSERT INTO `bbs` VALUES ('23', '1', '2', '0', '0', '0', '0', '0', '1460468697');
INSERT INTO `bbs` VALUES ('24', '1', '2', '0', '0', '0', '0', '0', '1460468697');
INSERT INTO `bbs` VALUES ('25', '1', '2', '0', '0', '0', '0', '0', '1460468697');
INSERT INTO `bbs` VALUES ('26', '1', '2', '0', '0', '0', '0', '0', '1460468698');
INSERT INTO `bbs` VALUES ('27', '1', '2', '0', '0', '0', '0', '0', '1460468698');
INSERT INTO `bbs` VALUES ('28', '1', '2', '0', '0', '0', '0', '0', '1460468698');
INSERT INTO `bbs` VALUES ('29', '1', '2', '0', '0', '0', '0', '0', '1460468699');
INSERT INTO `bbs` VALUES ('30', '1', '2', '0', '0', '0', '0', '0', '1460468699');
INSERT INTO `bbs` VALUES ('31', '1', '2', '0', '0', '0', '0', '0', '1460468699');
INSERT INTO `bbs` VALUES ('32', '1', '2', '0', '0', '0', '0', '0', '1460468699');
INSERT INTO `bbs` VALUES ('33', '1', '2', '0', '0', '0', '0', '0', '1460468699');
INSERT INTO `bbs` VALUES ('34', '1', '2', '0', '0', '0', '0', '0', '1460468700');
INSERT INTO `bbs` VALUES ('35', '1', '2', '0', '0', '0', '0', '0', '1460468700');
INSERT INTO `bbs` VALUES ('36', '1', '2', '0', '0', '0', '0', '0', '1460468700');
INSERT INTO `bbs` VALUES ('37', '1', '2', '0', '0', '0', '0', '0', '1460468700');
INSERT INTO `bbs` VALUES ('38', '1', '2', '0', '0', '0', '0', '0', '1460468700');
INSERT INTO `bbs` VALUES ('39', '1', '2', '0', '0', '0', '0', '0', '1460468701');
INSERT INTO `bbs` VALUES ('40', '1', '2', '0', '0', '0', '0', '0', '1460468716');
INSERT INTO `bbs` VALUES ('41', '1', '2', '0', '0', '0', '0', '0', '1460468744');
INSERT INTO `bbs` VALUES ('42', '1', '2', '0', '0', '0', '0', '0', '1460468963');
INSERT INTO `bbs` VALUES ('43', '1', '2', '0', '0', '0', '0', '0', '1460515206');
INSERT INTO `bbs` VALUES ('44', '1', '2', '0', '0', '0', '0', '0', '1460714520');

-- ----------------------------
-- Table structure for bbs_attribute
-- ----------------------------
DROP TABLE IF EXISTS `bbs_attribute`;
CREATE TABLE `bbs_attribute` (
  `bbs_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`bbs_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='留言板内容表';

-- ----------------------------
-- Records of bbs_attribute
-- ----------------------------
INSERT INTO `bbs_attribute` VALUES ('1', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('2', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('3', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('4', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('5', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('6', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('7', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('8', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('9', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('10', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('11', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('12', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('13', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('14', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('15', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('16', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('17', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('18', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('19', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('20', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('21', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('22', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('23', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('24', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('25', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('26', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('27', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('28', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('29', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('30', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('31', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('32', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('33', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('34', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('35', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('36', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('37', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('38', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('39', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('40', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('41', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('42', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('43', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('44', '发发的发');

-- ----------------------------
-- Table structure for bbs_gift
-- ----------------------------
DROP TABLE IF EXISTS `bbs_gift`;
CREATE TABLE `bbs_gift` (
  `bbs_gift_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '留言者',
  `to_roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '被留言者',
  `gift_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '礼物id,包括玫瑰花,金兰花,同心结',
  `gift_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '礼物数量',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态0正常,1删除',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`bbs_gift_id`),
  KEY `tsc` (`to_roleid`,`status`,`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='留言板礼物表';

-- ----------------------------
-- Records of bbs_gift
-- ----------------------------

-- ----------------------------
-- Table structure for bbs_popularity
-- ----------------------------
DROP TABLE IF EXISTS `bbs_popularity`;
CREATE TABLE `bbs_popularity` (
  `bbs_popularity_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '留言者',
  `to_roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '被留言者',
  `is_get` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否获得回馈,0非,1是',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态0正常,1删除',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`bbs_popularity_id`),
  KEY `rtsc` (`roleid`,`to_roleid`,`status`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='留言板人气表';

-- ----------------------------
-- Records of bbs_popularity
-- ----------------------------
INSERT INTO `bbs_popularity` VALUES ('1', '1', '1', '0', '0', '1459862489');

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
-- Table structure for role_attribute
-- ----------------------------
DROP TABLE IF EXISTS `role_attribute`;
CREATE TABLE `role_attribute` (
  `roleid` bigint(20) unsigned NOT NULL,
  `msg_count` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '消息数',
  `signature` varchar(100) NOT NULL DEFAULT '' COMMENT '签名',
  `popularity_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '人气数',
  `gift_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收到礼物数',
  `sound_url` varchar(255) NOT NULL DEFAULT '' COMMENT '录音地址',
  `avatar_url` varchar(255) NOT NULL DEFAULT '' COMMENT '头像',
  `avatar_verify` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '审核状态0审核中1通过',
  `place` varchar(60) NOT NULL DEFAULT '' COMMENT '地理位置, 如:北京 海淀',
  `longitude` decimal(9,6) unsigned NOT NULL DEFAULT '0.000000' COMMENT '经度',
  `latitude` decimal(9,6) unsigned NOT NULL DEFAULT '0.000000' COMMENT '纬度',
  PRIMARY KEY (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户属性表';

-- ----------------------------
-- Records of role_attribute
-- ----------------------------
INSERT INTO `role_attribute` VALUES ('1', '0', '哈哈哈哈<alert></alert>\'afafa\"fffff', '0', '0', '', '', '1', '', '0.000000', '0.000000');
INSERT INTO `role_attribute` VALUES ('110', '0', '', '0', '0', '', '', '0', '', '0.000000', '0.000000');
INSERT INTO `role_attribute` VALUES ('111', '0', '', '0', '0', '', '', '0', '', '0.000000', '0.000000');

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
  `hostname` varchar(60) NOT NULL DEFAULT '',
  `port` varchar(60) NOT NULL DEFAULT '',
  `username` varchar(60) NOT NULL DEFAULT '',
  `password` varchar(60) NOT NULL DEFAULT '',
  `database` varchar(60) NOT NULL DEFAULT '',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`serverlist_id`),
  KEY `sc` (`serverkey`,`create_time`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='区服连接信息表';

-- ----------------------------
-- Records of serverlist
-- ----------------------------
INSERT INTO `serverlist` VALUES ('1', 's1', '测试服务器', '59.151.112.48', '', 'jingling', '123456', 'mt3_weibo', '0');

-- ----------------------------
-- Table structure for status
-- ----------------------------
DROP TABLE IF EXISTS `status`;
CREATE TABLE `status` (
  `status_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态0正常,1删除',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`status_id`),
  KEY `rssc` (`roleid`,`status_id`,`status`,`create_time`) USING BTREE,
  KEY `rsc` (`roleid`,`status`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='微博表,索引表';

-- ----------------------------
-- Records of status
-- ----------------------------
INSERT INTO `status` VALUES ('28', '1', '0', '1459853477');
INSERT INTO `status` VALUES ('29', '1', '0', '1459856222');
INSERT INTO `status` VALUES ('30', '1', '0', '1459856248');
INSERT INTO `status` VALUES ('31', '1', '0', '1459856336');
INSERT INTO `status` VALUES ('1', '111', '0', '1459256225');
INSERT INTO `status` VALUES ('2', '111', '0', '1459256300');
INSERT INTO `status` VALUES ('3', '111', '0', '1459256364');
INSERT INTO `status` VALUES ('4', '111', '0', '1459256384');
INSERT INTO `status` VALUES ('5', '111', '0', '1459256534');
INSERT INTO `status` VALUES ('6', '111', '0', '1459256536');
INSERT INTO `status` VALUES ('7', '111', '0', '1459256546');
INSERT INTO `status` VALUES ('8', '111', '0', '1459256587');
INSERT INTO `status` VALUES ('9', '111', '0', '1459256696');
INSERT INTO `status` VALUES ('10', '111', '0', '1459257160');
INSERT INTO `status` VALUES ('11', '111', '0', '1459257369');
INSERT INTO `status` VALUES ('12', '111', '0', '1459257395');
INSERT INTO `status` VALUES ('13', '111', '0', '1459257414');
INSERT INTO `status` VALUES ('14', '111', '0', '1459299712');
INSERT INTO `status` VALUES ('15', '111', '0', '1459299742');
INSERT INTO `status` VALUES ('16', '111', '0', '1459299770');
INSERT INTO `status` VALUES ('17', '111', '0', '1459299859');
INSERT INTO `status` VALUES ('18', '111', '0', '1459300017');
INSERT INTO `status` VALUES ('19', '111', '0', '1459300055');
INSERT INTO `status` VALUES ('20', '111', '0', '1459300109');
INSERT INTO `status` VALUES ('21', '111', '0', '1459300140');
INSERT INTO `status` VALUES ('22', '111', '0', '1459300619');
INSERT INTO `status` VALUES ('23', '111', '0', '1459300625');
INSERT INTO `status` VALUES ('24', '111', '0', '1459304421');
INSERT INTO `status` VALUES ('25', '111', '0', '1459304515');
INSERT INTO `status` VALUES ('26', '111', '0', '1459304640');
INSERT INTO `status` VALUES ('27', '111', '0', '1459304914');

-- ----------------------------
-- Table structure for status_attribute
-- ----------------------------
DROP TABLE IF EXISTS `status_attribute`;
CREATE TABLE `status_attribute` (
  `status_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `content` varchar(255) NOT NULL DEFAULT '',
  `img_url` varchar(255) NOT NULL DEFAULT '0' COMMENT '图片地址',
  `comment_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '评论数',
  `favorite_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '点赞数',
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微博表,内容表';

-- ----------------------------
-- Records of status_attribute
-- ----------------------------
INSERT INTO `status_attribute` VALUES ('0', 'afafdfadf', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('1', 'afafdfadf', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('2', 'afafdfadf', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('3', 'afafdfad3\'f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('4', 'afafdfad<3\'f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('5', 'afafdfad<3\'f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('6', 'afafdfad<3\'f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('7', 'afafdfad<3\'f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('8', 'afafdfad;<3\'f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('9', 'afafdfad;<3\'f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('10', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('11', '0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('12', '0123456789&lt;012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('13', '0123456789&lt;012345&lt;678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('14', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('15', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('16', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('17', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('18', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('19', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('20', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('21', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('22', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('23', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('24', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('25', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('26', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('27', 'afafdfad;&lt;3&#039;f', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('28', '大发发的', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('29', '大发发的', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('30', '大发发的', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('31', '大发发的', '', '2', '1');

-- ----------------------------
-- Table structure for status_comment
-- ----------------------------
DROP TABLE IF EXISTS `status_comment`;
CREATE TABLE `status_comment` (
  `status_comment_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `status_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '评论者',
  `content` varchar(255) NOT NULL DEFAULT '' COMMENT '评论内容',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态0正常,1删除',
  `reply_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '回复评论',
  `reply_roleid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`status_comment_id`),
  KEY `ssc` (`status_id`,`status`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='微博评论表';

-- ----------------------------
-- Records of status_comment
-- ----------------------------
INSERT INTO `status_comment` VALUES ('1', '31', '1', '先发发大发', '0', '0', '0', '1459860325');
INSERT INTO `status_comment` VALUES ('2', '31', '1', '先发发大发', '0', '0', '0', '1459860392');
INSERT INTO `status_comment` VALUES ('3', '31', '1', '先发发大发', '1', '0', '0', '1459860858');

-- ----------------------------
-- Table structure for status_favorite
-- ----------------------------
DROP TABLE IF EXISTS `status_favorite`;
CREATE TABLE `status_favorite` (
  `status_favorite_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `status_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态0正常,1删除',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`status_favorite_id`),
  KEY `ssc` (`status_id`,`status`,`create_time`) USING BTREE,
  KEY `sr` (`status_id`,`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='微博点赞表';

-- ----------------------------
-- Records of status_favorite
-- ----------------------------
INSERT INTO `status_favorite` VALUES ('1', '31', '1', '1', '1459860014');
