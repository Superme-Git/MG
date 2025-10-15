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
class Faq_to_column_model extends MY_Model
{
	protected $table = 'faq_to_column';
	protected $primaryKey = 'to_column_id';
	//protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
	}
}
