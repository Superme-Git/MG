/*
Navicat MySQL Data Transfer

Source Server         : 42.62.47.222
Source Server Version : 50173
Source Host           : 42.62.47.222:3306
Source Database       : mt3_weibo

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2016-05-13 16:06:20
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
  `gift_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '礼物id,包括玫瑰花,金兰花,同心结',
  `gift_count` int(11) unsigned NOT NULL DEFAULT '0',
  `reply_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '回复id',
  `reply_roleid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`bbs_id`),
  KEY `tsc` (`to_roleid`,`status`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='留言板表';

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
-- Table structure for dashi_bbs
-- ----------------------------
DROP TABLE IF EXISTS `dashi_bbs`;
CREATE TABLE `dashi_bbs` (
  `dashi_bbs_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `push_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '推送时间,0表不推送;任务识别字段',
  `push_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态0未推送1已推送;需任务变更',
  `push_succ_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '已推送数',
  `push_fail_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '推送失败数',
  `push_total` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '总推送数',
  `serverlist_data` text NOT NULL COMMENT '选择推送的区服,serialize格式',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`dashi_bbs_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='内容表';

-- ----------------------------
-- Table structure for dashi_push
-- ----------------------------
DROP TABLE IF EXISTS `dashi_push`;
CREATE TABLE `dashi_push` (
  `dashi_push_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `dashi_bbs_id` int(11) unsigned NOT NULL DEFAULT '0',
  `serverlist_id` int(11) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '推送状态,0未推送,1推送成功,2推送失败',
  `start_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '执行时间',
  `end_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '停止时间',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`dashi_push_id`),
  KEY `ss` (`status`,`start_time`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='推送表';

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
