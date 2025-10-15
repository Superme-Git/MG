<?php
/**
 *
 * 角色数据模型
 *
 * @author xu.lihui@qq.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2015.12.2
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
