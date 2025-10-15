<?php
/**
 * 控制器继承类
 * 
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 *
 */
class MY_Controller extends CI_Controller
{
	protected $token_object = null;
	protected $log_data = array();
	
    public function __construct()
    {
        parent::__construct();
        
        $this->log_data['ip'] = $this->input->ip_address();
        $this->log_data['uri'] = $this->uri->uri_string();
        $this->log_data['request'] = $_REQUEST;
        $this->log_data['referrer'] = $this->agent->referrer();
        $this->log_data['agent_string'] = $this->agent->agent_string();
        log_message('error', ' -- request -- Controller start '.json_encode_cn($this->log_data));
        
        
        //设置应用标志, 用于缓存前缀
        setSite('useravatar');
        
        $this->form_validation->set_data($_REQUEST);
        
    }
  
    
}
?>