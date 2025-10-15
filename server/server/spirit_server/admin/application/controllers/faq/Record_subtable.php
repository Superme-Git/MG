<?php
/**
 * APP管理
 * 
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 */
class Record_subtable extends MY_Controller
{
    function __construct()
    {
        parent::__construct();
        $this->load->model(array("Faq_record_model", "Faq_record_subtable_model"));
    }
    
    public function index($offset =0)
    {
        $limit = 15;
        
    	$list = $this->Faq_record_subtable_model->getList(null, array('sort' => 'desc'));
    	
    	$record_count = $this->Faq_record_model->getCount(null);
    	 
    	return $this->load->view("faq/record_subtable/index", array(
    			'list' => $list,
    			'record_count' => $record_count
    	));
    }
    
    public function add()
    {
    	//提交表单
    	if (is_post())
    	{
    		$this->form_validation->set_rules('key', 'key', valid_string_rule(1, 30, true));
    		$this->form_validation->set_rules('name', 'name', valid_string_rule(1, 30, true));
    		$this->form_validation->set_rules('sort', 'sort', valid_int_rule());
    		if (!$this->form_validation->run())
    		{
    			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    			$this->session->set_flashdata('olddata', $this->input->post());
    			$this->session->set_flashdata('error', $this->form_validation->error_string());
    			return redirect('faq/record_subtable/add');
    		}
    		$key = $this->form_validation->set_value('key');
    		$name = $this->form_validation->set_value('name');
    		$sort = $this->form_validation->set_value('sort');
    		
    		//校验是否存在
    		$attributes = array();
    		$attributes['key'] = $key;
    		$info_ = $this->Faq_record_subtable_model->getInfoByAttribute($attributes);
    		if ($info_)
    		{
    			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($attributes, $this->log_data)));
    			$this->session->set_flashdata('error', '分表 '.$key.' 已存在, 不能重复创建');
    			return redirect('faq/record_subtable/index');
    		}
    		
    		//创建分表
    		$result = $this->Faq_record_model->create_subtable($key);
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' create_subtable '.serialize(array($key, $result)));
    		
    		//插入记录
    		$attributes = array();
    		$attributes['key'] = $key;
    		$attributes['name'] = $name ? $name : $key;
    		$attributes['sort'] = (int)$sort;
    		$attributes['create_time'] = date('Y-m-d H:i:s');
    		$oauth_id = $this->Faq_record_subtable_model->insertInfo($attributes);
    		
    			
    		$this->session->set_flashdata('success', lang('form_add_succ') .', 分表后, 请转移数据');
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($attributes)));
    		return redirect('faq/record_subtable/index');
    	}
    	
    	//查询可供分表的月份
    	$attributes = array();
    	$attributes['lessthan']['month'] = date('Ym');
    	$recordList = $this->Faq_record_model->getList($attributes, null, 0, 99, 'month');
    	if (!$recordList)
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($attributes, $this->log_data)));
    		$this->session->set_flashdata('error', '没有可供分表的数据, 不能创建分表.');
    		return redirect('faq/record_subtable/index');
    	}
    	
    	return $this->load->view("faq/record_subtable/add", array(
    			'recordList' => $recordList,
    	));
    }
    
    public function edit($Faq_record_subtable_id)
    {
    	$info = $this->Faq_record_subtable_model->getInfo($Faq_record_subtable_id);
    	if (!$info)
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($Faq_record_subtable_id, $this->log_data)));
    		$this->session->set_flashdata('error', '分表不存在');
    		return redirect('faq/record_subtable/index');
    	}
    	
    	
    	//提交表单
    	if (is_post())
    	{
    		//$this->form_validation->set_rules('key', 'key', valid_string_rule(1, 30, true));
    		$this->form_validation->set_rules('name', 'name', valid_string_rule(1, 30, true));
    		$this->form_validation->set_rules('sort', 'sort', valid_int_rule());
    		if (!$this->form_validation->run())
    		{
    			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string(), $this->log_data)));
    			$this->session->set_flashdata('olddata', $this->input->post());
    			$this->session->set_flashdata('error', $this->form_validation->error_string());
    			return redirect('faq/record_subtable/edit/'.$Faq_record_subtable_id);
    		}
    		//$key = $this->form_validation->set_value('key');
    		$name = $this->form_validation->set_value('name');
    		$sort = $this->form_validation->set_value('sort');
    
    		$attributes = array();
    		//$attributes['key'] = $key;//不能修改
    		$attributes['name'] = $name;
    		$attributes['sort'] = (int)$sort;
    		$this->Faq_record_subtable_model->updateInfo($Faq_record_subtable_id, $attributes);
    
    		$this->session->set_flashdata('success', lang('form_edit_succ').', 分表后, 请转移数据');
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($attributes)));
    		return redirect('faq/record_subtable/index');
    	}
    	 
    	return $this->load->view("faq/record_subtable/edit", array(
    			'info' => $info,
    	));
    }
    
    public function del($record_subtable_id)
    {
    	$recordSubtableInfo = $this->Faq_record_subtable_model->getInfo($record_subtable_id);
    	if (!$recordSubtableInfo)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($record_subtable_id)));
    		$this->session->set_flashdata('error', 'record_subtable_id error');
    		return redirect('faq/record_subtable/index');
    	}
    	
    	//已有数据, 不能删除
    	if ($recordSubtableInfo['count'] > 0)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($record_subtable_id)));
    		$this->session->set_flashdata('error', '已有数据, 不能删除');
    		return redirect('faq/record_subtable/index');
    	}
    	
    
    	$this->Faq_record_subtable_model->deleteInfo($record_subtable_id);
    
    	$this->session->set_flashdata('success', lang('form_del_succ'));
    	return redirect('faq/record_subtable/index');
    }
    
    public function transfer($key)
    {
    	//检查进程是否存在
    	$is_running = false;
    	$exec_result = ps('transfer_record');
    	if (count($exec_result['data'])) //加上自身, 所以是2个及2个以上
    	{
    		$is_running = true;
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' exec $is_running '.json_encode(array($is_running, $exec_result)));
    	}
    	
    	if ($is_running)
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' task_isrunning ');
    		$this->session->set_flashdata('error', lang('task_isrunning'));
    		return redirect('faq/record_subtable/index');
    	}
    	
    	
    	$attributes = array();
    	$attributes['key'] = $key;
    	$info_ = $this->Faq_record_subtable_model->getInfoByAttribute($attributes);
    	if (!$info_)
    	{
    		log_message('error', __CLASS__.' '.__FUNCTION__.' getInfoByAttribute '.json_encode(array($key)));
    		$this->session->set_flashdata('error', 'key 不存在');
    		return redirect('faq/record_subtable/index');
    	}
    
    	//--纳入后台任务, 执行数据分离-------
	    $cmd = "nohup /APP/user00/modules/php/bin/php ".FCPATH."index.php faq/command/transfer_record '".$key."'  > nohup.log 2>&1 &";
	    @exec($cmd);
	    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' exec '.json_encode(array($cmd)));
	    //--------------------
    
    	$this->session->set_flashdata('success', lang('cmd_running_succ'));
    	return redirect('faq/record_subtable/index');
    }
    
}
?>