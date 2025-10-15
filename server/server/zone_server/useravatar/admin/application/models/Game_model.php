<?php
/**
 *
 * Game_model 数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.29
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
