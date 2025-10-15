<?php

function create_file($filename = '', $data = '')
{
	$upload_path = FCPATH.'resource/uploads/files/'.date('Y/md');
	log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $upload_path '.serialize(array($upload_path)));

	//校验文件夹是否有效
	if (!file_exists($upload_path))
	{
		@mkdir($upload_path);
	}
	log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $upload_path1 '.serialize(array($upload_path)));

	$file_path = $upload_path .'/'.trim($filename, '/');
	
	//文件已存在, 自动更名
	if (file_exists($file_path))
	{
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' succ '.serialize(array($file_path)));

		if (strpos($filename, '.'))
		{
			$filename = str_replace('.', time() . '.', $filename);
		}
		else
		{
			$filename .= time();
		}

		$file_path = $upload_path .'/'.trim($filename, '/');
	}

	//写入文本
	write_file($file_path, $data);
	
	//校验结果
	if (file_exists($file_path))
	{
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' succ '.serialize(array($file_path)));
		return $file_path;
	}

	log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' false '.serialize(array($file_path)));
	return false;
}

function request($url,$params=array(),$requestMethod='GET',$jsonDecode=true,$headers=array()) {
    $ci = curl_init();
    curl_setopt($ci, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
    curl_setopt($ci, CURLOPT_USERAGENT, '1001 Magazine v1');
    curl_setopt($ci, CURLOPT_CONNECTTIMEOUT, 10);
    curl_setopt($ci, CURLOPT_TIMEOUT, 10);
    curl_setopt($ci, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($ci, CURLOPT_ENCODING, "");
    curl_setopt($ci, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ci, CURLOPT_HEADER, FALSE);

    $requestMethod = strtoupper($requestMethod);
    switch ($requestMethod) {
        case 'POST':
            curl_setopt($ci, CURLOPT_POST, TRUE);
            if ($params) {
                curl_setopt($ci, CURLOPT_POSTFIELDS, $params);
            }
            else {
                curl_setopt($ci, CURLOPT_POSTFIELDS, ''); // Don't know why: if not set,  413 Request Entity Too Large
            }
            break;
        case 'DELETE':
            curl_setopt($ci, CURLOPT_CUSTOMREQUEST, 'DELETE');
            if ($params) {
                $url = "{$url}?{$params}";
            }
            break;
        case 'GET':
            if($params) {
                $sep = false === strpos($url,'?') ? '?' : '&';
                $url .= $sep . http_build_query($params);
            }
            break;
        case 'PUT':
            if($params) {
                curl_setopt($ci, CURLOPT_CUSTOMREQUEST, "PUT");
                curl_setopt($ci, CURLOPT_POSTFIELDS, $params);
            }
            break;
    }
    //$headers[] = "APIWWW: " . $_SERVER['REMOTE_ADDR'];
    curl_setopt($ci, CURLOPT_URL, $url );
    curl_setopt($ci, CURLOPT_HTTPHEADER, $headers );
    curl_setopt($ci, CURLINFO_HEADER_OUT, TRUE );

    $response = curl_exec($ci);
    $httpCode = curl_getinfo($ci, CURLINFO_HTTP_CODE);
    $return = array(
        'httpcode' => $httpCode,
        'data' => $httpCode >= 300 ? $response : ($jsonDecode ? json_decode($response,true) : $response),
    );
    //$httpInfo = curl_getinfo($ci);
    curl_close ($ci);
    return $return;
}

function ps($name)
{
	$cmd = "ps -o pid,start,command ax | grep '".$name."' | awk '!/awk/ && !/grep/'";
	@exec($cmd, $return, $errno);
	log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' exec '.json_encode(array($cmd, $return, $errno)));
	
	return $return;
}


//分页函数
function page($count, $offset, $limit, $otherRoute = '')
{
	$ci = _get_object();
	$ci->load->library("pagination");
	$pageConfig = array();
	$pageConfig["base_url"] = base_url($ci->router->fetch_directory(). $ci->router->fetch_class() . '/'.$ci->router->fetch_method() . ($otherRoute ? '/'.trim($otherRoute, '/') : ''));
	$pageConfig["total_rows"] = $count;
	$pageConfig["per_page"] = $limit;
	$pageConfig["cur_page"] = $offset;
	$pageConfig["num_links"] = 6;

	$pageConfig['prev_tag_open'] = '<li class="paginate_button ">';
	$pageConfig['prev_tag_close'] = '</li>';
	$pageConfig['next_tag_open'] = '<li class="paginate_button ">';
	$pageConfig['next_tag_close'] = '</li>';

	$pageConfig['num_tag_open'] = '<li class="paginate_button ">';
	$pageConfig['num_tag_close'] = '</li>';

	$pageConfig['first_tag_open'] = '<li class="paginate_button ">';
	$pageConfig['first_tag_close'] = '<li class="paginate_button ">';
	$pageConfig['last_tag_open'] = '<li class="paginate_button ">';
	$pageConfig['last_tag_close'] = '</li>';
	$pageConfig['cur_tag_open'] = '<li class="paginate_button active"><a href="#">';
	$pageConfig['cur_tag_close'] = '</a></li>';
	$ci->pagination->initialize($pageConfig);

	return array(
			"pages" => $ci->pagination->create_links(),
			"total" => $pageConfig["total_rows"],
			"total_page" => ceil($count / $limit),
			"page" => $offset / $limit + 1,
			"limit" => $limit,
			"now" => $offset .' - ' . ($offset + $limit)
	);
}

/**
 * 10进制转换为其他进制
 *
 * $c = md5('ffff');
 echo $c, PHP_EOL;
 echo $r = convert($c, 64), PHP_EOL;
 echo convert($r, -64);
 *
 * @param unknown $s
 * @param number $to
 * @return string
 */
function dec_convert($int, $to_ary = 62)
{
	$int = intval($int);
	$chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@=';
	$arr = array();
	do {
		$mod = $int % $to_ary;
		$int = ($int - $mod) / $to_ary;
		array_unshift($arr, $chars[$mod]);
	} while ($int);

	return implode('', $arr);
}

/**
 * 其他进制转换为10进制
 *
 * @param string $str
 * @param int $ary
 * @return number
 */
function convert_todec($str, $ary = 62)
{
	$chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@=';
	$int = 0;
	for ($i = 0; $i < strlen($str); $i++)
	{
		$s = substr($str, -$i-1, 1);
		//var_dump($s);
		$pos = strpos($chars, $s);
		//var_dump($pos);
		$int += pow($ary, $i) * $pos;
	}
	return $int;
}

function upload_to_cdn($file_path)
{
	//ftp配置
	$ftpConfig = array(
			'hostname' => '210.14.130.170',
			'username' => 'cmsPHPUser_FTP',
			'password' => 'Bkkf#f)kf8-fiBIUy34kjh(*',
			'port' => 22234,
			'passive' => true
	);

	$file_name = pathinfo($file_path, PATHINFO_BASENAME);
	$remotefile = '/'.date('Y/m/d') . '/' . $file_name;
	$fileUrl = config_item('cdn_url').$remotefile;

	$CI =& get_instance();
	$CI->load->library('ftp');
	if ($CI->ftp->connect($ftpConfig))
	{
		$dirname = pathinfo($remotefile,PATHINFO_DIRNAME);
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' remote dirname '.serialize(array($dirname)));
			
		$nowdir = '/';
		foreach (explode('/', $dirname) as $v)
		{
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' remote dir '.serialize(array($nowdir.$v)));
			if ($v && !$CI->ftp->changedir($nowdir.$v))
			{
				$CI->ftp->changedir($nowdir);
				$CI->ftp->mkdir($v);
			}
			if ($v) $nowdir .= $v.'/';
		}

			
		if ($CI->ftp->upload($file_path, $remotefile))
		{
			$CI->ftp->close();
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' succ '.serialize(array($remotefile, $fileUrl)));
			return array('errno' => 0, 'error' => 0, 'message' => 'succ', 'url' => $fileUrl);
		}
		else
		{
			$CI->ftp->close();
			log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' 上传图片失败 '.serialize(array($remotefile, $file_path)));
			return array('errno' => 1, 'error' => 1, 'message' => '上传图片失败。');
		}
	}

	$CI->ftp->close();
	log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' 图片服务器连接失败 '.serialize(array($remotefile, $file_path)));
	return array('errno' => 1, 'error' => 1, 'message' => '图片服务器连接失败。');
}

$siteKey = '';
function setSite($siteKey_ = '')
{
    global $siteKey;
    $siteKey = $siteKey_;
    return $siteKey;
}

function getSite()
{
    global $siteKey;
    return $siteKey;
}

function base64_encode_($data)
{
	return str_replace(array('/','+','='), array('_a','_b','_c'), base64_encode($data));
}

function base64_decode_($data)
{
	return base64_decode(str_replace(array('_a','_b','_c'), array('/','+','='), $data));
}

/**
 * 获取文件夹下所有文件及文件夹
 * 
 * @param string $dir 文件夹路径 
 * @param number $level 文件夹深度
 * @return multitype:string
 */
function getAllByDir($dir, $childDir = '',$level = 99)
{
	$files = array();
	foreach(scandir($dir . $childDir) as $afile)
	{
		if(in_array($afile, array('.', '..', '.svn'))) continue;
		if(is_dir($dir . $childDir.'/'.$afile))
		{
			if ($level > 1)
			{
				$files_ = getAllByDir($dir, $childDir.'/'.$afile, $level - 1);
				if ($files_)
				{
					$files = array_merge($files, $files_);
				}
			}
			else 
			{
				$files[] = $childDir.'/' .$afile;
			}
		} else {
			
			$files[] = $childDir.'/'.$afile;
		}
	}
	
	return $files;
}


/**
 * 获取文件夹下所有文件及文件夹
 *
 * @param string $dir 文件夹路径
 * @param number $level 文件夹深度
 * @return multitype:string
 */
function getFilesByDir($dir, $childDir = '', $level = 99)
{
	$files = array();
	foreach(scandir($dir .$childDir) as $afile)
	{
		if(in_array($afile, array('.', '..', '.svn'))) continue;
		if(is_dir($dir .$childDir.'/'.$afile))
		{
			if ($level > 1)
			{
				$files_ = getFilesByDir($dir, $childDir.'/'.$afile, $level - 1);
				if ($files_)
				{
					$files = array_merge($files, $files_);
				}
			}
		} else {
			$files[] = $childDir.'/' .$afile;
		}
	}

	return $files;
}

/**
 * 获取文件夹下所有文件夹
 *
 * @param string $dir 文件夹路径
 * @param number $level 文件夹深度
 * @return multitype:string
 */
function getDirsByDir($dir, $childDir = '', $level = 99)
{
	$files = array();
	foreach(scandir($dir .$childDir) as $afile)
	{
		if(in_array($afile, array('.', '..', '.svn'))) continue;
		
		if(is_dir($dir .$childDir.'/'.$afile))
		{
			if ($level > 1)
			{
				$files_ = getDirsByDir($dir, $childDir.'/'.$afile, $level - 1);
				if ($files_)
				{
					$files = array_merge($files, $files_);
				}
			}
			else 
			{
				$files[] = $childDir.'/'.$afile;
			}
		}
	}

	return $files;
}

function scan_dirs($dir)
{
	$dirs = glob(rtrim($dir, '/') . '/*', GLOB_ONLYDIR);
	if (!$dirs)
	{
		return null;
	}
	
	$dirs_ = array();
	foreach ($dirs as $dir_)
	{
		$dir_item = array();
		$dir_item['path'] = $dir_;
		$dir_item['label'] = trim(strrchr($dir_, '/'), '/');
		$dir_item['children'] = scan_dirs($dir_);
		
		$dirs_[] = $dir_item;
	}
	
	return $dirs_;
}

function scan_files($dir)
{
	$files = glob(rtrim($dir, '/') . '/*.{*}', GLOB_BRACE);
	
	$files_ = array();
	foreach ($files as $file_)
	{
		$file_item = array();
		$file_item['path'] = $file_;
		$file_item['label'] = trim(strrchr($file_, '/'), '/');
	
		$files_[] = $file_item;
	}
	
	return $files_;
}


function toutf8($var)
{
	if (is_array($var))
	{
		$var_ = array();
		foreach ($var as $key => $item)
		{
			$var_[$key] = toutf8($item);
		}
		return  $var_;
	}
	else
	{
		$encode = mb_detect_encoding($var, array('ASCII','UTF-8','ASCII','GBK','GB2312','CP936'));
		if ($encode != 'UTF-8')
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' mb_convert_encoding '.json_encode(array($var)));
			$var = mb_convert_encoding($var, "UTF-8", $encode);
			log_message('error', __CLASS__.' '.__FUNCTION__.' mb_convert_encoding '.json_encode(array($var)));
		}
		
		return $var;
	}
}

function togbk($var)
{
	if (is_array($var))
	{
		$var_ = array();
		foreach ($var as $key => $item)
		{
			$var_[$key] = togbk($item);
		}
		return $var_;
	}
	else
	{
		$encode = mb_detect_encoding($var, array('ASCII','UTF-8','ASCII','GBK','GB2312','CP936'));
		if ($encode != 'GBK')
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' mb_convert_encoding '.json_encode(array($var)));
			$var = mb_convert_encoding($var, "GBK", $encode);
			log_message('error', __CLASS__.' '.__FUNCTION__.' mb_convert_encoding '.json_encode(array($var)));
		}
		
		return $var;
	}
}



//对一个给定的二维数组按照指定的键值进行排序
function array_sort($arr,$keys,$type='asc')
{
	$keysvalue = $new_array = array();
	foreach ($arr as $k=>$v){
		$keysvalue[$k] = $v[$keys];
	}
	if($type == 'asc'){
		asort($keysvalue);
	}else{
		arsort($keysvalue);
	}
	reset($keysvalue);
	foreach ($keysvalue as $k=>$v){
		$new_array[$k] = $arr[$k];
	}
	return $new_array;
}

/**
 * 返回结果格式化
 * @param unknown $data
 * @param string $errno
 * @param string $error
 * @return multitype:string Ambigous <string, unknown>
 */
function result($data, $errno = '', $message = '')
{
	return array('errno' => $errno, 'message' => $message, 'data' => $data ? $data : '');
}

/**
 * response
 *
 * @param var $data
 * @param var $errno
 * @param var $error
 */
function response($data, $errno = '', $message = '')
{
	$dataArr = array('errno' => empty($errno) ? '' : $errno, 'message' => $message, 'data' => $data);

	//?callback=jQuery213007410891919373785_1419918006860&
	//header("Content-type:text/html;charset=utf-8");
	$callback = !empty($_GET['callback']) ? $_GET['callback'] : '';
	$dataStr = json_encode($dataArr);
	if ($callback)
	{
		$dataStr = "$callback($dataStr);";
	}

	$CI = _get_object();
	$CI->output->set_content_type('application/json');
	$CI->output->set_output($dataStr);
	$CI->output->_display();

	$memory	= round(memory_get_usage() / 1024 / 1024, 2).'MB';
	log_message('error', ' -- response -- running info '.serialize(array('memory' => $memory, 'return' => $dataStr)));
	exit();
}

function getIp()
{
	if(getenv('HTTP_CLIENT_IP')){
		$onlineip = getenv('HTTP_CLIENT_IP');
	}
	elseif(getenv('HTTP_X_FORWARDED_FOR')){
		$onlineip = getenv('HTTP_X_FORWARDED_FOR');
	}
	elseif(getenv('REMOTE_ADDR')){
		$onlineip = getenv('REMOTE_ADDR');
	}
	else{
		$onlineip = $_SERVER['REMOTE_ADDR'];
	}

	return $onlineip;
}

function is_post()
{
	return strtolower($_SERVER['REQUEST_METHOD']) == 'post';
}

/**
 * json转码中文版
 *
 * @param int/string/array $var
 * @param bool $each : 内部使用变量, 外部无需传值
 * @return string
 */
function json_encode_cn($var, $each = false)
{
	if (is_array($var))
	{
		$arr_ = array();
		foreach ($var as $key => $val)
		{
			$arr_[$key] = json_encode_cn($val, true);
		}
		return urldecode(json_encode($arr_));
	}
	elseif ($each)
	{
		return urlencode($var);
	}

	return urldecode(json_encode(urlencode($var)));
}

/**
 * Validation Object
 *
 * Determines what the form validation class was instantiated as, fetches
 * the object and returns it.
 *
 * @access	private
 * @return	mixed
 */
if ( ! function_exists('_get_object'))
{
	function &_get_object()
	{
		$CI =& get_instance();

		return $CI;
	}
}