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
class Category extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		
		$this->load->model(array('Faq_category_model'));
	}

	public function index($offset = 0)
	{
		$offset < 1 && $offset = 0;
		$limit = 9999;
		
		$wheres = array();
		$categoryList = $this->Faq_category_model->getList($wheres, null, $offset, $limit);
		$categoryCount = $this->Faq_category_model->getCount($wheres);
		
		//分页
		$pagination = page($categoryCount, $offset, $limit);
		
		$this->load->view('faq/category/index', array(
				'list' => $categoryList,
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
			$attributes['count'] = (int)0;
			$attributes['create_time'] = (int)time();
			$category_id = $this->Faq_category_model->insertInfo($attributes);
			
			$this->session->set_flashdata('success', lang('form_add_succ'));
			
			log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($attributes, $category_id)));
			return redirect('faq/category/index');
		}
		
		$wheres = array();
		$categoryList = $this->Faq_category_model->getList($wheres, null, 0, 99);
		
		$this->load->view('faq/category/add', array(
				'categoryList' => $categoryList
		));
	}
	
	public function edit()
	{
		$category_id = $this->input->get('category_id', true);
		
		$info = $this->Faq_category_model->getInfo($category_id);
		if (!$info)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($category_id)));
			return redirect('faq/category/index');
		}
		
		//表单规则
		$this->form_validation->set_rules('name', '名称', valid_string_rule(1, 60, true));
		$this->form_validation->set_rules('sort', '排序', valid_int_rule());
		$this->form_validation->set_rules('count', '统计数', valid_int_rule());
		$this->form_validation->set_rules('parent_id', '父分类', valid_int_rule());
		if ($this->form_validation->run())
		{
			$name = $this->form_validation->set_value('name');
			$sort = $this->form_validation->set_value('sort');
			$count = $this->form_validation->set_value('count');
			$parent_id = $this->form_validation->set_value('parent_id');
			
			//文章表
			$attributes = array();
			$attributes['name'] = $name;
			$attributes['sort'] = (int)$sort;
			$attributes['count'] = (int)$count;
			$attributes['parent_id'] = (int)$parent_id;
			$this->Faq_category_model->updateInfo($category_id, $attributes);
		
			$this->session->set_flashdata('success', lang('form_edit_succ'));
			log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($attributes, $category_id)));
			return redirect('faq/category/index');
		}
		
		$wheres = array();
		$categoryList = $this->Faq_category_model->getList($wheres, null, 0, 99);
		
		$this->load->view('faq/category/edit', array(
				'info' => $info,
				'categoryList' => $categoryList
		));
	}
	
	public function del()
	{
		$category_id = $this->input->get('category_id', true);
		
		$categoryInfo = $this->Faq_category_model->getInfo($category_id);
		if (!$categoryInfo)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($category_id)));
			return redirect('faq/category/index');
		}
		
		$this->Faq_category_model->deleteInfo($category_id);
		
		$this->session->set_flashdata('success', lang('form_del_succ'));
		return redirect('faq/category/index');
	}
	
}
