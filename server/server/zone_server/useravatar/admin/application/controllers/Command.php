<?php
/**
 * 命令控制器
 *
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 *
 */
class Command extends CI_Controller
{
	function __construct()
	{
		parent::__construct();
	}
	
	public function index($p)
	{
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' total '.serialize(array($p)));
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' total '.serialize(array(time())));
		//sleep(300);
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' total '.serialize(array(time())));
	}
	
	public function db_init($serverkey)
	{
	    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' start '.serialize(array(time())));
	    //exit(222);
	    
	    $this->load->model(array('Serverlist_model'));
	    $this->Serverlist_model->connect_group('serverlist');
	    
	    $where = array();
	    $where['serverkey'] = $serverkey;
	    $where['status'] = 0;
	    $serverlist_info = $this->Serverlist_model->getInfoByAttribute($where, 0, false);
	    
	    //判断是否已初始化
	    if ($serverlist_info['db_status'])
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' total '.serialize(array(time())));
	        exit(1);
	    }
	    
	    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' mysql_connect '.serialize(array(time())));
	    
	    //校验数据库连接是否正确
	    $con = mysql_connect($serverlist_info['hostname'],$serverlist_info['username'],$serverlist_info['password']);
	    if(!$con)
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' connect fail '.serialize(array($serverlist_info)));
	        exit(2);
	    }
	    	
	    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' mysql_select_db '.serialize(array(time())));
	    
	    //校验数据库名是存在
	    $result = mysql_select_db($serverlist_info['database'], $con);
	    if ($result)
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' dbname already exist '.serialize(array($serverlist_info)));
	        exit(3);
	    }
	    
	    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' CREATE DATABASE '.serialize(array(time())));
	    
	    //创建数据库
	    $sql = "CREATE DATABASE ".$serverlist_info['database'];
	    mysql_query($sql,$con);
	    
	    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' mysql_select_db '.serialize(array(time())));
	    
	    //选择数据库
	    $result = mysql_select_db($serverlist_info['database'],$con);
	    if (!$result)
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' dbname already notexist '.serialize(array($serverlist_info)));
	        exit(4);
	    }
	    
	    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' run sql '.serialize(array(time())));
	    
	    //执行数据库语句
	    $sqls = "	    
SET names utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8 COMMENT='留言板表';
	    
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
  `signature` varchar(255) NOT NULL DEFAULT '' COMMENT '签名',
  `popularity_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '人气数',
  `gift_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '收到礼物数',
  `sound_url` varchar(255) NOT NULL DEFAULT '' COMMENT '录音地址',
  `avatar_url` varchar(255) NOT NULL DEFAULT '' COMMENT '头像',
  `avatar_verify` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '审核状态0审核中1通过2不通过',
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='微博点赞表';";
	    
	    $sql_arr = explode(';', $sqls);
	    if ($sql_arr && is_array($sql_arr))
	    {
	        foreach ($sql_arr as $sql)
	        {
	            $result = mysql_query($sql, $con);
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' sql '.serialize(array($result, $sql)));
	        }
	    }
	    
	    
	    //更改服务器记录的数据库状态字段
	    $this->Serverlist_model->updateInfo($serverlist_info['serverlist_id'], array('db_status' => 1));
	    
	    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' succ '.serialize(array($serverlist_info)));
	}
	
	public function dashi_gift($serverkeys_str, $count)
	{
	    $dashi_roleid = config_item('dashi_roleid');
	    $serverkeys_arr = unserialize(base64_decode_($serverkeys_str));
	    
	    
	    
	    include APPPATH.'third_party/gm/Gm.php';
	    $gm = new Gm();
	    
	    foreach ($serverkeys_arr as $serverkey)
	    {
	        $result = $gm->set_dashi_gift_count($serverkey, $count);
	        if ($result['errno'])
	        {
	            $this->session->set_flashdata('error', $result['message']);
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' user exist '.serialize(array($serverkey, $count, $this->log_data)));
	            exit();
	        }
	    }
	    
	    
	}
	
	//大师推送脚本
	public function dashi_bbs($serverkeys_str, $content)
	{
	    $this->load->model(array('Serverlist_model'));
	    $this->Serverlist_model->connect_group('serverlist');
	    
	    
	    $dashi_roleid = config_item('dashi_roleid');
	    $serverkeys_arr = unserialize(base64_decode_($serverkeys_str));
	    $content = base64_decode_($content);
        
        foreach ($serverkeys_arr as $serverlist_id)
        {
            $where = array();
            $where['serverkey'] = $serverlist_id;
            $where['status'] = 0;
            $serverlist_info = $this->Serverlist_model->getInfoByAttribute($where, 0, false);
            
            
            $this->load->model(array('Bbs_model', 'Bbs_attribute_model'));
            $this->Bbs_model->connect($serverlist_info['hostname'], $serverlist_info['port'], $serverlist_info['username'], $serverlist_info['password'], $serverlist_info['database']);
            $this->Bbs_attribute_model->connect($serverlist_info['hostname'], $serverlist_info['port'], $serverlist_info['username'], $serverlist_info['password'], $serverlist_info['database']);
            
            $data = array();
            $data['roleid'] = $dashi_roleid;
            $data['to_roleid'] = $dashi_roleid;
            $data['gift_id'] = 0;
            $data['gift_count'] = 0;
            $data['reply_id'] = 0;
            $data['reply_roleid'] = 0;
            $data['create_time'] = time();
            $bbs_id = $this->Bbs_model->insertInfo($data);
            
            $data = array();
            $data['bbs_id'] = $bbs_id;
            $data['content'] = htmlspecialchars($content, ENT_QUOTES);//最长255, 多出的自动剔除
            $this->Bbs_attribute_model->insertInfo($data);
            
        }
        
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' run succ '.json_encode(array(microtime(true), count($data))));
	}
	
	/**
	 * 
	 * 角色id不变
	 * 
	 * @param unknown $from_servers
	 * @param unknown $to_server_key
	 */
	public function server_merge($from_server_keys, $to_server_key)
	{
	    $this->load->model(array('Serverlist_model', 'Server_merge_model'));
	    $this->Serverlist_model->connect_group('serverlist');
	    $this->Server_merge_model->connect_group('serverlist');
	    
	    $attr = array();
	    $attr['from_server_keys'] = $from_server_keys;
	    $attr['to_server_key'] = $to_server_key;
	    $attr['status'] = 0;
	    $attr['create_time'] = time();
	    $this->Server_merge_model->insertInfo($attr);
	    
	    
	    $from_server_keys = unserialize(base64_decode_($from_server_keys));
	    
	    //目标服务器
	    $to_server = $this->Serverlist_model->getInfo($to_server_key);
	    
	    $this->load->model(array('Role_model'));
	    $this->Role_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Role_attribute_model'));
	    $this->Role_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Role_relation_model'));
	    $this->Role_relation_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    
	    $this->load->model(array('Status_model'));
	    $this->Status_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Status_attribute_model'));
	    $this->Status_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Status_comment_model'));
	    $this->Status_comment_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Status_favorite_model'));
	    $this->Status_favorite_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    
	    $this->load->model(array('Bbs_model'));
	    $this->Bbs_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Bbs_attribute_model'));
	    $this->Bbs_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Bbs_gift_model'));
	    $this->Bbs_gift_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Bbs_popularity_model'));
	    $this->Bbs_popularity_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	     
	    
	    //执行合并
	    foreach ($from_server_keys as $from_server_key)
	    {
	        $from_server = $this->Serverlist_model->getInfo($from_server_key);
	        
	        //更改状态,做备注
	        $where = array();
	        $where['status'] = 1;
	        $where['mark'] = $from_server['mark'].';合服到'.$to_server['serverkey'];
	        $this->Serverlist_model->updateInfo($from_server['serverlist_id'], $where);
	        
	        //生成新记录
	        $attr = array();
	        $attr['serverkey'] = $from_server['serverkey'];
	        $attr['servername'] = $from_server['servername'];
	        $attr['hostname'] = $to_server['hostname'];
	        $attr['port'] = $to_server['port'];
	        $attr['username'] = $to_server['username'];
	        $attr['password'] = $to_server['password'];
	        $attr['database'] = $to_server['database'];
	        $attr['status'] = 0;
	        $attr['create_time'] = time();
	        $this->Serverlist_model->insertInfo($attr);
	        
	        
	        $this->load->model(array('Role_model' => 'from_Role_model'));
	        $this->from_Role_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Role_attribute_model' => 'from_Role_attribute_model'));
	        $this->from_Role_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Role_relation_model' => 'from_Role_relation_model'));
	        $this->from_Role_relation_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	         
	        $this->load->model(array('Status_model' => 'from_Status_model'));
	        $this->from_Status_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Status_attribute_model' => 'from_Status_attribute_model'));
	        $this->from_Status_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Status_comment_model' => 'from_Status_comment_model'));
	        $this->from_Status_comment_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Status_favorite_model' => 'from_Status_favorite_model'));
	        $this->from_Status_favorite_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	         
	        $this->load->model(array('Bbs_model' => 'from_Bbs_model'));
	        $this->from_Bbs_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Bbs_attribute_model' => 'from_Bbs_attribute_model'));
	        $this->from_Bbs_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Bbs_gift_model' => 'from_Bbs_gift_model'));
	        $this->from_Bbs_gift_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Bbs_popularity_model' => 'from_Bbs_popularity_model'));
	        $this->from_Bbs_popularity_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        
	        
	        //------ role ----------------------------
	        
	        $total_count = $this->from_Role_model->getCount(null, false);
	        $once_count = 2000;
	        $total_times = ceil($total_count / $once_count);
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role start '.json_encode(array($total_times, $total_count, $once_count)));
	        
	        for ($i = 0; $i < $total_times; $i++)
	        {
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
	            
	            $from_list = $this->from_Role_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
	            if ($from_list)
	            {
	                foreach ($from_list as $from_item)
	                {
	                    //角色表
	                    $attr = array();
	                    $attr['roleid'] = $from_item['roleid'];
	                    $attr['name'] = $from_item['name'];
	                    $attr['avatar'] = $from_item['avatar'];
	                    $attr['level'] = $from_item['level'];
	                    $this->Role_model->insertInfo($attr);
	                }
	            }
	            
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role end '.json_encode(array(microtime(true))));
	        }
	        
	        //------ role attribute ----------------------------
	        
	        //角色属性表
	        $total_count = $this->from_Role_attribute_model->getCount(null, false);
	        $once_count = 2000;
	        $total_times = ceil($total_count / $once_count);
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_attr start '.json_encode(array($total_times, $total_count, $once_count)));
	         
	        for ($i = 0; $i < $total_times; $i++)
	        {
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_attr page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
                $from_list = $this->from_Role_attribute_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
	            if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
						//角色表
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['msg_count'] = $from_item['msg_count'];
            	        $attr['signature'] = $from_item['signature'];
            	        $attr['popularity_count'] = $from_item['popularity_count'];
            	        $attr['gift_count'] = $from_item['gift_count'];
            	        $attr['sound_url'] = $from_item['sound_url'];
            	        $attr['avatar_url'] = $from_item['avatar_url'];
            	        $attr['avatar_verify'] = $from_item['avatar_verify'];
            	        $attr['place'] = $from_item['place'];
            	        $attr['longitude'] = $from_item['longitude'];
            	        $attr['latitude'] = $from_item['latitude'];
						$this->Role_attribute_model->insertInfo($attr);
					}
				}
	             
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_attr end '.json_encode(array(microtime(true))));
	        }
	         
	        
	         
	        //-------------角色关系表--------------------------
	        
			$total_count = $this->from_Role_relation_model->getCount(null, false);
			$once_count = 2000;
			$total_times = ceil($total_count / $once_count);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_rela start '.json_encode(array($total_times, $total_count, $once_count)));
			 
			for ($i = 0; $i < $total_times; $i++)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_rela page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
				$from_list = $this->from_Role_relation_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
				if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
						//角色表
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['friendid'] = $from_item['friendid'];
						$attr['relation'] = $from_item['relation'];
						$this->Role_relation_model->insertInfo($attr);
					}
				}
				 
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_rela end '.json_encode(array(microtime(true))));
			}


			//-------------动态表--------------------------
			
			$total_count = $this->from_Status_model->getCount(null, false);
			$once_count = 2000;
			$total_times = ceil($total_count / $once_count);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status start '.json_encode(array($total_times, $total_count, $once_count)));
			 
			
			$status_ids = array();
			for ($i = 0; $i < $total_times; $i++)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
				$from_list = $this->from_Status_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
				if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['status'] = $from_item['status'];
						$attr['create_time'] = $from_item['create_time'];
						$status_id = $this->Status_model->insertInfo($attr);
						
						//新老状态id对应数组
						$status_ids[$from_item['status_id']] = $status_id;
						
						$attr_info = $this->from_Status_attribute_model->getInfo($from_item['status_id']);
						$attr = array();
						$attr['status_id'] = $status_id;
						$attr['content'] = $attr_info['content'];
						$attr['img_url'] = $attr_info['img_url'];
						$attr['comment_count'] = $attr_info['comment_count'];
						$attr['favorite_count'] = $attr_info['favorite_count'];
						$this->Status_attribute_model->insertInfo($attr);
						
						log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status_comment start '.json_encode(array(microtime(true))));
						$where = array('status_id' => $from_item['status_id']);
						$order = array('status_comment_id' => 'asc');
						$comment_list = $this->from_Status_comment_model->getList($where, $order, 0, 99999999, null, false,false );
						if ($comment_list)
						{
						    foreach ($comment_list as $item)
						    {
						        $reply_id = 0;
						        if($item['reply_id'])
						        {
						            if (isset($status_ids[$item['reply_id']]))
						            {
						                $reply_id = $status_ids[$item['reply_id']];
						            }
						            else
						            {
						                log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status_comment merge error: notfind reply_id '.json_encode(array($item)));
						            }
						        }
						        
						        
						        $attr = array();
						        $attr['status_id'] = $status_id;
						        $attr['roleid'] = $item['roleid'];
						        $attr['content'] = $item['content'];
						        $attr['status'] = $item['status'];
						        $attr['reply_id'] = $reply_id;
						        $attr['reply_roleid'] = $item['reply_roleid'];
						        $attr['create_time'] = $item['create_time'];
						        $this->Status_comment_model->insertInfo($attr);
						    }
						}
						log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status_comment end '.json_encode(array(microtime(true))));
						
						log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status_favorite start '.json_encode(array(microtime(true))));
						$where = array('status_id' => $from_item['status_id']);
						$order = array('status_favorite_id' => 'asc');
						$favorite_list = $this->from_Status_favorite_model->getList($where, $order, 0, 99999999, null, false,false );
						if ($favorite_list)
						{
						    foreach ($favorite_list as $item)
						    {
						        $attr = array();
						        $attr['status_id'] = $status_id;
						        $attr['roleid'] = $item['roleid'];
						        $attr['status'] = $item['status'];
						        $attr['create_time'] = $item['create_time'];
						        $this->Status_favorite_model->insertInfo($attr);
						    }
						}
						log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status_favorite end '.json_encode(array(microtime(true))));
						
					}
				}
				 
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status end '.json_encode(array(microtime(true))));
			}
	         
	        //-------留言板-------------------
			$total_count = $this->from_Bbs_model->getCount(null, false);
			$once_count = 2000;
			$total_times = ceil($total_count / $once_count);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs start '.json_encode(array($total_times, $total_count, $once_count)));
			 
			
			$bbs_ids = array();
			for ($i = 0; $i < $total_times; $i++)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
				$from_list = $this->from_Bbs_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
				if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
					    $reply_id = 0;
					    if($item['reply_id'])
					    {
					        if (isset($bbs_ids[$item['reply_id']]))
					        {
					            $reply_id = $bbs_ids[$item['reply_id']];
					        }
					        else
					        {
					            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs merge error: notfind reply_id '.json_encode(array($item)));
					        }
					    }
					    
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['to_roleid'] = $from_item['to_roleid'];
						$attr['status'] = $from_item['status'];
						$attr['gift_id'] = $from_item['gift_id'];
						$attr['gift_count'] = $from_item['gift_count'];
						$attr['reply_id'] = $reply_id;
						$attr['reply_roleid'] = $from_item['reply_roleid'];
						$attr['create_time'] = $from_item['create_time'];
						$bbs_id = $this->Bbs_model->insertInfo($attr);
						
						//新老状态id对应数组
						$bbs_ids[$from_item['bbs_id']] = $bbs_id;
						
						$attr_info = $this->from_Bbs_attribute_model->getInfo($from_item['bbs_id']);
						$attr = array();
						$attr['bbs_id'] = $bbs_id;
						$attr['content'] = $attr_info['content'];
						$this->Bbs_attribute_model->insertInfo($attr);
						
					}
				}
				 
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs end '.json_encode(array(microtime(true))));
			}
			
			//--------留言板礼物-----------------
			
			$total_count = $this->from_Bbs_gift_model->getCount(null, false);
			$once_count = 2000;
			$total_times = ceil($total_count / $once_count);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs_gift start '.json_encode(array($total_times, $total_count, $once_count)));
			
			for ($i = 0; $i < $total_times; $i++)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs_gift page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
				$from_list = $this->from_Bbs_gift_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
				if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['to_roleid'] = $from_item['to_roleid'];
						$attr['status'] = $from_item['status'];
						$attr['gift_id'] = $from_item['gift_id'];
						$attr['gift_count'] = $from_item['gift_count'];
						$attr['reply_id'] = $reply_id;
						$attr['reply_roleid'] = $from_item['reply_roleid'];
						$attr['create_time'] = $from_item['create_time'];
						$this->Bbs_gift_model->insertInfo($attr);
					}
				}
				 
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs_gift end '.json_encode(array(microtime(true))));
			}			
			
			//--------留言板人气-----------------
						
			$total_count = $this->from_Bbs_popularity_model->getCount(null, false);
			$once_count = 2000;
			$total_times = ceil($total_count / $once_count);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs_popularity start '.json_encode(array($total_times, $total_count, $once_count)));
			
			for ($i = 0; $i < $total_times; $i++)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_rela page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
				$from_list = $this->from_Bbs_popularity_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
				if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['to_roleid'] = $from_item['to_roleid'];
						$attr['is_get'] = $from_item['is_get'];
						$attr['status'] = $from_item['status'];
						$attr['create_time'] = $from_item['create_time'];
						$this->Bbs_popularity_model->insertInfo($attr);
					}
				}
				 
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs_popularity end '.json_encode(array(microtime(true))));
			}			
	        
	        //-----------end----------------------------
	        
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' end '.json_encode(array(microtime(true))));
	    }
	}
	
	
	public function server_merge_rollback($server_merge_id)
	{
	    $this->load->model(array('Serverlist_model', 'Server_merge_model'));
	    $this->Serverlist_model->connect_group('serverlist');
	    $this->Server_merge_model->connect_group('serverlist');
	    
	    
	    $info = $this->Server_merge_model->getInfo($server_merge_id);
	    if (!$info)
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' empty '.json_encode(array($this->log_data)));
	        exit('记录不存在');
	    }
	    elseif ($info['status'])
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' status error '.json_encode(array($this->log_data)));
	        exit('此操作已执行过, 不可重复执行');
	    }
	    
	    $where = array();
	    $where['status'] = 1;
	    $this->Server_merge_model->updateInfo($server_merge_id, $where);
	    
	    
		$from_server_keys = unserialize(base64_decode_($info['from_server_keys']));
		
		//目标服务器
		$to_server = $this->Serverlist_model->getInfo($info['to_server_key']);
		
		$this->load->model(array('Role_model'));
		$this->Role_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Role_attribute_model'));
		$this->Role_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Role_relation_model'));
		$this->Role_relation_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		
		$this->load->model(array('Status_model'));
		$this->Status_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Status_attribute_model'));
		$this->Status_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Status_comment_model'));
		$this->Status_comment_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Status_favorite_model'));
		$this->Status_favorite_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		
		$this->load->model(array('Bbs_model'));
		$this->Bbs_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Bbs_attribute_model'));
		$this->Bbs_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Bbs_gift_model'));
		$this->Bbs_gift_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Bbs_popularity_model'));
		$this->Bbs_popularity_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		 
		
		//执行合并
		foreach ($from_server_keys as $from_server_key)
		{
		    //-------------------------------------------
		    
			$from_server = $this->Serverlist_model->getInfo($from_server_key);
			
			$where = array();
			$where['serverkey'] = $from_server['serverkey'];
			$where['status'] = 0;
			$from_server_last = $this->Serverlist_model->getInfoByAttribute($where, 0, false);
			
			//关闭当前的服务器
			$where = array();
			$where['status'] = 1;
			$where['mark'] = $from_server_last['mark'].';合服恢复,关闭';
			$this->Serverlist_model->updateInfo($from_server_last['serverlist_id'], $where);
			
			//开启原服务器
			$where = array();
			$where['status'] = 0;
			$where['mark'] = $from_server['mark'].';合服恢复,启用';
			$this->Serverlist_model->updateInfo($from_server['serverlist_id'], $where);
			
			
			//-------------------------------------------
			
			
			$this->load->model(array('Role_model' => 'from_Role_model'));
			$this->from_Role_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Role_attribute_model' => 'from_Role_attribute_model'));
			$this->from_Role_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Role_relation_model' => 'from_Role_relation_model'));
			$this->from_Role_relation_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			 
			$this->load->model(array('Status_model' => 'from_Status_model'));
			$this->from_Status_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Status_attribute_model' => 'from_Status_attribute_model'));
			$this->from_Status_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Status_comment_model' => 'from_Status_comment_model'));
			$this->from_Status_comment_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Status_favorite_model' => 'from_Status_favorite_model'));
			$this->from_Status_favorite_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			 
			$this->load->model(array('Bbs_model' => 'from_Bbs_model'));
			$this->from_Bbs_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Bbs_attribute_model' => 'from_Bbs_attribute_model'));
			$this->from_Bbs_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Bbs_gift_model' => 'from_Bbs_gift_model'));
			$this->from_Bbs_gift_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Bbs_popularity_model' => 'from_Bbs_popularity_model'));
			$this->from_Bbs_popularity_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			
			
			//------ role ----------------------------
			
			
			$total_count = $this->Role_model->getCount(null, false);
			$from_list = $this->Role_model->getList(null, null, 0, 9999999, null, false, false);
			
			if ($from_list)
			{
				foreach ($from_list as $i => $from_item)
				{
					log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' foreach '.json_encode(array($i.'/'.$total_count)));
					
					$this->Role_model->deleteInfo($from_item['roleid']);
					
					$this->Role_attribute_model->deleteInfo($from_item['roleid']);
					
					$this->Role_relation_model->remove_info($from_item['roleid']);
					
					//---------------------------
					
					$where = array();
					$where['roleid'] = $from_item['roleid'];
					$list_ = $this->Status_model->getList($where, null, 0, 9999999, null, 0, false);
					if ($list_)
					{
						foreach ($list_ as $item_)
						{
							$this->Status_model->deleteInfo($item_['status_id']);
							$this->Status_attribute_model->deleteInfo($item_['status_id']);
							
							$where = array('status_id' => $item_['status_id']);
							$comment_list = $this->Status_comment_model->getList($where, null, 0, 999999, null, 0, false );
							if ($comment_list)
							{
								foreach ($comment_list as $item)
								{
									$this->Status_comment_model->deleteInfo($item_['status_comment_id']);
								}
							}
							
							$where = array('status_id' => $item_['status_id']);
							$comment_list = $this->Status_favorite_model->getList($where, null, 0, 999999, null, 0, false );
							if ($comment_list)
							{
								foreach ($comment_list as $item)
								{
									$this->Status_favorite_model->deleteInfo($item_['status_favorite_id']);
								}
							}
							
						}
					}
					
					
					//-----------------------------------------
					
					$where = array();
					$where['roleid'] = $from_item['roleid'];
					$list_ = $this->Bbs_model->getList($where, null, 0, 9999999, null, 0, false);
					if ($list_)
					{
						foreach ($list_ as $item_)
						{
							$this->Bbs_model->deleteInfo($item_['bbs_id']);
							$this->Bbs_attribute_model->deleteInfo($item_['bbs_id']);
						}
					}
					
					$where = array();
					$where['roleid'] = $from_item['roleid'];
					$list_ = $this->Bbs_gift_model->getList($where, null, 0, 9999999, null, 0, false);
					if ($list_)
					{
						foreach ($list_ as $item_)
						{
							$this->Bbs_gift_model->deleteInfo($item_['bbs_gift_id']);
						}
					}
					
					$where = array();
					$where['roleid'] = $from_item['roleid'];
					$list_ = $this->Bbs_popularity_model->getList($where, null, 0, 9999999, null, 0, false);
					if ($list_)
					{
						foreach ($list_ as $item_)
						{
							$this->Bbs_popularity_model->deleteInfo($item_['bbs_popularity_id']);
						}
					}
					
				}
				
				
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role end '.json_encode(array(microtime(true))));
			}
			
			//-----------end----------------------------
			
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' end '.json_encode(array(microtime(true))));	    
	    
		}
	}
	
	
	
}