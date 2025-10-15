<?php

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