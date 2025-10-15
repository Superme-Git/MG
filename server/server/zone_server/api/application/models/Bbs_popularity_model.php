<?php
/**
 *
 * Bbs_popularity_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Bbs_popularity_model extends MY_Model
{
	protected $table = 'bbs_popularity';
	protected $primaryKey = 'bbs_popularity_id';
	//protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
	}
}
