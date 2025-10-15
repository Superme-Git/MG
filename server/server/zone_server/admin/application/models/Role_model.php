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
	protected $primaryKey = 'roleid';
	
	public function __construct()
	{
		parent::__construct();
	}
	
	public function role_isexist($roleid)
	{
		$relation_info = $this->Role_model->getInfo($roleid);
		if (!$relation_info)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' user_notexist '.serialize(array($roleid)));
			return response(null, 'user_notexist', 'user_notexist');
		}
		
		return $relation_info;
	}
	
	
}
