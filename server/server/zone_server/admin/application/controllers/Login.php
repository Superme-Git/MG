<?php
/**
 * 登录页
 * 
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 */
class Login extends MY_Controller
{
    function __construct()
    {
        parent::__construct();
        $this->load->model("Admin_model");
        $this->Admin_model->connect_group('default');
    }
    
    public function index()
    {
    	if (get_admininfo())
		{
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' logined '.serialize(array(get_admininfo())));
			return redirect('/login/index');
		}
	
		//提交表单
		if (is_post())
		{
			$this->form_validation->set_rules('email', 'email', valid_email_rule(true));
			$this->form_validation->set_rules('password', 'password', valid_string_rule(true));
			$this->form_validation->set_rules('remember', 'remember', valid_int_rule());
			if (!$this->form_validation->run())
			{
				$message = $this->form_validation->error_string();
				$this->session->set_flashdata('error', $message);
				$this->session->set_flashdata('olddata', $this->input->post());
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string(), $this->input->post())));
				return redirect('/login/index');
			}
			$email = $this->form_validation->set_value('email');
			$password = $this->form_validation->set_value('password');
			$remember = $this->form_validation->set_value('remember');
			 
			//根据邮件获取用户信息
			$adminInfo = $this->Admin_model->getInfoByAttribute(array('email' => $email));
			if (!$adminInfo)
			{
				$this->session->set_flashdata('error', '管理员不存在');
				$this->session->set_flashdata('olddata', $this->input->post());
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' email notexist  '.serialize(array($this->input->post())));
				return redirect('/login/index');
			}
			 
			 
			//校验密码
			if($adminInfo["password"] != $password)
			{
				$this->session->set_flashdata('error', '密码错误');
				$this->session->set_flashdata('olddata', $this->input->post());
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' password error '.serialize(array($this->input->post())));
				return redirect('/login/index');
			}
			 
			/******登录成功******/
				
			//设置session
			$dataArray = array(
				"admin_id"=>$adminInfo["admin_id"],
				"email"=>$adminInfo["email"],
			    "role"=>$adminInfo["role"],
				"create_time"=>$adminInfo["create_time"],
			);
				
			//获取用户角色
// 			$roles = $this->Admin_model->getUserRoles($adminInfo["admin_id"]);
// 			$dataArray['roles'] = $roles;
	
// 			//获取用户的权限
// 			$accesses = array();
// 			$accessList = $this->Admin_model->getUserAccesses($adminInfo["admin_id"]);
// 			if ($accessList)
// 			{
// 				foreach ($accessList as $item)
// 				{
// 					$accesses[] = $item['path'];
// 				}
// 			}
// 			$dataArray['accesses'] = $accesses;
			
			//保存到session
			$this->session->set_userdata(array('admin_info' => $dataArray));
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' succ '.serialize(array($this->input->post())));
				
			return redirect('serverlist');
		}
		
		$olddata = $this->session->flashdata('olddata');
		
		return $this->load->view("/login", array(
				'olddata' => $olddata
		));
	}
    
    //退出
    public function logout()
    {
		$sessionData = array(
				'admin_info' => "",
		);
		$this->session->set_userdata($sessionData);
		
		return redirect('/login/index');
    }
}
?>