<?php
defined('BASEPATH') OR exit('No direct script access allowed');
/**
 *
 * 栏目管理
 *
 * @copyright www.locojoy.com
 * @author xinmanpei@joyogame.com
 * @version v1.0 2016.04.01
 *
 */
class Serverlist extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		
		$this->load->model(array('Serverlist_model', 'Server_merge_model'));
		$this->Serverlist_model->connect_group('serverlist');
		$this->Server_merge_model->connect_group('serverlist');
	}

	public function index($offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		$where = array();
		$where['status'] = 0;
		$list = $this->Serverlist_model->getList($where, null, $offset, $limit);
		$count = $this->Serverlist_model->getCount($where);
		
		//分页
		$pagination = page($count, $offset, $limit);
		
		$this->load->view('serverlist/index', array(
				'list' => $list,
				'pagination' => $pagination,
		));
		
	}
	
    public function select()
    {
    	$serverlist_id = $this->input->get('serverlist_id', true);
    	
    	$info = $this->Serverlist_model->getInfo($serverlist_id);
		if (!$info)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($serverlist_id)));
			return redirect('serverlist');
		}
		
		//设置区服标志
		setSite($info['serverkey']);
    	
    	$this->session->set_userdata(array('serverlist' => $info));
    	
    	log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' redirect to selected list '.serialize(array($serverlist_id)));
		return redirect('bbs/index');
    }
	
	public function add()
	{
	    if (!check_auth('root'))
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' check_auth root '.json_encode(array($this->log_data)));
	        $this->session->set_flashdata('error', '您没有权限');
	        return redirect(back_url('serverlist/index'));
	    }
	    
	    
		//提交表单
		if (is_post())
		{
			$this->form_validation->set_rules('serverkey', 'serverkey', valid_string_rule(true));
			$this->form_validation->set_rules('servername', 'servername', valid_string_rule());
			$this->form_validation->set_rules('hostname', 'hostname', valid_string_rule(true));
			$this->form_validation->set_rules('port', 'port', valid_string_rule(true));
			$this->form_validation->set_rules('username', 'username', valid_string_rule(true));
			$this->form_validation->set_rules('password', 'password', valid_string_rule(true));
			$this->form_validation->set_rules('database', 'database', valid_string_rule(true));
			
			if (!$this->form_validation->run())
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string())));
				$this->session->set_flashdata('olddata', $this->input->post());
				$this->session->set_flashdata('error', $this->form_validation->error_string());
				return redirect('serverlist/add');
			}
			
			$serverkey = $this->form_validation->set_value('serverkey');
			$servername = $this->form_validation->set_value('servername');
			$hostname = $this->form_validation->set_value('hostname');
			$port = $this->form_validation->set_value('port');
			$username = $this->form_validation->set_value('username');
			$password = $this->form_validation->set_value('password');
			$database = $this->form_validation->set_value('database');
			
			//服务器是否存在
			$params = array();
			$params['serverkey'] = $serverkey;
			$params['status'] = 0;
			$serverInfo = $this->Serverlist_model->getInfoByAttribute($params);
			if ($serverInfo)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' name already exist '.serialize(array($serverInfo)));
				$this->session->set_flashdata('error', 'name already exist');
				return redirect('serverlist/add');
			}
			
			//校验数据库连接是否正确
			$con = mysql_connect($hostname,$username,$password);
			if(!$con)
			{
			    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' connect fail '.serialize(array($serverInfo)));
			    $this->session->set_flashdata('error', '数据库连接不可用');
			    return redirect('serverlist/add');
			}
			
			//校验数据库名是存在
			$result = mysql_select_db($database, $con);
			if ($result)
			{
			    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' dbname already exist '.serialize(array($serverInfo)));
			    $this->session->set_flashdata('error', '数据库名已经存在');
			    return redirect('serverlist/add');
			}
			
			
			//添加服务器记录
			$params = array();
			$params['serverkey'] = $serverkey;
			$params['servername'] = $servername;
			$params['hostname'] = $hostname;
			$params['port'] = $port;
			$params['username'] = $username;
			$params['password'] = $password;
			$params['database'] = $database;
			$params['create_time'] = time();
			$result = $this->Serverlist_model->insertInfo($params);
			
			$this->session->set_flashdata('success', lang('form_edit_succ'));
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($params)));
			
			return redirect('index');
		}
		 
		$olddata = $this->session->flashdata('olddata');
    	
    	return $this->load->view("serverlist/add", array(
    		'olddata' => $olddata
    	));
	
	}
	
	public function edit()
	{
	    if (!check_auth('root'))
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' check_auth root '.json_encode(array($this->log_data)));
	        $this->session->set_flashdata('error', '您没有权限');
	        return redirect(back_url('serverlist/index'));
	    }
	    
	    
		$serverlist_id = $this->input->get('serverlist_id', true);
		
		$info = $this->Serverlist_model->getInfo($serverlist_id);
		if (!$info)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($serverlist_id)));
			return redirect('serverlist/edit?serverlist_id='.$serverlist_id);
		}
		
		//提交表单
		if (is_post())
		{
			$this->form_validation->set_rules('serverkey', 'serverkey', valid_string_rule(true));
			$this->form_validation->set_rules('servername', 'servername', valid_string_rule());
			$this->form_validation->set_rules('hostname', 'hostname', valid_string_rule(true));
			$this->form_validation->set_rules('port', 'port', valid_string_rule(true));
			$this->form_validation->set_rules('username', 'username', valid_string_rule(true));
			$this->form_validation->set_rules('password', 'password', valid_string_rule(true));
			$this->form_validation->set_rules('database', 'database', valid_string_rule(true));
			
			if (!$this->form_validation->run())
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string())));
				$this->session->set_flashdata('olddata', $this->input->post());
				$this->session->set_flashdata('error', $this->form_validation->error_string());
				return redirect('serverlist/add');
			}
			$serverkey = $this->form_validation->set_value('serverkey');
			$servername = $this->form_validation->set_value('servername');
			$hostname = $this->form_validation->set_value('hostname');
			$port = $this->form_validation->set_value('port');
			$username = $this->form_validation->set_value('username');
			$password = $this->form_validation->set_value('password');
			$database = $this->form_validation->set_value('database');
			
			$params = array();
			$params['serverkey'] = $serverkey;
			$params['status'] = 0;
			$serverInfo = $this->Serverlist_model->getInfoByAttribute($params);
			if ($serverInfo)
			{
				//排除自身
				if ($serverInfo['serverlist_id'] != $serverlist_id)
				{
					log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' name already exist '.serialize(array($serverInfo)));
					$this->session->set_flashdata('error', 'name already exist');
					return redirect('serverlist/edit?serverlist_id='.$serverlist_id);
				}
			}
			
			$params = array();
			$serverkey && $params['serverkey'] = $serverkey;
			$servername && $params['servername'] = $servername;
			$hostname && $params['hostname'] = $hostname;
			$port && $params['port'] = $port;
			$username && $params['username'] = $username;
			$password && $params['password'] = $password;
			$database && $params['database'] = $database;
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $params '.serialize(array($params)));
			$result = $this->Serverlist_model->updateInfo($serverlist_id, $params);
			
			$this->session->set_flashdata('success', lang('form_edit_succ'));
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($params)));
			
			return redirect('serverlist/edit?serverlist_id='.$serverlist_id);
		}
		 
		return $this->load->view("serverlist/edit", array(
				'info' => $info
		));
		
	}
	
	public function del()
	{
	    if (!check_auth('root'))
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' check_auth root '.json_encode(array($this->log_data)));
	        $this->session->set_flashdata('error', '您没有权限');
	        return redirect(back_url('serverlist/index'));
	    }
	    
	    
		$serverlist_id = $this->input->get('serverlist_id', true);
		
		$serverInfo = $this->Serverlist_model->getInfo($serverlist_id);
		if (!$serverInfo)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($serverlist_id, $this->log_data)));
			return redirect('index');
		}
		
		$this->Serverlist_model->deleteInfo($serverlist_id);
		
		$this->session->set_flashdata('success', lang('form_del_succ'));
		return redirect('serverlist');
	}
	
	public function merge()
	{
	    if (!check_auth('root'))
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' check_auth root '.json_encode(array($this->log_data)));
	        $this->session->set_flashdata('error', '您没有权限');
	        return redirect(back_url('serverlist/index'));
	    }
	    
	    
	    if (is_post())
	    {
	        $from_servers = $this->input->get_post('from_servers', true);
	        $to_server = $this->input->get_post('to_server', true);
	        
	        if (!$from_servers)
	        {
	            log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($this->log_data)));
	            $this->session->set_flashdata('error', '请选择要合并的服务器');
	            return redirect('serverlist/merge');
	        }
	        
	        if (!$to_server)
	        {
	            log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($this->log_data)));
	            $this->session->set_flashdata('error', '请选择目标服务器');
	            return redirect('serverlist/merge');
	        }
	        
	        if (in_array($to_server, $from_servers))
	        {
	            log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($this->log_data)));
	            $key = array_search($to_server, $from_servers);
	            $from_servers[$key] = '';
	            unset($from_servers[$key]);
	        }
	        
	        if (count($from_servers) < 1)
	        {
	            log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($this->log_data)));
	            $this->session->set_flashdata('error', '请选择要合并的服务器(除目标服务器)');
	            return redirect('serverlist/merge');
	        }
	        
	        $server_ids = base64_encode_(serialize($from_servers));
	         
	        $cmd = "nohup ".config_item('php_bin')." ".FCPATH."index.php command/server_merge $server_ids $to_server > nohup.log 2>&1 &";
	        @exec($cmd);
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' restart, exec '.json_encode(array($cmd)));
	        
	        $this->session->set_flashdata('success', lang('cmd_running_succ'));
	        log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($this->log_data)));
	        return redirect('serverlist/merge');
	    }
	    
	    $where = array();
	    $where['status'] = 0;
	    $server_list = $this->Serverlist_model->getList($where, null, 0, 99999);
	    
	    return $this->load->view("serverlist/merge", array(
	        'server_list' => $server_list
	    ));
	}
	
	public function merge_list()
	{
	    if (!check_auth('root'))
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' check_auth root '.json_encode(array($this->log_data)));
	        $this->session->set_flashdata('error', '您没有权限');
	        return redirect(back_url('serverlist/index'));
	    }
	    
	    $where = array();
	    $where['status'] = 0;
	    $list = $this->Server_merge_model->getList($where, null, 0, 99999);
	
	    return $this->load->view("serverlist/merge_list", array(
	        'list' => $list
	    ));
	}
	
	public function merge_rollback()
	{
	    if (!check_auth('root'))
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' check_auth root '.json_encode(array($this->log_data)));
	        $this->session->set_flashdata('error', '您没有权限');
	        return redirect(back_url('serverlist/index'));
	    }
	    
	    $server_merge_id = $this->input->get('server_merge_id', true);
	    
	    $info = $this->Server_merge_model->getInfo($server_merge_id);
	    if (!$info)
	    {
	        $this->session->set_flashdata('error', '记录不存在');
	        log_message('error', __CLASS__.' '.__FUNCTION__.' empty '.json_encode(array($this->log_data)));
	        return redirect('serverlist/merge_list');
	    }
	    elseif ($info['status'])
	    {
	        $this->session->set_flashdata('error', '此操作已执行过, 不可重复执行');
	        log_message('error', __CLASS__.' '.__FUNCTION__.' status error '.json_encode(array($this->log_data)));
	        return redirect('serverlist/merge_list');
	    }
	    
	    if (is_post())
	    {
	        $cmd = "nohup ".config_item('php_bin')." ".FCPATH."index.php command/server_merge_rollback $server_merge_id > nohup.log 2>&1 &";
	        @exec($cmd);
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' restart, exec '.json_encode(array($cmd)));
	         
	        $this->session->set_flashdata('success', lang('cmd_running_succ'));
	        log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($this->log_data)));
	        return redirect('serverlist/merge_list');
	    }
	     
	    return $this->load->view("serverlist/merge_rollback", array(
	        'info' => $info
	    ));
	}
	
	public function db_init()
	{
	    if (!check_auth('root'))
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' check_auth root '.json_encode(array($this->log_data)));
	        $this->session->set_flashdata('error', '您没有权限');
	        return redirect(back_url('serverlist/index'));
	    }
	    
	    
	    $serverkey = $this->input->get('serverkey', true);
	    
	    
	    //服务器是否存在
	    $params = array();
	    $params['serverkey'] = $serverkey;
	    $params['status'] = 0;
	    $serverlist_info = $this->Serverlist_model->getInfoByAttribute($params);
	    if (!$serverlist_info)
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' name already exist '.serialize(array($serverlist_info)));
	        $this->session->set_flashdata('error', 'name already exist');
	        return redirect(back_url('serverlist/index'));
	    }
	    	
	    //校验数据库连接是否正确
	    $con = mysql_connect($serverlist_info['hostname'],$serverlist_info['username'],$serverlist_info['password']);
	    if(!$con)
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' connect fail '.serialize(array($serverlist_info)));
	        $this->session->set_flashdata('error', '数据库连接不可用');
	        return redirect(back_url('serverlist/index'));
	    }
	    	
	    //校验数据库名是存在
	    $result = mysql_select_db($serverlist_info['database'], $con);
	    if ($result)
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' dbname already exist '.serialize(array($serverlist_info)));
	        $this->session->set_flashdata('error', '数据库名已经存在');
	        return redirect(back_url('serverlist/index'));
	    }
	    
	    //创建数据库
	    $sql = "CREATE DATABASE ".$serverlist_info['database'];
	    $result = mysql_query($sql,$con);
	    if (!$result)
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' dbname already notexist '.serialize(array($sql, $serverlist_info)));
	        $this->session->set_flashdata('error', '数据库创建失败;use='.$serverlist_info['username'].';sql='.$sql);
	        return redirect(back_url('serverlist/index'));
	    }
	     
	    //选择数据库
	    $result = mysql_select_db($serverlist_info['database'],$con);
	    if (!$result)
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' dbname notexist '.serialize(array($serverlist_info)));
	        $this->session->set_flashdata('error', '数据库名选择失败, 数据库不存在');
	        return redirect(back_url('serverlist/index'));
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
	    
	    
	    $this->session->set_flashdata('success', '数据库初始化完成');
	    log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($this->log_data)));
	    return redirect(back_url('serverlist/index'));
	}
}
