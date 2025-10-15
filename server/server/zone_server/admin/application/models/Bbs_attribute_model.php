<?php
/**
 *
 * Bbs_attribute_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Bbs_attribute_model extends MY_Model
{
	protected $table = 'bbs_attribute';
	protected $primaryKey = 'bbs_id';
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
	
	public function insert_new($bbs_id, $content)
	{
		$data = array();
		$data['bbs_id'] = $bbs_id;
		$data['content'] = htmlspecialchars($content, ENT_QUOTES);//最长255, 多出的自动剔除
		$this->Bbs_attribute_model->insertInfo($data);
		
		return true;
	}
	
}
