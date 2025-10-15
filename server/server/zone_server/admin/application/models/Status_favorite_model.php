<?php
/**
 *
 * Status_favorite_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Status_favorite_model extends MY_Model
{
	protected $table = 'status_favorite';
	protected $primaryKey = 'status_favorite_id';
	//protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
	}
}
