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
class Bbs extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model(array('bbs_model', 'Bbs_attribute_model'));
		
		$serverInfo = $this->session->userdata('serverlist');
		$this->bbs_model->connect($serverInfo['hostname'], $serverInfo['port'], $serverInfo['username'], $serverInfo['password'], $serverInfo['database']);
		$this->Bbs_attribute_model->connect($serverInfo['hostname'], $serverInfo['port'], $serverInfo['username'], $serverInfo['password'], $serverInfo['database']);
		
		
		//设置分站点
		setSite($serverInfo['serverkey']);
	}

	public function index($offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		$bbs_list = $this->bbs_model->getList(null, null, $offset, $limit);
		$bbsListCount = $this->bbs_model->getCount(null);
		
		if ($bbs_list)
		{
			//因列表的数量有限, 用foreach
			foreach ($bbs_list as $k => $item)
			{
				//查询并合并属性表信息
				$item_attribute = $this->Bbs_attribute_model->getInfo($item['bbs_id']);
				$bbs_list[$k] = array_merge($item, $item_attribute);
			}
		}
		
		//分页
		$pagination = page($bbsListCount, $offset, $limit);
		
		$this->load->view('bbs/index', array(
				'list' => $bbs_list,
				'pagination' => $pagination,
		));
		
	}
	
	public function del($status_id)
	{
	    $info = $this->bbs_model->getInfo($status_id);
	    if (!$info)
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($status_id)));
	        return redirect('bbs/index');
	    }
	
	    $set = array();
	    $set['status'] = 1;
	    $this->bbs_model->updateInfo($status_id, $set);
	
	    $this->session->set_flashdata('success', lang('form_del_succ'));
	    return redirect('bbs/index');
	}
	
}
