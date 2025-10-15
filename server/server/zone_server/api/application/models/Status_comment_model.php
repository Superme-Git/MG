<?php
/**
 *
 * Status_comment_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Status_comment_model extends MY_Model
{
	protected $table = 'status_comment';
	protected $primaryKey = 'status_comment_id';
	//protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
	}
	
	public function getInfoRelation($info)
	{
		$info['content'] = htmlspecialchars_decode($info['content'], ENT_QUOTES);
	
		return $info;
	}
	
	public function insert_new($roleid, $status_id, $content, $reply_id = 0, $reply_roleid = 0)
	{
		//存入微博表
		$data = array();
		$data['roleid'] = $roleid;
		$data['status_id'] = $status_id;
		$data['content'] = htmlspecialchars($content, ENT_QUOTES);
		$data['reply_id'] = $reply_id;
		$data['reply_roleid'] = $reply_roleid;
		$data['status'] = 0;
		$data['create_time'] = time();
		$last_id = $this->Status_comment_model->insertInfo($data);
		if (!$last_id)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' no $last_id '.serialize(array($data)));
			return null;
		}
			
		return $last_id;
	}
}
