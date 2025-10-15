<?php
/**
 *
 * Serverlist_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Serverlist_model extends MY_Model
{
	protected $table = 'serverlist';
	protected $primaryKey = 'serverlist_id';
	
	public function __construct()
	{
		parent::__construct();
	}
	
}
