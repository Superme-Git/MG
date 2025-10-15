<?php
/**
 *
 * User_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
 *
 */
class Role_model extends MY_Model
{
	protected $table = 'role';
	protected $primaryKey = 'role_id';
	
	public function __construct()
	{
		parent::__construct();
	}
	
}
