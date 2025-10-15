<?php
/**
 * 命令控制器
 *
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 *
 */
class Command extends CI_Controller
{
	function __construct()
	{
		parent::__construct();
		
		error_reporting(-1);
		ini_set('display_errors', 1);
	}
	
	public function index($p)
	{
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' total '.serialize(array($p)));
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' total '.serialize(array(time())));
		//sleep(300);
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' total '.serialize(array(time())));
		
		var_dump(config_item('log_path'));
		
		var_dump($p);
		var_dump(time());
	}
	
	public function dashi_gift($serverkeys_str, $count)
	{
	    $dashi_roleid = config_item('dashi_roleid');
	    $serverkeys_arr = unserialize(base64_decode_($serverkeys_str));
	    
	    
	    
	    include APPPATH.'third_party/gm/Gm.php';
	    $gm = new Gm();
	    
	    foreach ($serverkeys_arr as $serverkey)
	    {
	        $result = $gm->set_dashi_gift_count($serverkey, $count);
	        if ($result['errno'])
	        {
	            $this->session->set_flashdata('error', $result['message']);
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' user exist '.serialize(array($serverkey, $count, $this->log_data)));
	            exit();
	        }
	    }
	    
	    
	}
	
	//大师推送脚本
	public function dashi_bbs($serverkeys_str, $content)
	{
	    $this->load->model(array('Serverlist_model'));
	    $this->Serverlist_model->connect_group('serverlist');
	    
	    
	    $dashi_roleid = config_item('dashi_roleid');
	    $serverkeys_arr = unserialize(base64_decode_($serverkeys_str));
	    $content = base64_decode_($content);
        
        foreach ($serverkeys_arr as $serverlist_id)
        {
            $where = array();
            $where['serverkey'] = $serverlist_id;
            $where['status'] = 0;
            $serverlist_info = $this->Serverlist_model->getInfoByAttribute($where, 0, false);
            
            
            $this->load->model(array('Bbs_model', 'Bbs_attribute_model'));
            $this->Bbs_model->connect($serverlist_info['hostname'], $serverlist_info['port'], $serverlist_info['username'], $serverlist_info['password'], $serverlist_info['database']);
            $this->Bbs_attribute_model->connect($serverlist_info['hostname'], $serverlist_info['port'], $serverlist_info['username'], $serverlist_info['password'], $serverlist_info['database']);
            
            $data = array();
            $data['roleid'] = $dashi_roleid;
            $data['to_roleid'] = $dashi_roleid;
            $data['gift_id'] = 0;
            $data['gift_count'] = 0;
            $data['reply_id'] = 0;
            $data['reply_roleid'] = 0;
            $data['create_time'] = time();
            $bbs_id = $this->Bbs_model->insertInfo($data);
            
            $data = array();
            $data['bbs_id'] = $bbs_id;
            $data['content'] = htmlspecialchars($content, ENT_QUOTES);//最长255, 多出的自动剔除
            $this->Bbs_attribute_model->insertInfo($data);
            
        }
        
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' run succ '.json_encode(array(microtime(true), count($data))));
	}
	
	/**
	 * 
	 * 角色id不变
	 * 
	 * @param unknown $from_servers
	 * @param unknown $to_server_key
	 */
	public function server_merge($from_server_keys, $to_server_key)
	{
	    $this->load->model(array('Serverlist_model', 'Server_merge_model'));
	    $this->Serverlist_model->connect_group('serverlist');
	    $this->Server_merge_model->connect_group('serverlist');
	    
	    $attr = array();
	    $attr['from_server_keys'] = $from_server_keys;
	    $attr['to_server_key'] = $to_server_key;
	    $attr['status'] = 0;
	    $attr['create_time'] = time();
	    $this->Server_merge_model->insertInfo($attr);
	    
	    
	    $from_server_keys = unserialize(base64_decode_($from_server_keys));
	    
	    //目标服务器
	    $to_server = $this->Serverlist_model->getInfo($to_server_key);
	    
	    $this->load->model(array('Role_model'));
	    $this->Role_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Role_attribute_model'));
	    $this->Role_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Role_relation_model'));
	    $this->Role_relation_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    
	    $this->load->model(array('Status_model'));
	    $this->Status_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Status_attribute_model'));
	    $this->Status_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Status_comment_model'));
	    $this->Status_comment_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Status_favorite_model'));
	    $this->Status_favorite_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    
	    $this->load->model(array('Bbs_model'));
	    $this->Bbs_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Bbs_attribute_model'));
	    $this->Bbs_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Bbs_gift_model'));
	    $this->Bbs_gift_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	    $this->load->model(array('Bbs_popularity_model'));
	    $this->Bbs_popularity_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
	     
	    
	    //执行合并
	    foreach ($from_server_keys as $from_server_key)
	    {
	        $from_server = $this->Serverlist_model->getInfo($from_server_key);
	        
	        //更改状态,做备注
	        $where = array();
	        $where['status'] = 1;
	        $where['mark'] = $from_server['mark'].';合服到'.$to_server['serverkey'];
	        $this->Serverlist_model->updateInfo($from_server['serverlist_id'], $where);
	        
	        //生成新记录
	        $attr = array();
	        $attr['serverkey'] = $from_server['serverkey'];
	        $attr['servername'] = $from_server['servername'];
	        $attr['hostname'] = $to_server['hostname'];
	        $attr['port'] = $to_server['port'];
	        $attr['username'] = $to_server['username'];
	        $attr['password'] = $to_server['password'];
	        $attr['database'] = $to_server['database'];
	        $attr['status'] = 0;
	        $attr['create_time'] = time();
	        $this->Serverlist_model->insertInfo($attr);
	        
	        
	        $this->load->model(array('Role_model' => 'from_Role_model'));
	        $this->from_Role_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Role_attribute_model' => 'from_Role_attribute_model'));
	        $this->from_Role_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Role_relation_model' => 'from_Role_relation_model'));
	        $this->from_Role_relation_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	         
	        $this->load->model(array('Status_model' => 'from_Status_model'));
	        $this->from_Status_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Status_attribute_model' => 'from_Status_attribute_model'));
	        $this->from_Status_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Status_comment_model' => 'from_Status_comment_model'));
	        $this->from_Status_comment_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Status_favorite_model' => 'from_Status_favorite_model'));
	        $this->from_Status_favorite_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	         
	        $this->load->model(array('Bbs_model' => 'from_Bbs_model'));
	        $this->from_Bbs_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Bbs_attribute_model' => 'from_Bbs_attribute_model'));
	        $this->from_Bbs_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Bbs_gift_model' => 'from_Bbs_gift_model'));
	        $this->from_Bbs_gift_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        $this->load->model(array('Bbs_popularity_model' => 'from_Bbs_popularity_model'));
	        $this->from_Bbs_popularity_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
	        
	        
	        //------ role ----------------------------
	        
	        $total_count = $this->from_Role_model->getCount(null, false);
	        $once_count = 2000;
	        $total_times = ceil($total_count / $once_count);
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role start '.json_encode(array($total_times, $total_count, $once_count)));
	        
	        for ($i = 0; $i < $total_times; $i++)
	        {
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
	            
	            $from_list = $this->from_Role_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
	            if ($from_list)
	            {
	                foreach ($from_list as $from_item)
	                {
	                    //角色表
	                    $attr = array();
	                    $attr['roleid'] = $from_item['roleid'];
	                    $attr['name'] = $from_item['name'];
	                    $attr['avatar'] = $from_item['avatar'];
	                    $attr['level'] = $from_item['level'];
	                    $this->Role_model->insertInfo($attr);
	                }
	            }
	            
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role end '.json_encode(array(microtime(true))));
	        }
	        
	        //------ role attribute ----------------------------
	        
	        //角色属性表
	        $total_count = $this->from_Role_attribute_model->getCount(null, false);
	        $once_count = 2000;
	        $total_times = ceil($total_count / $once_count);
	        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_attr start '.json_encode(array($total_times, $total_count, $once_count)));
	         
	        for ($i = 0; $i < $total_times; $i++)
	        {
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_attr page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
                $from_list = $this->from_Role_attribute_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
	            if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
						//角色表
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['msg_count'] = $from_item['msg_count'];
            	        $attr['signature'] = $from_item['signature'];
            	        $attr['popularity_count'] = $from_item['popularity_count'];
            	        $attr['gift_count'] = $from_item['gift_count'];
            	        $attr['sound_url'] = $from_item['sound_url'];
            	        $attr['avatar_url'] = $from_item['avatar_url'];
            	        $attr['avatar_verify'] = $from_item['avatar_verify'];
            	        $attr['place'] = $from_item['place'];
            	        $attr['longitude'] = $from_item['longitude'];
            	        $attr['latitude'] = $from_item['latitude'];
						$this->Role_attribute_model->insertInfo($attr);
					}
				}
	             
	            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_attr end '.json_encode(array(microtime(true))));
	        }
	         
	        
	         
	        //-------------角色关系表--------------------------
	        
			$total_count = $this->from_Role_relation_model->getCount(null, false);
			$once_count = 2000;
			$total_times = ceil($total_count / $once_count);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_rela start '.json_encode(array($total_times, $total_count, $once_count)));
			 
			for ($i = 0; $i < $total_times; $i++)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_rela page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
				$from_list = $this->from_Role_relation_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
				if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
						//角色表
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['friendid'] = $from_item['friendid'];
						$attr['relation'] = $from_item['relation'];
						$this->Role_relation_model->insertInfo($attr);
					}
				}
				 
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_rela end '.json_encode(array(microtime(true))));
			}


			//-------------动态表--------------------------
			
			$total_count = $this->from_Status_model->getCount(null, false);
			$once_count = 2000;
			$total_times = ceil($total_count / $once_count);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status start '.json_encode(array($total_times, $total_count, $once_count)));
			 
			
			$status_ids = array();
			for ($i = 0; $i < $total_times; $i++)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
				$from_list = $this->from_Status_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
				if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['status'] = $from_item['status'];
						$attr['create_time'] = $from_item['create_time'];
						$status_id = $this->Status_model->insertInfo($attr);
						
						//新老状态id对应数组
						$status_ids[$from_item['status_id']] = $status_id;
						
						$attr_info = $this->from_Status_attribute_model->getInfo($from_item['status_id']);
						$attr = array();
						$attr['status_id'] = $status_id;
						$attr['content'] = $attr_info['content'];
						$attr['img_url'] = $attr_info['img_url'];
						$attr['comment_count'] = $attr_info['comment_count'];
						$attr['favorite_count'] = $attr_info['favorite_count'];
						$this->Status_attribute_model->insertInfo($attr);
						
						log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status_comment start '.json_encode(array(microtime(true))));
						$where = array('status_id' => $from_item['status_id']);
						$order = array('status_comment_id' => 'asc');
						$comment_list = $this->from_Status_comment_model->getList($where, $order, 0, 99999999, null, false,false );
						if ($comment_list)
						{
						    foreach ($comment_list as $item)
						    {
						        $reply_id = 0;
						        if($item['reply_id'])
						        {
						            if (isset($status_ids[$item['reply_id']]))
						            {
						                $reply_id = $status_ids[$item['reply_id']];
						            }
						            else
						            {
						                log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status_comment merge error: notfind reply_id '.json_encode(array($item)));
						            }
						        }
						        
						        
						        $attr = array();
						        $attr['status_id'] = $status_id;
						        $attr['roleid'] = $item['roleid'];
						        $attr['content'] = $item['content'];
						        $attr['status'] = $item['status'];
						        $attr['reply_id'] = $reply_id;
						        $attr['reply_roleid'] = $item['reply_roleid'];
						        $attr['create_time'] = $item['create_time'];
						        $this->Status_comment_model->insertInfo($attr);
						    }
						}
						log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status_comment end '.json_encode(array(microtime(true))));
						
						log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status_favorite start '.json_encode(array(microtime(true))));
						$where = array('status_id' => $from_item['status_id']);
						$order = array('status_favorite_id' => 'asc');
						$favorite_list = $this->from_Status_favorite_model->getList($where, $order, 0, 99999999, null, false,false );
						if ($favorite_list)
						{
						    foreach ($favorite_list as $item)
						    {
						        $attr = array();
						        $attr['status_id'] = $status_id;
						        $attr['roleid'] = $item['roleid'];
						        $attr['status'] = $item['status'];
						        $attr['create_time'] = $item['create_time'];
						        $this->Status_favorite_model->insertInfo($attr);
						    }
						}
						log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status_favorite end '.json_encode(array(microtime(true))));
						
					}
				}
				 
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table status end '.json_encode(array(microtime(true))));
			}
	         
	        //-------留言板-------------------
			$total_count = $this->from_Bbs_model->getCount(null, false);
			$once_count = 2000;
			$total_times = ceil($total_count / $once_count);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs start '.json_encode(array($total_times, $total_count, $once_count)));
			 
			
			$bbs_ids = array();
			for ($i = 0; $i < $total_times; $i++)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
				$from_list = $this->from_Bbs_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
				if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
					    $reply_id = 0;
					    if($item['reply_id'])
					    {
					        if (isset($bbs_ids[$item['reply_id']]))
					        {
					            $reply_id = $bbs_ids[$item['reply_id']];
					        }
					        else
					        {
					            log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs merge error: notfind reply_id '.json_encode(array($item)));
					        }
					    }
					    
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['to_roleid'] = $from_item['to_roleid'];
						$attr['status'] = $from_item['status'];
						$attr['gift_id'] = $from_item['gift_id'];
						$attr['gift_count'] = $from_item['gift_count'];
						$attr['reply_id'] = $reply_id;
						$attr['reply_roleid'] = $from_item['reply_roleid'];
						$attr['create_time'] = $from_item['create_time'];
						$bbs_id = $this->Bbs_model->insertInfo($attr);
						
						//新老状态id对应数组
						$bbs_ids[$from_item['bbs_id']] = $bbs_id;
						
						$attr_info = $this->from_Bbs_attribute_model->getInfo($from_item['bbs_id']);
						$attr = array();
						$attr['bbs_id'] = $bbs_id;
						$attr['content'] = $attr_info['content'];
						$this->Bbs_attribute_model->insertInfo($attr);
						
					}
				}
				 
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs end '.json_encode(array(microtime(true))));
			}
			
			//--------留言板礼物-----------------
			
			$total_count = $this->from_Bbs_gift_model->getCount(null, false);
			$once_count = 2000;
			$total_times = ceil($total_count / $once_count);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs_gift start '.json_encode(array($total_times, $total_count, $once_count)));
			
			for ($i = 0; $i < $total_times; $i++)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs_gift page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
				$from_list = $this->from_Bbs_gift_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
				if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['to_roleid'] = $from_item['to_roleid'];
						$attr['status'] = $from_item['status'];
						$attr['gift_id'] = $from_item['gift_id'];
						$attr['gift_count'] = $from_item['gift_count'];
						$attr['reply_id'] = $reply_id;
						$attr['reply_roleid'] = $from_item['reply_roleid'];
						$attr['create_time'] = $from_item['create_time'];
						$this->Bbs_gift_model->insertInfo($attr);
					}
				}
				 
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs_gift end '.json_encode(array(microtime(true))));
			}			
			
			//--------留言板人气-----------------
						
			$total_count = $this->from_Bbs_popularity_model->getCount(null, false);
			$once_count = 2000;
			$total_times = ceil($total_count / $once_count);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs_popularity start '.json_encode(array($total_times, $total_count, $once_count)));
			
			for ($i = 0; $i < $total_times; $i++)
			{
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role_rela page '.json_encode(array($i.'/'.$total_times, $once_count, $total_count)));
				$from_list = $this->from_Bbs_popularity_model->getList(null, null, $i * $once_count, $once_count, null, false, false);
				if ($from_list)
				{
					foreach ($from_list as $from_item)
					{
						$attr = array();
						$attr['roleid'] = $from_item['roleid'];
						$attr['to_roleid'] = $from_item['to_roleid'];
						$attr['is_get'] = $from_item['is_get'];
						$attr['status'] = $from_item['status'];
						$attr['create_time'] = $from_item['create_time'];
						$this->Bbs_popularity_model->insertInfo($attr);
					}
				}
				 
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table bbs_popularity end '.json_encode(array(microtime(true))));
			}			
	        
	        //-----------end----------------------------
	        
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' end '.json_encode(array(microtime(true))));
	    }
	}
	
	
	public function server_merge_rollback($server_merge_id)
	{
	    $this->load->model(array('Serverlist_model', 'Server_merge_model'));
	    $this->Serverlist_model->connect_group('serverlist');
	    $this->Server_merge_model->connect_group('serverlist');
	    
	    
	    $info = $this->Server_merge_model->getInfo($server_merge_id);
	    if (!$info)
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' empty '.json_encode(array($this->log_data)));
	        exit('记录不存在');
	    }
	    elseif ($info['status'])
	    {
	        log_message('error', __CLASS__.' '.__FUNCTION__.' status error '.json_encode(array($this->log_data)));
	        exit('此操作已执行过, 不可重复执行');
	    }
	    
	    $where = array();
	    $where['status'] = 1;
	    $this->Server_merge_model->updateInfo($server_merge_id, $where);
	    
	    
		$from_server_keys = unserialize(base64_decode_($info['from_server_keys']));
		
		//目标服务器
		$to_server = $this->Serverlist_model->getInfo($info['to_server_key']);
		
		$this->load->model(array('Role_model'));
		$this->Role_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Role_attribute_model'));
		$this->Role_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Role_relation_model'));
		$this->Role_relation_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		
		$this->load->model(array('Status_model'));
		$this->Status_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Status_attribute_model'));
		$this->Status_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Status_comment_model'));
		$this->Status_comment_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Status_favorite_model'));
		$this->Status_favorite_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		
		$this->load->model(array('Bbs_model'));
		$this->Bbs_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Bbs_attribute_model'));
		$this->Bbs_attribute_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Bbs_gift_model'));
		$this->Bbs_gift_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		$this->load->model(array('Bbs_popularity_model'));
		$this->Bbs_popularity_model->connect($to_server['hostname'], $to_server['port'], $to_server['username'], $to_server['password'], $to_server['database']);
		 
		
		//执行合并
		foreach ($from_server_keys as $from_server_key)
		{
		    //-------------------------------------------
		    
			$from_server = $this->Serverlist_model->getInfo($from_server_key);
			
			$where = array();
			$where['serverkey'] = $from_server['serverkey'];
			$where['status'] = 0;
			$from_server_last = $this->Serverlist_model->getInfoByAttribute($where, 0, false);
			
			//关闭当前的服务器
			$where = array();
			$where['status'] = 1;
			$where['mark'] = $from_server_last['mark'].';合服恢复,关闭';
			$this->Serverlist_model->updateInfo($from_server_last['serverlist_id'], $where);
			
			//开启原服务器
			$where = array();
			$where['status'] = 0;
			$where['mark'] = $from_server['mark'].';合服恢复,启用';
			$this->Serverlist_model->updateInfo($from_server['serverlist_id'], $where);
			
			
			//-------------------------------------------
			
			
			$this->load->model(array('Role_model' => 'from_Role_model'));
			$this->from_Role_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Role_attribute_model' => 'from_Role_attribute_model'));
			$this->from_Role_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Role_relation_model' => 'from_Role_relation_model'));
			$this->from_Role_relation_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			 
			$this->load->model(array('Status_model' => 'from_Status_model'));
			$this->from_Status_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Status_attribute_model' => 'from_Status_attribute_model'));
			$this->from_Status_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Status_comment_model' => 'from_Status_comment_model'));
			$this->from_Status_comment_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Status_favorite_model' => 'from_Status_favorite_model'));
			$this->from_Status_favorite_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			 
			$this->load->model(array('Bbs_model' => 'from_Bbs_model'));
			$this->from_Bbs_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Bbs_attribute_model' => 'from_Bbs_attribute_model'));
			$this->from_Bbs_attribute_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Bbs_gift_model' => 'from_Bbs_gift_model'));
			$this->from_Bbs_gift_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			$this->load->model(array('Bbs_popularity_model' => 'from_Bbs_popularity_model'));
			$this->from_Bbs_popularity_model->connect($from_server['hostname'], $from_server['port'], $from_server['username'], $from_server['password'], $from_server['database']);
			
			
			//------ role ----------------------------
			
			
			$total_count = $this->Role_model->getCount(null, false);
			$from_list = $this->Role_model->getList(null, null, 0, 9999999, null, false, false);
			
			if ($from_list)
			{
				foreach ($from_list as $i => $from_item)
				{
					log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' foreach '.json_encode(array($i.'/'.$total_count)));
					
					$this->Role_model->deleteInfo($from_item['roleid']);
					
					$this->Role_attribute_model->deleteInfo($from_item['roleid']);
					
					$this->Role_relation_model->remove_info($from_item['roleid']);
					
					//---------------------------
					
					$where = array();
					$where['roleid'] = $from_item['roleid'];
					$list_ = $this->Status_model->getList($where, null, 0, 9999999, null, 0, false);
					if ($list_)
					{
						foreach ($list_ as $item_)
						{
							$this->Status_model->deleteInfo($item_['status_id']);
							$this->Status_attribute_model->deleteInfo($item_['status_id']);
							
							$where = array('status_id' => $item_['status_id']);
							$comment_list = $this->Status_comment_model->getList($where, null, 0, 999999, null, 0, false );
							if ($comment_list)
							{
								foreach ($comment_list as $item)
								{
									$this->Status_comment_model->deleteInfo($item_['status_comment_id']);
								}
							}
							
							$where = array('status_id' => $item_['status_id']);
							$comment_list = $this->Status_favorite_model->getList($where, null, 0, 999999, null, 0, false );
							if ($comment_list)
							{
								foreach ($comment_list as $item)
								{
									$this->Status_favorite_model->deleteInfo($item_['status_favorite_id']);
								}
							}
							
						}
					}
					
					
					//-----------------------------------------
					
					$where = array();
					$where['roleid'] = $from_item['roleid'];
					$list_ = $this->Bbs_model->getList($where, null, 0, 9999999, null, 0, false);
					if ($list_)
					{
						foreach ($list_ as $item_)
						{
							$this->Bbs_model->deleteInfo($item_['bbs_id']);
							$this->Bbs_attribute_model->deleteInfo($item_['bbs_id']);
						}
					}
					
					$where = array();
					$where['roleid'] = $from_item['roleid'];
					$list_ = $this->Bbs_gift_model->getList($where, null, 0, 9999999, null, 0, false);
					if ($list_)
					{
						foreach ($list_ as $item_)
						{
							$this->Bbs_gift_model->deleteInfo($item_['bbs_gift_id']);
						}
					}
					
					$where = array();
					$where['roleid'] = $from_item['roleid'];
					$list_ = $this->Bbs_popularity_model->getList($where, null, 0, 9999999, null, 0, false);
					if ($list_)
					{
						foreach ($list_ as $item_)
						{
							$this->Bbs_popularity_model->deleteInfo($item_['bbs_popularity_id']);
						}
					}
					
				}
				
				
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' table role end '.json_encode(array(microtime(true))));
			}
			
			//-----------end----------------------------
			
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' end '.json_encode(array(microtime(true))));	    
	    
		}
	}
	
	
	
}