<?php
/**
 * 管理首页
 *
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 */
class Index extends MY_Controller
{
	function __construct()
	{
		parent::__construct();
		
		$this->load->model("Admin_model");
	}

	public function index()
	{
		
		 
		return $this->load->view("index/index", array());
	}
	
	public function login()
	{
		if (get_admininfo())
		{
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' logined '.serialize(array(get_admininfo())));
			return redirect('/index');
		}
	
		//提交表单
		if (is_post())
		{
			$this->form_validation->set_rules('email', 'email', valid_email_rule(true));
			$this->form_validation->set_rules('password', 'password', valid_string_rule(true));
			$this->form_validation->set_rules('remember', 'remember', valid_int_rule());
			if (!$this->form_validation->run())
			{
				$message = $this->form_validation->error_string();
				$this->session->set_flashdata('error', $message);
				$this->session->set_flashdata('olddata', $this->input->post());
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' form_validation '.serialize(array($this->form_validation->error_string(), $this->log_data)));
				return redirect('/index/login');
			}
			$email = $this->form_validation->set_value('email');
			$password = $this->form_validation->set_value('password');
			$remember = $this->form_validation->set_value('remember');
			 
			//根据邮件获取用户信息
			$adminInfo = $this->Admin_model->getInfoByAttribute(array('email' => $email), 0, false);
			if (!$adminInfo)
			{
				$this->session->set_flashdata('error', '管理员不存在');
				$this->session->set_flashdata('olddata', $this->input->post());
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' email notexist  '.serialize(array($this->log_data)));
				return redirect('/index/login');
			}
			 
			 
			//校验密码
			if($adminInfo["password"] != $password)
			{
				$this->session->set_flashdata('error', '密码错误');
				$this->session->set_flashdata('olddata', $this->input->post());
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' password error '.serialize(array($adminInfo, $this->log_data)));
				return redirect('/index/login');
			}
			 
			/******登录成功******/
				
			//设置session
			$dataArray = array(
					"admin_id"=>$adminInfo["admin_id"],
					"email"=>$adminInfo["email"],
					"create_time"=>$adminInfo["create_time"],
			);
				
			//获取用户角色
// 			$roles = $this->Admin_model->getUserRoles($adminInfo["admin_id"]);
// 			$dataArray['roles'] = $roles;
	
// 			//获取用户的权限
// 			$accesses = array();
// 			$accessList = $this->Admin_model->getUserAccesses($adminInfo["admin_id"]);
// 			if ($accessList)
// 			{
// 				foreach ($accessList as $item)
// 				{
// 					$accesses[] = $item['path'];
// 				}
// 			}
// 			$dataArray['accesses'] = $accesses;
			
			//保存到session
			$this->session->set_userdata(array('admin_info' => $dataArray));
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' succ '.serialize(array($this->log_data)));
				
			return redirect('index');
		}
		
		$olddata = $this->session->flashdata('olddata');
		
		return $this->load->view("index/login", array(
				'olddata' => $olddata
		));
	}
	
	//退出
	public function logout()
	{
		$sessionData = array(
				'admin_info' => "",
		);
		$this->session->set_userdata($sessionData);
		
		return redirect('/index/login');
	}
	
	
	public function uploadfile_to_cdn()
	{
		if (is_post())
		{
			set_time_limit(0);
			ini_set("memory_limit", "1024M");
			header("Content-type: text/html; charset=utf-8");
	
			$file_path = upload_file($_FILES['imgFile']);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' upload_file $file_path '.serialize(array($file_path)));
	
			$result = upload_to_cdn($file_path);
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' upload_to_cdn $file_path '.serialize(array($result)));
	
			echo json_encode($result);
			return ;
		}
	}
	
	public function kindEditorUploadImg()
	{
		//pic
		if (!empty($_FILES['imgFile']))
		{
			$configs = array(
				'upload_path' => FCPATH . 'resource/uploads/images/'.date('Y/m/d/'),//
				'allowed_types' => 'gif|jpg|jpeg|png|txt|pdf|rar|zip|doc|docx|xls',
				'file_ext_tolower' => true,//如果设置为 TRUE ，文件后缀名将转换为小写
				'max_size' => '2048',//允许上传文件大小的最大值（单位 KB），设置为 0 表示无限制
				'max_width' => '2048',//图片的最大宽度（单位为像素），设置为 0 表示无限制
				'max_height' => '2048',//图片的最大高度（单位为像素），设置为 0 表示无限制
				'encrypt_name' => true //文件名将会转换为一个随机的字符串 如果你不希望上传文件的人知道保存后的文件名，这个参数会很有用
			);
			$this->load->library('upload');
			$this->upload->initialize($configs);
			if (!is_dir($configs['upload_path']))
			{
				@mkdir($configs['upload_path'],0777,true);
			}
	
			if ($this->upload->do_upload('imgFile'))
			{
				$file_info = $this->upload->data();
				log_message('error', __CLASS__.' '.__FUNCTION__.' upload_file succ '.json_encode(array($file_info)));
				$file_info['full_path'] = str_replace(FCPATH, '', $file_info['full_path']);
				
				
				//同步到cdn服务器
				$result = upload_to_cdn($file_info['full_path']);
				if ($result['errno'])
				{
					echo json_encode($result);
					exit;
				}
				log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' upload_to_cdn $file_path '.serialize(array($result)));
				echo json_encode($result);
				exit;
				
				//只上传到本地
// 				$ret = array();
// 				$ret['error'] = 0;
// 				$ret['url'] = base_url($file_info['full_path']);
	
// 				echo json_encode($ret);
// 				exit;
			}
			else
			{
				$upload_error = $this->upload->display_errors();
				log_message('error', __CLASS__.' '.__FUNCTION__.' upload_file error '.json_encode(array($upload_error)));
	
				$ret = array();
				$ret['error'] = 1;
				$ret['message'] = $upload_error;
	
				echo json_encode($ret);
				exit;
			}
		}
	}
	
	public function kindEditorManage()
	{
		$php_path = dirname(__FILE__) . '/';
		$php_url = dirname($_SERVER['PHP_SELF']) . '/';
	
		//根目录路径，可以指定绝对路径，比如 /var/www/attached/
		$root_path = FCPATH . 'resource/uploads/images/';
		//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		//$root_url = base_url('resource/uploads/images/') .'/';
		$root_url = config_item('cdn_url');//cdn路径
		//图片扩展名
		$ext_arr = array('gif', 'jpg', 'jpeg', 'png', 'bmp');
	
		log_message('error', __CLASS__.' '.__FUNCTION__.' 11111  '.json_encode(array($root_path, $root_url)));
	
		//目录名
		// 		$dir_name = empty($_GET['dir']) ? '' : trim($_GET['dir']);
		// 		if (!in_array($dir_name, array('', 'image', 'flash', 'media', 'file'))) {
		// 			echo "Invalid Directory name.";
		// 			exit;
		// 		}
		// 		if ($dir_name !== '') {
		// 			$root_path .= $dir_name . "/";
		// 			$root_url .= $dir_name . "/";
		// 			if (!file_exists($root_path)) {
		// 				mkdir($root_path);
		// 			}
		// 		}
	
		//根据path参数，设置各路径和URL
		if (empty($_GET['path'])) {
			$current_path = realpath($root_path) . '/';
			$current_url = $root_url;
			$current_dir_path = '';
			$moveup_dir_path = '';
		} else {
			$current_path = realpath($root_path) . '/' . $_GET['path'];
			$current_url = $root_url . $_GET['path'];
			$current_dir_path = $_GET['path'];
			$moveup_dir_path = preg_replace('/(.*?)[^\/]+\/$/', '$1', $current_dir_path);
		}
		//echo realpath($root_path);
		//排序形式，name or size or type
		$order = empty($_GET['order']) ? 'name' : strtolower($_GET['order']);
	
		//不允许使用..移动到上一级目录
		if (preg_match('/\.\./', $current_path)) {
			echo 'Access is not allowed.';
			exit;
		}
		//最后一个字符不是/
		if (!preg_match('/\/$/', $current_path)) {
			echo 'Parameter is not valid.';
			exit;
		}
		//目录不存在或不是目录
		if (!file_exists($current_path) || !is_dir($current_path)) {
			echo 'Directory does not exist.';
			exit;
		}
	
		//遍历目录取得文件信息
		$file_list = array();
		if ($handle = opendir($current_path)) {
			$i = 0;
			while (false !== ($filename = readdir($handle))) {
				if ($filename{0} == '.') continue;
				$file = $current_path . $filename;
				if (is_dir($file)) {
					$file_list[$i]['is_dir'] = true; //是否文件夹
					$file_list[$i]['has_file'] = (count(glob($file)) > 0); //文件夹是否包含文件
					$file_list[$i]['filesize'] = 0; //文件大小
					$file_list[$i]['is_photo'] = false; //是否图片
					$file_list[$i]['filetype'] = ''; //文件类别，用扩展名判断
				} else {
					$file_list[$i]['is_dir'] = false;
					$file_list[$i]['has_file'] = false;
					$file_list[$i]['filesize'] = filesize($file);
					$file_list[$i]['dir_path'] = '';
					$file_ext = strtolower(pathinfo($file, PATHINFO_EXTENSION));
					$file_list[$i]['is_photo'] = in_array($file_ext, $ext_arr);
					$file_list[$i]['filetype'] = $file_ext;
				}
				$file_list[$i]['filename'] = $filename; //文件名，包含扩展名
				$file_list[$i]['datetime'] = date('Y-m-d H:i:s', filemtime($file)); //文件最后修改时间
				$i++;
			}
			closedir($handle);
		}
	
		//排序
		function cmp_func($a, $b) {
			global $order;
			if ($a['is_dir'] && !$b['is_dir']) {
				return -1;
			} else if (!$a['is_dir'] && $b['is_dir']) {
				return 1;
			} else {
				if ($order == 'size') {
					if ($a['filesize'] > $b['filesize']) {
						return 1;
					} else if ($a['filesize'] < $b['filesize']) {
						return -1;
					} else {
						return 0;
					}
				} else if ($order == 'type') {
					return strcmp($a['filetype'], $b['filetype']);
				} else {
					return strcmp($a['filename'], $b['filename']);
				}
			}
		}
		usort($file_list, 'cmp_func');
	
		$result = array();
		//相对于根目录的上一级目录
		$result['moveup_dir_path'] = $moveup_dir_path;
		//相对于根目录的当前目录
		$result['current_dir_path'] = $current_dir_path;
		//当前目录的URL
		$result['current_url'] = $current_url;
		//文件数
		$result['total_count'] = count($file_list);
		//文件列表数组
		$result['file_list'] = $file_list;
	
		//输出JSON字符串
		header('Content-type: application/json; charset=UTF-8');
		//$json = new Services_JSON();
		//echo $json->encode($result);
	
		echo json_encode($result);
	}
	
}