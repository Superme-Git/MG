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
class Favorite extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model(array('Status_favorite_model'));
		$serverInfo = $this->session->userdata('serverlist');
		$this->Status_favorite_model->connect($serverInfo['hostname'], $serverInfo['port'], $serverInfo['username'], $serverInfo['password'], $serverInfo['database']);
		
		//设置分站点
		setSite($serverInfo['serverkey']);
	}

	public function index($offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		
		$favoriteList = $this->Status_favorite_model->getList(null, null, $offset, $limit);
		$favoriteListCount = $this->Status_favorite_model->getCount(null);
		
		//分页
		$pagination = page($favoriteListCount, $offset, $limit);
		
		$this->load->view('favorite/index', array(
				'list' => $favoriteList,
				'pagination' => $pagination,
		));
		
	}
}
