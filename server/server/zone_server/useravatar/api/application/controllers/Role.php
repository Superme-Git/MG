<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Role extends MY_Controller
{
	function __construct()
	{
		parent::__construct();
		$this->load->model("role_model");
	}
	
	//
	public function index()
	{
		
		
	}
	
	/**
	 * 设置用户信息
	 * 
	 * /role/set?gameid=1&userid=1&serverid=1&icon=1&sign=2ea5b261f06ca771033a5fa9e22493f1
	 */
	public function set()
	{
		$this->form_validation->set_rules('gameid', 'gameid', valid_int_rule(true));
		$this->form_validation->set_rules('userid', 'userid', valid_string_rule(1, 60, true));
		$this->form_validation->set_rules('serverid', 'serverid', valid_int_rule(true));
		$this->form_validation->set_rules('icon', 'icon', valid_string_rule(1, 255, true));
		$this->form_validation->set_rules('sign', 'sign', valid_text_rule(true));
		if (!$this->form_validation->run())
		{
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' request_params_error form_validation '.serialize(array($this->form_validation->error_string(), $this->log_data)));
			return response(null, '4100', lang('request_params_error'));
		}
		$gameid = $this->form_validation->set_value('gameid');
		$userid = $this->form_validation->set_value('userid');
		$serverid = $this->form_validation->set_value('serverid');
		$icon = $this->form_validation->set_value('icon');
		$sign = $this->form_validation->set_value('sign');
		
		$sign_ = md5($gameid .'|'.$userid.'|'.$serverid);
		if ($sign_ != $sign)
		{
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' role exist '.serialize(array($sign, $sign_, $this->log_data)));
			//return response('', '4101', 'sign error');
		}
		
		//查询角色是否存在
		$attrs = array();
		$attrs['gameid'] = $gameid;
		$attrs['userid'] = $userid;
		$attrs['serverid'] = $serverid;
		$this->role_model->setSubtableSuffix($gameid);
		$result = $this->role_model->getInfoByAttribute($attrs);
		
		//存在
		if ($result)
		{
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' role exist '.serialize(array($result, $attrs, $this->log_data)));
			return response('', '4200', 'record already exist');
		}
		
		//不存在
		$attrs = array();
		$attrs['gameid'] = $gameid;
		$attrs['userid'] = $userid;
		$attrs['serverid'] = $serverid;
		$attrs['icon'] = $icon;
		$attrs['create_time'] = time();
		$this->role_model->insertInfo($attrs);
		
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' insert succ '.serialize(array($attrs, $this->log_data)));
		return response(1);
	}
	
	/**
	 * 设置用户信息
	 *
	 * http://mt3.pengyouquan001.locojoy.com:8800/role/get?gameid=1&userid=1
	 */
	public function get()
	{
		$this->form_validation->set_rules('gameid', 'gameid', valid_int_rule(true));
		$this->form_validation->set_rules('userid', 'userid', valid_string_rule(1, 60, true));
		if (!$this->form_validation->run())
		{
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' request_params_error form_validation '.serialize(array($this->form_validation->error_string(), $this->log_data)));
			return response(null, 'request_params_error', lang('request_params_error'));
		}
		$gameid = $this->form_validation->set_value('gameid');
		$userid = $this->form_validation->set_value('userid');
		
		//查询用户所有区服的角色
		$attrs = array();
		$attrs['gameid'] = $gameid;
		$attrs['userid'] = $userid;
		$this->role_model->setSubtableSuffix($gameid);
		$list = $this->role_model->getList($attrs, null, 0, 99, null, 600);
		
		//存在
		if (!$list)
		{
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' succ '.serialize(array($attrs, $this->log_data)));
			return response('', '4200', 'record isnotexist');
		}
		
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' succ '.serialize(array($list, $this->log_data)));
		return response($list);
	}
	
}
