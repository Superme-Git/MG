<?php
/**
 * 管理用户管理
 * 
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 */
class Admin extends MY_Controller
{
    function __construct()
    {
        parent::__construct();
        
        $this->load->model("Admin_model");
    }
    
    public function index()
    {
    	$offset = 0;
    	$limit = 15;
    	
    	
    	$params = array();
    	$list = $this->Admin_model->getList($params);
    	
    	$count = $this->Admin_model->getCount($params);
    	
    	$pagination = page($count, $offset, $limit);
    	
    	
    	return $this->load->view("admin/index", array(
    			'list' => $list, 
    			'pagination' => $pagination
    	));
    }
    
    public function add()
    {
    	//提交表单
    	if (is_post())
    	{
    		$this->form_validation->set_rules('email', '邮箱', valid_email_rule(true));
    		$this->form_validation->set_rules('password', '密码', valid_string_rule(1, 30, true));
    		if (!$this->form_validation->run())
    		{
    			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    			$this->session->set_flashdata('olddata', $this->input->post());
    			$this->session->set_flashdata('error', '您填写的表单有误, 请检查'.$this->form_validation->error_string());
    			return redirect('admin/add');
    		}
    		$email = $this->form_validation->set_value('email');
    		$password = $this->form_validation->set_value('password');
    		
    		//检查邮箱是否存在
    		$attributes = array();
    		$attributes['email'] = $email;
    		$info_ = $this->Admin_model->getInfoByAttribute($attributes);
    		if ($info_)
    		{
    			$this->session->set_flashdata('olddata', $this->input->post());
    			$this->session->set_flashdata('error', '用户已存在');
    			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' user exist '.serialize(array($attributes, $this->log_data)));
    			return redirect('admin/add');
    		}
    		
    		//插入数据
    		$attributes = array();
    		$attributes['email'] = $email;
    		$attributes['password'] = $password;
    		$attributes['create_time'] = date('Y-m-d H:i:s');
    		$admin_id = $this->Admin_model->insertInfo($attributes);
    		
    		$this->session->set_flashdata('success', lang('form_add_succ'));
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($attributes, $this->log_data)));
    		
    		return redirect('admin/edit?admin_id='.$admin_id);
    	}
    	
    	
    	
    	//临时数据
    	$olddata = $this->session->flashdata('olddata');
    	
    	return $this->load->view("admin/add", array(
    			'olddata' => $olddata,
    	));
    }
    
	public function edit()
	{
		$admin_id = $this->input->get('admin_id', true);
		if (!$admin_id)
		{
			$this->session->set_flashdata('error', '请传递管理员ID');
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' admin_id empty '.serialize(array($this->log_data)));
			return redirect('admin/index');
		}
		
		$info = $this->Admin_model->getInfo($admin_id);
		if (empty($info))
		{
			$this->session->set_flashdata('error', '管理员不存在');
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' admin_id notexist '.serialize(array($admin_id, $this->log_data)));
			return redirect('admin/index');
		}
		
		//提交表单
		if (is_post())
		{
			$this->form_validation->set_rules('email', '邮箱', valid_email_rule(true));
    		$this->form_validation->set_rules('password', '密码', valid_string_rule(1, 30));
    		if (!$this->form_validation->run())
    		{
    			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    			$this->session->set_flashdata('olddata', $this->input->post());
    			$this->session->set_flashdata('error', '您填写的表单有误, 请检查'.$this->form_validation->error_string());
    			return redirect('admin/edit');
    		}
    		$email = $this->form_validation->set_value('email');
    		$password = $this->form_validation->set_value('password');
		
			//检查邮箱是否存在
			$attributes = array();
			$attributes['email'] = $email;
			$info_ = $this->Admin_model->getInfoByAttribute($attributes);
			if ($info_)
			{
				if ($info_['admin_id'] != $admin_id)
				{
					$this->session->set_flashdata('olddata', $this->input->post());
					$this->session->set_flashdata('error', '用户已存在');
					log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' user exist '.serialize(array($attributes, $this->log_data)));
					return redirect('admin/edit');
				}
			}
			
			//插入数据
			$attributes = array();
			$email && $attributes['email'] = $email;
			$password && $attributes['password'] = $password;
			$result = $this->Admin_model->updateInfo($admin_id, $attributes);
			
			$this->session->set_flashdata('success', lang('form_edit_succ'));
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($admin_id, $attributes, $this->log_data)));
			return redirect('admin/edit?admin_id='.$admin_id);
		}
		
		//临时数据
		$olddata = $this->session->flashdata('olddata');
		
		return $this->load->view("admin/edit", array(
				'olddata' => $olddata,
				'info' => $info
		));
	}
	
}
?>