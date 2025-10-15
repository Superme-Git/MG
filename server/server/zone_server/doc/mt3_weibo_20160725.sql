/*
Navicat MySQL Data Transfer

Source Server         : 59.151.112.48
Source Server Version : 50173
Source Host           : 59.151.112.48:3306
Source Database       : mt3_weibo

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2016-07-25 11:11:39
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
INSERT INTO `admin` VALUES ('1', 'admin@joyogame.com', 'YHJNFo8I', '9', '1457916733', '', '0');
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
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8 COMMENT='留言板表';

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
INSERT INTO `bbs` VALUES ('45', '213991', '213991', '0', '0', '0', '0', '0', '1463045149');
INSERT INTO `bbs` VALUES ('46', '50151', '50151', '1', '0', '0', '0', '0', '1463157750');
INSERT INTO `bbs` VALUES ('47', '50151', '50151', '1', '0', '0', '0', '0', '1463157870');
INSERT INTO `bbs` VALUES ('48', '50151', '50151', '1', '0', '0', '0', '0', '1463160947');
INSERT INTO `bbs` VALUES ('49', '50151', '50151', '1', '0', '0', '0', '0', '1463160950');
INSERT INTO `bbs` VALUES ('50', '50151', '50151', '1', '0', '0', '0', '0', '1463160952');
INSERT INTO `bbs` VALUES ('51', '50151', '50151', '1', '0', '0', '0', '0', '1463160960');
INSERT INTO `bbs` VALUES ('52', '50151', '50151', '1', '0', '0', '0', '0', '1463203582');
INSERT INTO `bbs` VALUES ('53', '50151', '50151', '1', '0', '0', '0', '0', '1463203584');
INSERT INTO `bbs` VALUES ('54', '50151', '50151', '1', '0', '0', '0', '0', '1463203593');
INSERT INTO `bbs` VALUES ('55', '50151', '50151', '1', '0', '0', '0', '0', '1463203604');
INSERT INTO `bbs` VALUES ('56', '50151', '50151', '1', '0', '0', '0', '0', '1463203613');
INSERT INTO `bbs` VALUES ('57', '50151', '50151', '1', '0', '0', '0', '0', '1463203913');
INSERT INTO `bbs` VALUES ('58', '50151', '50151', '1', '0', '0', '0', '0', '1463203919');
INSERT INTO `bbs` VALUES ('59', '50151', '50151', '0', '0', '0', '0', '0', '1463209957');
INSERT INTO `bbs` VALUES ('60', '50151', '50151', '0', '0', '0', '0', '0', '1463209977');
INSERT INTO `bbs` VALUES ('61', '50151', '50151', '0', '0', '0', '0', '0', '1463209987');
INSERT INTO `bbs` VALUES ('62', '50151', '50151', '0', '0', '0', '0', '0', '1463210524');
INSERT INTO `bbs` VALUES ('63', '50151', '50151', '0', '0', '0', '0', '0', '1463210531');
INSERT INTO `bbs` VALUES ('64', '164839', '164839', '0', '0', '0', '0', '0', '1463222496');
INSERT INTO `bbs` VALUES ('65', '164839', '164839', '0', '0', '0', '0', '0', '1463222498');
INSERT INTO `bbs` VALUES ('66', '164839', '164839', '0', '0', '0', '0', '0', '1463222498');
INSERT INTO `bbs` VALUES ('67', '164839', '164839', '0', '0', '0', '0', '0', '1463222498');
INSERT INTO `bbs` VALUES ('68', '164839', '164839', '0', '0', '0', '0', '0', '1463222498');
INSERT INTO `bbs` VALUES ('69', '164839', '164839', '0', '0', '0', '67', '164839', '1463222504');
INSERT INTO `bbs` VALUES ('70', '50151', '127975', '0', '0', '0', '0', '0', '1463228836');
INSERT INTO `bbs` VALUES ('71', '41959', '41959', '0', '0', '0', '0', '0', '1463251833');
INSERT INTO `bbs` VALUES ('72', '41959', '41959', '0', '0', '0', '0', '0', '1463251834');
INSERT INTO `bbs` VALUES ('73', '41959', '41959', '0', '0', '0', '0', '0', '1463251834');
INSERT INTO `bbs` VALUES ('74', '41959', '41959', '0', '0', '0', '0', '0', '1463251834');
INSERT INTO `bbs` VALUES ('75', '41959', '41959', '0', '0', '0', '0', '0', '1463251849');
INSERT INTO `bbs` VALUES ('76', '87015', '87015', '0', '0', '0', '0', '0', '1463257721');
INSERT INTO `bbs` VALUES ('77', '29672', '25576', '0', '0', '0', '0', '0', '1463364389');
INSERT INTO `bbs` VALUES ('78', '29672', '25576', '0', '0', '0', '0', '0', '1463364706');
INSERT INTO `bbs` VALUES ('79', '29672', '25576', '0', '0', '0', '0', '0', '1463364707');
INSERT INTO `bbs` VALUES ('80', '29672', '25576', '0', '0', '0', '0', '0', '1463364708');
INSERT INTO `bbs` VALUES ('81', '29672', '25576', '0', '0', '0', '0', '0', '1463364708');
INSERT INTO `bbs` VALUES ('82', '29672', '25576', '0', '0', '0', '0', '0', '1463364708');
INSERT INTO `bbs` VALUES ('83', '29672', '25576', '0', '0', '0', '0', '0', '1463364709');
INSERT INTO `bbs` VALUES ('84', '29672', '25576', '0', '0', '0', '0', '0', '1463364709');
INSERT INTO `bbs` VALUES ('85', '29672', '25576', '0', '0', '0', '0', '0', '1463364709');
INSERT INTO `bbs` VALUES ('86', '29672', '25576', '0', '0', '0', '0', '0', '1463364709');
INSERT INTO `bbs` VALUES ('87', '29672', '25576', '0', '0', '0', '0', '0', '1463364710');
INSERT INTO `bbs` VALUES ('88', '29672', '25576', '0', '0', '0', '0', '0', '1463364710');
INSERT INTO `bbs` VALUES ('89', '29672', '25576', '0', '0', '0', '0', '0', '1463364710');
INSERT INTO `bbs` VALUES ('90', '29672', '29672', '0', '0', '0', '0', '0', '1463365558');
INSERT INTO `bbs` VALUES ('91', '29672', '29672', '0', '0', '0', '0', '0', '1463365560');
INSERT INTO `bbs` VALUES ('92', '37864', '29672', '0', '0', '0', '0', '0', '1463366081');
INSERT INTO `bbs` VALUES ('93', '37864', '29672', '0', '0', '0', '0', '0', '1463366090');
INSERT INTO `bbs` VALUES ('94', '29672', '37864', '0', '0', '0', '0', '0', '1463366462');
INSERT INTO `bbs` VALUES ('95', '37864', '29672', '0', '0', '0', '0', '0', '1463367185');
INSERT INTO `bbs` VALUES ('96', '29672', '29672', '0', '0', '0', '0', '0', '1463377092');
INSERT INTO `bbs` VALUES ('97', '29672', '29672', '0', '0', '0', '0', '0', '1463377093');
INSERT INTO `bbs` VALUES ('98', '29672', '29672', '0', '0', '0', '0', '0', '1463377120');
INSERT INTO `bbs` VALUES ('99', '29672', '29672', '0', '0', '0', '0', '0', '1463377594');
INSERT INTO `bbs` VALUES ('100', '29672', '29672', '0', '0', '0', '0', '0', '1463377595');
INSERT INTO `bbs` VALUES ('101', '21480', '21480', '0', '0', '0', '0', '0', '1463378469');
INSERT INTO `bbs` VALUES ('102', '21480', '21480', '0', '0', '0', '0', '0', '1463378471');
INSERT INTO `bbs` VALUES ('103', '21480', '21480', '0', '0', '0', '0', '0', '1463378471');
INSERT INTO `bbs` VALUES ('104', '21480', '21480', '0', '0', '0', '0', '0', '1463378611');
INSERT INTO `bbs` VALUES ('105', '21480', '21480', '0', '0', '0', '0', '0', '1463378612');
INSERT INTO `bbs` VALUES ('106', '21480', '21480', '0', '0', '0', '0', '0', '1463382686');
INSERT INTO `bbs` VALUES ('107', '58344', '21480', '0', '339103', '1', '0', '0', '1463385998');
INSERT INTO `bbs` VALUES ('108', '66536', '66536', '1', '0', '0', '0', '0', '1463415803');
INSERT INTO `bbs` VALUES ('109', '66536', '66536', '1', '0', '0', '0', '0', '1463415804');
INSERT INTO `bbs` VALUES ('110', '66536', '66536', '1', '0', '0', '0', '0', '1463415804');
INSERT INTO `bbs` VALUES ('111', '66536', '66536', '1', '0', '0', '0', '0', '1463415804');
INSERT INTO `bbs` VALUES ('112', '66536', '66536', '1', '0', '0', '0', '0', '1463415805');
INSERT INTO `bbs` VALUES ('113', '66536', '66536', '1', '0', '0', '0', '0', '1463415805');
INSERT INTO `bbs` VALUES ('114', '66536', '66536', '1', '0', '0', '0', '0', '1463415805');
INSERT INTO `bbs` VALUES ('115', '66536', '66536', '1', '0', '0', '0', '0', '1463415805');
INSERT INTO `bbs` VALUES ('116', '66536', '66536', '1', '0', '0', '0', '0', '1463415805');
INSERT INTO `bbs` VALUES ('117', '164839', '164838', '0', '0', '0', '0', '0', '1463491738');
INSERT INTO `bbs` VALUES ('118', '164839', '164838', '0', '0', '0', '0', '0', '1463491758');
INSERT INTO `bbs` VALUES ('119', '54247', '17383', '0', '339103', '2', '0', '0', '1463576459');
INSERT INTO `bbs` VALUES ('120', '54247', '54247', '1', '0', '0', '0', '0', '1463577613');
INSERT INTO `bbs` VALUES ('121', '41959', '50151', '0', '339104', '25', '0', '0', '1463650533');
INSERT INTO `bbs` VALUES ('122', '17383', '29671', '0', '339104', '1', '0', '0', '1463677814');
INSERT INTO `bbs` VALUES ('123', '17383', '29671', '0', '339104', '1', '0', '0', '1463677822');
INSERT INTO `bbs` VALUES ('124', '17383', '29671', '0', '339104', '3', '0', '0', '1463679292');
INSERT INTO `bbs` VALUES ('125', '17383', '29671', '0', '339104', '1', '0', '0', '1463679294');
INSERT INTO `bbs` VALUES ('126', '17383', '29671', '0', '339104', '1', '0', '0', '1463679295');
INSERT INTO `bbs` VALUES ('127', '17383', '29671', '0', '339104', '1', '0', '0', '1463679299');
INSERT INTO `bbs` VALUES ('128', '17383', '29671', '0', '339103', '1', '0', '0', '1463679328');
INSERT INTO `bbs` VALUES ('129', '17383', '37863', '0', '339103', '1', '0', '0', '1463679373');
INSERT INTO `bbs` VALUES ('130', '17383', '9191', '0', '339103', '1', '0', '0', '1463679387');
INSERT INTO `bbs` VALUES ('131', '17383', '50151', '0', '339104', '1', '0', '0', '1463679652');
INSERT INTO `bbs` VALUES ('132', '17383', '21479', '0', '339103', '1', '0', '0', '1463679692');
INSERT INTO `bbs` VALUES ('133', '17383', '21479', '0', '339104', '1', '0', '0', '1463679706');
INSERT INTO `bbs` VALUES ('134', '17383', '21479', '0', '339104', '1', '0', '0', '1463679736');
INSERT INTO `bbs` VALUES ('135', '17383', '29671', '0', '339104', '1', '0', '0', '1463680118');
INSERT INTO `bbs` VALUES ('136', '17383', '29671', '0', '339104', '2', '0', '0', '1463680362');
INSERT INTO `bbs` VALUES ('137', '17383', '29671', '0', '339104', '98', '0', '0', '1463680422');
INSERT INTO `bbs` VALUES ('138', '17383', '29671', '0', '339104', '1', '0', '0', '1463680430');
INSERT INTO `bbs` VALUES ('139', '17383', '13287', '0', '339104', '1', '0', '0', '1463680456');
INSERT INTO `bbs` VALUES ('140', '17383', '13287', '0', '339104', '1', '0', '0', '1463680460');
INSERT INTO `bbs` VALUES ('141', '17383', '29671', '0', '339103', '1', '0', '0', '1463680472');
INSERT INTO `bbs` VALUES ('142', '66535', '66535', '0', '0', '0', '0', '0', '1463735248');
INSERT INTO `bbs` VALUES ('143', '66535', '66535', '0', '0', '0', '0', '0', '1463735255');
INSERT INTO `bbs` VALUES ('144', '29671', '123879', '0', '339103', '1', '0', '0', '1464234430');
INSERT INTO `bbs` VALUES ('145', '25575', '193511', '0', '339104', '1', '0', '0', '1464248212');
INSERT INTO `bbs` VALUES ('146', '25575', '111591', '0', '339103', '1', '0', '0', '1464248231');
INSERT INTO `bbs` VALUES ('147', '9223372036854775807', '9223372036854775807', '0', '0', '0', '0', '0', '1466674367');
INSERT INTO `bbs` VALUES ('148', '9223372036854775807', '9223372036854775807', '0', '0', '0', '0', '0', '1466678182');
INSERT INTO `bbs` VALUES ('149', '9223372036854775807', '9223372036854775807', '0', '0', '0', '0', '0', '1466679979');
INSERT INTO `bbs` VALUES ('150', '9223372036854775807', '9223372036854775807', '0', '0', '0', '0', '0', '1466679995');
INSERT INTO `bbs` VALUES ('151', '9223372036854775807', '9223372036854775807', '0', '0', '0', '0', '0', '1466738041');

-- ----------------------------
-- Table structure for bbs_attribute
-- ----------------------------
DROP TABLE IF EXISTS `bbs_attribute`;
CREATE TABLE `bbs_attribute` (
  `bbs_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '内容',
  PRIMARY KEY (`bbs_id`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8 COMMENT='留言板内容表';

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
INSERT INTO `bbs_attribute` VALUES ('45', '&lt;T t=&quot;gfhj&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('46', '&lt;T t=&quot;yghk&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('47', '&lt;T t=&quot;yghk卡奴&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('48', '&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('49', '&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('50', '&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('51', '&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;&lt;T t=&quot;lij&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('52', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('53', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('54', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;T t=&quot;垃圾lo&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('55', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('56', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('57', '&lt;T t=&quot;啦啦&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('58', '&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('59', '&lt;T t=&quot;llkon&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('60', '&lt;T t=&quot;llkon&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('61', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;E e=&quot;905&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('62', '&lt;E e=&quot;905&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('63', '&lt;E e=&quot;905&quot;&gt;&lt;/E&gt;&lt;T t=&quot;al&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('64', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('65', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('66', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('67', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('68', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('69', '&lt;E e=&quot;917&quot;&gt;&lt;/E&gt;&lt;E e=&quot;900&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('70', '&lt;T t=&quot;bbh&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('71', '&lt;T t=&quot;ss&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('72', '&lt;T t=&quot;ss&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('73', '&lt;T t=&quot;ss&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('74', '&lt;T t=&quot;ss&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('75', '&lt;T t=&quot;ss&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('76', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('77', '&lt;T t=&quot;yghhj&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('78', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('79', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('80', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('81', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('82', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('83', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('84', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('85', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('86', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('87', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('88', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('89', '&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('90', '&lt;T t=&quot;yhhm&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('91', '&lt;T t=&quot;yhhm&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('92', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('93', '&lt;T t=&quot;tyt&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('94', '&lt;T t=&quot;123&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('95', '&lt;T t=&quot;yyy&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('96', '&lt;T t=&quot;guj&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('97', '&lt;T t=&quot;guj&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('98', '&lt;T t=&quot;guj&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('99', '&lt;T t=&quot;yytgh&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('100', '&lt;T t=&quot;yytgh&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('101', '&lt;T t=&quot;ert&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('102', '&lt;T t=&quot;ert&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('103', '&lt;T t=&quot;ert&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('104', '&lt;T t=&quot;ertytyu&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('105', '&lt;T t=&quot;ertytyu&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('106', '&lt;T t=&quot;123&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('107', '1222');
INSERT INTO `bbs_attribute` VALUES ('108', '&lt;T t=&quot;的东西&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('109', '&lt;T t=&quot;的东西&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('110', '&lt;T t=&quot;的东西&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('111', '&lt;T t=&quot;的东西&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('112', '&lt;T t=&quot;的东西&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('113', '&lt;T t=&quot;的东西&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('114', '&lt;T t=&quot;的东西&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('115', '&lt;T t=&quot;的东西&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('116', '&lt;T t=&quot;的东西&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('117', '解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准');
INSERT INTO `bbs_attribute` VALUES ('118', '解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准解决部分标题不能精准');
INSERT INTO `bbs_attribute` VALUES ('119', '滚滚滚');
INSERT INTO `bbs_attribute` VALUES ('120', '&lt;E e=&quot;900&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('121', '好');
INSERT INTO `bbs_attribute` VALUES ('122', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('123', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('124', '微微亏赠送给你3朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('125', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('126', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('127', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('128', '由于 uii');
INSERT INTO `bbs_attribute` VALUES ('129', '微微亏赠送给你1朵玫瑰花');
INSERT INTO `bbs_attribute` VALUES ('130', '微微亏赠送给你1朵玫瑰花');
INSERT INTO `bbs_attribute` VALUES ('131', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('132', '微微亏赠送给你1朵玫瑰花');
INSERT INTO `bbs_attribute` VALUES ('133', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('134', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('135', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('136', '微微亏赠送给你2朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('137', '微微亏赠送给你99朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('138', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('139', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('140', '微微亏赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('141', '微微亏赠送给你1朵玫瑰花');
INSERT INTO `bbs_attribute` VALUES ('142', '&lt;T t=&quot;bvfhjm&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;');
INSERT INTO `bbs_attribute` VALUES ('143', '&lt;E e=&quot;910&quot;&gt;&lt;/E&gt;&lt;E e=&quot;920&quot;&gt;&lt;/E&gt;');
INSERT INTO `bbs_attribute` VALUES ('144', '叶天赠送给你1朵玫瑰花');
INSERT INTO `bbs_attribute` VALUES ('145', '雷神夜王赠送给你1朵郁金香');
INSERT INTO `bbs_attribute` VALUES ('146', '雷神夜王赠送给你1朵玫瑰花');
INSERT INTO `bbs_attribute` VALUES ('147', '测试,大师');
INSERT INTO `bbs_attribute` VALUES ('148', '6666');
INSERT INTO `bbs_attribute` VALUES ('149', '2');
INSERT INTO `bbs_attribute` VALUES ('150', '666');
INSERT INTO `bbs_attribute` VALUES ('151', '20160624');

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
  `content` text NOT NULL COMMENT '内容',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`bbs_gift_id`),
  KEY `tsc` (`to_roleid`,`status`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='留言板礼物表';

-- ----------------------------
-- Records of bbs_gift
-- ----------------------------
INSERT INTO `bbs_gift` VALUES ('1', '58344', '21480', '339103', '1', '0', '1222', '1463385998');
INSERT INTO `bbs_gift` VALUES ('2', '54247', '17383', '339103', '2', '0', '滚滚滚', '1463576459');
INSERT INTO `bbs_gift` VALUES ('3', '41959', '50151', '339104', '25', '0', '好', '1463650533');
INSERT INTO `bbs_gift` VALUES ('4', '17383', '29671', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463677814');
INSERT INTO `bbs_gift` VALUES ('5', '17383', '29671', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463677822');
INSERT INTO `bbs_gift` VALUES ('6', '17383', '29671', '339104', '3', '0', '微微亏赠送给你3朵郁金香', '1463679292');
INSERT INTO `bbs_gift` VALUES ('7', '17383', '29671', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463679294');
INSERT INTO `bbs_gift` VALUES ('8', '17383', '29671', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463679295');
INSERT INTO `bbs_gift` VALUES ('9', '17383', '29671', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463679299');
INSERT INTO `bbs_gift` VALUES ('10', '17383', '29671', '339103', '1', '0', '由于 uii', '1463679328');
INSERT INTO `bbs_gift` VALUES ('11', '17383', '37863', '339103', '1', '0', '微微亏赠送给你1朵玫瑰花', '1463679373');
INSERT INTO `bbs_gift` VALUES ('12', '17383', '9191', '339103', '1', '0', '微微亏赠送给你1朵玫瑰花', '1463679387');
INSERT INTO `bbs_gift` VALUES ('13', '17383', '50151', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463679652');
INSERT INTO `bbs_gift` VALUES ('14', '17383', '21479', '339103', '1', '0', '微微亏赠送给你1朵玫瑰花', '1463679692');
INSERT INTO `bbs_gift` VALUES ('15', '17383', '21479', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463679706');
INSERT INTO `bbs_gift` VALUES ('16', '17383', '21479', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463679736');
INSERT INTO `bbs_gift` VALUES ('17', '17383', '29671', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463680118');
INSERT INTO `bbs_gift` VALUES ('18', '17383', '29671', '339104', '2', '0', '微微亏赠送给你2朵郁金香', '1463680362');
INSERT INTO `bbs_gift` VALUES ('19', '17383', '29671', '339104', '98', '0', '微微亏赠送给你99朵郁金香', '1463680422');
INSERT INTO `bbs_gift` VALUES ('20', '17383', '29671', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463680430');
INSERT INTO `bbs_gift` VALUES ('21', '17383', '13287', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463680456');
INSERT INTO `bbs_gift` VALUES ('22', '17383', '13287', '339104', '1', '0', '微微亏赠送给你1朵郁金香', '1463680460');
INSERT INTO `bbs_gift` VALUES ('23', '17383', '29671', '339103', '1', '0', '微微亏赠送给你1朵玫瑰花', '1463680472');
INSERT INTO `bbs_gift` VALUES ('24', '29671', '123879', '339103', '1', '0', '叶天赠送给你1朵玫瑰花', '1464234430');
INSERT INTO `bbs_gift` VALUES ('25', '25575', '193511', '339104', '1', '0', '雷神夜王赠送给你1朵郁金香', '1464248212');
INSERT INTO `bbs_gift` VALUES ('26', '25575', '111591', '339103', '1', '0', '雷神夜王赠送给你1朵玫瑰花', '1464248231');

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='留言板人气表';

-- ----------------------------
-- Records of bbs_popularity
-- ----------------------------
INSERT INTO `bbs_popularity` VALUES ('1', '1', '1', '0', '0', '1459862489');
INSERT INTO `bbs_popularity` VALUES ('2', '9191', '5095', '0', '0', '1463238669');
INSERT INTO `bbs_popularity` VALUES ('3', '17383', '33767', '0', '0', '1463238778');
INSERT INTO `bbs_popularity` VALUES ('4', '17383', '41959', '0', '0', '1463238820');
INSERT INTO `bbs_popularity` VALUES ('5', '54247', '41959', '0', '0', '1463238845');
INSERT INTO `bbs_popularity` VALUES ('6', '41959', '17383', '1', '0', '1463238897');
INSERT INTO `bbs_popularity` VALUES ('7', '25575', '37863', '0', '0', '1463251763');
INSERT INTO `bbs_popularity` VALUES ('8', '62439', '29671', '0', '0', '1463252559');
INSERT INTO `bbs_popularity` VALUES ('9', '87015', '25575', '0', '0', '1463254224');
INSERT INTO `bbs_popularity` VALUES ('10', '29672', '25576', '0', '0', '1463364383');
INSERT INTO `bbs_popularity` VALUES ('11', '37864', '29672', '0', '0', '1463366277');
INSERT INTO `bbs_popularity` VALUES ('12', '5095', '9191', '0', '0', '1463418199');
INSERT INTO `bbs_popularity` VALUES ('13', '123879', '115687', '0', '0', '1463684679');

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
INSERT INTO `role` VALUES ('5095', '道里斯火影', '5', '1');
INSERT INTO `role` VALUES ('5096', '憎恶依耶芙特', '1', '1');
INSERT INTO `role` VALUES ('5097', '王子暗歌', '5', '1');
INSERT INTO `role` VALUES ('5098', '马修丹迪', '1', '1');
INSERT INTO `role` VALUES ('9191', '野兽夜锋', '5', '1');
INSERT INTO `role` VALUES ('9192', '潮汐', '4', '1');
INSERT INTO `role` VALUES ('9193', '迪斯克艾尼', '5', '1');
INSERT INTO `role` VALUES ('9194', '欧阳卡戎', '3', '1');
INSERT INTO `role` VALUES ('13287', '宝贝', '6', '1');
INSERT INTO `role` VALUES ('13288', '大少爷', '1', '1');
INSERT INTO `role` VALUES ('13289', '红人小强', '5', '1');
INSERT INTO `role` VALUES ('13290', '大爱雏田', '4', '1');
INSERT INTO `role` VALUES ('17383', '我的', '1', '1');
INSERT INTO `role` VALUES ('17384', '风灵格拉迪斯', '1', '1');
INSERT INTO `role` VALUES ('17385', '慕容暗星', '1', '1');
INSERT INTO `role` VALUES ('17386', '哈帝悲悯', '5', '1');
INSERT INTO `role` VALUES ('21479', '格林顿赞比', '5', '1');
INSERT INTO `role` VALUES ('21480', '风行者竹子', '1', '1');
INSERT INTO `role` VALUES ('21481', '迪克卡内基', '5', '1');
INSERT INTO `role` VALUES ('21482', '加里维文', '5', '1');
INSERT INTO `role` VALUES ('25575', '迈克尔夕红', '4', '1');
INSERT INTO `role` VALUES ('25576', '艾德里羽毛', '4', '1');
INSERT INTO `role` VALUES ('25577', '伊莱诗桃', '2', '1');
INSERT INTO `role` VALUES ('25578', '谜团多恩', '5', '1');
INSERT INTO `role` VALUES ('29671', '给你', '2', '1');
INSERT INTO `role` VALUES ('29672', '法师轻歌', '5', '1');
INSERT INTO `role` VALUES ('29673', '猛犸明角', '5', '1');
INSERT INTO `role` VALUES ('29674', '米鲁兹天痕', '5', '1');
INSERT INTO `role` VALUES ('33767', '葛列格匪徒', '3', '1');
INSERT INTO `role` VALUES ('33768', '布莱恩小渔', '3', '1');
INSERT INTO `role` VALUES ('33769', '提利尔风行', '5', '1');
INSERT INTO `role` VALUES ('33770', '艾奥罗佛能', '5', '1');
INSERT INTO `role` VALUES ('37863', '死亡之翼古多拉', '5', '1');
INSERT INTO `role` VALUES ('37864', '污染者风歌', '3', '1');
INSERT INTO `role` VALUES ('37865', '唐纳德罗里', '5', '1');
INSERT INTO `role` VALUES ('37866', '狄蒂丝竹子', '5', '1');
INSERT INTO `role` VALUES ('41959', '哈伦书文', '6', '1');
INSERT INTO `role` VALUES ('41960', '安迪伊迪丝', '5', '1');
INSERT INTO `role` VALUES ('41961', '凯里约克', '5', '1');
INSERT INTO `role` VALUES ('41962', '马丁夕红', '2', '1');
INSERT INTO `role` VALUES ('46055', '埃尔维珀莉', '6', '1');
INSERT INTO `role` VALUES ('46056', '兽王凯莉', '6', '1');
INSERT INTO `role` VALUES ('46057', '艾德蒙莎柏', '6', '1');
INSERT INTO `role` VALUES ('46058', '祖达克阿丽', '2', '1');
INSERT INTO `role` VALUES ('50151', '国王奥利', '1', '1');
INSERT INTO `role` VALUES ('50152', '汉米敦基思', '5', '1');
INSERT INTO `role` VALUES ('50153', '雅各布贾尔', '1', '1');
INSERT INTO `role` VALUES ('50154', '徒利秘书', '2', '1');
INSERT INTO `role` VALUES ('54247', '哈伦娜提', '4', '1');
INSERT INTO `role` VALUES ('54248', '圣骑士爱琳', '6', '1');
INSERT INTO `role` VALUES ('54249', '泰兰德罗睺', '5', '1');
INSERT INTO `role` VALUES ('54250', '奥斯汀幻巧', '2', '1');
INSERT INTO `role` VALUES ('58343', '洛艾儿', '6', '1');
INSERT INTO `role` VALUES ('58344', '道格拉爱尔', '2', '1');
INSERT INTO `role` VALUES ('58345', '盗贼悟空', '5', '1');
INSERT INTO `role` VALUES ('58346', '阿曼尼香远', '4', '1');
INSERT INTO `role` VALUES ('62439', '暗矛索隆', '5', '1');
INSERT INTO `role` VALUES ('62440', '古路迪小秘', '4', '1');
INSERT INTO `role` VALUES ('62441', '奥克斯罗伦', '6', '1');
INSERT INTO `role` VALUES ('62442', '熊怪杜瓦', '5', '1');
INSERT INTO `role` VALUES ('66535', '卡德加乔伊斯', '5', '1');
INSERT INTO `role` VALUES ('66536', '延森炎鹰', '5', '1');
INSERT INTO `role` VALUES ('66537', '卡利姆莱兹', '6', '1');
INSERT INTO `role` VALUES ('66538', '哈伦暗歌', '5', '1');
INSERT INTO `role` VALUES ('70631', '伊里亚一辉', '5', '1');
INSERT INTO `role` VALUES ('70632', '赌神赖吉', '5', '1');
INSERT INTO `role` VALUES ('70633', '约瑟夫烁星', '3', '1');
INSERT INTO `role` VALUES ('74727', '猎潮者安特', '1', '1');
INSERT INTO `role` VALUES ('74728', '大爱黑眼', '3', '1');
INSERT INTO `role` VALUES ('74729', '永恒泡泡', '5', '1');
INSERT INTO `role` VALUES ('78823', '上古之神凯丝', '6', '1');
INSERT INTO `role` VALUES ('78825', '克修拉赤髪', '1', '1');
INSERT INTO `role` VALUES ('82919', '司马卡蒙', '3', '1');
INSERT INTO `role` VALUES ('82921', '泰兰德贝尔', '1', '1');
INSERT INTO `role` VALUES ('87015', '乌瑟尔洛萨', '5', '1');
INSERT INTO `role` VALUES ('87017', '加百利山中', '5', '1');
INSERT INTO `role` VALUES ('91111', '宇智波吉欣', '5', '1');
INSERT INTO `role` VALUES ('91113', '天使柔歌', '5', '1');
INSERT INTO `role` VALUES ('95207', '伊比喜千凡', '6', '1');
INSERT INTO `role` VALUES ('95209', '德波尔星光', '5', '1');
INSERT INTO `role` VALUES ('99303', '霍夫曼菲丽丝', '2', '1');
INSERT INTO `role` VALUES ('99305', '马科斯咆哮', '5', '1');
INSERT INTO `role` VALUES ('103399', '黑丝大白腿', '6', '1');
INSERT INTO `role` VALUES ('103401', '哈里森碧翠丝', '2', '1');
INSERT INTO `role` VALUES ('107495', '善良维雅', '4', '1');
INSERT INTO `role` VALUES ('107497', '艾利克旺妲', '4', '1');
INSERT INTO `role` VALUES ('111591', '艾德蒙邪武', '3', '1');
INSERT INTO `role` VALUES ('111593', '雅各布迪肯斯', '5', '1');
INSERT INTO `role` VALUES ('115687', '旋律华德', '5', '1');
INSERT INTO `role` VALUES ('115689', '潮汐yoyo', '2', '1');
INSERT INTO `role` VALUES ('119783', '德摩尔朱莉娅', '2', '1');
INSERT INTO `role` VALUES ('119785', '女王瑞伊', '4', '1');
INSERT INTO `role` VALUES ('123879', '精英霍尔', '3', '1');
INSERT INTO `role` VALUES ('123881', '爱格伯小渔', '3', '1');
INSERT INTO `role` VALUES ('127975', 'hhhhhhhhhhhhhh', '5', '1');
INSERT INTO `role` VALUES ('127977', '史丹杜如凡', '4', '1');
INSERT INTO `role` VALUES ('132071', '丧尸法奥', '5', '1');
INSERT INTO `role` VALUES ('132073', '格纳茨芷珊', '2', '1');
INSERT INTO `role` VALUES ('136167', '勇士华德', '3', '1');
INSERT INTO `role` VALUES ('136169', '施密特夜语', '3', '1');
INSERT INTO `role` VALUES ('140263', '迪斯克彼得', '5', '1');
INSERT INTO `role` VALUES ('140265', '盖伊血斧', '5', '1');
INSERT INTO `role` VALUES ('144359', '爱德华婕西', '2', '1');
INSERT INTO `role` VALUES ('144361', '盖雷玛', '5', '1');
INSERT INTO `role` VALUES ('148455', '杜威留斯', '5', '1');
INSERT INTO `role` VALUES ('148457', '史里乌艾文', '5', '1');
INSERT INTO `role` VALUES ('152551', '雅可夫南希', '2', '1');
INSERT INTO `role` VALUES ('152553', '加布力埃拉', '6', '1');
INSERT INTO `role` VALUES ('156647', '霍尔美罗', '2', '1');
INSERT INTO `role` VALUES ('156649', '寂寞小雯', '4', '1');
INSERT INTO `role` VALUES ('160743', '单身风歌', '5', '1');
INSERT INTO `role` VALUES ('160745', '柔情耶呼', '5', '1');
INSERT INTO `role` VALUES ('164839', '凯斯鲁思', '3', '1');
INSERT INTO `role` VALUES ('164841', '法师珀西', '5', '1');
INSERT INTO `role` VALUES ('168935', '吉姆卡拉', '6', '1');
INSERT INTO `role` VALUES ('168937', '卑弥呼乔拉', '6', '1');
INSERT INTO `role` VALUES ('173031', '加布力浪子', '5', '1');
INSERT INTO `role` VALUES ('173033', '伊夫力婕咪', '6', '1');
INSERT INTO `role` VALUES ('177127', '吉恩依丝', '4', '1');
INSERT INTO `role` VALUES ('177129', '瑞利安卡米拉', '6', '1');
INSERT INTO `role` VALUES ('181223', '古加多威尼斯', '5', '1');
INSERT INTO `role` VALUES ('181225', '约翰卡门', '3', '1');
INSERT INTO `role` VALUES ('185319', '午夜梦', '2', '1');
INSERT INTO `role` VALUES ('185321', '吉安娜童虎', '3', '1');
INSERT INTO `role` VALUES ('189415', '温柔阿芙', '4', '1');
INSERT INTO `role` VALUES ('189417', '霍华德苜蓿', '1', '1');
INSERT INTO `role` VALUES ('193511', '王子雷玛', '3', '1');
INSERT INTO `role` VALUES ('193513', '阿鲁巴鲁思', '5', '1');
INSERT INTO `role` VALUES ('197607', '撒密安火石', '1', '1');
INSERT INTO `role` VALUES ('197609', '爱尔玛金百莉', '5', '1');
INSERT INTO `role` VALUES ('201703', '冰龙爱玛', '4', '1');
INSERT INTO `role` VALUES ('201705', '卡卡西克林', '5', '1');
INSERT INTO `role` VALUES ('205799', '无情土豪', '1', '1');
INSERT INTO `role` VALUES ('205801', '葛列格烁星', '1', '1');
INSERT INTO `role` VALUES ('209895', '杰弗瑞羽弓', '1', '1');
INSERT INTO `role` VALUES ('209897', '贾斯汀桃乐斯', '5', '1');
INSERT INTO `role` VALUES ('213991', '德鲁伊波特', '5', '1');
INSERT INTO `role` VALUES ('213993', '苏兰特炎伤', '5', '1');
INSERT INTO `role` VALUES ('218087', '范凝云', '6', '1');
INSERT INTO `role` VALUES ('218089', '安威尔皇帝', '5', '1');
INSERT INTO `role` VALUES ('222183', '比利玛瑙', '5', '1');
INSERT INTO `role` VALUES ('222185', '夏侯猿飞', '3', '1');
INSERT INTO `role` VALUES ('226279', '哈里森洛克', '5', '1');
INSERT INTO `role` VALUES ('226281', '汉森特洛', '5', '1');
INSERT INTO `role` VALUES ('230375', '经典狄更斯', '5', '1');
INSERT INTO `role` VALUES ('230377', '幻想跃火', '1', '1');
INSERT INTO `role` VALUES ('234471', '大爱翠柏', '6', '1');
INSERT INTO `role` VALUES ('234473', '伊夫力小雯', '2', '1');
INSERT INTO `role` VALUES ('238567', '查尔斯哈里', '5', '1');
INSERT INTO `role` VALUES ('238569', '乔葛谢', '6', '1');
INSERT INTO `role` VALUES ('242663', '艾德蒙乌龙', '5', '1');
INSERT INTO `role` VALUES ('242665', '爱迪生露丝', '2', '1');
INSERT INTO `role` VALUES ('246759', '泰兰德兰琪', '6', '1');
INSERT INTO `role` VALUES ('246761', '八宝饭好吃', '6', '1');
INSERT INTO `role` VALUES ('250855', '艾伯特瞻得', '5', '1');
INSERT INTO `role` VALUES ('254951', '丹尼尔莱特', '5', '1');
INSERT INTO `role` VALUES ('259047', '肯尼斯依夫', '5', '1');
INSERT INTO `role` VALUES ('263143', '安莱莎', '4', '1');
INSERT INTO `role` VALUES ('267239', '罗卡奥林亚', '1', '1');
INSERT INTO `role` VALUES ('271335', '大卫史考', '5', '1');
INSERT INTO `role` VALUES ('275431', '撒密安文文', '5', '1');
INSERT INTO `role` VALUES ('279527', '杰佛理路飞', '5', '1');
INSERT INTO `role` VALUES ('283623', '加百利晶钻', '5', '1');
INSERT INTO `role` VALUES ('287719', '兽人波奇', '1', '1');
INSERT INTO `role` VALUES ('291815', '杰西阳翼', '3', '1');

-- ----------------------------
-- Table structure for role_attribute
-- ----------------------------
DROP TABLE IF EXISTS `role_attribute`;
CREATE TABLE `role_attribute` (
  `roleid` bigint(20) unsigned NOT NULL,
  `msg_count` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '消息数',
  `signature` varchar(255) NOT NULL DEFAULT '' COMMENT '签名',
  `popularity_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '人气数',
  `gift_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收到礼物数',
  `sound_url` varchar(255) NOT NULL DEFAULT '' COMMENT '录音地址',
  `avatar_url` varchar(255) NOT NULL DEFAULT '' COMMENT '头像',
  `avatar_verify` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '审核状态0审核中1通过2不通过',
  `place` varchar(60) NOT NULL DEFAULT '' COMMENT '地理位置, 如:北京 海淀',
  `longitude` decimal(9,6) unsigned NOT NULL DEFAULT '0.000000' COMMENT '经度',
  `latitude` decimal(9,6) unsigned NOT NULL DEFAULT '0.000000' COMMENT '纬度',
  `avatar_verify_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户属性表';

-- ----------------------------
-- Records of role_attribute
-- ----------------------------
INSERT INTO `role_attribute` VALUES ('1', '0', '哈哈哈哈<alert></alert>\'afafa\"fffff', '0', '0', '', 'http://59.151.112.45:8822/images/2016/06/29/57733d3c9ef84.jpg', '2', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('110', '0', '', '0', '0', '', '', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('111', '0', '', '0', '0', '', '', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('5095', '0', 'xbjxjjxjznzjzjjznznznznnznznznznnnxnxnxnxnxnnxnxnxjxnjxnxjkxjxjnjdjdjsjsjxjzjksmsksmsmmsmxmsmsmzmsms', '0', '0', '', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/17/573a008122777.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('9191', '0', '', '0', '1', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('13287', '0', '', '0', '2', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('17383', '0', '', '0', '1', '', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/17/573b38cfde4ea.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('21480', '0', '', '0', '1', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('25575', '1', '', '0', '0', '', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/57377351c8d08.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('25576', '2', '', '0', '0', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('29671', '0', '', '0', '12', '', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/18/573c590b7cdc6.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('29672', '0', 'tfgy', '0', '0', 'http://59.151.112.45:8821/resource/uploads/sounds/2016/05/16/57392ec0d0118.mp3', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/16/57392be42a955.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('33767', '2', '', '0', '0', '', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/573773b20a0a9.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('37863', '5', '', '0', '1', 'http://59.151.112.45:8821/resource/uploads/sounds/2016/05/15/5737766c20505.mp3', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/5737726b85df8.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('37864', '0', '', '0', '0', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('41959', '1', '', '0', '0', 'http://59.151.112.45:8821/resource/uploads/sounds/2016/05/15/57377970e4ca7.mp3', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('46055', '1', '', '0', '0', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('46056', '0', '爱来', '0', '0', 'http://59.151.112.45:8821/resource/uploads/sounds/2016/05/16/57394323b89ad.mp3', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/16/57394252df7a1.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('50151', '1', 'njle5w11', '0', '2', 'http://59.151.112.45:8821/resource/uploads/sounds/2016/05/14/57370f1f64a32.mp3', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/14/5736ef67707ec.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('54247', '0', '', '0', '0', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('62439', '1', '', '0', '0', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('66535', '0', 'sdrcyggyggggggffffgyhgfff', '0', '0', 'http://59.151.112.45:8821/resource/uploads/sounds/2016/05/20/573ed279bb9e5.mp3', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/20/573ed00d2bb7a.png', '1', '北京市 . 朝阳区', '116.410293', '40.033374', '0');
INSERT INTO `role_attribute` VALUES ('66536', '0', '', '0', '0', 'http://59.151.112.45:8821/resource/uploads/sounds/2016/05/17/5739f3d3553ad.mp3', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('70631', '1', '', '0', '0', 'http://59.151.112.45:8821/resource/uploads/sounds/2016/05/15/57377cd5519da.mp3', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/57377cb1eca65.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('74727', '1', '', '0', '0', '', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/57377cb862ec0.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('87015', '0', 'ww', '0', '0', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('115687', '0', '', '0', '0', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('127975', '0', '', '0', '0', '', '', '0', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('164839', '1', 'u5u54e6u62114c8u54c8', '0', '0', 'http://59.151.112.45:8821/resource/uploads/sounds/2016/05/14/57370ac2106d3.mp3', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/14/57370b0d04ca4.png', '1', '', '0.000000', '0.000000', '0');
INSERT INTO `role_attribute` VALUES ('213991', '0', '', '0', '0', 'http://59.151.112.45:8821/resource/uploads/sounds/2016/05/12/5734982f598dd.wav', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/12/57348d7b3faf3.png', '1', '', '0.000000', '0.000000', '0');

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
INSERT INTO `role_relation` VALUES ('9192', '17384', '1');
INSERT INTO `role_relation` VALUES ('9192', '21480', '1');
INSERT INTO `role_relation` VALUES ('9192', '13288', '1');
INSERT INTO `role_relation` VALUES ('13288', '9192', '1');
INSERT INTO `role_relation` VALUES ('29672', '25576', '1');
INSERT INTO `role_relation` VALUES ('17384', '50152', '1');
INSERT INTO `role_relation` VALUES ('50152', '25576', '1');
INSERT INTO `role_relation` VALUES ('50152', '54248', '1');
INSERT INTO `role_relation` VALUES ('177127', '173031', '1');
INSERT INTO `role_relation` VALUES ('177127', '87015', '1');
INSERT INTO `role_relation` VALUES ('177127', '95207', '1');
INSERT INTO `role_relation` VALUES ('127975', '87015', '1');
INSERT INTO `role_relation` VALUES ('173031', '177127', '1');
INSERT INTO `role_relation` VALUES ('201703', '87015', '1');
INSERT INTO `role_relation` VALUES ('201703', '197607', '1');
INSERT INTO `role_relation` VALUES ('201703', '173031', '1');
INSERT INTO `role_relation` VALUES ('201703', '127975', '1');
INSERT INTO `role_relation` VALUES ('201703', '177127', '1');
INSERT INTO `role_relation` VALUES ('201703', '193511', '1');
INSERT INTO `role_relation` VALUES ('201703', '209895', '1');
INSERT INTO `role_relation` VALUES ('201703', '54247', '1');
INSERT INTO `role_relation` VALUES ('201703', '95207', '1');
INSERT INTO `role_relation` VALUES ('201703', '70631', '1');
INSERT INTO `role_relation` VALUES ('201703', '205799', '1');
INSERT INTO `role_relation` VALUES ('9191', '37863', '1');
INSERT INTO `role_relation` VALUES ('9191', '17383', '1');
INSERT INTO `role_relation` VALUES ('9191', '25575', '1');
INSERT INTO `role_relation` VALUES ('9191', '29671', '1');
INSERT INTO `role_relation` VALUES ('9191', '33767', '1');
INSERT INTO `role_relation` VALUES ('9191', '21479', '1');
INSERT INTO `role_relation` VALUES ('78823', '66535', '1');
INSERT INTO `role_relation` VALUES ('78823', '62439', '1');
INSERT INTO `role_relation` VALUES ('78823', '70631', '1');
INSERT INTO `role_relation` VALUES ('78823', '58343', '1');
INSERT INTO `role_relation` VALUES ('78823', '50151', '1');
INSERT INTO `role_relation` VALUES ('78823', '74727', '1');
INSERT INTO `role_relation` VALUES ('74727', '37863', '1');
INSERT INTO `role_relation` VALUES ('74727', '78823', '1');
INSERT INTO `role_relation` VALUES ('74727', '33767', '1');
INSERT INTO `role_relation` VALUES ('74727', '66535', '1');
INSERT INTO `role_relation` VALUES ('74727', '50151', '1');
INSERT INTO `role_relation` VALUES ('66535', '78823', '1');
INSERT INTO `role_relation` VALUES ('66535', '87015', '1');
INSERT INTO `role_relation` VALUES ('66535', '33767', '1');
INSERT INTO `role_relation` VALUES ('66535', '91111', '1');
INSERT INTO `role_relation` VALUES ('66535', '82919', '1');
INSERT INTO `role_relation` VALUES ('66535', '58343', '1');
INSERT INTO `role_relation` VALUES ('103399', '17383', '1');
INSERT INTO `role_relation` VALUES ('103399', '78823', '1');
INSERT INTO `role_relation` VALUES ('103399', '37863', '1');
INSERT INTO `role_relation` VALUES ('103399', '111591', '1');
INSERT INTO `role_relation` VALUES ('103399', '50151', '1');
INSERT INTO `role_relation` VALUES ('95207', '37863', '1');
INSERT INTO `role_relation` VALUES ('95207', '107495', '1');
INSERT INTO `role_relation` VALUES ('95207', '66535', '1');
INSERT INTO `role_relation` VALUES ('95207', '87015', '1');
INSERT INTO `role_relation` VALUES ('95207', '103399', '1');
INSERT INTO `role_relation` VALUES ('95207', '5095', '1');
INSERT INTO `role_relation` VALUES ('103399', '95207', '1');
INSERT INTO `role_relation` VALUES ('246761', '193513', '1');
INSERT INTO `role_relation` VALUES ('246761', '201705', '1');
INSERT INTO `role_relation` VALUES ('246761', '213993', '1');
INSERT INTO `role_relation` VALUES ('246761', '230377', '1');
INSERT INTO `role_relation` VALUES ('246761', '226281', '1');
INSERT INTO `role_relation` VALUES ('246761', '181225', '1');
INSERT INTO `role_relation` VALUES ('29674', '37866', '1');
INSERT INTO `role_relation` VALUES ('29674', '46058', '1');
INSERT INTO `role_relation` VALUES ('29674', '17386', '1');
INSERT INTO `role_relation` VALUES ('29674', '41962', '1');
INSERT INTO `role_relation` VALUES ('29674', '13290', '1');
INSERT INTO `role_relation` VALUES ('29674', '9194', '1');
INSERT INTO `role_relation` VALUES ('46058', '29674', '1');
INSERT INTO `role_relation` VALUES ('46058', '13290', '1');
INSERT INTO `role_relation` VALUES ('136169', '46057', '1');
INSERT INTO `role_relation` VALUES ('136169', '9193', '1');
INSERT INTO `role_relation` VALUES ('136169', '70633', '1');
INSERT INTO `role_relation` VALUES ('136169', '29673', '1');
INSERT INTO `role_relation` VALUES ('136169', '66537', '1');
INSERT INTO `role_relation` VALUES ('136169', '119785', '1');
INSERT INTO `role_relation` VALUES ('127977', '107497', '1');
INSERT INTO `role_relation` VALUES ('197609', '201705', '1');
INSERT INTO `role_relation` VALUES ('58345', '5097', '1');
INSERT INTO `role_relation` VALUES ('58345', '37865', '1');

-- ----------------------------
-- Table structure for server_merge
-- ----------------------------
DROP TABLE IF EXISTS `server_merge`;
CREATE TABLE `server_merge` (
  `server_merge_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `from_server_keys` varchar(255) NOT NULL DEFAULT '',
  `to_server_key` varchar(255) NOT NULL DEFAULT '',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态,0正常,1已回滚',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`server_merge_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COMMENT='服务器合并表';

-- ----------------------------
-- Records of server_merge
-- ----------------------------
INSERT INTO `server_merge` VALUES ('1', 'YToxOntpOjA7czoxOiI3Ijt9', '8', '1', '1467104576');

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
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态,0可用,1不可用',
  `db_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '数据库状态,0未初始化,1已初始化',
  `mark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`serverlist_id`),
  KEY `sc` (`serverkey`,`create_time`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='区服连接信息表';

-- ----------------------------
-- Records of serverlist
-- ----------------------------
INSERT INTO `serverlist` VALUES ('1', '1101961001', '测试服务器', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo', '0', '1', '', '1463228000');
INSERT INTO `serverlist` VALUES ('3', '1101981002', '1101981002', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_1101981002', '0', '1', '', '1463228819');
INSERT INTO `serverlist` VALUES ('4', '1101961002', '产品测试服', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_1101961002', '0', '1', '', '1463469903');
INSERT INTO `serverlist` VALUES ('5', '1101991001', '永恒之井', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_1101991001', '0', '1', '', '1466492314');
INSERT INTO `serverlist` VALUES ('6', '1101991002', '圣雪峰', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_1101991002', '0', '1', '', '1466492461');
INSERT INTO `serverlist` VALUES ('7', '110199100101', 'test0', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_test0', '0', '1', ';合服到110199100101;合服恢复,启用原先;合服恢复,启用原先', '0');
INSERT INTO `serverlist` VALUES ('8', '110199100102', 'test1', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_test1', '0', '1', '', '0');
INSERT INTO `serverlist` VALUES ('9', '110199100101', 'test0', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_test1', '1', '1', ';合服恢复,关闭当前', '1467104576');
INSERT INTO `serverlist` VALUES ('10', '1101981004', '[测试]点卡服', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_1101981004', '0', '1', '', '1467525562');
INSERT INTO `serverlist` VALUES ('11', '110198100401', 'test3', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_110198100401', '0', '0', '', '1467618025');
INSERT INTO `serverlist` VALUES ('12', '1101971097', '踏上', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_1101971097', '0', '1', '', '1467689817');
INSERT INTO `serverlist` VALUES ('13', '1201071002', '月光圣地', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_1201071002', '0', '1', '', '1467689845');
INSERT INTO `serverlist` VALUES ('14', '1101971094', '泰坦', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_1101971094', '0', '1', '', '1467729693');
INSERT INTO `serverlist` VALUES ('15', '1101971098', '泰坦创世', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_1101971098', '0', '1', '', '1468207015');
INSERT INTO `serverlist` VALUES ('16', '1101981005', '北风台原', '59.151.112.48', '3306', 'weibo', 'YHJNFo8I*', 'mt3_weibo_1101981005', '0', '1', '', '1468287122');

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
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 COMMENT='微博表,索引表';

-- ----------------------------
-- Records of status
-- ----------------------------
INSERT INTO `status` VALUES ('28', '1', '0', '1459853477');
INSERT INTO `status` VALUES ('29', '1', '0', '1459856222');
INSERT INTO `status` VALUES ('31', '1', '0', '1459856336');
INSERT INTO `status` VALUES ('30', '1', '1', '1459856248');
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
INSERT INTO `status` VALUES ('71', '5095', '1', '1463417421');
INSERT INTO `status` VALUES ('72', '5095', '1', '1463417468');
INSERT INTO `status` VALUES ('73', '5095', '1', '1463418604');
INSERT INTO `status` VALUES ('74', '5095', '1', '1463463531');
INSERT INTO `status` VALUES ('75', '5095', '1', '1463464307');
INSERT INTO `status` VALUES ('76', '5095', '1', '1463464356');
INSERT INTO `status` VALUES ('65', '21480', '0', '1463378523');
INSERT INTO `status` VALUES ('66', '21480', '0', '1463381055');
INSERT INTO `status` VALUES ('58', '29672', '0', '1463364325');
INSERT INTO `status` VALUES ('59', '29672', '0', '1463364351');
INSERT INTO `status` VALUES ('60', '29672', '0', '1463364489');
INSERT INTO `status` VALUES ('61', '29672', '0', '1463365442');
INSERT INTO `status` VALUES ('54', '33767', '0', '1463252016');
INSERT INTO `status` VALUES ('50', '37863', '0', '1463251523');
INSERT INTO `status` VALUES ('62', '37864', '0', '1463370762');
INSERT INTO `status` VALUES ('51', '41959', '0', '1463251825');
INSERT INTO `status` VALUES ('52', '41959', '0', '1463251863');
INSERT INTO `status` VALUES ('53', '41959', '0', '1463251910');
INSERT INTO `status` VALUES ('63', '46056', '0', '1463370890');
INSERT INTO `status` VALUES ('64', '46056', '0', '1463370907');
INSERT INTO `status` VALUES ('42', '50151', '0', '1463157736');
INSERT INTO `status` VALUES ('43', '50151', '0', '1463160914');
INSERT INTO `status` VALUES ('44', '50151', '0', '1463161257');
INSERT INTO `status` VALUES ('45', '50151', '0', '1463203567');
INSERT INTO `status` VALUES ('46', '50151', '0', '1463209907');
INSERT INTO `status` VALUES ('47', '50151', '0', '1463210193');
INSERT INTO `status` VALUES ('49', '50151', '0', '1463228596');
INSERT INTO `status` VALUES ('41', '50151', '1', '1463157723');
INSERT INTO `status` VALUES ('77', '54247', '1', '1463577413');
INSERT INTO `status` VALUES ('79', '66535', '0', '1463734311');
INSERT INTO `status` VALUES ('80', '66535', '0', '1463735353');
INSERT INTO `status` VALUES ('68', '66536', '0', '1463417038');
INSERT INTO `status` VALUES ('69', '66536', '0', '1463417094');
INSERT INTO `status` VALUES ('70', '66536', '0', '1463417120');
INSERT INTO `status` VALUES ('67', '66536', '1', '1463415414');
INSERT INTO `status` VALUES ('57', '70631', '0', '1463254271');
INSERT INTO `status` VALUES ('78', '70631', '0', '1463627802');
INSERT INTO `status` VALUES ('56', '74727', '0', '1463254159');
INSERT INTO `status` VALUES ('55', '87015', '0', '1463254156');
INSERT INTO `status` VALUES ('48', '164839', '0', '1463223576');
INSERT INTO `status` VALUES ('33', '213991', '0', '1463045125');
INSERT INTO `status` VALUES ('35', '213991', '0', '1463059839');
INSERT INTO `status` VALUES ('36', '213991', '0', '1463059929');
INSERT INTO `status` VALUES ('37', '213991', '0', '1463060069');
INSERT INTO `status` VALUES ('38', '213991', '0', '1463060884');
INSERT INTO `status` VALUES ('39', '213991', '0', '1463060925');
INSERT INTO `status` VALUES ('40', '213991', '0', '1463060956');
INSERT INTO `status` VALUES ('32', '213991', '1', '1463045104');
INSERT INTO `status` VALUES ('34', '213991', '1', '1463054360');

-- ----------------------------
-- Table structure for status_attribute
-- ----------------------------
DROP TABLE IF EXISTS `status_attribute`;
CREATE TABLE `status_attribute` (
  `status_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `content` text NOT NULL COMMENT '内容',
  `img_url` varchar(255) NOT NULL DEFAULT '0' COMMENT '图片地址',
  `comment_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '评论数',
  `favorite_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '点赞数',
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微博表,内容表';

-- ----------------------------
-- Records of status_attribute
-- ----------------------------
INSERT INTO `status_attribute` VALUES ('0', 'afafdfadf', '', '3', '1');
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
INSERT INTO `status_attribute` VALUES ('32', '&lt;T t=&quot;tyt&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('33', '&lt;T t=&quot;tdhh&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('34', '&lt;T t=&quot;dch&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/12/57347018314e2.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('35', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/12/5734857f4f7fe.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('36', '&lt;T t=&quot;u&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/12/573485d97ba84.png', '0', '1');
INSERT INTO `status_attribute` VALUES ('37', '&lt;T t=&quot;gf&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/12/573486654a13a.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('38', '&lt;T t=&quot;yg&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/12/5734899420945.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('39', '&lt;T t=&quot;dfds&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/12/573489bd7ddec.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('40', '&lt;T t=&quot;hg&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/12/573489dc3faab.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('41', '&lt;T t=&quot;fruu&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/14/573603dbcf67e.jpg', '0', '1');
INSERT INTO `status_attribute` VALUES ('42', '&lt;T t=&quot;fgh&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '3', '1');
INSERT INTO `status_attribute` VALUES ('43', '&lt;T t=&quot;jiko&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/14/57361052871bd.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('44', '&lt;T t=&quot;lll&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/14/573611a98bad3.png', '0', '1');
INSERT INTO `status_attribute` VALUES ('45', '&lt;T t=&quot;lij&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/14/5736b6ef1f120.png', '1', '0');
INSERT INTO `status_attribute` VALUES ('46', '&lt;T t=&quot;咯哦哦&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;915&quot;&gt;&lt;/E&gt;', '', '1', '0');
INSERT INTO `status_attribute` VALUES ('47', '&lt;T t=&quot;kjn&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;910&quot;&gt;&lt;/E&gt;&lt;E e=&quot;912&quot;&gt;&lt;/E&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/14/5736d0d1df039.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('48', '&lt;T t=&quot;QQ&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/14/5737051803e7b.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('49', '&lt;T t=&quot;zzs&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/14/573718b43e636.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('50', '&lt;T t=&quot;可口可乐了&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/57377243c88e8.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('51', '&lt;T t=&quot;sss&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;909&quot;&gt;&lt;/E&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/5737737116db2.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('52', '&lt;T t=&quot;jjc&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('53', '&lt;T t=&quot;rr&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('54', '&lt;T t=&quot;cao&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/573774300a11a.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('55', '&lt;T t=&quot;suhs&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/57377c8c2fc11.png', '10', '0');
INSERT INTO `status_attribute` VALUES ('56', '&lt;T t=&quot;我们&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/57377c8fecbfc.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('57', '&lt;T t=&quot;充值bug&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/15/57377cff2dccb.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('58', '&lt;T t=&quot;hdhdu&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('59', '&lt;T t=&quot;fghj&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('60', '&lt;T t=&quot;ghh&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('61', '&lt;T t=&quot;ggh&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('62', '&lt;T t=&quot;hhh&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/16/5739440ad5eae.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('63', '&lt;T t=&quot;alaoo&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('64', '&lt;T t=&quot;mka&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/16/5739449b39bde.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('65', '&lt;T t=&quot;wer&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('66', '&lt;T t=&quot;hdhfh&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('67', '&lt;T t=&quot;hdhdhdhhdhsh&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '104', '1');
INSERT INTO `status_attribute` VALUES ('68', '&lt;T t=&quot;休息下&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('69', '&lt;T t=&quot;谢谢&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/17/5739f906ed3cf.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('70', '&lt;T t=&quot;吃很喜欢&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/17/5739f920ce43f.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('71', '&lt;T t=&quot;xxx&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '1', '1');
INSERT INTO `status_attribute` VALUES ('72', '&lt;T t=&quot;cc&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;901&quot;&gt;&lt;/E&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/17/5739fa7c05a4c.png', '6', '0');
INSERT INTO `status_attribute` VALUES ('73', '&lt;T t=&quot;发的几点&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/17/5739feec9a3c9.png', '0', '0');
INSERT INTO `status_attribute` VALUES ('74', '&lt;T t=&quot;谢谢&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '2', '0');
INSERT INTO `status_attribute` VALUES ('75', '&lt;T t=&quot;vvv&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('76', '&lt;T t=&quot;沉沉浮浮&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('77', '&lt;T t=&quot;收拾收拾&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('78', '&lt;E e=&quot;102&quot;&gt;&lt;/E&gt;&lt;E e=&quot;102&quot;&gt;&lt;/E&gt;&lt;E e=&quot;102&quot;&gt;&lt;/E&gt;&lt;E e=&quot;102&quot;&gt;&lt;/E&gt;&lt;E e=&quot;102&quot;&gt;&lt;/E&gt;&lt;E e=&quot;102&quot;&gt;&lt;/E&gt;&lt;E e=&quot;102&quot;&gt;&lt;/E', '', '0', '0');
INSERT INTO `status_attribute` VALUES ('79', '&lt;T t=&quot;fg&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', 'http://59.151.112.45:8821/resource/uploads/images/2016/05/20/573ed027ea62e.png', '1', '1');
INSERT INTO `status_attribute` VALUES ('80', '&lt;T t=&quot;azzq&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '', '0', '0');

-- ----------------------------
-- Table structure for status_comment
-- ----------------------------
DROP TABLE IF EXISTS `status_comment`;
CREATE TABLE `status_comment` (
  `status_comment_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `status_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `roleid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '评论者',
  `content` text NOT NULL COMMENT '内容',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态0正常,1删除',
  `reply_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '回复评论',
  `reply_roleid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`status_comment_id`),
  KEY `ssc` (`status_id`,`status`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8 COMMENT='微博评论表';

-- ----------------------------
-- Records of status_comment
-- ----------------------------
INSERT INTO `status_comment` VALUES ('1', '31', '1', '先发发大发', '0', '0', '0', '1459860325');
INSERT INTO `status_comment` VALUES ('2', '31', '1', '先发发大发', '0', '0', '0', '1459860392');
INSERT INTO `status_comment` VALUES ('3', '31', '1', '先发发大发', '1', '0', '0', '1459860858');
INSERT INTO `status_comment` VALUES ('4', '42', '50151', '&lt;E e=&quot;905&quot;&gt;&lt;/E&gt;', '1', '0', '0', '1463157742');
INSERT INTO `status_comment` VALUES ('5', '42', '50151', '&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463203854');
INSERT INTO `status_comment` VALUES ('6', '42', '50151', '&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463203859');
INSERT INTO `status_comment` VALUES ('7', '42', '50151', '&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463203860');
INSERT INTO `status_comment` VALUES ('8', '45', '50151', '&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;905&quot;&gt;&lt;/E&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463210468');
INSERT INTO `status_comment` VALUES ('9', '46', '50151', '&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463210508');
INSERT INTO `status_comment` VALUES ('10', '55', '87015', '&lt;T t=&quot;waw&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463254164');
INSERT INTO `status_comment` VALUES ('11', '55', '87015', '&lt;T t=&quot;wawsa&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;', '1', '0', '0', '1463254172');
INSERT INTO `status_comment` VALUES ('12', '55', '87015', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '1', '0', '0', '1463257730');
INSERT INTO `status_comment` VALUES ('13', '55', '87015', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463257732');
INSERT INTO `status_comment` VALUES ('14', '55', '87015', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463257732');
INSERT INTO `status_comment` VALUES ('15', '55', '87015', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463257732');
INSERT INTO `status_comment` VALUES ('16', '55', '87015', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463257732');
INSERT INTO `status_comment` VALUES ('17', '55', '87015', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463257732');
INSERT INTO `status_comment` VALUES ('18', '55', '87015', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463257733');
INSERT INTO `status_comment` VALUES ('19', '55', '87015', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463257733');
INSERT INTO `status_comment` VALUES ('20', '55', '87015', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463257733');
INSERT INTO `status_comment` VALUES ('21', '55', '87015', '&lt;T t=&quot;as&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463257733');
INSERT INTO `status_comment` VALUES ('22', '0', '46056', '&lt;T t=&quot;ss&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;908&quot;&gt;&lt;/E&gt;&lt;E e=&quot;908&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463370736');
INSERT INTO `status_comment` VALUES ('23', '0', '46056', '&lt;T t=&quot;ss&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;908&quot;&gt;&lt;/E&gt;&lt;E e=&quot;908&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463370737');
INSERT INTO `status_comment` VALUES ('24', '0', '46056', '&lt;T t=&quot;ss&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;908&quot;&gt;&lt;/E&gt;&lt;E e=&quot;908&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463370737');
INSERT INTO `status_comment` VALUES ('25', '67', '66536', '&lt;T t=&quot;wekwwwwkk&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415445');
INSERT INTO `status_comment` VALUES ('26', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415471');
INSERT INTO `status_comment` VALUES ('27', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415472');
INSERT INTO `status_comment` VALUES ('28', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415472');
INSERT INTO `status_comment` VALUES ('29', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415473');
INSERT INTO `status_comment` VALUES ('30', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415473');
INSERT INTO `status_comment` VALUES ('31', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415473');
INSERT INTO `status_comment` VALUES ('32', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415474');
INSERT INTO `status_comment` VALUES ('33', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415474');
INSERT INTO `status_comment` VALUES ('34', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415474');
INSERT INTO `status_comment` VALUES ('35', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415475');
INSERT INTO `status_comment` VALUES ('36', '67', '66536', '&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415475');
INSERT INTO `status_comment` VALUES ('37', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415483');
INSERT INTO `status_comment` VALUES ('38', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415486');
INSERT INTO `status_comment` VALUES ('39', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415490');
INSERT INTO `status_comment` VALUES ('40', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415508');
INSERT INTO `status_comment` VALUES ('41', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415509');
INSERT INTO `status_comment` VALUES ('42', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415510');
INSERT INTO `status_comment` VALUES ('43', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415510');
INSERT INTO `status_comment` VALUES ('44', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415513');
INSERT INTO `status_comment` VALUES ('45', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415513');
INSERT INTO `status_comment` VALUES ('46', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415514');
INSERT INTO `status_comment` VALUES ('47', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415515');
INSERT INTO `status_comment` VALUES ('48', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415516');
INSERT INTO `status_comment` VALUES ('49', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415517');
INSERT INTO `status_comment` VALUES ('50', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415520');
INSERT INTO `status_comment` VALUES ('51', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415522');
INSERT INTO `status_comment` VALUES ('52', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415522');
INSERT INTO `status_comment` VALUES ('53', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415530');
INSERT INTO `status_comment` VALUES ('54', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415531');
INSERT INTO `status_comment` VALUES ('55', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415533');
INSERT INTO `status_comment` VALUES ('56', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415534');
INSERT INTO `status_comment` VALUES ('57', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415535');
INSERT INTO `status_comment` VALUES ('58', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415542');
INSERT INTO `status_comment` VALUES ('59', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415545');
INSERT INTO `status_comment` VALUES ('60', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415546');
INSERT INTO `status_comment` VALUES ('61', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415547');
INSERT INTO `status_comment` VALUES ('62', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415550');
INSERT INTO `status_comment` VALUES ('63', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415552');
INSERT INTO `status_comment` VALUES ('64', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415552');
INSERT INTO `status_comment` VALUES ('65', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;915&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415555');
INSERT INTO `status_comment` VALUES ('66', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;915&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415555');
INSERT INTO `status_comment` VALUES ('67', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;915&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415556');
INSERT INTO `status_comment` VALUES ('68', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;915&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415561');
INSERT INTO `status_comment` VALUES ('69', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;915&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;', '0', '0', '0', '1463415566');
INSERT INTO `status_comment` VALUES ('70', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;915&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;', '0', '0', '0', '1463415572');
INSERT INTO `status_comment` VALUES ('71', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;E e=&quot;915&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;', '0', '0', '0', '1463415576');
INSERT INTO `status_comment` VALUES ('72', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;T t=&quot;#&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415583');
INSERT INTO `status_comment` VALUES ('73', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;T t=&quot;#&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415583');
INSERT INTO `status_comment` VALUES ('74', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;T t=&quot;#&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415587');
INSERT INTO `status_comment` VALUES ('75', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;T t=&quot;#&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&l', '0', '0', '0', '1463415591');
INSERT INTO `status_comment` VALUES ('76', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;T t=&quot;#&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&l', '0', '0', '0', '1463415596');
INSERT INTO `status_comment` VALUES ('77', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;&lt;T t=&quot;#&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&l', '0', '0', '0', '1463415600');
INSERT INTO `status_comment` VALUES ('78', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;91&quot;&gt;&lt;/E&gt;&lt;T t=&quot;用&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415611');
INSERT INTO `status_comment` VALUES ('79', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;91&quot;&gt;&lt;/E&gt;&lt;T t=&quot;用&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415613');
INSERT INTO `status_comment` VALUES ('80', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;91&quot;&gt;&lt;/E&gt;&lt;T t=&quot;用&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415616');
INSERT INTO `status_comment` VALUES ('81', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;91&quot;&gt;&lt;/E&gt;&lt;T t=&quot;用&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt', '0', '0', '0', '1463415621');
INSERT INTO `status_comment` VALUES ('82', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;T t=&quot;111我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;91&quot;&gt;&lt;/E&gt;&lt;T t=&quot;用&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt', '0', '0', '0', '1463415626');
INSERT INTO `status_comment` VALUES ('83', '67', '66536', '&lt;T t=&quot;我换个&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;91&quot;&gt;&lt;/E&gt;&lt;T t=&quot;我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;91&quot;&gt;&lt;/E&gt;&lt;T t=&quot;用&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e', '0', '0', '0', '1463415637');
INSERT INTO `status_comment` VALUES ('84', '67', '66536', '&lt;T t=&quot;我换我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;91&quot;&gt;&lt;/E&gt;&lt;T t=&quot;用&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415644');
INSERT INTO `status_comment` VALUES ('85', '67', '66536', '&lt;T t=&quot;我换我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;91&quot;&gt;&lt;/E&gt;&lt;T t=&quot;用&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415647');
INSERT INTO `status_comment` VALUES ('86', '67', '66536', '&lt;T t=&quot;我换我&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;91&quot;&gt;&lt;/E&gt;&lt;T t=&quot;用&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415649');
INSERT INTO `status_comment` VALUES ('87', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415780');
INSERT INTO `status_comment` VALUES ('88', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415781');
INSERT INTO `status_comment` VALUES ('89', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415782');
INSERT INTO `status_comment` VALUES ('90', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415783');
INSERT INTO `status_comment` VALUES ('91', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415783');
INSERT INTO `status_comment` VALUES ('92', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415783');
INSERT INTO `status_comment` VALUES ('93', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415784');
INSERT INTO `status_comment` VALUES ('94', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415784');
INSERT INTO `status_comment` VALUES ('95', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415784');
INSERT INTO `status_comment` VALUES ('96', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415784');
INSERT INTO `status_comment` VALUES ('97', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415784');
INSERT INTO `status_comment` VALUES ('98', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415809');
INSERT INTO `status_comment` VALUES ('99', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415810');
INSERT INTO `status_comment` VALUES ('100', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415810');
INSERT INTO `status_comment` VALUES ('101', '67', '66536', '&lt;T t=&quot; 小弟弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415831');
INSERT INTO `status_comment` VALUES ('102', '67', '66536', '&lt;T t=&quot; 小弟弟姑姑&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415852');
INSERT INTO `status_comment` VALUES ('103', '67', '66536', '&lt;T t=&quot; 小弟弟姑姑&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415854');
INSERT INTO `status_comment` VALUES ('104', '67', '66536', '&lt;T t=&quot; 小弟弟姑姑&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;905&quot;&gt;&lt;/E&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;E e=&quot;903&quot;&gt;&lt;/E&gt;&lt;E e', '0', '0', '0', '1463415864');
INSERT INTO `status_comment` VALUES ('105', '67', '66536', '&lt;T t=&quot; 小弟弟姑姑&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;905&quot;&gt;&lt;/E&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;E e=&quot;903&quot;&gt;&lt;/E&gt;&lt;E e', '0', '0', '0', '1463415875');
INSERT INTO `status_comment` VALUES ('106', '67', '66536', '&lt;T t=&quot; 小弟弟姑姑&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;T t=&quot;#90&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463415897');
INSERT INTO `status_comment` VALUES ('107', '67', '66536', '&lt;T t=&quot; 小弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415921');
INSERT INTO `status_comment` VALUES ('108', '67', '66536', '&lt;T t=&quot; 小弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415930');
INSERT INTO `status_comment` VALUES ('109', '67', '66536', '&lt;T t=&quot; 小弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;&lt;E e=&quot;909&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415950');
INSERT INTO `status_comment` VALUES ('110', '67', '66536', '&lt;T t=&quot; 小弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;&lt;E e=&quot;909&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415954');
INSERT INTO `status_comment` VALUES ('111', '67', '66536', '&lt;T t=&quot; 小弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;&lt;E e=&quot;909&quot;&gt;&lt;/E&gt;&lt;E e=&quot;903&quot;&gt;&lt;/E&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463415963');
INSERT INTO `status_comment` VALUES ('112', '67', '66536', '&lt;T t=&quot; 小弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;904&quot;&gt;&lt;/E&gt;&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;&lt;E e=&quot;909&quot;&gt;&lt;/E&gt;&lt;E e=&quot;903&quot;&gt;&lt;/E&gt;&lt;E e=&quot;913&quot;&gt;&lt;/E&gt;&lt;E e=&q', '0', '0', '0', '1463416003');
INSERT INTO `status_comment` VALUES ('113', '67', '66536', '&lt;T t=&quot; 小弟&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;902&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416040');
INSERT INTO `status_comment` VALUES ('114', '67', '66536', '&lt;T t=&quot; 小功夫干活交话费废话今年&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416081');
INSERT INTO `status_comment` VALUES ('115', '67', '66536', '&lt;T t=&quot; 小功夫干活交话费废话今年&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416099');
INSERT INTO `status_comment` VALUES ('116', '67', '66536', '&lt;T t=&quot; 小功夫干活交话费废话今年&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416100');
INSERT INTO `status_comment` VALUES ('117', '67', '66536', '&lt;T t=&quot; 小功夫干活交话费废话今年&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416100');
INSERT INTO `status_comment` VALUES ('118', '67', '66536', '&lt;T t=&quot; 小功夫干活交话费废话今年&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416100');
INSERT INTO `status_comment` VALUES ('119', '67', '66536', '&lt;T t=&quot; 小功夫干活交话费废话今年&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416100');
INSERT INTO `status_comment` VALUES ('120', '67', '66536', '&lt;T t=&quot; 小功夫干活交话费废话今年&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416102');
INSERT INTO `status_comment` VALUES ('121', '67', '66536', '&lt;T t=&quot; 这种事&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '117', '66536', '1463416114');
INSERT INTO `status_comment` VALUES ('122', '67', '66536', '&lt;T t=&quot; 这种事&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416115');
INSERT INTO `status_comment` VALUES ('123', '67', '66536', '&lt;T t=&quot; 这种事&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416115');
INSERT INTO `status_comment` VALUES ('124', '67', '66536', '&lt;T t=&quot; 这种事&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416115');
INSERT INTO `status_comment` VALUES ('125', '67', '66536', '&lt;T t=&quot; 这种事&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416116');
INSERT INTO `status_comment` VALUES ('126', '67', '66536', '&lt;T t=&quot; 这种事&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416116');
INSERT INTO `status_comment` VALUES ('127', '67', '66536', '&lt;T t=&quot; 这种事&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416116');
INSERT INTO `status_comment` VALUES ('128', '67', '66536', '&lt;T t=&quot; 这种事&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463416117');
INSERT INTO `status_comment` VALUES ('129', '67', '66536', '&lt;T t=&quot; 这种事&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;&lt;E e=&quot;906&quot;&gt;&lt;/E&gt;&lt;E e=&quot;914&quot;&gt;&lt;/E&gt;', '1', '0', '0', '1463416117');
INSERT INTO `status_comment` VALUES ('130', '71', '5095', '&lt;T t=&quot;xxx&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '1', '0', '0', '1463417442');
INSERT INTO `status_comment` VALUES ('131', '71', '5095', '&lt;T t=&quot;xxx&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '1', '0', '0', '1463417443');
INSERT INTO `status_comment` VALUES ('132', '71', '5095', '&lt;T t=&quot;xxx&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '1', '0', '0', '1463417444');
INSERT INTO `status_comment` VALUES ('133', '71', '5095', '&lt;T t=&quot;xxx&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '1', '0', '0', '1463417444');
INSERT INTO `status_comment` VALUES ('134', '71', '5095', '&lt;T t=&quot;xxx&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '1', '0', '0', '1463417444');
INSERT INTO `status_comment` VALUES ('135', '71', '5095', '&lt;E e=&quot;907&quot;&gt;&lt;/E&gt;', '0', '0', '0', '1463417773');
INSERT INTO `status_comment` VALUES ('136', '72', '5095', '&lt;T t=&quot;ccc&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463417836');
INSERT INTO `status_comment` VALUES ('137', '72', '5095', '&lt;T t=&quot;ccc&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463417837');
INSERT INTO `status_comment` VALUES ('138', '72', '5095', '&lt;T t=&quot;ccc&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463417837');
INSERT INTO `status_comment` VALUES ('139', '72', '5095', '&lt;T t=&quot;ccc&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463417837');
INSERT INTO `status_comment` VALUES ('140', '72', '5095', '&lt;T t=&quot;ccc&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463417837');
INSERT INTO `status_comment` VALUES ('141', '72', '5095', '&lt;T t=&quot;ccc&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '1', '0', '0', '1463417837');
INSERT INTO `status_comment` VALUES ('142', '74', '5095', '&lt;T t=&quot;给发给&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463463561');
INSERT INTO `status_comment` VALUES ('143', '74', '5095', '&lt;T t=&quot;给发给&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463463561');
INSERT INTO `status_comment` VALUES ('144', '74', '5095', '&lt;T t=&quot;给发给&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '1', '0', '0', '1463463561');
INSERT INTO `status_comment` VALUES ('145', '72', '5095', '&lt;T t=&quot;给发给到底&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463463589');
INSERT INTO `status_comment` VALUES ('146', '77', '54247', '&lt;T t=&quot;哈哈哈哈&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '1', '0', '0', '1463577554');
INSERT INTO `status_comment` VALUES ('147', '57', '54247', '&lt;T t=&quot;视频&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '1', '0', '0', '1463577568');
INSERT INTO `status_comment` VALUES ('148', '79', '66535', '&lt;T t=&quot;hhj&quot; c=&quot;FFFFFFFF&quot;&gt;&lt;/T&gt;', '0', '0', '0', '1463735233');

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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='微博点赞表';

-- ----------------------------
-- Records of status_favorite
-- ----------------------------
INSERT INTO `status_favorite` VALUES ('1', '31', '1', '1', '1459860014');
INSERT INTO `status_favorite` VALUES ('2', '33', '213991', '1', '1463045136');
INSERT INTO `status_favorite` VALUES ('3', '32', '213991', '1', '1463045358');
INSERT INTO `status_favorite` VALUES ('4', '36', '213991', '0', '1463061028');
INSERT INTO `status_favorite` VALUES ('5', '1', '1', '1', '1463123074');
INSERT INTO `status_favorite` VALUES ('6', '42', '50151', '0', '1463157737');
INSERT INTO `status_favorite` VALUES ('7', '41', '50151', '0', '1463160922');
INSERT INTO `status_favorite` VALUES ('8', '45', '50151', '1', '1463203622');
INSERT INTO `status_favorite` VALUES ('9', '44', '50151', '0', '1463203889');
INSERT INTO `status_favorite` VALUES ('10', '46', '50151', '1', '1463210483');
INSERT INTO `status_favorite` VALUES ('11', '47', '164839', '1', '1463218868');
INSERT INTO `status_favorite` VALUES ('12', '56', '74727', '1', '1463254181');
INSERT INTO `status_favorite` VALUES ('13', '0', '46056', '0', '1463370730');
INSERT INTO `status_favorite` VALUES ('14', '67', '66536', '0', '1463415416');
INSERT INTO `status_favorite` VALUES ('15', '71', '5095', '0', '1463417438');
INSERT INTO `status_favorite` VALUES ('16', '77', '54247', '1', '1463577548');
INSERT INTO `status_favorite` VALUES ('17', '57', '54247', '1', '1463577580');
INSERT INTO `status_favorite` VALUES ('18', '79', '66535', '0', '1463735224');
