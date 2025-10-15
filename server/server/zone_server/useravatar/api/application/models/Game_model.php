<?php
/**
 *
 * 游戏数据模型
 *
 * @author xu.lihui@qq.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2015.12.2
 *
 */
class Game_model extends MY_Model
{
	protected $table = 'game';
	protected $primaryKey = 'game_id';
	
	public function __construct()
	{
		parent::__construct();
	}
	
    
}
