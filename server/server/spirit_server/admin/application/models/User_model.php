<?php
/**
 *
 * 用户数据模型
 *
 * @author xu.lihui@qq.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2015.8.9
 *
 */
class User_model extends MY_Model
{
	protected $table = 'user';
	protected $primaryKey = 'user_id';
	protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
	}
}
