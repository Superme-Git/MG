<?php
/**
 * Faq
 * 
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.15
 */
class Faq extends MY_Controller
{
    public function __construct()
    {
        parent::__construct();
    }
    
    //1.1热点问题, 第一栏内容
    public function getListByColumn()
    {
    	$column_id = $this->input->get('column_id', true);
    	
    	$this->load->model(array('Faq_to_column_model', 'Faq_model'));
    	
    	$where = array();
    	$where['column_id'] = $column_id;//$column_info['column_id'];
    	$order = array();
    	$order['sort'] = 'desc';
    	$order['create_time'] = 'desc';
    	$list = $this->Faq_to_column_model->getList($where, $order, 0, 999);
    	
    	$faqList = array();
    	if ($list)
    	{
    		foreach ($list as $item)
    		{
    			$faqInfo = $this->Faq_model->getInfo($item['faq_id']);
    			$faqList[] = array(
    					'faq_id' => $faqInfo['faq_id'],
    					'title' => $faqInfo['title']
    			);
    		}
    	}
    	
    	return response($faqList);
    }
    
    
    public function getListByCategory()
    {
    	$category_id = $this->input->get('category_id', true);
    	
    	$this->load->model(array('Faq_model'));
    	
    	$where = array();
    	$where['category_id'] = $category_id;
    	$order = array();
    	$order['sort'] = 'desc';
    	$order['create_time'] = 'desc';
    	$faqList = $this->Faq_model->getList($where, $order, 0, 99);
    	
    	return response($faqList);
    }
    
    
    //4获得帖子详情
    public function detail()
    {
    	$faq_id = (int)$this->input->get('faq_id', true);
    	
    	if (!$faq_id)
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' faq_id_empty '.serialize(array($faq_id, $this->log_data)));
    		return response('', '4001', 'faq_id_empty');
    	}
    	
    	$this->load->model(array('Faq_model', 'Faq_content_model'));
    	
    	$faqInfo = $this->Faq_model->getInfo($faq_id);
    	
    	if (!$faqInfo)
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' not_found '.serialize(array($faq_id, $this->log_data)));
    		return response('', '4002', 'faq_not_found');
    	}
    	
    	$content = $this->Faq_content_model->getInfo($faq_id);
    	$faqInfo['content'] = htmlspecialchars_decode($content['content'], ENT_QUOTES);
    	
    	return response($faqInfo);
    }
    
    //5反馈接口
    public function feedback()
    {
    	$userkey = trim($this->input->get('userkey', true));
    	$from_keyword = trim($this->input->get('from_keyword', true));
    	$faq_id = (int)$this->input->get('faq_id', true);
    	$feedback_result = (int)$this->input->get('feedback_result', true);
    	
    	if (!$userkey)
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $userkey empty '.serialize(array($userkey, $this->log_data)));
    		return response('', '5001', 'userkey_empty');
    	}
    	
    	if (!$from_keyword)
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $from_keyword empty '.serialize(array($from_keyword, $this->log_data)));
    		return response('', '5002', 'from_keyword_empty');
    	}
    	
    	
    	if (!$faq_id)
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $faq_id empty '.serialize(array($faq_id, $this->log_data)));
    		return response('', '5003', 'faq_id_empty');
    	}
    	
    	if (!$feedback_result)
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $feedback_result empty '.serialize(array($feedback_result, $this->log_data)));
    		return response('', '5004', 'feedback_result_empty');
    	}
    	
    	
    	//保存记录
    	$this->load->model(array('Record_model'));
    	$attrs = array();
    	$attrs['userkey'] = $userkey;
    	$attrs['keyword'] = $from_keyword;
    	$attrs['answer_faq_id'] = $faq_id;
    	$attrs['feedback_result'] = $feedback_result;
    	$attrs['month'] = date('Ym');
    	$attrs['create_time'] = time();
    	$this->Record_model->insertInfo($attrs);
    	
    	return response('1');
    }
    
    //6搜索接口
    public function search()
    {
    	//接收参数
    	$q = trim($this->input->get('q', true));
    	$m = trim($this->input->get('m', true));
    	$f = trim($this->input->get('f', true));
    	$s = trim($this->input->get('s', true));
    	$p = trim($this->input->get('p', true));
    	$syn = trim($this->input->get('syn', true));
    	 
    	if (empty($q))
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $q_empty '.serialize(array($q, $this->log_data)));
    		return response('', '6001', 'q_empty');
    	}
    	 
    	if (empty($m))
    	{
    		$m = 'yes';
    	}
    	
    	//根据关键词检索
    	require_once APPPATH . '/libraries/XunSearch.php';
    	$xunSearch = new XunSearch('mt3_jingling_keyword');
    	$result = $xunSearch->getSearch($q, $m, $f, $s, $p, $syn);
    	
    	if (!empty($result['list']) && is_array($result['list'][0]))
    	{
    		$faq_id = $result['list'][0]['faq_id'];
    		$title = $result['list'][0]['title'];
    		
    		//-必须完全匹配关键词其中之一
    		if (strpos($title, ' '))
    		{
    			$keywords = explode(' ', $title);
    		}
    		elseif (strpos($title, ','))
    		{
    			$keywords = explode(',', $title);
    		}
    		else 
    		{
    			$keywords = array($title);
    		}
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' test '.serialize(array($q, $keywords, $this->log_data)));
    		
    		//-必须完全匹配关键词其中之一
    		if (in_array($q, $keywords))
    		{
    			$this->load->model(array('Faq_model', 'Faq_content_model'));
    			
    			$faqInfo = $this->Faq_model->getInfo($faq_id);
    			
    			if ($faqInfo)
    			{
    				$content = $this->Faq_content_model->getInfo($faq_id);
    				$faqInfo['content'] = htmlspecialchars_decode($content['content'], ENT_QUOTES);
    				 
    				$result['info'] = $faqInfo;
    				$result['list'] = '';
    				unset($result['list']);
    				return response($result);
    			}
    		}
    		
    	}
    	
    	//模糊匹配 
    	$xunSearch = new XunSearch('mt3_jingling');
    	$result = $xunSearch->getSearch($q, $m, $f, $s, $p, $syn);
    	
    	if (!empty($result['list']) && is_array($result['list'][0]))
    	{
    	    $faq_id = $result['list'][0]['faq_id'];
    	    $title = $result['list'][0]['title'];
    	
    	    //-必须完全匹配关键词其中之一
    	    if ($q == $title)
    	    {
    	        $this->load->model(array('Faq_model', 'Faq_content_model'));
    	         
    	        $faqInfo = $this->Faq_model->getInfo($faq_id);
    	         
    	        if ($faqInfo)
    	        {
    	            $content = $this->Faq_content_model->getInfo($faq_id);
    	            $faqInfo['content'] = htmlspecialchars_decode($content['content'], ENT_QUOTES);
    	            	
    	            $result['info'] = $faqInfo;
    	            $result['list'] = '';
    	            unset($result['list']);
    	            return response($result);
    	        }
    	    }
    	
    	}
    	
    	return response($result);
    }
    
    //7热词
    public function hotquery()
    {
    	require_once APPPATH . '/libraries/XunSearch.php';
    	$xunSearch = new XunSearch('mt3_jingling');
    	 
    	$result = $xunSearch->getHotQuery();
    	 
    	return response($result);
    }
    
    
}
?>