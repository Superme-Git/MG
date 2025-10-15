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
class Comment extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		
		$this->load->model(array('Status_attribute_model', 'Status_comment_model'));
		$serverInfo = $this->session->userdata('serverlist');
		$this->Status_comment_model->connect($serverInfo['hostname'], $serverInfo['port'], $serverInfo['username'], $serverInfo['password'], $serverInfo['database']);
		
		//设置分站点
		setSite($serverInfo['serverkey']);
	}

	public function index($offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		
		$commentList = $this->Status_comment_model->getList(null, null, $offset, $limit);
		$commentListCount = $this->Status_comment_model->getCount(null);
		
		//分页
		$pagination = page($commentListCount, $offset, $limit);
		
		$this->load->view('comment/index', array(
				'list' => $commentList,
				'pagination' => $pagination,
		));
		
	}
	
	public function del($status_id)
	{
	    $info = $this->Status_comment_model->getInfo($status_id);
	    if (!$info)
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($status_id)));
	        return redirect('comment/index');
	    }
	
	    $set = array();
	    $set['status'] = 1;
	    $this->Status_comment_model->updateInfo($status_id, $set);
	
	    $this->session->set_flashdata('success', lang('form_del_succ'));
	    return redirect('comment/index');
	}
}
