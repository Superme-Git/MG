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
class Popularity extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model(array('Bbs_popularity_model'));
		$serverInfo = $this->session->userdata('serverlist');
		$this->Bbs_popularity_model->connect($serverInfo['hostname'], $serverInfo['port'], $serverInfo['username'], $serverInfo['password'], $serverInfo['database']);
		
		//设置分站点
		setSite($serverInfo['serverkey']);
	}

	public function index($offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		
		$popularityList = $this->Bbs_popularity_model->getList(null, null, $offset, $limit);
		$popularityListCount = $this->Bbs_popularity_model->getCount(null);
		
		//分页
		$pagination = page($popularityListCount, $offset, $limit);
		
		$this->load->view('popularity/index', array(
				'list' => $popularityList,
				'pagination' => $pagination,
		));
		
	}
	
}
