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
        
        log_message('debug', ' ------------ request ----------------- Controller start '.json_encode_cn($this->log_data));
        
        //-----测试开, 非测试关!!!
        //form校验
        $this->form_validation->set_error_delimiters('', '');
        $this->form_validation->set_data($_REQUEST);
        //----------------------
        
        //form校验去除附加标签
        $this->form_validation->set_error_delimiters('', '');
        
        
        //设置应用标志, 用于缓存前缀
        setSite('weibo');
        
        //设置站点
        $this->check_site();
    }
  	
    
    private function check_site()
    {
    	//表单规则
    	$this->form_validation->set_rules('serverkey', '区服', valid_string_rule(1, 30, true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' serverkey_fail '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'serverkey_fail', $this->form_validation->error_string());
    	}
    	$serverkey = $this->form_validation->set_value('serverkey');
    	
    	//获取区服对应数据库连接信息
    	$this->load->model('Serverlist_model');
    	$where = array();
    	$where['serverkey'] = $serverkey;
    	$where['status'] = 0;
    	$this->Serverlist_model->connect_config('serverlist');
    	$serverlist_info = $this->Serverlist_model->getInfoByAttribute($where);
    	if (!$serverlist_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' server_notexist '.serialize(array($where, $this->log_data)));
    		return response(null, 'server_notexist', 'server_notexist');
    	}
    	$this->Serverlist_model->close();
    	 
    	//设置区服标志
    	setSite($serverkey);
    	
    	//区服信息
    	$this->site_info = $serverlist_info;
    }
    
    
    
}
?>