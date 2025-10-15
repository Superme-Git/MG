<?php
/**
 *
 * Status_attribute_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Status_attribute_model extends MY_Model
{
	protected $table = 'status_attribute';
	protected $primaryKey = 'status_id';
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
	
	public function insert_new($status_id, $content, $img_url = '')
	{
		$data = array();
		$data['status_id'] = $status_id;
		$data['content'] = htmlspecialchars($content, ENT_QUOTES);//最长255, 多出的自动剔除
		$data['img_url'] = $img_url;
		$this->Status_attribute_model->insertInfo($data);
		
		return true;
	}
	
}
