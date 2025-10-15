<?php
/**
 * 控制器继承类
 * 
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 *
 */
include APPPATH . '/helpers/admin_helper.php';
class MY_Controller extends CI_Controller
{
	protected $token_object = null;
	protected $log_data = array();
	
    public function __construct()
    {
        parent::__construct();
        
        $this->log_data['ip'] = $this->input->ip_address();
        $this->log_data['uri'] = $this->uri->uri_string();
        $this->log_data['request'] = $this->input->get(null, true);
        $this->log_data['referrer'] = $this->agent->referrer();
        $this->log_data['agent_string'] = $this->agent->agent_string();
        log_message('error', ' -- request -- Controller start '.json_encode_cn($this->log_data));
        
        //请求的路由
        $path = '/'.$this->router->fetch_directory() . $this->router->fetch_class().'/'.$this->router->fetch_method();
        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' router '.serialize(array($path)));
        
        //判断登录状态
        $this->check_login($path);
        
    }
  	
    
    protected function check_login($path)
    {
    	if (!in_array($path, array('/index/login')))
    	{
    		if (!check_adminlogin())
    		{
    			if ($this->input->is_ajax_request())
    			{
    				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' false ajax '.serialize(array($path)));
    				response(null, 'user_nologin', lang('user_nologin'));
    				exit();
    			}
    			else
    			{
    				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' false  '.serialize(array($path)));
    				redirect('/index/login');
    				exit();
    			}
    		}
    	}
    	
    }
    
}
?>