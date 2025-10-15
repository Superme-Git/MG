<?php
/**
 *
 * Status_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Status_model extends MY_Model
{
	protected $table = 'status';
	protected $primaryKey = 'status_id';
	//protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
		
		
	}
	
	public function getInfoRelation($info)
	{
		return $info;
	}
	
	//存入新微博
	public function insert_new($roleid)
	{
		//存入微博表
		$data = array();
		$data['roleid'] = $roleid;
		$data['status'] = 0;
		$data['create_time'] = time();
		$status_id = $this->Status_model->insertInfo($data);
		if (!$status_id)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' Status_model insertInfo error: no $status_id '.serialize(array($data)));
			return null;
		}
		 
		return $status_id;
	}
	
	//根据用户ID获取微博列表
	public function get_byusers($roleids, $status_id, $limit)
	{
		$where = array();
		$where['in']['roleid'] = $roleids;
		$status_id && $where['less']['status_id'] = $status_id;
		$where['status'] = 0;
		$order = array('status_id' => 'desc');
		$cache_time = $status_id ? 300 : 0;
		$status_list = $this->Status_model->getList($where, $order, 0, $limit, null, $cache_time, false);
		
		return $status_list;
	}
	
	//根据用户ID获取微博列表
	public function get_byuser($roleid, $status_id, $limit)
	{
		$where = array();
		$where['roleid'] = $roleid;
		$status_id && $where['less']['status_id'] = $status_id;
		$where['status'] = 0;
		$order = array('status_id' => 'desc');
		$cache_time = $status_id ? 300 : 0;
		$status_list = $this->Status_model->getList($where, $order, 0, $limit, null, $cache_time, false);
	
		return $status_list;
	}
}
