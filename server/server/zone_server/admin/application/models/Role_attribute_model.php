<?php
/**
 *
 * Role_attribute_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Role_attribute_model extends MY_Model
{
	protected $table = 'role_attribute';
	protected $primaryKey = 'roleid';
	
	public function __construct()
	{
		parent::__construct();
	}
	
    //校验用户是否存在. 不存在则创建
	public function get_withrepair($roleid)
	{
		$attribute_info = $this->Role_attribute_model->getInfo($roleid);
		if ($attribute_info)
		{
			return $attribute_info;
		}
		else
		{
			//用户不存在, 则自动创建
			$data = array();
			$data['roleid'] = $roleid;
			$data['msg_count'] = 0;
			$data['signature'] = '';
			$data['popularity_count'] = 0;
			$data['gift_count'] = 0;
			$data['sound_url'] = '';
			$result = $this->Role_attribute_model->insertInfo($data);
			
			log_message('error', __CLASS__.' '.__FUNCTION__.' user_notexist '.serialize(array($data, $result)));
			
			return $data;
		}
	}
	
	//获取在线用户列表
	public function get_online()
	{
		$online_userlist_key = redis_key('role:online');
		$online_arr = redis_zrangescore($online_userlist_key, time() - 120, time());
		
		return $online_arr;
	}
	
	//通知在线用户
	public function notice_online($online_arr, $status_id)
	{
		if ($online_arr)
		{
			foreach ($online_arr as $roleid_)
			{
				$msgbox_key = redis_key('msgbox:'.$roleid_);
				redis_hset($msgbox_key, $status_id, $status_id);
			}
		}
	}
	
	//通知离线用户
	public function notice_offline($offline_arr)
	{
		if ($offline_arr)
		{
			foreach ($offline_arr as $roleid_)
			{
				$data = array();
    			$data['msg_count'] = 1;
    			$this->Role_attribute_model->increment($roleid_, $data);
			}
		}
	}
	
	//统计在线消息数
	public function notice_online_count($user_id)
	{
		$msgbox_key = redis_key('msgbox:'.$user_id);
		
		return redis_hlen($msgbox_key);
	}
	
	//获取在线消息列表, 并清空
	public function fetch_notice_online($user_id)
	{
		$msgbox_key = redis_key('msgbox:'.$user_id);
		
		$arr = redis_hvals($msgbox_key);
		redis_del($msgbox_key);
	
		return $arr;
	}
}
