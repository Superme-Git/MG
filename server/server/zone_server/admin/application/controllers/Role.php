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
class Role extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model(array('Role_model','Role_attribute_model'));
		$serverInfo = $this->session->userdata('serverlist');
		$this->Role_model->connect($serverInfo['hostname'], $serverInfo['port'], $serverInfo['username'], $serverInfo['password'], $serverInfo['database']);
		$this->Role_attribute_model->connect($serverInfo['hostname'], $serverInfo['port'], $serverInfo['username'], $serverInfo['password'], $serverInfo['database']);
		
		//设置分站点
		setSite($serverInfo['serverkey']);
		
	}

	public function index($avatar_verify = 0, $offset = 0)
	{
	    $avatar_verify = intval($avatar_verify);
		$offset < 1 && $offset = 0;
		$limit = 10;
		
		$where = array();
		if($avatar_verify == 0)
		{
		    $where['avatar_verify'] = 0;
		    $where['not']['avatar_url'] = '';
		}
		$roleList = $this->Role_attribute_model->getList($where, array('avatar_verify_time' => 'desc'), $offset, $limit, null, 0);
		$roleListCount = $this->Role_attribute_model->getCount($where);
		
		if ($roleList)
		{
		    foreach ($roleList as $k => $item)
		    {
		        $role_info = $this->Role_model->getInfo($item['roleid']);
		        $roleList[$k] = array_merge($item, $role_info);
		    }
		}
		
		
		//分页
		$pagination = page($roleListCount, $offset, $limit, $avatar_verify);
		
		$this->load->view('role/index', array(
		    'avatar_verify' => $avatar_verify,
				'list' => $roleList,
				'pagination' => $pagination,
		));
		
	}
	
	public function verify()
	{
		$roleid = $this->input->get('roleid', true);
		$verify = $this->input->get('verify', true);
		
		$roleInfo = $this->Role_attribute_model->getInfo($roleid);
		if (!$roleInfo)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' getInfo '.json_encode(array($roleInfo)));
			return redirect('role/index');
		}
		
		include APPPATH.'third_party/gm/Gm.php';
		$gm = new Gm();
		$serverInfo = $this->session->userdata('serverlist');
		$serverid = $serverInfo['serverkey'];
		
		if ($verify == 1)
		{
		    $type = 1;
		    $content = '您上传的头像已经审核成功，所有玩家都能看到您上传的头像了';
		}
		else
		{
		    $type = 2;
		    $content = '您上传的头像不符合条件，审核不通过。';
		}
		$result = $gm->mail_verify_avatar($roleid, $serverid, $type, $content);
		if ($result['errno'])
		{
		    log_message('error', __CLASS__.' '.__FUNCTION__.' mail_verify_avatar fail '.json_encode(array($result, $roleInfo)));
		    $this->session->set_flashdata('error', $result['message']);
		    return redirect('role/index');
		}
		
		$attribute = array();
		$attribute['avatar_verify'] = $verify;
		$attribute['avatar_verify_time'] = time();
		$this->Role_attribute_model->updateInfo($roleid, $attribute);
		$this->session->set_flashdata('success', '审核成功');
		return redirect('role/index');
	}
	
}
