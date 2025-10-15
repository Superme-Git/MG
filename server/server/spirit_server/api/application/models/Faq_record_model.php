<?php
/**
 *
 * Record数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.15
 *
 */
class Faq_record_model extends MY_Model
{
	protected $table = 'faq_record';
	protected $primaryKey = 'record_id';
	//protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
	}
}
