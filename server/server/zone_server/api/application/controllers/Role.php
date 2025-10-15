<?php
/**
 * User
 * 朋友圈业务接口
 * 
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.15
 */
class Role extends MY_Controller
{
    public function __construct()
    {
        parent::__construct();
    }
    
    /**
     * 获取用户信息
     * 
     * http://mt3.pengyouquan001.locojoy.com:8810/role/show?serverkey=1101011005&roleid=36553709&is_self=0
     * 
     * 留言板上方三数值是取自服务器端的值, 并非role_attribute的字段
     * 
     */
    public function show()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_string_rule(1, 30, true));
    	$this->form_validation->set_rules('is_self', 'is_self', valid_int_rule());
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$is_self = $this->form_validation->set_value('is_self');
    	
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	//判断用户是否在本地存在
    	$this->load->model('Role_attribute_model');
    	$this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	//检测是否在本地, 不在则创建
    	$role_attribute_info = $this->Role_attribute_model->get_withrepair($roleid);
    	
    	//本人请求接口, 需要显示已上传头像
    	if ($is_self)
    	{
    	   
    	}
    	//头像审核
    	else if (isset($role_attribute_info['avatar_verify']) && $role_attribute_info['avatar_verify'] != 1)
    	{
    		$role_attribute_info['avatar_url'] = '';
    	}
    	
    	$role_info = array_merge($role_info, $role_attribute_info);
    	
    	return response($role_info);
    }
    
    public function update_signature()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_string_rule(1, 30, true));
    	$this->form_validation->set_rules('signature', '签名内容', valid_string_rule(1, 120, true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$signature = $this->form_validation->set_value('signature');
    	 
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	//判断用户是否在本地存在
    	$this->load->model('Role_attribute_model');
    	$this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	//检测是否在本地, 不在则创建
    	$role_attribute_info = $this->Role_attribute_model->get_withrepair($roleid);
    	
    	//更新签名
    	$attribute = array();
    	$attribute['signature'] = $signature;
    	$this->Role_attribute_model->updateInfo($roleid, $attribute);
    	
    	return response(true);
    }
    
    public function upload_avatar()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	
    	//判断用户是否真实存在
		$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	//判断用户是否在本地存在
    	$this->load->model('Role_attribute_model');
    	$this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	//检测是否在本地, 不在则创建
    	$role_attribute_info = $this->Role_attribute_model->get_withrepair($roleid);
    	
    	//处理图片
    	$img_type = $this->input->post("img_type", true);
    	$img_data = $this->input->post("img_data", false);//勿设置第二参数true, xss会屏蔽掉data:image_apng;base64,
    	if (!$img_type || !$img_data)
    	{
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' params_error error '.json_encode(array($img_type)));
    	    return response('', 'params_error', 'params_error');
    	}
    	
    	$img_data = base64_decode_($img_data);
    	
    	//请求接口
		include APPPATH.'/third_party/uploadfile/Uploadfile.php';
		$uploadfile = new Uploadfile();
		$result = $uploadfile->image($img_type, $img_data);
		if ($result['errno'])
		{
		    log_message('error', __CLASS__.' '.__FUNCTION__.' upload image error '.serialize(array($result, $this->log_data)));
		    return response(null, $result['errno'], $result['message']);
		}
		$img_url = $result['data'];
		log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($img_url, $result, $this->log_data)));
    	 
    	//更新签名
    	$attribute = array();
    	$attribute['avatar_url'] = $img_url;
    	$attribute['avatar_verify'] = 0;
    	$this->Role_attribute_model->updateInfo($roleid, $attribute);
    	 
    	log_message('debug', __CLASS__.' '.__FUNCTION__.' upload_path succ '.json_encode(array($img_url, $this->log_data)));
    	return response($img_url);
    }
    
    public function del_avatar()
    {
        //表单规则
        $this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
        if (!$this->form_validation->run())
        {
            log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
            return response(null, 'params_error', $this->form_validation->error_string());
        }
        $roleid = $this->form_validation->set_value('roleid');
         
        //判断用户是否真实存在
        $this->load->model(array('Role_model'));
        $this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
         
        $role_info = $this->Role_model->role_isexist($roleid);
        if (!$role_info)
        {
            log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
            return response(null, 'role_notexist', 'role_notexist');
        }
         
        //判断用户是否在本地存在
        $this->load->model('Role_attribute_model');
        $this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
         
        //检测是否在本地, 不在则创建
        $role_attribute_info = $this->Role_attribute_model->get_withrepair($roleid);
        
        //更新签名
        $attribute = array();
        $attribute['avatar_url'] = '';
        $attribute['avatar_verify'] = 0;
        $this->Role_attribute_model->updateInfo($roleid, $attribute);
        
        return response(true);
    }
    
    public function update_place()
    {
        //表单规则
        $this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
        $this->form_validation->set_rules('place', '角色', valid_string_rule(1, 60, true));
        $this->form_validation->set_rules('longitude', '经度', valid_string_rule(1, 255, true));
        $this->form_validation->set_rules('latitude', '纬度', valid_string_rule(1, 255, true));
        if (!$this->form_validation->run())
        {
            log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
            return response(null, 'params_error', $this->form_validation->error_string());
        }
        $roleid = $this->form_validation->set_value('roleid');
        $place = $this->form_validation->set_value('place');
        $longitude = $this->form_validation->set_value('longitude');
        $latitude = $this->form_validation->set_value('latitude');
        
        //判断用户是否真实存在
        $this->load->model(array('Role_model'));
        $this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
         
        $role_info = $this->Role_model->role_isexist($roleid);
        if (!$role_info)
        {
            log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
            return response(null, 'role_notexist', 'role_notexist');
        }
         
        //判断用户是否在本地存在
        $this->load->model('Role_attribute_model');
        $this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    
        //检测是否在本地, 不在则创建
        $role_attribute_info = $this->Role_attribute_model->get_withrepair($roleid);
         
        //更新签名
        $attribute = array();
        $attribute['place'] = $place;
        $attribute['longitude'] = $longitude;
        $attribute['latitude'] = $latitude;
        $this->Role_attribute_model->updateInfo($roleid, $attribute);
         
        return response(true);
    }
    
    public function upload_sound()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	 
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	 
    	//判断用户是否在本地存在
    	$this->load->model('Role_attribute_model');
    	$this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	//检测是否在本地, 不在则创建
    	$role_attribute_info = $this->Role_attribute_model->get_withrepair($roleid);
    	
    	//处理图片
    	$sound_type = $this->input->post("sound_type", true);
    	$sound_data = $this->input->post("sound_data", false);//勿设置第二参数true, xss会屏蔽掉data:image_apng;base64,
    	if (!$sound_type || !$sound_data)
    	{
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' params_error error '.json_encode(array($sound_type)));
    	    return response('', 'params_error', 'params_error');
    	}
    	
    	$sound_data = base64_decode_($sound_data);
    	
    	//调用接口
    	include APPPATH.'/third_party/uploadfile/Uploadfile.php';
    	$uploadfile = new Uploadfile();
    	$result = $uploadfile->sound($sound_type, $sound_data);
    	if ($result['errno'])
    	{
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' upload sound error '.serialize(array($result, $this->log_data)));
    	    return response(null, $result['errno'], $result['message']);
    	}
    	$sound_url = $result['data'];
    	log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($sound_url, $this->log_data)));
    	 
    	//更新签名
    	$attribute = array();
    	$attribute['sound_url'] = $sound_url;
    	$this->Role_attribute_model->updateInfo($roleid, $attribute);
    	
    	log_message('debug', __CLASS__.' '.__FUNCTION__.' upload_path succ '.json_encode(array($sound_url, $this->log_data)));
    	return response($sound_url);
    }
    
    public function del_sound()
    {
        //表单规则
        $this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
        if (!$this->form_validation->run())
        {
            log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
            return response(null, 'params_error', $this->form_validation->error_string());
        }
        $roleid = $this->form_validation->set_value('roleid');
         
        //判断用户是否真实存在
        $this->load->model(array('Role_model'));
        $this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
         
        $role_info = $this->Role_model->role_isexist($roleid);
        if (!$role_info)
        {
            log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
            return response(null, 'role_notexist', 'role_notexist');
        }
         
        //判断用户是否在本地存在
        $this->load->model('Role_attribute_model');
        $this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
         
        //检测是否在本地, 不在则创建
        $role_attribute_info = $this->Role_attribute_model->get_withrepair($roleid);
    
        //更新签名
        $attribute = array();
        $attribute['sound_url'] = '';
        //$attribute['avatar_verify'] = 0;
        $this->Role_attribute_model->updateInfo($roleid, $attribute);
    
        return response(true);
    }
    
    public function get_unread()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	
    	//引入角色关系模型
		$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	//引入用户模型
    	$this->load->model('Role_attribute_model');
    	$this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	//检测是否在本地, 不在则创建
    	$role_attribute_info = $this->Role_attribute_model->get_withrepair($roleid);
    	
    	return response($role_attribute_info['msg_count']);
    }
    
    /**
     * 清除统计信息
     * 包括, 新消息
     */
    public function clean_unread()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	
    	//引入角色关系模型
    	$this->load->model('Role_model');
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	//判断用户是否真实存在
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	//引入用户模型
    	$this->load->model('Role_attribute_model');
    	$this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	//检测是否在本地, 不在则创建
    	$role_attribute_info = $this->Role_attribute_model->get_withrepair($roleid);
    	
    	//清空用户消息数
    	$attribute = array();
    	$attribute['msg_count'] = 0;
    	$this->Role_attribute_model->updateInfo($roleid, $attribute);
    	
    	return response(true);
    	 
    }
}
?>