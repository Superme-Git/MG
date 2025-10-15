<?php
defined('BASEPATH') OR exit('No direct script access allowed');
/**
 * 加密解密类
 * 生成 Token
 * 
 * @copyright Locojoy.com
 * @author lihuixu@locojoy.com
 * @version 2014.11.19
 */
class Token_v3
{
	protected $ci = null;
	protected $app_key = null;
	protected $app_secret = null;
	protected $app_version = null;
	protected $des_tag = null;
	protected $des_key = null;
	protected $time = null;
	protected $timeout = 7200;//token有效期
	protected $params = null;
	protected $connect_tag = '___';
	protected $token = null;
	
	public function __construct($tokenConfig = null)
	{
		if ($tokenConfig === null)
		{
			$tokenConfig = config_item('token');
		}
		
		$this->des_tag = $tokenConfig['tag'];
		$this->des_key = $tokenConfig['key'];
		if (!empty($tokenConfig['timeout']) && $tokenConfig['timeout'] > 0)
		{
			$this->setTimeout($tokenConfig['timeout']);
		}
		
		$this->ci =& get_instance();
	}
	
	/**
	 * 生产token安全码
	 * @return string
	 */
	public function make()
	{
		if (!$this->app_key || !$this->app_secret)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' app_key or app_secret empty ');
			return array('code' => 'token_make_param_fail', 'message' => 'app_key or app_secret 为空', 'data' => '');
		}
		
		$dataArr = array(
				$this->app_key,
				$this->app_secret,
				$this->app_version,
				$this->des_tag,
				time(),
				$this->timeout,
				count($this->params) > 0 ? serialize($this->params) : ''
				
		);
		log_message('info', __CLASS__.' '.__FUNCTION__.' $dataArr '.json_encode(array($dataArr)));
		$dataStr = implode($this->connect_tag, $dataArr);
		
		$token = $this->ci->encrypt->encode($dataStr, $this->des_key);
		
		if (!$token)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' des_encode fail '.json_encode(array($this->params, $token)));
			return array('code' => 'token_make_fail', 'message' => '解析错误', 'data' => '');
		}
		
		$this->token = str_replace(array('/','+','='), array('_a','_b','_c'), $token);
		
		return array('code' => '', 'message' => '', 'data' => true);
	}
	
	/**
	 * 校验token正确性
	 *
	 * @param string $token
	 * @param bool $resetAttributes
	 * @return bool
	 */
	public function check($token)
	{
		$tokenStr = $this->ci->encrypt->decode(str_replace(array('_a','_b','_c'), array('/','+','='), $token), $this->des_key);
		if (!strpos($tokenStr, $this->connect_tag))
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' token err '.serialize(array($token, $tokenStr)));
			return array('code' => 'token_check_fail', 'message' => '解析错误', 'data' => '');
		}
		log_message('info', __CLASS__.' '.__FUNCTION__.' token $tokenStr '.serialize($tokenStr));
		
		$tokenArr = explode($this->connect_tag, $tokenStr);
		if (count($tokenArr) != 7)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' token item count err '.serialize(array(count($tokenArr), $tokenArr)));
			return array('code' => 'token_check_length_fail', 'message' => '解析错误', 'data' => '');
		}
		
		//还原加密数字
		$tokenArr = array(
				'app_key' => $tokenArr[0],
				'app_secret' => $tokenArr[1],
				'app_version' => $tokenArr[2],
				'tag' => $tokenArr[3],
				'time' => $tokenArr[4],
				'timeout' => $tokenArr[5],
				'params' => $tokenArr[6] ? unserialize($tokenArr[6]) : ''
		);
		log_message('info', __CLASS__.' '.__FUNCTION__.' token $tokenArr '.serialize($tokenArr));
		
		//判断 app_key , app_secret
		if (!$tokenArr['app_key'] || !$tokenArr['app_secret'])
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' app_key | app_secret error '.serialize(array($tokenArr)));
			return array('code' => 'token_check_param_fail', 'message' => '解析错误', 'data' => '');
		}
		
		//判断私钥
		if ($tokenArr['tag'] != $this->des_tag)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' token tag err '.json_encode(array($tokenArr)));
			return array('code' => 'token_check_param_fail', 'message' => '解析错误', 'data' => '');
		}
		
		//判断有效期, 未设置不校验
		if ($tokenArr['timeout'] > 0 && time() > ($tokenArr['time'] + $tokenArr['timeout']))
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' token expired '.serialize(array($tokenArr, time())));
			return array('code' => 'token_timeout', 'message' => 'TOKEN已过期', 'data' => '');
		}
		
		//反赋值
		$this->app_key = $tokenArr['app_key'];
		$this->app_secret = $tokenArr['app_secret'];
		$this->app_version = $tokenArr['app_version'];
		$this->time = $tokenArr['time'];
		$this->timeout = $tokenArr['timeout'];
		$this->params = $tokenArr['params'];
		
		return array('code' => '', 'message' => '', 'data' => true);
	}
	
	public function setAppKey($str)
	{
		$this->app_key = $str;
	}
	
	public function getAppKey()
	{
		return $this->app_key;
	}
	
	public function setAppSecret($str)
	{
		$this->app_secret = $str;
	}
	
	public function getAppSecret()
	{
		return $this->app_secret;
	}
	
	public function setAppVersion($str)
	{
		$this->app_version = $str;
	}
	
	public function getAppVersion()
	{
		return $this->app_version;
	}
	
	public function getTime()
	{
		return (int)$this->time;
	}
	
	public function setTimeout($str)
	{
		$this->timeout = $str;
	}
	
	public function getTimeout()
	{
		return $this->timeout;
	}
	
	
	public function setConnectTag($str)
	{
		$this->connect_tag = $str;
	}
	
	public function addParam($key, $val)
	{
		$this->params[$key] = $val;
	}
	
	public function delParam($key)
	{
		if (isset($this->params[$key]))
		{
			$this->params[$key] = '';
			unset($this->params[$key]);
		}
	}
	
	public function getParams()
	{
		return $this->params;
	}
	
	public function getToken()
	{
		return $this->token;
	}
}
