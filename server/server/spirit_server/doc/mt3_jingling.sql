/*
Navicat MySQL Data Transfer

Source Server         : 192.168.41.5
Source Server Version : 50537
Source Host           : 192.168.41.5:3306
Source Database       : mt3_jingling

Target Server Type    : MYSQL
Target Server Version : 50537
File Encoding         : 65001

Date: 2016-04-11 19:31:49
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='管理员表';

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'admin@joyogame.com', '111111', '9', '1457916733', '', '0');
INSERT INTO `admin` VALUES ('2', 'test@add.com', '123456', '0', '0', '', '0');

-- ----------------------------
-- Table structure for faq
-- ----------------------------
DROP TABLE IF EXISTS `faq`;
CREATE TABLE `faq` (
  `faq_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` tinyint(2) unsigned NOT NULL DEFAULT '0',
  `title` varchar(60) NOT NULL DEFAULT '',
  `style` varchar(255) NOT NULL DEFAULT '',
  `thumb` varchar(255) NOT NULL DEFAULT '',
  `keywords` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `sort` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '1',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`faq_id`),
  KEY `status` (`status`,`sort`) USING BTREE,
  KEY `csc` (`category_id`,`sort`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1017 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of faq
-- ----------------------------
INSERT INTO `faq` VALUES ('1014', '1', '【新闻专区】《梦幻西游MG》种族介绍 六大种族任你挑选', '', '', '', '【新闻专区】《梦幻西游MG》种族介绍 六大种族任你挑选', '0', '0', '1458125127');
INSERT INTO `faq` VALUES ('1015', '0', '宠物宝宝', '', '', '宝宝 宠物 宝贝', '', '0', '0', '1458126712');
INSERT INTO `faq` VALUES ('1016', '0', '从前有座山', '', '', '和尚', '', '0', '0', '1460370395');

-- ----------------------------
-- Table structure for faq_category
-- ----------------------------
DROP TABLE IF EXISTS `faq_category`;
CREATE TABLE `faq_category` (
  `category_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL DEFAULT '',
  `sort` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `parent_id` int(11) unsigned NOT NULL DEFAULT '0',
  `level_low` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '务任适合等级下线',
  `level_hight` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '任务适合等级上线',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`category_id`),
  KEY `lls` (`level_low`,`level_hight`,`sort`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of faq_category
-- ----------------------------
INSERT INTO `faq_category` VALUES ('1', 'MT人文', '0', '0', '0', '0', '0');
INSERT INTO `faq_category` VALUES ('2', '古神爪牙', '0', '0', '0', '0', '0');
INSERT INTO `faq_category` VALUES ('3', '助战', '0', '0', '0', '0', '0');
INSERT INTO `faq_category` VALUES ('4', '技能', '0', '0', '0', '0', '0');
INSERT INTO `faq_category` VALUES ('5', '进入公会', '0', '0', '0', '0', '0');
INSERT INTO `faq_category` VALUES ('6', '宝图', '0', '0', '0', '0', '0');
INSERT INTO `faq_category` VALUES ('7', '巨龙护卫', '0', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for faq_column
-- ----------------------------
DROP TABLE IF EXISTS `faq_column`;
CREATE TABLE `faq_column` (
  `column_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL DEFAULT '',
  `parent_id` int(11) unsigned NOT NULL DEFAULT '0',
  `relation_module` varchar(30) NOT NULL DEFAULT '' COMMENT '对应客户端模块',
  `sort` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`column_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of faq_column
-- ----------------------------
INSERT INTO `faq_column` VALUES ('1', '热点问题', '0', 'HotQuestionColumn1', '0', '0');
INSERT INTO `faq_column` VALUES ('2', '精灵推荐', '0', 'Recommend', '0', '0');
INSERT INTO `faq_column` VALUES ('3', '游戏指引', '0', 'HotQuestionColumn2', '0', '0');
INSERT INTO `faq_column` VALUES ('4', '全服更新总汇', '0', '', '0', '0');
INSERT INTO `faq_column` VALUES ('5', '活动推荐', '0', '', '0', '0');
INSERT INTO `faq_column` VALUES ('6', '节日玩法', '0', '', '0', '0');
INSERT INTO `faq_column` VALUES ('7', '宝宝加点等', '0', '', '0', '0');
INSERT INTO `faq_column` VALUES ('8', '1111', '0', '', '0', '0');

-- ----------------------------
-- Table structure for faq_content
-- ----------------------------
DROP TABLE IF EXISTS `faq_content`;
CREATE TABLE `faq_content` (
  `faq_id` int(11) unsigned NOT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`faq_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of faq_content
-- ----------------------------
INSERT INTO `faq_content` VALUES ('1013', '@$%@$%#&amp;amp;&amp;amp;^%&amp;amp;%^RTRTYGDDGSDFG$#$#%^&amp;amp;***(**(JGHJKLKL&quot;L&quot;L&amp;gt;国民经典《我叫MT》获苹果专属页面推荐卓越游戏旗下的国民级卡牌手游《我叫MT》已经在1亿玩家的陪伴下度过了三年。曾经AppStore六榜第一等');
INSERT INTO `faq_content` VALUES ('1014', '&lt;p&gt;\r\n	&lt;strong&gt;巨 魔A&lt;/strong&gt; &lt;A&gt;\r\n&lt;/p&gt;\r\n&lt;br /&gt;\r\n这个蠢萌的家伙是一个实实在在的巨魔。小贱牙外漏的他作为猎人，箭术可是出类拔萃的，单点攻击还是群体射杀他可是游刃有余的。&lt;br /&gt;\r\n智商略显捉急的家伙，也是最爱慕美屡的。他可是传说中灰太狼一样的好男人呢！！蠢呆呆的家伙经常会进入自己的迷糊圈无法抽身。不过，他当他辅修德鲁伊时候，治疗同伴的作用还是略微靠的住的。&lt;br /&gt;\r\n&lt;br /&gt;\r\n可选职业：[盗贼] &amp;nbsp;[猎人] &amp;nbsp;[德鲁伊]&lt;br /&gt;\r\n&lt;br /&gt;\r\n&lt;div style=&quot;text-align:center;&quot;&gt;\r\n	&lt;img src=&quot;http://resource.locojoy.com/cms/2016/0122/20160122160509954.png&quot; alt=&quot;\\&quot; style=&quot;line-height:1.5;&quot; /&gt; \r\n&lt;/div&gt;\r\n&lt;strong&gt;人 类&lt;/strong&gt;&lt;br /&gt;\r\n&lt;br /&gt;\r\n身为一个名门后代的大小姐，怎能不傲娇呢？大小姐是她的天性，不讲理是她的本性。你难道有意见？不过，人家不只会蛮不讲理，为所欲为的发泄情绪。&lt;br /&gt;\r\n作为一名**的圣骑士，彪悍的攻击力，哼，小菜一碟。强悍的防御力，信手拈来啊。治疗补血不发愁，圣光照耀就搞定。手持作战大锤的人家可不单单是娇滴滴的大小姐呢！想挑衅的家伙们，尽管放马过来吧。&lt;br /&gt;\r\n&lt;br /&gt;\r\n可选职业：[战士] &amp;nbsp;[圣骑] &lt;br /&gt;\r\n&lt;br /&gt;\r\n&lt;div style=&quot;text-align:center;&quot;&gt;\r\n	&lt;span style=&quot;line-height:1.5;&quot;&gt; &lt;/span&gt;&lt;img src=&quot;http://resource.locojoy.com/cms/2016/0122/20160122160527256.png&quot; alt=&quot;\\&quot; style=&quot;line-height:1.5;&quot; /&gt; \r\n&lt;/div&gt;\r\n&lt;strong&gt;牛头人&lt;/strong&gt;&lt;br /&gt;\r\n&lt;br /&gt;\r\n看看老牛那标志性疤痕，是不是觉得霸气侧漏，威风凌凌的？不过，你可别被这小小细节欺骗了。&lt;br /&gt;\r\n表面凶悍的牛头大哥其实并不是IQ满点，时时犯点小蠢才是他的生活原状。他的眼中永远第一看到玉器珠宝。见利忘义四个字简直就是为他量身打造的。不过这\r\n位到处拉仇恨的家伙，那可不是一无是处啊！！当偶尔的小正义激发他体内的洪荒之力时，那后果，啧啧……想要毫无风险承担的话，也得先掂量掂量自己哦！&lt;br /&gt;\r\n&lt;br /&gt;\r\n可选职业：[ 萨满] &amp;nbsp;[战士]&lt;br /&gt;\r\n&lt;br /&gt;\r\n&lt;div style=&quot;text-align:center;&quot;&gt;\r\n	&lt;span style=&quot;line-height:1.5;&quot;&gt; &lt;/span&gt;&lt;img src=&quot;http://resource.locojoy.com/cms/2016/0122/20160122160815678.jpg&quot; alt=&quot;\\&quot; style=&quot;line-height:1.5;&quot; /&gt; \r\n&lt;/div&gt;\r\n&lt;strong&gt;亡 灵&lt;/strong&gt;&lt;br /&gt;\r\n&lt;br /&gt;\r\n看着这森白的面庞，是不是被吓到了，觉得这个家伙是一个超级超级可怕的人物咧！不过他可是烹饪高手，面包制作高手。&lt;br /&gt;\r\n他十分有原则的家伙，“人不犯我，我不犯人”是他一贯奉行的标准。不过不要以为人家的存在感低就忽略人家，辅修法师的他可是最最实际的行动派。不动则\r\n已，一动惊人行事作风，可能会让你吃不了兜着走。治疗这点功效他也略懂一二。所以，作为伙伴他是不错的依靠，但是敌方可就需要费点神了！&lt;br /&gt;\r\n&lt;br /&gt;\r\n可选职业：[牧师] [术士] [法师]&lt;br /&gt;\r\n&lt;br /&gt;\r\n&lt;div style=&quot;text-align:center;&quot;&gt;\r\n	&lt;span style=&quot;line-height:1.5;&quot;&gt; &lt;/span&gt;&lt;img src=&quot;http://resource.locojoy.com/cms/2016/0122/20160122160558120.png&quot; alt=&quot;\\&quot; style=&quot;line-height:1.5;&quot; /&gt; \r\n&lt;/div&gt;\r\n&lt;strong&gt;暗夜精灵&lt;/strong&gt;&lt;br /&gt;\r\n&lt;br /&gt;\r\n看着那一双灵动忽闪的璀璨眼眸，是不是被萌翻了？我们的暗夜精灵小妹妹可是不简单的女孩纸哦！&lt;br /&gt;\r\n研修德鲁伊职业的她，可以自如的使用大自然的神秘力量来对付敌人，纵使你会千变万化，也逃不过自然的强大惩罚。小萝莉还是贴心小棉袄，她会利用自然之力来为同伴保驾护航，恢复同伴的生命。&lt;br /&gt;\r\n偶尔辅修盗贼职业的她，见缝插针的攻击可是经常使用，嘻嘻，最好别轻易招惹萌妹子。&lt;br /&gt;\r\n&lt;br /&gt;\r\n可选职业：[盗贼] &amp;nbsp;[猎人] &amp;nbsp;[德鲁伊]&lt;br /&gt;\r\n&lt;br /&gt;\r\n&lt;br /&gt;\r\n&lt;div style=&quot;text-align:center;&quot;&gt;\r\n	&lt;img src=&quot;http://resource.locojoy.com/cms/2016/0122/20160122160622399.png&quot; alt=&quot;\\&quot; style=&quot;line-height:1.5;&quot; /&gt; \r\n&lt;/div&gt;\r\n&lt;strong&gt;血精灵&lt;/strong&gt;&lt;br /&gt;\r\n&lt;br /&gt;\r\n迷人优雅的外貌，鲜红妖娆的装扮，我们的血精灵女王看起来永远是一副“只可远观，不可亵玩焉”的高贵模样。但这仅仅是她的伪装。她可是真正的卖萌高手，腹黑达人。&lt;br /&gt;\r\n拥有魅惑身姿的“血小贱”，哦不~血精灵女王大人可是精灵界钻石级人物，魔法是她最擅长的法宝，法术攻击也是手到擒来，偶尔客串一把牧师，治疗辅助不在话下。&lt;br /&gt;\r\n所以“花瓶”这个词可不能用在我们女王大人身上，否则，呵呵……结果你懂得&lt;br /&gt;\r\n&lt;br /&gt;\r\n&lt;br /&gt;\r\n可选职业：[牧师] [术士] [法师]');
INSERT INTO `faq_content` VALUES ('1015', '&lt;p&gt;&lt;a&gt;&lt;aDFFFD rhf=&quot;发大发@#@#@@##EDDDD&quot;&gt;AA\r\n	22222222222&lt;/aa&gt;&lt;/a&gt;&lt;/p&gt;&lt;p&gt;&lt;a&gt;%^%$^%$&amp;%*^&amp;*^&amp;*^JUJHJKJLL:&lt;m&lt;?m&lt;bnmbmnbcnmmn&lt;&lt;&gt;&lt;/m&lt;?m&lt;bnmbmnbcnmmn&lt;&lt;&gt;&lt;/a&gt;&lt;/p&gt;&lt;p&gt;&lt;a&gt;&lt;br /&gt;&lt;/a&gt;&lt;/p&gt;&lt;p&gt;&lt;a&gt;65656232323&lt;/a&gt;&lt;/p&gt;&lt;p&gt;&lt;a&gt;&lt;br /&gt;&lt;/a&gt;&lt;/p&gt;&lt;p&gt;&lt;a&gt;&lt;img title=&quot;&quot; src=&quot;http://resource.locojoy.com/cms/2016/03/16/5bf047b5da8db7fdd2dc84163229269c.jpg&quot; alt=&quot;&quot; height=&quot;614&quot; align=&quot;&quot; width=&quot;408&quot; /&gt; &lt;/a&gt;&lt;/p&gt;&lt;p&gt;&lt;a&gt;&lt;br /&gt;&lt;/a&gt;&lt;/p&gt;&#039;&lt;&gt;&gt;(*()*&amp;)UYUI');
INSERT INTO `faq_content` VALUES ('1016', '从前有座山,山里有个庙;庙里有个老和尚');

-- ----------------------------
-- Table structure for faq_record
-- ----------------------------
DROP TABLE IF EXISTS `faq_record`;
CREATE TABLE `faq_record` (
  `record_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userkey` varchar(30) NOT NULL,
  `keyword` varchar(60) NOT NULL DEFAULT '',
  `answer_faq_id` int(11) unsigned NOT NULL DEFAULT '0',
  `feedback_result` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '结果反馈,0未选择,1满意,2不满意',
  `month` varchar(10) NOT NULL DEFAULT '' COMMENT '月份,201603',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  KEY `mc` (`month`,`create_time`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='记录表';

-- ----------------------------
-- Records of faq_record
-- ----------------------------

-- ----------------------------
-- Table structure for faq_record_201601
-- ----------------------------
DROP TABLE IF EXISTS `faq_record_201601`;
CREATE TABLE `faq_record_201601` (
  `record_id` int(11) unsigned NOT NULL,
  `userkey` varchar(30) NOT NULL,
  `keyword` varchar(60) NOT NULL DEFAULT '',
  `answer_faq_id` int(11) unsigned NOT NULL DEFAULT '0',
  `feedback_result` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '结果反馈,0未选择,1满意,2不满意',
  `month` varchar(10) NOT NULL DEFAULT '' COMMENT '月份,201603',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='记录表';

-- ----------------------------
-- Records of faq_record_201601
-- ----------------------------
INSERT INTO `faq_record_201601` VALUES ('1', '1', '12121', '1', '1', '201601', '0');
INSERT INTO `faq_record_201601` VALUES ('3', '3', '3', '1', '2', '201601', '0');

-- ----------------------------
-- Table structure for faq_record_201603
-- ----------------------------
DROP TABLE IF EXISTS `faq_record_201603`;
CREATE TABLE `faq_record_201603` (
  `record_id` int(11) unsigned NOT NULL,
  `userkey` varchar(30) NOT NULL,
  `keyword` varchar(60) NOT NULL DEFAULT '',
  `answer_faq_id` int(11) unsigned NOT NULL DEFAULT '0',
  `feedback_result` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '结果反馈,0未选择,1满意,2不满意',
  `month` varchar(10) NOT NULL DEFAULT '' COMMENT '月份,201603',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='记录表';

-- ----------------------------
-- Records of faq_record_201603
-- ----------------------------
INSERT INTO `faq_record_201603` VALUES ('2', '1', '1', '1', '1', '201603', '0');

-- ----------------------------
-- Table structure for faq_record_subtable
-- ----------------------------
DROP TABLE IF EXISTS `faq_record_subtable`;
CREATE TABLE `faq_record_subtable` (
  `record_subtable_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) unsigned NOT NULL DEFAULT '0',
  `key` varchar(60) NOT NULL DEFAULT '' COMMENT '分表后缀名',
  `name` varchar(60) NOT NULL DEFAULT '' COMMENT '分表名称',
  `sort` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '排序,越大越靠前',
  `count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '数据量',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`record_subtable_id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='记录表分表';

-- ----------------------------
-- Records of faq_record_subtable
-- ----------------------------
INSERT INTO `faq_record_subtable` VALUES ('5', '0', '201601', '222', '0', '7', '2016-03-16 22:04:48');
INSERT INTO `faq_record_subtable` VALUES ('6', '0', '201603', '201603', '0', '1', '2016-03-17 09:36:06');

-- ----------------------------
-- Table structure for faq_to_column
-- ----------------------------
DROP TABLE IF EXISTS `faq_to_column`;
CREATE TABLE `faq_to_column` (
  `to_column_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `column_id` int(11) unsigned NOT NULL DEFAULT '0',
  `faq_id` int(11) unsigned NOT NULL DEFAULT '0',
  `sort` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`to_column_id`),
  KEY `csc` (`column_id`,`sort`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8 COMMENT='faq和栏目关系表';

-- ----------------------------
-- Records of faq_to_column
-- ----------------------------
INSERT INTO `faq_to_column` VALUES ('32', '2', '1014', '0', '1459394798');
INSERT INTO `faq_to_column` VALUES ('34', '3', '1014', '0', '1459396951');
INSERT INTO `faq_to_column` VALUES ('57', '1', '1015', '3', '1460344280');
INSERT INTO `faq_to_column` VALUES ('58', '2', '1015', '0', '1460344280');
