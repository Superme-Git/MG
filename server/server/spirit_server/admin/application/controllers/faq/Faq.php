<?php
defined('BASEPATH') OR exit('No direct script access allowed');
/**
 * 管理用户管理
 * 
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 */
class Faq extends MY_Controller
{
    function __construct()
    {
        parent::__construct();
        
        $this->load->model(array("Faq_model","Faq_content_model", 'Faq_category_model', 'Faq_column_model', 'Faq_to_column_model'));
    }
    
    public function index($category_id = 0, $offset = 0)
    {
    	$offset < 1 && $offset = 0;
    	$limit = 10;
    	
    	
    	$params = array();
    	$category_id && $params['category_id'] = $category_id;
    	$list = $this->Faq_model->getList($params, null, $offset, $limit);
    	$count = $this->Faq_model->getCount($params);
    	
    	$pagination = page($count, $offset, $limit, $category_id);
    	
    	//分类列表
    	$categoryList = $this->Faq_category_model->getList(null, array('sort' => 'desc'), 0, 999);
    	
    	
    	return $this->load->view("faq/faq/index", array(
    			'cur_category_id' => $category_id,
    			'list' => $list, 
    			'pagination' => $pagination,
    			'categoryList' => $categoryList
    	));
    }
    
    public function add()
    {
    	//提交表单
    	if (is_post())
    	{
    		$this->form_validation->set_rules('category_id', 'category_id', valid_int_rule(true));
    		$this->form_validation->set_rules('title', 'title', valid_string_rule(1, 60, true));
    		$this->form_validation->set_rules('style', 'style', valid_string_rule(1, 255));
    		$this->form_validation->set_rules('thumb', 'thumb', valid_string_rule(1, 255));
    		$this->form_validation->set_rules('keywords', 'keywords', valid_string_rule(1, 255));
    		$this->form_validation->set_rules('description', 'description', valid_string_rule(1, 255));
    		$this->form_validation->set_rules('sort', 'sort', valid_int_rule());
    		$this->form_validation->set_rules('status', 'status', valid_int_rule());
    		$this->form_validation->set_rules('content', 'content', valid_text_rule());
    		if (!$this->form_validation->run())
    		{
    			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    			$this->session->set_flashdata('olddata', $this->input->post());
    			$this->session->set_flashdata('error', '您填写的表单有误, 请检查'.$this->form_validation->error_string());
    			return redirect('faq/faq/add');
    		}
    		$category_id = $this->form_validation->set_value('category_id');
    		$title = $this->form_validation->set_value('title');
    		$style = $this->form_validation->set_value('style');
    		$thumb = $this->form_validation->set_value('thumb');
    		$keywords = $this->form_validation->set_value('keywords');
    		$description = $this->form_validation->set_value('description');
    		$sort = $this->form_validation->set_value('sort');
    		$status = $this->form_validation->set_value('status');
    		$content = $this->form_validation->set_value('content');
    		
    		//插入数据
    		$attributes = array();
    		$attributes['category_id'] = $category_id;
    		$attributes['title'] = $title;
    		$attributes['style'] = $style;
    		$attributes['thumb'] = $thumb;
    		$attributes['keywords'] = $keywords;
    		$attributes['description'] = $description;
    		$attributes['sort'] = $sort;
    		$attributes['status'] = $status;
    		$attributes['create_time'] = time();
    		$faq_id = $this->Faq_model->insertInfo($attributes);
    		
    		$attributes = array();
    		$attributes['faq_id'] = $faq_id;
    		$attributes['content'] = $content;
    		$this->Faq_content_model->insertInfo($attributes);
    		
    		
    		$attributes = array();
    		$attributes['count'] = (int)1;
    		$this->Faq_category_model->increment($category_id, $attributes);
    		
    		//***处理栏目*****************************/
    		$sorts = $this->input->post('sorts', true);
    		$column_ids = $this->input->post('column_ids', true);
    			
    		//新增关联关系
    		if ($column_ids)
    		{
    			foreach ($column_ids as $k => $v)
    			{
    				$attributes = array();
    				$attributes['faq_id'] = $faq_id;
    				$attributes['column_id'] = $k;
    				$attributes['sort'] = isset($sorts[$k]) ? $sorts[$k] : 0;
    				$attributes['create_time'] = time();
    				$this->Faq_to_column_model->insertInfo($attributes);
    			}
    		}
    		
    		$this->session->set_flashdata('success', lang('form_add_succ'));
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($attributes, $faq_id, $this->log_data)));
    		
    		return redirect('faq/faq/edit?faq_id='.$faq_id);
    	}
    	
    	
    	//临时数据
    	$olddata = $this->session->flashdata('olddata');
    	
    	//分类列表
    	$categoryList = $this->Faq_category_model->getList(null, array('sort' => 'desc'), 0, 999);
    	
    	//分类列表
    	$columnList = $this->Faq_column_model->getList(null, array('sort' => 'desc'), 0, 999);
    	
    	//文章与栏目对应关系
    	$faqToColumnArr = array();
    	
    	return $this->load->view("faq/faq/add", array(
    			'olddata' => $olddata,
    			'categoryList' => $categoryList,
    			'columnList' => $columnList,
    			'faqToColumnArr' => $faqToColumnArr
    	));
    }
    
	public function edit()
	{
		$faq_id = $this->input->get('faq_id', true);
		if (!$faq_id)
		{
			$this->session->set_flashdata('error', '请传递记录ID');
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' faq_id empty '.serialize(array($this->log_data)));
			return redirect('faq/faq/index');
		}
		
		$info = $this->Faq_model->getInfo($faq_id);
		if (empty($info))
		{
			$this->session->set_flashdata('error', '记录不存在');
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' faq_id notexist '.serialize(array($faq_id, $this->log_data)));
			return redirect('faq/faq/index');
		}
		
		//提交表单
		if (is_post())
		{
			$this->form_validation->set_rules('category_id', 'category_id', valid_int_rule(true));
			$this->form_validation->set_rules('title', 'title', valid_string_rule(1, 60, true));
			$this->form_validation->set_rules('style', 'style', valid_string_rule(1, 255));
			$this->form_validation->set_rules('thumb', 'thumb', valid_string_rule(1, 255));
			$this->form_validation->set_rules('keywords', 'keywords', valid_string_rule(1, 255));
			$this->form_validation->set_rules('description', 'description', valid_string_rule(1, 255));
			$this->form_validation->set_rules('sort', 'sort', valid_int_rule());
			$this->form_validation->set_rules('status', 'status', valid_int_rule());
			$this->form_validation->set_rules('content', 'content', valid_text_rule());
			if (!$this->form_validation->run())
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string(), $this->log_data)));
				$this->session->set_flashdata('olddata', $this->input->post());
				$this->session->set_flashdata('error', '您填写的表单有误, 请检查'.$this->form_validation->error_string());
				return redirect('faq/faq/edit?faq_id='.$faq_id);
			}
			$category_id = $this->form_validation->set_value('category_id');
			$title = $this->form_validation->set_value('title');
			$style = $this->form_validation->set_value('style');
			$thumb = $this->form_validation->set_value('thumb');
			$keywords = $this->form_validation->set_value('keywords');
			$description = $this->form_validation->set_value('description');
			$sort = $this->form_validation->set_value('sort');
			$status = $this->form_validation->set_value('status');
			$content = $this->form_validation->set_value('content');
			
			//插入数据
			$attributes = array();
			$attributes['category_id'] = $category_id;
			$attributes['title'] = $title;
			$attributes['style'] = $style;
			$attributes['thumb'] = $thumb;
			$attributes['keywords'] = $keywords;
			$attributes['description'] = $description;
			$attributes['sort'] = $sort;
			$attributes['status'] = $status;
			$result = $this->Faq_model->updateInfo($faq_id, $attributes);
			
			//清空文章内容
			$this->Faq_content_model->deleteInfo($faq_id);
			
			//内容表
			$attributes = array();
			$attributes['faq_id'] = $faq_id;
			$attributes['content'] = htmlspecialchars($content, ENT_QUOTES);
			$this->Faq_content_model->insertInfo($attributes);
			
			if ($info['category_id'] != $category_id)
			{
				$attributes = array();
				$attributes['count'] = (int)1;
				$this->Faq_category_model->increment($category_id, $attributes);
				
				$attributes = array();
				$attributes['count'] = (int)1;
				$this->Faq_category_model->decrement($info['category_id'], $attributes);
			}
			
			
			
			//***处理栏目*****************************/
			$sorts = $this->input->post('sorts', true);
			$column_ids = $this->input->post('column_ids', true);
			
			//清除关联关系
			$faqToColumnList = $this->Faq_to_column_model->getList(array('faq_id' => $faq_id), array('sort' => 'desc'), 0, 999);
			if ($faqToColumnList)
			{
				foreach ($faqToColumnList as $item)
				{
					$this->Faq_to_column_model->deleteInfo($item['to_column_id']);
				}
			}
			
			//新增关联关系
			if ($column_ids)
			{
				foreach ($column_ids as $k => $v)
				{
					$attributes = array();
					$attributes['faq_id'] = $faq_id;
					$attributes['column_id'] = $k;
					$attributes['sort'] = isset($sorts[$k]) ? $sorts[$k] : 0;
					$attributes['create_time'] = time();
					$this->Faq_to_column_model->insertInfo($attributes);
				}
			}
			
				
			$this->session->set_flashdata('success', lang('form_edit_succ'));
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($faq_id, $attributes, $this->log_data)));
			return redirect('faq/faq/edit?faq_id='.$faq_id);
		}
		
		//临时数据
		$olddata = $this->session->flashdata('olddata');
		
		//分类列表
		$categoryList = $this->Faq_category_model->getList(null, array('sort' => 'desc'), 0, 999);
		
		//分类列表
		$columnList = $this->Faq_column_model->getList(null, array('sort' => 'desc'), 0, 999);
		
		//文章与栏目对应关系
		$faqToColumnList = $this->Faq_to_column_model->getList(array('faq_id' => $faq_id), array('sort' => 'desc'), 0, 999);
		$faqToColumnArr = array();
		if ($faqToColumnList)
		{
			foreach ($faqToColumnList as $item)
			{
				$faqToColumnArr[$item['column_id']] = $item;
			}
		}
		
		return $this->load->view("faq/faq/edit", array(
				'olddata' => $olddata,
				'info' => $info,
				'categoryList' => $categoryList,
				'columnList' => $columnList,
				'faqToColumnArr' => $faqToColumnArr
		));
	}
	
	public function del()
	{
		$faq_id = $this->input->get('faq_id', true);
	
		$faqInfo = $this->Faq_model->getInfo($faq_id);
		if (!$faqInfo)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($faq_id)));
			return redirect('faq/faq/index');
		}
	
		$this->Faq_model->deleteInfo($faq_id);
		
		$attributes = array();
		$attributes['count'] = (int)1;
		$this->Faq_category_model->decrement($faqInfo['category_id'], $attributes);
	
		$this->session->set_flashdata('success', lang('form_del_succ'));
		return redirect('faq/faq/index');
	}
	
	public function rebuild_indexer()
	{
		//检查进程是否存在
		$is_running = false;
		$exec_result = ps('rebuild');
		if (count($exec_result['data'])) //加上自身, 所以是2个及2个以上
		{
			$is_running = true;
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' exec $is_running '.json_encode(array($is_running, $exec_result)));
		}
		 
		if (is_post())
		{
			if ($is_running)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' task_isrunning ');
				$this->session->set_flashdata('error', lang('task_isrunning'));
				return redirect('faq/faq/rebuild_indexer');
			}
			
			$cron = "nohup /APP/user00/modules/php/bin/php ".FCPATH."index.php faq/command/rebuild_indexer > nohup.log 2>&1 &";
			@exec($cron);
			log_message('error', __CLASS__.' '.__FUNCTION__.' cron '.serialize($cron));
			
			log_message('error', __CLASS__.' '.__FUNCTION__.' succ ');
			$this->session->set_flashdata('success', '搜索引擎-索引重建工作已提交到后台运行, 请1分钟后生效');
			return redirect('faq/faq/rebuild_indexer');
		}
		
		return $this->load->view("faq/faq/rebuild_indexer", array(
				'message' => $is_running ? lang('task_isrunning') : ''
		));
	}
}
?>