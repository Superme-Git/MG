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
class Faq_category_model extends MY_Model
{
	protected $table = 'faq_category';
	protected $primaryKey = 'category_id';
	//protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
	}
}
