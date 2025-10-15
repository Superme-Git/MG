<?php
/**
 * 命令控制器
 *
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2016.03.16
 *
 */
class Command extends CI_Controller
{
	public function __construct()
	{
		parent::__construct();
	}
	
	public function index($p)
	{
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' total '.serialize(array($p)));
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' total '.serialize(array(time())));
		
		sleep(300);
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' total '.serialize(array(time())));
	}
	
	//php index.php faq/command/transfer_record '201601'
	public function transfer_record($month = null)
	{
		$this->load->model(array("Faq_record_model", "Faq_record_subtable_model"));
		
		if (!$month)
		{
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $month params error '.serialize(array($month)));
    		exit();
		}
		
		$attributes = array();
    	$attributes['key'] = $month;
    	$recordSubtableInfo = $this->Faq_record_subtable_model->getInfoByAttribute($attributes);
    	if (!$recordSubtableInfo)
    	{
    		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $month notexist '.serialize(array($month)));
    		exit();
    	}
		
		//获取统计数
		$wheres = array();
		$wheres['month'] = $month;
		$count = $this->Faq_record_model->getCount($wheres);
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $month $count '.serialize(array($month, $count)));
		
		$once = 1999;
		$total = ceil($count/$once);
		
		for ($i = 1;$i<=$total;$i++)
		{
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' run '.serialize(array($i.'/'.$total, $month)));
			
			$this->Faq_record_model->setSubtableSuffix('');
			$list = $this->Faq_record_model->getList($wheres, array('create_time' => 'desc'), 0, $once);
			
			if ($list)
			{
				foreach ($list as $item)
				{
					//插入新表数据
					$attributes = $item;
					$this->Faq_record_model->setSubtableSuffix($month);
					$this->Faq_record_model->insertInfo($attributes);
				}
				
				foreach ($list as $item)
				{
					//删除旧表数据
					$this->Faq_record_model->setSubtableSuffix('');
					$this->Faq_record_model->deleteInfo($item['record_id']);
				}
				
				$attributes = array();
				$attributes['count'] = count($list);
				$this->Faq_record_subtable_model->increment($recordSubtableInfo['record_subtable_id'], $attributes);
				
			}
			
			sleep(1);
		}
	
	}
	
	public function rebuild_indexer()
	{
		
		include(APPPATH.'config/database.php');
		$path = config_item('xunsearch_sdk');
		
		$cmd_user = '/APP/user00/modules/php/bin/php '.$path.'/php/util/Indexer.php ';
		$mysql = 'mysql://'.$db['write']['username'].':'.$db['write']['password'].'@'.$db['write']['hostname'].'/'.$db['write']['database'].' ';
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $mysql '.json_encode(array($mysql)));
		
		$cmd = $cmd_user.' --rebuild --source='.$mysql.' --sql="select faq_id,title as subject,description,create_time from faq" --project=mt3_jingling';
		@exec($cmd, $return, $errno);
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' exec data '.json_encode(array($cmd, $return, $errno)));
		if ($errno)
		{
			$cmd = $cmd_user.' --stop-rebuild --project=mt3_jingling';
			@exec($cmd, $return, $errno);
		}
		
		//间隔时间
		sleep(1);
		
		$cmd = $cmd_user.' --rebuild --source='.$mysql.' --sql="select faq_id,concat(title,\' \',keywords) as subject,description,create_time from faq" --project=mt3_jingling_keyword';
		@exec($cmd, $return, $errno);
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' exec data '.json_encode(array($cmd, $return, $errno)));
		if ($errno)
		{
			$cmd = $cmd_user.' --stop-rebuild --project=mt3_jingling_keyword';
			@exec($cmd, $return, $errno);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' exec data '.json_encode(array($cmd, $return, $errno)));
			
		}
		
		//间隔时间
		sleep(60);
		
		$cmd_arr = config_item('sync_cron');
		if ($cmd_arr)
		{
		    foreach ($cmd_arr as $cmd)
		    {
		        @exec($cmd, $return, $errno);
		        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' exec data '.json_encode(array($cmd, $return, $errno)));
		       
		    }
		}
		
	}
	
}