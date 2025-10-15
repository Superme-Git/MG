/*
Navicat MySQL Data Transfer

Source Server         : 42.62.47.222
Source Server Version : 50173
Source Host           : 42.62.47.222:3306
Source Database       : mt3_weibo

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2016-04-06 14:38:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bbs
-- ----------------------------
DROP TABLE IF EXISTS `bbs`;
CREATE TABLE `bbs` (
  `bbs_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '留言者',
  `to_roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '被留言者',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态0正常,1删除',
  `gift_type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '礼物类型,0无礼物,1玫瑰花,2金兰花,3同心结',
  `reply_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '回复id',
  `reply_roleid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`bbs_id`),
  KEY `tsc` (`to_roleid`,`status`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='留言板表';

-- ----------------------------
-- Records of bbs
-- ----------------------------
INSERT INTO `bbs` VALUES ('1', '1', '2', '0', '0', '0', '0', '1459862023');
INSERT INTO `bbs` VALUES ('2', '1', '2', '1', '0', '0', '0', '1459862276');
INSERT INTO `bbs` VALUES ('3', '1', '2', '0', '0', '0', '0', '1459862316');
INSERT INTO `bbs` VALUES ('4', '1', '2', '0', '0', '0', '0', '1459862321');
INSERT INTO `bbs` VALUES ('5', '1', '1', '0', '0', '0', '0', '1459862401');

-- ----------------------------
-- Table structure for bbs_attribute
-- ----------------------------
DROP TABLE IF EXISTS `bbs_attribute`;
CREATE TABLE `bbs_attribute` (
  `bbs_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`bbs_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='留言板内容表';

-- ----------------------------
-- Records of bbs_attribute
-- ----------------------------
INSERT INTO `bbs_attribute` VALUES ('1', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('2', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('3', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('4', '发发的发');
INSERT INTO `bbs_attribute` VALUES ('5', '发发的发');

-- ----------------------------
-- Table structure for bbs_gift
-- ----------------------------
DROP TABLE IF EXISTS `bbs_gift`;
CREATE TABLE `bbs_gift` (
  `bbs_gift_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '留言者',
  `to_roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '被留言者',
  `gift_type` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '礼物类型,1玫瑰花,2金兰花,3同心结',
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
  PRIMARY KEY (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户属性表';

-- ----------------------------
-- Records of role_attribute
-- ----------------------------
INSERT INTO `role_attribute` VALUES ('1', '0', '哈哈哈哈<alert></alert>\'afafa\"fffff', '0', '0', '', '', '0');
INSERT INTO `role_attribute` VALUES ('110', '0', '', '0', '0', '', '', '0');
INSERT INTO `role_attribute` VALUES ('111', '0', '', '0', '0', '', '', '0');

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
  `status_favorite_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `status_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态0正常,1删除',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`status_favorite_id`),
  KEY `ssc` (`status_id`,`status`,`create_time`) USING BTREE,
  KEY `sr` (`status_id`,`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微博点赞表';

-- ----------------------------
-- Records of status_favorite
-- ----------------------------
INSERT INTO `status_favorite` VALUES ('0', '31', '1', '1', '1459860014');
