<?php
/**
 * Statuses
 * 朋友圈业务接口
 * 
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.15
 */
class Status extends MY_Controller
{
    public function __construct()
    {
        parent::__construct();
        
    }
    
    /**
     * 发布一条微博信息
     */
    public function update()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('content', '动态内容', valid_text_rule(true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$content = $this->form_validation->set_value('content');
    	
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model', 'Role_relation_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Role_relation_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	//处理图片
    	$img_url = '';
    	$img_type = $this->input->post("img_type", true);
    	$img_data = $this->input->post("img_data", false);//勿设置第二参数true, xss会屏蔽掉data:image_apng;base64,
    	if ($img_type && $img_data)
    	{
    	    $img_data = base64_decode_($img_data);
    	    
    		//保存图片到硬盘
    		include APPPATH.'/third_party/uploadfile/Uploadfile.php';
    		$uploadfile = new Uploadfile();
    		$result = $uploadfile->image($img_type, $img_data);
    		if ($result['errno'])
    		{
    		    log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($result, $this->log_data)));
    		    return response(null, $result['errno'], $result['message']);
    		}
    		$img_url = $result['data'];
    		log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($img_url, $this->log_data)));
    	}
    	
    	//执行发微博业务
    	$this->load->model(array('Status_model', 'Status_attribute_model'));
    	$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	
    	//保存微博表
    	$status_id = $this->Status_model->insert_new($roleid);
    	if (!$status_id)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' system_error,Status_model insertInfo '.serialize(array($this->log_data)));
    		return response(null, 'system_error', 'system_error');
    	}
    	
    	//存入微博属性表<E e="102"></E>
    	$this->Status_attribute_model->insert_new($status_id, $content, $img_url);
    	
    	//引入用户模型
    	$this->load->model('Role_attribute_model');
    	$this->Role_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	
    	//发布新微博后的处理业务
		//查询好友列表
		$friend_arr = $this->Role_relation_model->get_friendids($roleid);
		if ($friend_arr)
		{
			//在线用户key
			$online_arr = $this->Role_attribute_model->get_online();
			
			if ($online_arr)
			{
				//对在线的好友们发一条提示
				//计算数组交集
				$online_friend_arr = array_intersect($friend_arr, $online_arr);
				$online_friend_arr && $this->Role_attribute_model->notice_online($online_friend_arr, $status_id);
				 
				//对不在线的好友们增加新消息数
				//$offline_friend_arr = array_diff($friend_arr, $online_arr);
				//$offline_friend_arr && $this->Role_attribute_model->notice_offline($offline_friend_arr);
				
			}
			else
			{
				//对不在线的好友们增加新消息数
				$this->Role_attribute_model->notice_offline($friend_arr);
			}
		}
    	
    	
    	//返回数据
    	$info = $this->Status_model->getInfo($status_id);
    	$attribute = $this->Status_attribute_model->getInfo($status_id);
    	if ($attribute)
    	{
    		$info = array_merge($info, $attribute);
    	}
    	$info['name'] = $role_info['name'];
    	$info['avatar'] = $role_info['avatar'];
    	$info['level'] = $role_info['level'];
    	
    	$info['favorites'] = array();
    	$info['comments'] = array();
    	
    	
    	log_message('debug', __CLASS__.' '.__FUNCTION__.' succ '.serialize(array($info, $this->log_data)));
    	return response($info);
    }
    
    /**
     * 获取单个动态信息
     */
    public function show()
    {
    	//表单规则
    	$this->form_validation->set_rules('status_id', '动态', valid_int_rule(true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$status_id = $this->form_validation->set_value('status_id');
    	
    	
    	//执行发微博业务
    	$this->load->model(array('Status_model', 'Status_attribute_model', 'Status_favorite_model', 'Status_comment_model'));
    	$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_favorite_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_comment_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	
    	//获取微博索引表信息
    	$info = $this->Status_model->getInfo($status_id);
    	if (!$info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' status_notexist,Status_model getInfo '.serialize(array($status_id, $this->log_data)));
    		return response(null, 'status_notexist', 'status_notexist');
    	}
    	
    	$attribute = $this->Status_attribute_model->getInfo($status_id);
    	if ($attribute)
    	{
    		$info = array_merge($info, $attribute);
    	}
    	
    	//判断用户是否真实存在
    	$this->load->model(array('Role_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	$role_info = $this->Role_model->role_isexist($info['roleid']);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($info, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	$info['name'] = $role_info['name'];
    	$info['avatar'] = $role_info['avatar'];
    	$info['level'] = $role_info['level'];
    	
    	//查询点赞记录
    	$info['favorites'] = array();
    	if ($attribute['favorite_count'] > 0)
    	{
    		$where = array();
    		$where['status_id'] = $status_id;
    		$where['status'] = 0;
    		$order = array('create_time' => 'desc');
    		$favorite_list = $this->Status_favorite_model->getList($where, $order, 0, 20, null, 0, false);
    		if ($favorite_list)
    		{
    			foreach ($favorite_list as $k_ => $item_)
    			{
    				$role_info_ = $this->Role_model->getInfo($item_['roleid']);
    				$favorite_list[$k_]['name'] = $role_info_['name'];
    				$favorite_list[$k_]['avatar'] = $role_info_['avatar'];
    				$favorite_list[$k_]['level'] = $role_info_['level'];
    			}
    		}
    	
    		$info['favorites'] = $favorite_list;
    	}
    	 
    	//查询评论列表
    	$info['comments'] = array();
    	if ($attribute['comment_count'] > 0)
    	{
    	
    		$where = array();
    		$where['status_id'] = $status_id;
    		$where['status'] = 0;
    		$order = array('create_time' => 'desc');
    		$comment_list = $this->Status_comment_model->getList($where, $order, 0, 20, null, 0, false);
    		if ($comment_list)
    		{
    			foreach ($comment_list as $k_ => $item_)
    			{
    				$role_info_ = $this->Role_model->getInfo($item_['roleid']);
    				$comment_list[$k_]['name'] = $role_info_['name'];
    				$comment_list[$k_]['avatar'] = $role_info_['avatar'];
    				$comment_list[$k_]['level'] = $role_info_['level'];
    				
    				//被回复人信息
    				$comment_list[$k_]['reply_name'] = '';
    				$comment_list[$k_]['reply_avatar'] = '';
    				$comment_list[$k_]['reply_level'] = 0;
    				if ($item_['reply_roleid'])
    				{
    				    $reply_role_info = $this->Role_model->getInfo($item_['reply_roleid']);
    				    $comment_list[$k_]['reply_name'] = $reply_role_info['name'];
    				    $comment_list[$k_]['reply_avatar'] = $reply_role_info['avatar'];
    				    $comment_list[$k_]['reply_level'] = $reply_role_info['level'];
    				}
    			}
    		}
    		 
    		$info['comments'] = $comment_list;
    	}
    	
    	
    	log_message('debug', __CLASS__.' '.__FUNCTION__.' succ '.serialize(array($info, $this->log_data)));
    	return response($info);
    }
    
    //删除动态
    public function delete()
    {
        //表单规则
        $this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('status_id', '动态', valid_int_rule(true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$status_id = $this->form_validation->set_value('status_id');
    	
    	
    	//执行发微博业务
    	$this->load->model(array('Status_model', 'Status_attribute_model', 'Status_favorite_model', 'Status_comment_model'));
    	$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_favorite_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_comment_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	
    	//获取微博索引表信息
    	$info = $this->Status_model->getInfo($status_id);
    	if (!$info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' status_notexist,Status_model getInfo '.serialize(array($status_id, $this->log_data)));
    		return response(null, 'status_notexist', 'status_notexist');
    	}
    	
    	if ($info['roleid'] != $roleid)
    	{
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' status_notexist,Status_model getInfo '.serialize(array($status_id, $this->log_data)));
    	    return response(null, 'status_notexist', 'status_notexist');
    	}
        
    	//更改动态的状态
    	$set = array();
    	$set['status'] = 1;
    	$this->Status_model->updateInfo($status_id, $set);
    
        return response(true);
    }
    
	/**
     * 获取我的朋友圈动态列表
     */
    public function home_timeline()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('status_id', '动态id', valid_int_rule());
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
			return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$status_id = (int)$this->form_validation->set_value('status_id');
    	
    	$status_id < 1 && $status_id = 0;
    	$count = 10;
    	
    	//引入角色关系模型
    	$this->load->model(array('Role_model', 'Role_relation_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Role_relation_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
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
    	
    	//查询好友列表
    	$friend_arr = $this->Role_relation_model->get_friendids($roleid);
    	//加入自己
    	$friend_arr[] = $roleid;
    	
    	//引入微博模型
    	$this->load->model(array('Status_model', 'Status_attribute_model', 'Status_favorite_model', 'Status_comment_model'));
    	$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_favorite_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_comment_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	//根据好友IDs获取微博列表
    	$status_list = $this->Status_model->get_byusers($friend_arr, $status_id, $count);
    	
    	if ($status_list)
    	{
    		foreach ($status_list as $k => $item)
    		{
    			//查询并合并属性表信息
    			$item_attribute = $this->Status_attribute_model->getInfo($item['status_id']);
    			if ($item_attribute)
    			{
    				$status_list[$k] = array_merge($item, $item_attribute);
    			}
    			
    			//查询用户昵称信息
    			$item_user = $this->Role_model->getInfo($item['roleid']);
    			$status_list[$k]['name'] = $item_user['name'];
    			$status_list[$k]['avatar'] = $item_user['avatar'];
    			$status_list[$k]['level'] = $item_user['level'];
    			
    			//查询点赞记录
    			$status_list[$k]['favorites'] = array();
    			if ($item_attribute['favorite_count'] > 0)
    			{
    				$where = array();
    				$where['status_id'] = $item['status_id'];
    				$where['status'] = 0;
    				$order = array('create_time' => 'desc');
    				$favorite_list = $this->Status_favorite_model->getList($where, $order, 0, 20, null, 0, false);
    				if ($favorite_list)
    				{
    					foreach ($favorite_list as $k_ => $item_)
    					{
    						$role_info_ = $this->Role_model->getInfo($item_['roleid']);
		    				$favorite_list[$k_]['name'] = $role_info_['name'];
		    				$favorite_list[$k_]['avatar'] = $role_info_['avatar'];
		    				$favorite_list[$k_]['level'] = $role_info_['level'];
    					}
    				}
    			
    				$status_list[$k]['favorites'] = $favorite_list;
    			}
    			 
    			//查询评论列表
    			$status_list[$k]['comments'] = array();
    			if ($item_attribute['comment_count'] > 0)
    			{
    				$where = array();
    				$where['status_id'] = $item['status_id'];
    				$where['status'] = 0;
    				$order = array('create_time' => 'desc');
    				$comment_list = $this->Status_comment_model->getList($where, $order, 0, 20, null, 0, false);
    				if ($comment_list)
    				{
    					foreach ($comment_list as $k_ => $item_)
    					{
    						$role_info_ = $this->Role_model->getInfo($item_['roleid']);
		    				$comment_list[$k_]['name'] = $role_info_['name'];
		    				$comment_list[$k_]['avatar'] = $role_info_['avatar'];
		    				$comment_list[$k_]['level'] = $role_info_['level'];
		    				
		    				//被回复人信息
		    				$comment_list[$k_]['reply_name'] = '';
		    				$comment_list[$k_]['reply_avatar'] = '';
		    				$comment_list[$k_]['reply_level'] = 0;
		    				if ($item_['reply_roleid'])
		    				{
		    				    $reply_role_info = $this->Role_model->getInfo($item_['reply_roleid']);
		    				    $comment_list[$k_]['reply_name'] = $reply_role_info['name'];
		    				    $comment_list[$k_]['reply_avatar'] = $reply_role_info['avatar'];
		    				    $comment_list[$k_]['reply_level'] = $reply_role_info['level'];
		    				}
    					}
    				}
    				 
    				$status_list[$k]['comments'] = $comment_list;
    			}
    		}
    	}
    	
    	return response($status_list);
    }
    
    
    
    /**
     * 获取我的动态列表
     */
    public function user_timeline()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('page', '页码', valid_int_rule());
    	$this->form_validation->set_rules('count', '每页显示数', valid_int_rule());
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$page = (int)$this->form_validation->set_value('page');
    	$count = (int)$this->form_validation->set_value('count');
    	 
    	$page < 1 && $page = 1;
    	$count < 1 && $count = 15;
    	 
    	//引入角色关系模型
    	$this->load->model(array('Role_model', 'Role_relation_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Role_relation_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	//判断用户是否真实存在
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	 
    	//引入微博模型
    	$this->load->model(array('Status_model', 'Status_attribute_model', 'Status_favorite_model', 'Status_comment_model'));
    	$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_favorite_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_comment_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	//根据好友IDs获取微博列表
    	$status_list = $this->Status_model->get_byuser($roleid, ($page-1)*$count, $count);
    	 
    	if ($status_list)
    	{
    		//因列表的数量有限, 用foreach
    		foreach ($status_list as $k => $item)
    		{
    			//查询并合并属性表信息
    			$item_attribute = $this->Status_attribute_model->getInfo($item['status_id']);
    			if ($item_attribute)
    			{
    				$status_list[$k] = array_merge($item, $item_attribute);
    			}
    			
    			//查询用户昵称信息
    			$item_user = $this->Role_model->getInfo($item['roleid']);
    			$status_list[$k]['name'] = $item_user['name'];
    			$status_list[$k]['avatar'] = $item_user['avatar'];
    			$status_list[$k]['level'] = $item_user['level'];
    			
    			//查询点赞记录
    			$status_list[$k]['favorites'] = array();
    			if ($item_attribute['favorite_count'] > 0)
    			{
    				$where = array();
    				$where['status_id'] = $item['status_id'];
    				$where['status'] = 0;
    				$order = array('create_time' => 'desc');
    				$favorite_list = $this->Status_favorite_model->getList($where, $order, 0, 20, null, 0, false);
    				if ($favorite_list)
    				{
    					foreach ($favorite_list as $k_ => $item_)
    					{
    						$role_info_ = $this->Role_model->getInfo($item_['roleid']);
		    				$favorite_list[$k_]['name'] = $role_info_['name'];
		    				$favorite_list[$k_]['avatar'] = $role_info_['avatar'];
		    				$favorite_list[$k_]['level'] = $role_info_['level'];
    					}
    				}
    				
    				$status_list[$k]['favorites'] = $favorite_list;
    			}
    			
    			//查询评论列表
    			$status_list[$k]['comments'] = array();
    			if ($item_attribute['comment_count'] > 0)
    			{
    				$where = array();
    				$where['status_id'] = $item['status_id'];
    				$where['status'] = 0;
    				$order = array('create_time' => 'desc');
    				$comment_list = $this->Status_comment_model->getList($where, $order, 0, 20, null, 0, false);
    				if ($comment_list)
    				{
    					foreach ($comment_list as $k_ => $item_)
    					{
    						$role_info_ = $this->Role_model->getInfo($item_['roleid']);
		    				$comment_list[$k_]['name'] = $role_info_['name'];
		    				$comment_list[$k_]['avatar'] = $role_info_['avatar'];
		    				$comment_list[$k_]['level'] = $role_info_['level'];
		    				
		    				//被回复人信息
		    				$comment_list[$k_]['reply_name'] = '';
		    				$comment_list[$k_]['reply_avatar'] = '';
		    				$comment_list[$k_]['reply_level'] = 0;
		    				if ($item_['reply_roleid'])
		    				{
		    				    $reply_role_info = $this->Role_model->getInfo($item_['reply_roleid']);
		    				    $comment_list[$k_]['reply_name'] = $reply_role_info['name'];
		    				    $comment_list[$k_]['reply_avatar'] = $reply_role_info['avatar'];
		    				    $comment_list[$k_]['reply_level'] = $reply_role_info['level'];
		    				}
    					}
    				}
    			
    				$status_list[$k]['comments'] = $comment_list;
    			}
    		}
    	}
    	 
    	return response($status_list);
    }
    
    /**
     * 点赞
     */
    public function update_favorite()
    {
    	//表单规则
    	$this->form_validation->set_rules('status_id', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$status_id = $this->form_validation->set_value('status_id');
    	$roleid = $this->form_validation->set_value('roleid');
    	
    	//引入角色关系模型
    	$this->load->model(array('Role_model', 'Role_relation_model'));
    	$this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Role_relation_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	//判断用户是否真实存在
    	$role_info = $this->Role_model->role_isexist($roleid);
    	if (!$role_info)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' role_notexist '.serialize(array($roleid, $this->log_data)));
    		return response(null, 'role_notexist', 'role_notexist');
    	}
    	
    	//引入微博模型
    	$this->load->model(array('Status_model', 'Status_attribute_model', 'Status_favorite_model'));
    	$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_favorite_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	//查询是否已经点赞
    	$attribute = array();
    	$attribute['status_id'] = $status_id;
    	$attribute['roleid'] = $roleid;
    	$favorite_info = $this->Status_favorite_model->getInfoByAttribute($attribute);
    	if ($favorite_info)
    	{
    		//已有记录,调整状态
    		$attribute = array();
    		$attribute['status'] = $favorite_info['status'] ? 0 : 1;
    		$this->Status_favorite_model->updateInfo($favorite_info['status_favorite_id'], $attribute);
    		
    		if($favorite_info['status'])
    		{
    			//微博点赞数+1
    			$attribute = array();
    			$attribute['favorite_count'] = 1;
    			$this->Status_attribute_model->increment($status_id, $attribute);
    			
    			return response(1);
    		}
    		else 
    		{
    			//微博点赞数+1
    			$attribute = array();
    			$attribute['favorite_count'] = 1;
    			$this->Status_attribute_model->decrement($status_id, $attribute);
    			
    			return response(0);
    		}
    		
    	}
    	else
    	{
    		//微博点赞数+1
    		$attribute = array();
    		$attribute['favorite_count'] = 1;
    		$this->Status_attribute_model->increment($status_id, $attribute);
    		 
    		$attribute = array();
    		$attribute['status_id'] = $status_id;
    		$attribute['roleid'] = $roleid;
    		$attribute['status'] = 0;
    		$attribute['create_time'] = time();
    		$this->Status_favorite_model->insertInfo($attribute);
    		
    		return response(1);
    	}
    }
    
    //发布评论
    public function create_comment()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('status_id', '微博', valid_int_rule(true));
    	$this->form_validation->set_rules('content', '动态内容', valid_text_rule(true));
    	$this->form_validation->set_rules('reply_id', '回复评论', valid_int_rule());
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$status_id = $this->form_validation->set_value('status_id');
    	$content = $this->form_validation->set_value('content');
    	$reply_id = $this->form_validation->set_value('reply_id');
    
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
    	$this->load->model(array('Status_attribute_model', 'Status_comment_model'));
    	//$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_comment_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	
    	$reply_roleid = 0;
    	if ($reply_id)
    	{
    		$reply_info = $this->Status_comment_model->getInfo($reply_id);
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
    	$comment_id = $this->Status_comment_model->insert_new($roleid, $status_id, $content, $reply_id, $reply_roleid);
    	if (!$comment_id)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' system_error,Status_model insertInfo '.serialize(array($this->log_data)));
    		return response(null, 'system_error', 'system_error');
    	}
    	 
    	//微博的评论数+1
    	$attribute = array();
    	$attribute['comment_count'] = 1;
    	$this->Status_attribute_model->increment($status_id, $attribute);
    	 
    	//返回数据
    	$info = $this->Status_comment_model->getInfo($comment_id);
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
    
    	log_message('debug', __CLASS__.' '.__FUNCTION__.' succ '.serialize(array($info, $this->log_data)));
    	return response($info);
    }
    
    
    //删除评论
    public function destroy_comment()
    {
    	//表单规则
    	$this->form_validation->set_rules('roleid', '角色', valid_int_rule(true));
    	$this->form_validation->set_rules('comment_id', '评论', valid_int_rule(true));
    	if (!$this->form_validation->run())
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    		return response(null, 'params_error', $this->form_validation->error_string());
    	}
    	$roleid = $this->form_validation->set_value('roleid');
    	$comment_id = $this->form_validation->set_value('comment_id');
    	 
    	 
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
    	$this->load->model(array('Status_attribute_model', 'Status_comment_model'));
    	//$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_attribute_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	$this->Status_comment_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	 
    	$comment_info = $this->Status_comment_model->getInfo($comment_id);
    	 
    	if ($comment_info)
    	{
    		if (!$comment_info['status'])
    		{
    			$attrs = array();
    			$attrs['status'] = 1;
    			$this->Status_comment_model->updateInfo($comment_id, $attrs);
    			 
    			//微博的评论数+1
    			$attribute = array();
    			$attribute['comment_count'] = 1;
    			$this->Status_attribute_model->decrement($comment_info['status_id'], $attribute);
    		}
    	}
    	 
    	 
    	return response(true);
    }
    
    /**
     * 获取动态的评论列表
     * 
     * 
     * status_id 动态id
     * status_comment_id 后翻页最大评论id, 将要获取的评论<此值
     * 
     */
    public function get_comment_list()
    {
        //表单规则
        $this->form_validation->set_rules('status_id', '动态', valid_int_rule(true));
        $this->form_validation->set_rules('status_comment_id', '页码', valid_int_rule());
        if (!$this->form_validation->run())
        {
            log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
            return response(null, 'params_error', $this->form_validation->error_string());
        }
        $status_id = $this->form_validation->set_value('status_id');
        $status_comment_id = (int)$this->form_validation->set_value('status_comment_id');
        
        $status_comment_id < 1 && $status_comment_id = 0;
        $count = 20;
        
        
        //判断用户是否真实存在
        $this->load->model(array('Role_model'));
        $this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
        
        //执行发微博业务
        $this->load->model(array('Status_comment_model'));
        $this->Status_comment_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
        
        
        $where = array();
        $where['status_id'] = $status_id;
        $where['status'] = 0;
        $status_comment_id && $where['less']['status_comment_id'] = $status_comment_id;
        $order = array('create_time' => 'desc');
        $cache_time = $status_comment_id ? 300 : 0;
        $comment_list = $this->Status_comment_model->getList($where, $order, 0, $count, null, $cache_time, false);
        if ($comment_list)
        {
            foreach ($comment_list as $k_ => $item_)
            {
                $role_info_ = $this->Role_model->getInfo($item_['roleid']);
                $comment_list[$k_]['name'] = $role_info_['name'];
                $comment_list[$k_]['avatar'] = $role_info_['avatar'];
                $comment_list[$k_]['level'] = $role_info_['level'];
        
                //被回复人信息
                $comment_list[$k_]['reply_name'] = '';
                $comment_list[$k_]['reply_avatar'] = '';
                $comment_list[$k_]['reply_level'] = 0;
                if ($item_['reply_roleid'])
                {
                    $reply_role_info = $this->Role_model->getInfo($item_['reply_roleid']);
                    $comment_list[$k_]['reply_name'] = $reply_role_info['name'];
                    $comment_list[$k_]['reply_avatar'] = $reply_role_info['avatar'];
                    $comment_list[$k_]['reply_level'] = $reply_role_info['level'];
                }
            }
        }
        
        return response($comment_list);
    }
    
    public function get_favorite_list()
    {
        //表单规则
        $this->form_validation->set_rules('status_id', '动态', valid_int_rule(true));
        $this->form_validation->set_rules('status_favorite_id', '页码', valid_int_rule());
        if (!$this->form_validation->run())
        {
            log_message('error', __CLASS__.' '.__FUNCTION__.' params_error '.serialize(array($this->form_validation->error_string(), $this->log_data)));
            return response(null, 'params_error', $this->form_validation->error_string());
        }
        $status_id = $this->form_validation->set_value('status_id');
        $status_favorite_id = (int)$this->form_validation->set_value('status_favorite_id');
        
        $status_favorite_id < 1 && $status_favorite_id = 0;
        $count = 20;
        
        
        //判断用户是否真实存在
        $this->load->model(array('Role_model'));
        $this->Role_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
        
        //执行发微博业务
        $this->load->model(array('Status_favorite_model'));
        $this->Status_favorite_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
        
        $where = array();
        $where['status_id'] = $status_id;
        $where['status'] = 0;
        $status_favorite_id && $where['less']['status_favorite_id'] = $status_favorite_id;
        $order = array('create_time' => 'desc');
        $cache_time = $status_favorite_id ? 300 : 0;
        $favorite_list = $this->Status_favorite_model->getList($where, $order, 0, $count, null, $cache_time, false);
        if ($favorite_list)
        {
            foreach ($favorite_list as $k_ => $item_)
            {
                $role_info_ = $this->Role_model->getInfo($item_['roleid']);
                $favorite_list[$k_]['name'] = $role_info_['name'];
                $favorite_list[$k_]['avatar'] = $role_info_['avatar'];
                $favorite_list[$k_]['level'] = $role_info_['level'];
            }
        }
        
        return response($favorite_list);
    }
    
    //获取未读新状态数
    public function get_unread_count()
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
    
    	//获取在线性通知, 并清空
    	$count = $this->Role_attribute_model->notice_online_count($roleid);
    	log_message('error', __CLASS__.' '.__FUNCTION__.' notice_online_count '.serialize(array($count, $roleid, $this->log_data)));
    	
    	return response($count ? $count : 0);
    }
    
    //获取未读新状态列表, 并清空
    public function fetch_unread_list()
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
    
    	//引入微博模型
    	$this->load->model(array('Status_model', 'Status_attribute_model'));
    	$this->Status_model->connect($this->site_info['hostname'], $this->site_info['port'], $this->site_info['username'], $this->site_info['password'], $this->site_info['database']);
    	 
    	//获取在线性通知, 并清空
    	$statusids = $this->Role_attribute_model->fetch_notice_online($roleid);
    	
    	$status_list = array();
    	if ($statusids)
    	{
    		//因列表的数量有限, 用foreach
    		foreach ($statusids as $k => $status_id)
    		{
    			$item = $this->Status_model->getInfo($status_id);
    			
    			//查询并合并属性表信息
    			$item_attribute = $this->Status_attribute_model->getInfo($item['status_id']);
    			if ($item_attribute)
    			{
    				$status_list[$k] = array_merge($item, $item_attribute);
    			}
    	
    			//查询用户昵称信息
    			$item_user = $this->Role_model->getInfo($item['roleid']);
    			$status_list[$k]['name'] = $item_user['name'];
    			$status_list[$k]['avatar'] = $item_user['avatar'];
    			$status_list[$k]['level'] = $item_user['level'];
    			 
    			//查询点赞记录
    			$status_list[$k]['favorites'] = array();
    			if ($item_attribute['favorite_count'] > 0)
    			{
    				$where = array();
    				$where['status_id'] = $item['status_id'];
    				$where['status'] = 0;
    				$order = array('create_time' => 'desc');
    				$favorite_list = $this->Status_favorite_model->getList($where, $order, 0, 20, null, 0, false);
    				if ($favorite_list)
    				{
    					foreach ($favorite_list as $k_ => $item_)
    					{
    						$role_info_ = $this->Role_model->getInfo($item_['roleid']);
		    				$favorite_list[$k_]['name'] = $role_info_['name'];
		    				$favorite_list[$k_]['avatar'] = $role_info_['avatar'];
		    				$favorite_list[$k_]['level'] = $role_info_['level'];
    					}
    				}
    	
    				$status_list[$k]['favorites'] = $favorite_list;
    			}
    			 
    			//查询评论列表
    			$status_list[$k]['comments'] = array();
    			if ($item_attribute['comment_count'] > 0)
    			{
    				$where = array();
    				$where['status_id'] = $item['status_id'];
    				$where['status'] = 0;
    				$order = array('create_time' => 'desc');
    				$comment_list = $this->Status_comment_model->getList($where, $order, 0, 20, null, 0, false);
    				if ($comment_list)
    				{
    					foreach ($comment_list as $k_ => $item_)
    					{
    						$role_info_ = $this->Role_model->getInfo($item_['roleid']);
		    				$comment_list[$k_]['name'] = $role_info_['name'];
		    				$comment_list[$k_]['avatar'] = $role_info_['avatar'];
		    				$comment_list[$k_]['level'] = $role_info_['level'];
		    				
		    				//被回复人信息
		    				$comment_list[$k_]['reply_name'] = '';
		    				$comment_list[$k_]['reply_avatar'] = '';
		    				$comment_list[$k_]['reply_level'] = 0;
		    				if ($item_['reply_roleid'])
		    				{
		    				    $reply_role_info = $this->Role_model->getInfo($item_['reply_roleid']);
		    				    $comment_list[$k_]['reply_name'] = $reply_role_info['name'];
		    				    $comment_list[$k_]['reply_avatar'] = $reply_role_info['avatar'];
		    				    $comment_list[$k_]['reply_level'] = $reply_role_info['level'];
		    				}
    					}
    				}
    				 
    				$status_list[$k]['comments'] = $comment_list;
    			}
    		}
    	}
    	
    	return response($status_list);
    }
    
    
}
?>