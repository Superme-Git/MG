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
class Game extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		
		$this->load->model(array('Game_model'));
	}

	public function index($offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		$where = array();
		$list = $this->Game_model->getList($where, null, $offset, $limit);
		$count = $this->Game_model->getCount($where);
		
		//分页
		$pagination = page($count, $offset, $limit);
		
		$this->load->view('game/index', array(
				'list' => $list,
				'pagination' => $pagination,
		));
		
	}
	
	public function add()
	{
		//提交表单
		if (is_post())
		{
			$this->form_validation->set_rules('gameid', 'gameid', valid_string_rule(true));
			$this->form_validation->set_rules('name', 'name', valid_string_rule());
			if (!$this->form_validation->run())
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string())));
				$this->session->set_flashdata('olddata', $this->input->post());
				$this->session->set_flashdata('error', $this->form_validation->error_string());
				return redirect('game/add');
			}
			$gameid = $this->form_validation->set_value('gameid');
			$name = $this->form_validation->set_value('name');
			
			//服务器是否存在
			$params = array();
			$params['gameid'] = $gameid;
			$info = $this->Game_model->getInfoByAttribute($params);
			if ($info)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' name already exist '.serialize(array($info)));
				$this->session->set_flashdata('error', 'name already exist');
				return redirect('game/add');
			}
			
			//添加服务器记录
			$params = array();
			$params['gameid'] = $gameid;
			$params['name'] = $name;
			$params['create_time'] = time();
			$result = $this->Game_model->insertInfo($params);
			
			$this->session->set_flashdata('success', lang('form_edit_succ'));
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($params)));
			
			return redirect(back_url('game/index'));
		}
		 
		$olddata = $this->session->flashdata('olddata');
    	
    	return $this->load->view("game/add", array(
    		'olddata' => $olddata
    	));
	
	}
	
	public function edit($game_id)
	{
		$info = $this->Game_model->getInfo($game_id);
		if (!$info)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($game_id)));
			return redirect('game/edit/'.$game_id);
		}
		
		//提交表单
		if (is_post())
		{
			$this->form_validation->set_rules('gameid', 'gameid', valid_string_rule(true));
			$this->form_validation->set_rules('name', 'name', valid_string_rule());
			if (!$this->form_validation->run())
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string())));
				$this->session->set_flashdata('olddata', $this->input->post());
				$this->session->set_flashdata('error', $this->form_validation->error_string());
				return redirect('game/add');
			}
			$gameid = $this->form_validation->set_value('gameid');
			$name = $this->form_validation->set_value('name');
			
			$params = array();
			$params['gameid'] = $gameid;
			$info = $this->Game_model->getInfoByAttribute($params);
			if ($info)
			{
				//排除自身
				if ($info['game_id'] != $gameid)
				{
					log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' name already exist '.serialize(array($info)));
					$this->session->set_flashdata('error', 'name already exist');
					return redirect('game/edit/'.$gameid);
				}
			}
			
			$params = array();
			$gameid && $params['gameid'] = $gameid;
			$name && $params['name'] = $name;
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $params '.serialize(array($params)));
			$result = $this->Game_model->updateInfo($game_id, $params);
			
			$this->session->set_flashdata('success', lang('form_edit_succ'));
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($params)));
			
			return redirect('game/edit/'.$game_id);
		}
		 
		return $this->load->view("game/edit", array(
				'info' => $info
		));
		
	}
	
	public function del($game_id)
	{
		$serverInfo = $this->Game_model->getInfo($game_id);
		if (!$serverInfo)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($game_id, $this->log_data)));
			return redirect('index');
		}
		$this->Game_model->deleteInfo($game_id);
		
		$this->session->set_flashdata('success', lang('form_del_succ'));
		return redirect(back_url('game/index'));
	}
	
	public function table_init()
	{
	    $gameid = $this->input->get('gameid', true);
	    
	    //服务器是否存在
	    $params = array();
	    $params['gameid'] = $gameid;
	    $info = $this->Game_model->getInfoByAttribute($params);
	    if (!$info)
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' name already exist '.serialize(array($info)));
	        $this->session->set_flashdata('error', 'name already exist');
	        return redirect(back_url('game/index'));
	    }
	    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' run sql '.serialize(array(time())));
	    
	    
	    $db = $this->load->database('write', true);
	    
	    //创建数据库
	    $table_name = "`role_".$info['gameid']."`";
	    $sql = "SHOW TABLES LIKE ".$table_name."";
	    $result = $db->query($sql);
	    if ($result)
	    {
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' dbname already exist '.serialize(array($table_name, $sql)));
	        $this->session->set_flashdata('error', '数据库创建失败;use='.$table_name.';sql='.$sql);
	        return redirect(back_url('game/index'));
	    }
	    
	     
	    //执行数据库语句
	    $sqls = "
SET names utf8;
SET FOREIGN_KEY_CHECKS=0;
	  
CREATE TABLE ".$table_name." (
  `role_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `gameid` varchar(30) NOT NULL DEFAULT '',
  `userid` varchar(30) NOT NULL DEFAULT '',
  `serverid` varchar(30) NOT NULL DEFAULT '',
  `icon` varchar(255) NOT NULL DEFAULT '',
  `create_time` int(10) unsigned NOT NULL DEFAULT '0',
  `nickname` varchar(60) NOT NULL DEFAULT '',
  PRIMARY KEY (`role_id`),
  KEY `gus` (`gameid`,`userid`,`serverid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色表,根据游戏进行分表';";
	     
	    $sql_arr = explode(';', $sqls);
	    if ($sql_arr && is_array($sql_arr))
	    {
	        foreach ($sql_arr as $sql)
	        {
	            $result = $db->query($sql);
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' sql '.serialize(array($result, $sql)));
	        }
	    }
	     
	    //更改服务器记录的数据库状态字段
	    $this->Game_model->updateInfo($info['game_id'], array('table_status' => 1));
	    
	    
	    $this->session->set_flashdata('success', '数据库初始化完成');
	    log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($this->log_data)));
	    return redirect(back_url('game/index'));
	}
}
