<?php
defined('BASEPATH') OR exit('No direct script access allowed');
/**
 *
 * 栏目管理
 *
 * @copyright www.locojoy.com
 * @author xinmanpei@joyogame.com
 * @version v1.0 2016.04.01
 *
 */
class Role extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model(array('Game_model', 'Role_model'));
	}

	public function index($gameid = 0, $offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		$where = array();
		$order = array('role_id' => 'asc');
		$game_list = $this->Game_model->getList($where, null, 0, 999, null, 0);
		if (!$gameid && $game_list)
		{
		    $gameid = $game_list[0]['gameid'];
		}
		
		$roleList = array();
		$roleListCount = 0;
		if ($gameid)
		{
		    $params = array();
			$params['gameid'] = $gameid;
			$game_info = $this->Game_model->getInfoByAttribute($params);
		    
		    $where = array();
		    $this->Role_model->setSubtableSuffix($game_info['gameid']);
		    $roleList = $this->Role_model->getList($where, null, $offset, $limit, null, 0);
		    $roleListCount = $this->Role_model->getCount($where);
		}
		
		
		//分页
		$pagination = page($roleListCount, $offset, $limit, $gameid);
		
		$this->load->view('role/index', array(
		    'gameid' => $gameid,
                'game_list' => $game_list,
				'list' => $roleList,
				'pagination' => $pagination,
		));
		
	}
	
	
}
