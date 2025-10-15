<?php
/**
 * Bbs
 * 朋友圈业务接口
 * 
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.15
 */
class Bbs extends MY_Controller
{
    public function __construct()
    {
        parent::__construct();
    }
    
    /**
     * 发布一条微博信息
     */
    public function create()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('to_roleid', '目标角色', valid_int_rule(true));
    	$this->form_validation->set_rules('content', '动态内容', valid_text_rule(true));
    	$this->form_validation->set_rules('reply_id', '回复评论', valid_int_rule());
    	$this->form_validation->set_rules('gift_id', '礼物类型', valid_int_rule());
    	$this->form_validation->set_rules('gift_count', '礼物数', valid_int_rule());
    	$this->form_validation->set_rules('is_popularity', '增加人气', valid_int_rule());
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$to_roleid = $this->form_validation->set_value('to_roleid');
    	$content = $this->form_validation->set_value('content');
    	$reply_id = $this->form_validation->set_value('reply_id');
    	$gift_id = $this->form_validation->set_value('gift_id');
    	$gift_count = $this->form_validation->set_value('gift_count');
    	$is_popularity = $this->form_validation->set_value('is_popularity');
    	
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	 
    	//引入模型
    	$this->load->model(array('Bbs_model', 'Bbs_attribute_model', 'Bbs_gift_model'));
    	$this->Bbs_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Bbs_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Bbs_gift_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	$reply_roleid = 0;
    	if ($reply_id)
    	{
    		$reply_info = $this->Bbs_model->getInfo($reply_id);
    		if (!$reply_info)
    		{
    			$reply_id = 0;
    		}
    		else
    		{
    			$reply_roleid = $reply_info['roleid'];
    		}
    	}
    	
    	//保存微博表
    	$bbs_id = $this->Bbs_model->insert_new($roleid, $to_roleid, $reply_id, $reply_roleid, $gift_id, $gift_count);
    	if (!$bbs_id)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' system_error,Status_model insertInfo '.serialize(array($this->log_data)));
    		return response(null, 'system_error', 'system_error');
    	}
    	$this->Bbs_attribute_model->insert_new($bbs_id, $content);
    	
    	if ($gift_id && $gift_count)
    	{
    	    $this->Bbs_gift_model->insert_new($roleid, $to_roleid, $gift_id, $gift_count, $content);
    	}
    	
    	
    	//执行发微博业务
    	$this->load->model(array('Role_attribute_model'));
    	//$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	
    	//微博的评论数+1
    	$attribute = array();
    	$is_popularity && $attribute['popularity_count'] = 1;
    	$gift_id && $attribute['gift_count'] = 1;
    	$this->Role_attribute_model->increment($to_roleid, $attribute);
    	 
    	//返回数据
    	$info = $this->Bbs_model->getInfo($bbs_id);
    	if(!$info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' bbs_notexist '.serialize(array($bbs_id, $this->log_data)));
    		return response(null, 'bbs_notexist', 'bbs_notexist');
    	}
    	$attribute = $this->Bbs_attribute_model->getInfo($bbs_id);
    	if ($attribute)
    	{
    		$info = array_merge($info, $attribute);
    	}
    	$info['name'] = $role_info['name'];
    	$info['avatar'] = $role_info['avatar'];
    	$info['level'] = $role_info['level'];
    	
    	//被回复人信息
    	$info['reply_name'] = '';
    	$info['reply_avatar'] = '';
    	$info['reply_level'] = 0;
    	if ($info['reply_roleid'])
    	{
    		$reply_role_info = $this->Role_model->role_isexist($info['reply_roleid']);
    		$info['reply_name'] = $reply_role_info['name'];
    		$info['reply_avatar'] = $reply_role_info['avatar'];
    		$info['reply_level'] = $reply_role_info['level'];
    	}
    	
    	log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.serialize(array($info, $this->log_data)));
    	return response($info);
    }
    
    
    /**
     * 删除一条留言
     */
    public function destroy()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('bbs_id', '评论', valid_int_rule(true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$bbs_id = $this->form_validation->set_value('bbs_id');
    	
    	
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	//执行发微博业务
    	$this->load->model(array('Bbs_model'));
    	//$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Bbs_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	
    	$bbs_info = $this->Bbs_model->getInfo($bbs_id);
    	
    	if ($bbs_info)
    	{
    		if (!$bbs_info['status'])
    		{
    			$attrs = array();
    			$attrs['status'] = 1;
    			$this->Bbs_model->updateInfo($bbs_id, $attrs);
    		}
    	}
    	
    	return response(true);
    }
    
    
    //根据用户获取留言列表
    public function bbs_timeline()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('bbs_id', '页码', valid_int_rule());
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$bbs_id = (int)$this->form_validation->set_value('bbs_id');
    	
    	$bbs_id < 1 && $bbs_id = 0;
    	$count = 10;
    	
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	//引入模型
    	$this->load->model(array('Bbs_model', 'Bbs_attribute_model'));
    	$this->Bbs_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Bbs_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	//获取留言列表
    	$where = array();
    	$where['to_roleid'] = $roleid;
    	$where['status'] = 0;
    	$bbs_id && $where['less']['bbs_id'] = $bbs_id;
    	$order = array('create_time' => 'desc');
    	$cache_time = $bbs_id ? 300 : 0;
    	$bbs_list = $this->Bbs_model->getList($where, $order, 0, $count, null, $cache_time, false);
    	
    	if ($bbs_list)
    	{
    		//因列表的数量有限, 用foreach
    		foreach ($bbs_list as $k => $item)
    		{
    		    if ($item['status'])
    		    {
    		        $bbs_list[$k] = null;
    		        unset($bbs_list[$k]);
    		        log_message('error', __CLASS__.' '.__FUNCTION__.' had deleted '.serialize(array($item, $this->log_data)));
    		        continue;
    		    }
    		    
    			//查询并合并属性表信息
    			$item_attribute = $this->Bbs_attribute_model->getInfo($item['bbs_id']);
    			$bbs_list[$k] = array_merge($item, $item_attribute);
    			
    			//查询用户昵称信息
    			$item_user = $this->Role_model->getInfo($item['roleid']);
    			$bbs_list[$k]['name'] = $item_user['name'];
    			$bbs_list[$k]['avatar'] = $item_user['avatar'];
    			$bbs_list[$k]['level'] = $item_user['level'];
    			
    			//被回复人信息
    			$bbs_list[$k]['reply_name'] = '';
    			$bbs_list[$k]['reply_avatar'] = '';
    			$bbs_list[$k]['reply_level'] = 0;
    			if ($item['reply_roleid'])
    			{
    			    $reply_role_info = $this->Role_model->getInfo($item['reply_roleid']);
    			    $bbs_list[$k]['reply_name'] = $reply_role_info['name'];
    			    $bbs_list[$k]['reply_avatar'] = $reply_role_info['avatar'];
    			    $bbs_list[$k]['reply_level'] = $reply_role_info['level'];
    			}
    		}
    	}
    	
    	return response($bbs_list);
    }
    
    //增加人气
    public function add_popularity()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '当前角色', valid_int_rule(true));
    	$this->form_validation->set_rules('to_roleid', '目标角色', valid_int_rule(true));
    	$this->form_validation->set_rules('is_get', '获得回馈', valid_int_rule());
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$to_roleid = (int)$this->form_validation->set_value('to_roleid');
    	$is_get = (int)$this->form_validation->set_value('is_get');
    
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	$to_role_info = $this->Role_model->role_isexist($to_roleid);
    	if (!$to_role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    
    	//引入模型
    	$this->load->model(array('Bbs_popularity_model'));
    	$this->Bbs_popularity_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	//获取留言列表, 此为查表方式, 可改进为redis方式
    	$where = array();
    	$where['roleid'] = $roleid;
    	$where['to_roleid'] = $to_roleid;
    	$where['status'] = 0;
    	$bbs_popularity_list = $this->Bbs_popularity_model->getList($where, array('create_time' => 'desc'), 0, 1, null, 0, false);
    	
    	if ($bbs_popularity_list)
    	{
    	    //一天之内踩一次
    		if ($bbs_popularity_list[0]['create_time'] > strtotime(date('Y-m-d', time())))
    		{
    			log_message('error', __CLASS__.' '.__FUNCTION__.' bbs_addpopularity_repeat '.serialize(array($where, $this->log_data)));
    			return response(null, 'bbs_addpopularity_repeat', 'bbs_addpopularity_repeat');
    		}
    	}
    	
    	$data = array();
    	$data['roleid'] = $roleid;
    	$data['to_roleid'] = $to_roleid;
    	$data['status'] = 0;
    	$data['is_get'] = $is_get;
    	$data['create_time'] = time();
    	$this->Bbs_popularity_model->insertInfo($data);
    	
    	log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.serialize(array($where, $this->log_data)));
    	return response(true);
    }
    
    
    public function popularity_timeline()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('bbs_popularity_id', '页码', valid_int_rule());
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$bbs_popularity_id = (int)$this->form_validation->set_value('bbs_popularity_id');
    	 
    	$bbs_popularity_id < 1 && $bbs_popularity_id = 0;
    	$count = 10;
    	 
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	 
    	//引入模型
    	$this->load->model(array('Bbs_popularity_model'));
    	$this->Bbs_popularity_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	//获取留言列表
    	$where = array();
    	$where['to_roleid'] = $roleid;
    	$where['status'] = 0;
    	$bbs_popularity_id && $where['less']['bbs_popularity_id'] = $bbs_popularity_id;
    	$order = array('create_time' => 'desc');
    	$cache_time = $bbs_popularity_id ? 300 : 0;
    	$bbs_popularity_list = $this->Bbs_popularity_model->getList($where, $order, 0, $count, null, $cache_time, false);
    	 
    	if ($bbs_popularity_list)
    	{
    		//因列表的数量有限, 用foreach
    		foreach ($bbs_popularity_list as $k => $item)
    		{
    		    if ($item['status'])
    		    {
    		        $bbs_popularity_list[$k] = null;
    		        unset($bbs_popularity_list[$k]);
    		        log_message('error', __CLASS__.' '.__FUNCTION__.' had deleted '.serialize(array($item, $this->log_data)));
    		        continue;
    		    }
    		    
    			//查询用户昵称信息
    			$item_user = $this->Role_model->getInfo($item['roleid']);
    			$bbs_popularity_list[$k]['name'] = $item_user['name'];
    			$bbs_popularity_list[$k]['avatar'] = $item_user['avatar'];
    			$bbs_popularity_list[$k]['level'] = $item_user['level'];
    		}
    	}
    	 
    	return response($bbs_popularity_list);
    }
    
    
    
    public function gift_timeline()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('bbs_gift_id', '页码', valid_int_rule());
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$bbs_gift_id = (int)$this->form_validation->set_value('bbs_gift_id');
    	
    	$bbs_gift_id < 1 && $bbs_gift_id = 0;
    	$count = 10;
    	
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	//引入模型
    	$this->load->model(array('Bbs_gift_model'));
    	$this->Bbs_gift_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	//获取留言列表
    	$where = array();
    	$where['to_roleid'] = $roleid;
    	$where['status'] = 0;
    	$bbs_gift_id && $where['less']['bbs_gift_id'] = $bbs_gift_id;
    	$order = array('create_time' => 'desc');
    	$cache_time = $bbs_gift_id ? 300 : 0;
    	$bbs_gift_list = $this->Bbs_gift_model->getList($where, $order, 0, $count, null, $cache_time, false);
    	
    	if ($bbs_gift_list)
    	{
    		//因列表的数量有限, 用foreach
    		foreach ($bbs_gift_list as $k => $item)
    		{
    		    if ($item['status'])
    		    {
    		        $bbs_gift_list[$k] = null;
    		        unset($bbs_gift_list[$k]);
    		        log_message('error', __CLASS__.' '.__FUNCTION__.' had deleted '.serialize(array($item, $this->log_data)));
    		        continue;
    		    }
    		    
    			//查询用户昵称信息
    			$item_user = $this->Role_model->getInfo($item['roleid']);
    			$bbs_gift_list[$k]['name'] = $item_user['name'];
    			$bbs_gift_list[$k]['avatar'] = $item_user['avatar'];
    			$bbs_gift_list[$k]['level'] = $item_user['level'];
    		}
    	}
    	
    	return response($bbs_gift_list);
    }
    
    
}