<?php
defined('BASEPATH') OR exit('No direct script access allowed');
/**
 *
 * 栏目管理
 *
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 *
 */
class Record extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		
		$this->load->model(array('Faq_record_model', 'Faq_record_subtable_model'));
	}

	public function index($cur_month = '', $offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 15;
		
		
		//查询所有分表
		$wheres = array();
		$recordSubtableList = $this->Faq_record_subtable_model->getList($wheres, null, 0, 99);
		
		
		$wheres = array();
		$this->Faq_record_model->setSubtableSuffix($cur_month);
		$list = $this->Faq_record_model->getList($wheres, null, $offset, $limit);
		$count = $this->Faq_record_model->getCount($wheres);
		
		//分页
		$pagination = page($count, $offset, $limit, $cur_month);
		
		$this->load->view('faq/record/index', array(
				'cur_month' => $cur_month,
				'list' => $list,
				'pagination' => $pagination,
				'recordSubtableList' => $recordSubtableList
		));
		
	}
	
	public function del()
	{
		$record_id = $this->input->get('record_id', true);
		
		$recordInfo = $this->Faq_record_model->getInfo($record_id);
		if (!$recordInfo)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($record_id)));
			return redirect('faq/record/index');
		}
		
		$this->Faq_record_model->deleteInfo($record_id);
		
		$this->session->set_flashdata('success', lang('form_del_succ'));
		return redirect('faq/record/index');
	}
	
}
