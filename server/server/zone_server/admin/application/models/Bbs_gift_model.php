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
}
