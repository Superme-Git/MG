<?php
/**
 *
 * Bbs_gift_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Bbs_gift_model extends MY_Model
{
	protected $table = 'bbs_gift';
	protected $primaryKey = 'bbs_gift_id';
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
	
	public function insert_new($roleid, $to_roleid, $gift_id, $gift_count, $content)
	{
	    $attribute = array();
	    $attribute['roleid'] = $roleid;
	    $attribute['to_roleid'] = $to_roleid;
	    $attribute['gift_id'] = $gift_id;
	    $attribute['gift_count'] = $gift_count;
	    $attribute['status'] = 0;
	    $attribute['content'] = htmlspecialchars($content, ENT_QUOTES);
	    $attribute['create_time'] = time();
	    $this->Bbs_gift_model->insertInfo($attribute);
	
	    return true;
	}
}
