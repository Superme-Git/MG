<?php
/**
 *
 * User_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Role_relation_model extends MY_Model
{
	protected $table = 'role_relation';
	protected $primaryKey = '';
	
	public function __construct()
	{
		parent::__construct();
	}
	
	public function get_friendids($roleid)
	{
		//加600秒缓存,不挂载模式
		$where = array();
		$where['roleid'] = $roleid;
		$relation_list = $this->Role_relation_model->getList($where, null, 0, 150, null, 600, false);
		
		$friend_arr = array();
		if($relation_list)
		{
			$black_arr = array();
			foreach ($relation_list as $item)
			{
				//无friendid, 意为表明此人存在
				if (!$item['friendid'])
				{
					$friend_arr[] = $item['roleid'];
				}
				//角色关系类型:1,好友 2,黑名单 3,待定
				elseif ($item['relation'] == 1)
				{
					$friend_arr[] = $item['friendid'];
				}
				//角色关系类型:1,好友 2,黑名单 3,待定
				elseif ($item['relation'] == 2)
				{
					$black_arr[] = $item['friendid'];
				}
			}
			
			if ($friend_arr && $black_arr)
			{
				$friend_arr = array_diff($friend_arr, $black_arr);
			}
		}
		
		return $friend_arr;
	}
	
	public function remove_info($roleid)
	{
	    $sql = "delete from role_relation where `roleid`=$roleid or friendid=$roleid";
	    log_message('error', __CLASS__.' '.__FUNCTION__.' test '.serialize(array($sql)));
	    $query = $this->db()->query($sql);
	    
	    return $query;
	}
	
	
}
