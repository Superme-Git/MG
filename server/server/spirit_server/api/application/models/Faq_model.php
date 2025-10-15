<?php
/**
 *
 * Faq数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.15
 *
 */
class Faq_model extends MY_Model
{
	protected $table = 'faq';
	protected $primaryKey = 'faq_id';
	//protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
	}
	
	public function getInfoRelation($info)
	{
		$info_ = array();
		$info_['faq_id'] = $info['faq_id'];
		$info_['title'] = $info['title'];
		$info_['style'] = $info['style'];
		$info_['create_time'] = $info['create_time'];
		
		return $info_;
	}
	
}
