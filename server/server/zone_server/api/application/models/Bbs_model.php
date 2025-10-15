<?php
/**
 *
 * Bbs_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Bbs_model extends MY_Model
{
	protected $table = 'bbs';
	protected $primaryKey = 'bbs_id';
	
	public function __construct()
	{
		parent::__construct();
	}
	
	public function getInfoRelation($info)
	{
		return $info;
	}
	
	public function insert_new($roleid, $to_roleid, $reply_id = 0, $reply_roleid = 0, $gift_id = 0, $gift_count = 0)
	{
		$data = array();
		$data['roleid'] = $roleid;
		$data['to_roleid'] = $to_roleid;
		$data['gift_id'] = $gift_id;
		$data['gift_count'] = $gift_count;
		$data['reply_id'] = $reply_id;
		$data['reply_roleid'] = $reply_roleid;
		$data['create_time'] = time();
		$bbs_id = $this->Bbs_model->insertInfo($data);
	
		return $bbs_id;
	}
}
