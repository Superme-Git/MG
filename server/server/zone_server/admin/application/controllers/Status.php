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
class Status extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model(array('status_model', 'Status_attribute_model'));
		$serverInfo = $this->session->userdata('serverlist');
		$this->status_model->connect($serverInfo['hostname'], $serverInfo['port'], $serverInfo['username'], $serverInfo['password'], $serverInfo['database']);
		$this->Status_attribute_model->connect($serverInfo['hostname'], $serverInfo['port'], $serverInfo['username'], $serverInfo['password'], $serverInfo['database']);
		
		//设置分站点
		setSite($serverInfo['serverkey']);
	}

	public function index($offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		$where = array();
		$where['status'] = 0;
		$status_list = $this->status_model->getList($where, array('status_id' => 'desc'), $offset, $limit);
		$statusListCount = $this->status_model->getCount($where);
		
		if ($status_list)
		{
			foreach ($status_list as $k => $item)
			{
				//查询并合并属性表信息
				$item_attribute = $this->Status_attribute_model->getInfo($item['status_id']);
				$status_list[$k] = array_merge($item, $item_attribute);
			}
		}
		
		//分页
		$pagination = page($statusListCount, $offset, $limit);
		
		$this->load->view('status/index', array(
				'list' => $status_list,
				'pagination' => $pagination,
		));
		
	}
	
	public function del($status_id)
	{
	    $info = $this->status_model->getInfo($status_id);
	    if (!$info)
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($status_id)));
	        return redirect('status/index');
	    }
	       
	    $set = array();
	    $set['status'] = 1;
	    $this->status_model->updateInfo($status_id, $set);
	
	    $this->session->set_flashdata('success', lang('form_del_succ'));
	    return redirect('status/index');
	}
}
