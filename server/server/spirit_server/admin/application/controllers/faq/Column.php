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
class Column extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		
		$this->load->model(array('Faq_column_model', 'Faq_model', 'Faq_to_column_model'));
	}

	public function index($offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		$wheres = array();
		$columnList = $this->Faq_column_model->getList($wheres, null, $offset, $limit);
		$columnCount = $this->Faq_column_model->getCount($wheres);
		
		//分页
		$pagination = page($columnCount, $offset, $limit);
		
		$this->load->view('faq/column/index', array(
				'list' => $columnList,
				'pagination' => $pagination,
		));
		
	}
	
	public function add()
	{
		//表单规则
		$this->form_validation->set_rules('name', '名称', valid_string_rule(1, 60, true));
		$this->form_validation->set_rules('sort', '排序', valid_int_rule());
		$this->form_validation->set_rules('parent_id', '父分类', valid_int_rule());
		if ($this->form_validation->run())
		{
			$name = $this->form_validation->set_value('name');
			$sort = $this->form_validation->set_value('sort');
			$parent_id = $this->form_validation->set_value('parent_id');
			
			//文章表
			$attributes = array();
			$attributes['name'] = $name;
			$attributes['sort'] = (int)$sort;
			$attributes['parent_id'] = (int)$parent_id;
			$attributes['create_time'] = (int)time();
			$column_id = $this->Faq_column_model->insertInfo($attributes);
			
			$this->session->set_flashdata('success', lang('form_add_succ'));
			
			log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($attributes, $column_id)));
			return redirect('faq/column/index');
		}
		
		$wheres = array();
		$columnList = $this->Faq_column_model->getList($wheres, null, 0, 99);
		
		$this->load->view('faq/column/add', array(
				'columnList' => $columnList,
		));
	}
	
	public function edit()
	{
		$column_id = $this->input->get('column_id', true);
		
		$info = $this->Faq_column_model->getInfo($column_id);
		if (!$info)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($column_id)));
			return redirect('faq/column/index');
		}
		
		//表单规则
		$this->form_validation->set_rules('name', '名称', valid_string_rule(1, 60, true));
		$this->form_validation->set_rules('sort', '排序', valid_int_rule());
		$this->form_validation->set_rules('parent_id', '父分类', valid_int_rule());
		if ($this->form_validation->run())
		{
			$name = $this->form_validation->set_value('name');
			$sort = $this->form_validation->set_value('sort');
			$parent_id = $this->form_validation->set_value('parent_id');
			
			//文章表
			$attributes = array();
			$attributes['name'] = $name;
			$attributes['sort'] = (int)$sort;
			$attributes['parent_id'] = (int)$parent_id;
			$this->Faq_column_model->updateInfo($column_id, $attributes);
			
			
			
			//***处理栏目*****************************/
			$sorts = $this->input->post('sorts', true);
			$faq_ids = $this->input->post('faq_ids', true);
				
			//清除关联关系
			$faqToColumnList = $this->Faq_to_column_model->getList(array('column_id' => $column_id), array('sort' => 'desc'), 0, 999);
			if ($faqToColumnList)
			{
				foreach ($faqToColumnList as $item)
				{
					$this->Faq_to_column_model->deleteInfo($item['to_column_id']);
				}
			}
				
			//新增关联关系
			if ($faq_ids)
			{
				foreach ($faq_ids as $k => $v)
				{
					$attributes = array();
					$attributes['column_id'] = $column_id;
					$attributes['faq_id'] = $k;
					$attributes['sort'] = isset($sorts[$k]) ? $sorts[$k] : 0;
					$attributes['create_time'] = time();
					$this->Faq_to_column_model->insertInfo($attributes);
				}
			}
			
		
			$this->session->set_flashdata('success', lang('form_edit_succ'));
			
			log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($attributes, $column_id)));
			return redirect('faq/column/index');
		}
		
		$wheres = array();
		$columnList = $this->Faq_column_model->getList($wheres, null, 0, 99);
		
		$params = array('status' => 0);
		$order = array('sort' => 'desc');
		$faqList = $this->Faq_model->getList($params, $order, 0, 9999);
		
		$faqList_ = array();
		if ($faqList)
		{
			foreach ($faqList as $item)
			{
				$category_info = null;
				if ($item['category_id'])
				{
					$category_info = $this->Faq_category_model->getInfo($item['category_id']);
				}
				$faqList_[$item['category_id']]['info'] = $category_info;
				$faqList_[$item['category_id']]['list'][] = $item;
			}
		}
		
		
		//文章与栏目对应关系
		$faqToColumnList = $this->Faq_to_column_model->getList(array('column_id' => $column_id), array('sort' => 'desc'), 0, 999);
		$faqToColumnArr = array();
		if ($faqToColumnList)
		{
			foreach ($faqToColumnList as $item)
			{
				$faqToColumnArr[$item['faq_id']] = $item;
			}
		}
		
		$this->load->view('faq/column/edit', array(
				'info' => $info,
				'columnList' => $columnList,
				'faqList' => $faqList_,
				'faqToColumnArr' => $faqToColumnArr
		));
	}
	
	public function del()
	{
		$column_id = $this->input->get('column_id', true);
		
		$columnInfo = $this->Faq_column_model->getInfo($column_id);
		if (!$columnInfo)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($column_id)));
			return redirect('faq/column/index');
		}
		
		$this->Faq_column_model->deleteInfo($column_id);
		
		$this->session->set_flashdata('success', lang('form_del_succ'));
		return redirect('faq/column/index');
	}
	
}
