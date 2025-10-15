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
class Server_merge_model extends MY_Model
{
	protected $table = 'server_merge';
	protected $primaryKey = 'server_merge_id';
	
	public function __construct()
	{
		parent::__construct();
	}
	
}
