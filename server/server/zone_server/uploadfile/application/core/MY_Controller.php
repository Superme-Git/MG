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
	protected $site_info = null;
	
    public function __construct()
    {
        parent::__construct();
        
        $this->log_data['ip'] = $this->input->ip_address();
        $this->log_data['uri'] = $this->uri->uri_string();
        $this->log_data['referrer'] = $this->agent->referrer();
        $this->log_data['agent_string'] = $this->agent->agent_string();
        
        if ($_REQUEST)
        {
            foreach ($_REQUEST as $k => $v)
            {
                $this->log_data['request'][$k] = strlen($v) > 500 ? substr($v, 0, 500) .'>>500' : $v;
            }
        }
        
        log_message('error', ' ------------ request ----------------- Controller start '.json_encode_cn($this->log_data));
        
    }
  	
    
    
}
?>