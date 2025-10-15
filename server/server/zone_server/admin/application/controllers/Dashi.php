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
class Dashi extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		
		$this->load->model(array('Serverlist_model'));
		$this->Serverlist_model->connect_group('serverlist');
		
	}
	
	public function index()
	{
	    return redirect('dashi/bbs');
	}
	

	public function gift()
	{
		if (is_post())
		{
		    $server_ids = $this->input->post('server_ids');
		    $gift_count = $this->input->post('gift_count');
		    
		    
		    if (!$server_ids)
		    {
		        $this->session->set_flashdata('error', '请至少选择一个服务器');
		        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $server_ids not exist '.serialize(array($this->log_data)));
		        return redirect('dashi/gift');
		    }
		    
		    if (!$gift_count)
		    {
		        $this->session->set_flashdata('error', '礼物数必须大于0');
		        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $gift_count <1 '.serialize(array($this->log_data)));
		        return redirect('dashi/gift');
		    }
		    
		    if ($server_ids)
		    {
		        $server_ids = base64_encode_(serialize($server_ids));
		        
		        $cmd = "nohup php ".FCPATH."index.php command/dashi_gift $server_ids $gift_count > nohup.log 2>&1 &";
		        @exec($cmd);
		        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' restart, exec '.json_encode(array($cmd)));
		    }
		    
		    $this->session->set_flashdata('success', lang('form_edit_succ'));
		    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($this->log_data)));
		    return redirect('dashi/gift');
		}
		
		$where = array();
		$where['status'] = 0;
		$server_list = $this->Serverlist_model->getList($where, null, 0, 99999);
		if ($server_list)
		{
		    include APPPATH.'third_party/gm/Gm.php';
		    $gm = new Gm();
		    
		    foreach ($server_list as $k => $server_info)
		    {
		        $result = $gm->get_dashi_gift_count($server_info['serverkey']);
		        if ($result['errno'])
		        {
		            $server_list[$k]['amount'] = -1;
		        }
		        else
		        {
		            $server_list[$k]['amount'] = $result['data'];
		        }
		    }
		}
		
		
		return $this->load->view('dashi/gift', array(
				'server_list' => $server_list,
		));
		
	}
	
	public function bbs()
	{
	    if (is_post())
	    {
	        $server_ids = $this->input->post('server_ids');
	        $content = $this->input->post('content');
	         
	        if (!$server_ids)
	        {
	            $this->session->set_flashdata('error', '请至少选择一个服务器');
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $server_ids not exist '.serialize(array($this->log_data)));
	            return redirect('dashi/bbs');
	        }
	         
	        if (strlen($content) < 1)
	        {
	            $this->session->set_flashdata('error', '留言内容必须填写');
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $content empty '.serialize(array($this->log_data)));
	            return redirect('dashi/bbs');
	        }
	         
	        if ($server_ids)
	        {
	            $server_ids = base64_encode_(serialize($server_ids));
	            $content = base64_encode_($content);
	            
	            $cmd = "nohup ".config_item('php_bin')." ".FCPATH."index.php command/dashi_bbs $server_ids $content > nohup.log 2>&1 &";
	            @exec($cmd);
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' restart, exec '.json_encode(array($cmd)));
	             
	        }
	         
	        $this->session->set_flashdata('success', lang('cmd_running_succ'));
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_edit_succ '.serialize(array($this->log_data)));
	        return redirect('dashi/bbs');
	    }
	
	    //服务器列表
	    
	    $where = array();
	    $where['status'] = 0;
	    $server_list = $this->Serverlist_model->getList($where, null, 0, 99999);
	     
	    return $this->load->view("dashi/bbs", array(
	        'server_list' => $server_list,
	    ));
	
	}
	
}
