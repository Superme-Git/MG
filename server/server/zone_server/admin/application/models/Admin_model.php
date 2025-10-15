<?php
/**
 *
 * 管理员数据模型
 *
 * @copyright www.locojoy.com
 * @author xu.lihui@qq.com
 * @version v1.0 2015.8.9
 *
 */
class Admin_model extends MY_Model
{
	protected $table = 'admin';
	protected $primaryKey = 'admin_id';
	
	public function __construct()
	{
		parent::__construct();
	}
}
