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
class Gift extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model(array('Bbs_gift_model'));
		$serverInfo = $this->session->userdata('serverlist');
		$this->Bbs_gift_model->connect($serverInfo['hostname'], $serverInfo['port'], $serverInfo['username'], $serverInfo['password'], $serverInfo['database']);
		
		//设置分站点
		setSite($serverInfo['serverkey']);
	}

	public function index($offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		
		$giftList = $this->Bbs_gift_model->getList(null, null, $offset, $limit);
		$giftListCount = $this->Bbs_gift_model->getCount(null);
		
		//分页
		$pagination = page($giftListCount, $offset, $limit);
		
		$this->load->view('gift/index', array(
				'list' => $giftList,
				'pagination' => $pagination,
		));
		
	}

	
}
