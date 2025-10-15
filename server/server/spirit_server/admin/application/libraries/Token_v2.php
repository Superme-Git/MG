<?php
/**
 * 加密解密类
 * 生成 Token
 * 
 * @copyright Locojoy.com
 * @author lihuixu@locojoy.com
 * @version 2014.11.19
 */
class Token_v2
{
	protected $ci = null;
	protected $app_key = null;
	protected $app_secret = null;
	protected $app_version = null;
	protected $des_tag = null;
	protected $des_key = null;
	protected $timeout = 8640000;//token有效期
	protected $params = null;
	protected $connect_tag = '___';
	protected $token = null;
	
	public function __construct($tokenConfig = null)
	{
		if ($tokenConfig === null)
		{
			$tokenConfig = config_item('token');
		}
		
		$this->setAppKey($tokenConfig['app_key']);
		$this->setAppSecret($tokenConfig['app_secret']);
		$this->setAppVersion($tokenConfig['app_version']);
		$this->des_tag = $tokenConfig['tag'];
		$this->des_key = $tokenConfig['key'];
		$this->setTimeout($tokenConfig['timeout']);
		
		$this->ci =& get_instance();
	}
	
	/**
	 * 生产token安全码
	 * @return string
	 */
	public function make()
	{
		$dataArr = array(
				$this->app_key,
				$this->app_secret,
				$this->app_version,
				$this->des_tag,
				time(),
				serialize($this->params)
		);
		//log_message('error', __CLASS__.' '.__FUNCTION__.' $dataArr '.json_encode(array($dataArr)));
		$dataStr = implode($this->connect_tag, $dataArr);
		
		//$des = new des_v1();
		//$token = $des->encrypt($data, $tokenConfig['key']);
		$token = $this->ci->encrypt->encode($dataStr, $this->des_key);
		
		if (!$token)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' des_encode fail '.json_encode(array($this->params, $token)));
			return false;
		}
		
		$this->token = str_replace(array('/','+','='), array('_a','_b','_c'), $token);
		
		return true;
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
		//$des = new des_v1();
		//$tokenStr = $des->decrypt($token, $tokenConfig['key']);
		$tokenStr = $this->ci->encrypt->decode(str_replace(array('_a','_b','_c'), array('/','+','='), $token), $this->des_key);
		if (!strpos($tokenStr, $this->connect_tag))
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' token err '.serialize(array($token, $tokenStr)));
			return false;
		}
		//log_message('error', __CLASS__.' '.__FUNCTION__.' token $tokenStr '.serialize($tokenStr));
		
		$tokenArr = explode($this->connect_tag, $tokenStr);
		if (count($tokenArr) != 6)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' token item count err '.serialize(array(count($tokenArr), $tokenArr)));
			return false;
		}
		
		if (time() > ($tokenArr[4] + $this->timeout))
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' token expired '.serialize(array($tokenArr[4], time())));
			return false;
		}
			
		if ($tokenArr[3] != $this->des_tag)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' token tag err '.json_encode(array($tokenArr)));
			return false;
		}
	
		$tokenArr = array(
				'app_key' => $tokenArr[0],
				'app_secret' => $tokenArr[1],
				'app_version' => $tokenArr[2],
				'tag' => $tokenArr[3],
				'time' => $tokenArr[4],
				'params' => unserialize($tokenArr[5])
		);
		
		if ($tokenArr['app_key'] != $this->app_key || $tokenArr['app_secret'] != $this->app_secret)
		{
			log_message('error', __CLASS__.' '.__FUNCTION__.' app_key | app_secret error '.serialize(array($tokenArr, $this->app_key, $this->app_secret)));
			return false;
		}
		
		//设置属性
		$this->params = $tokenArr['params'];
		
		return true;
	}
	
	public function setAppKey($str)
	{
		$this->app_key = $str;
	}
	
	public function setAppSecret($str)
	{
		$this->app_secret = $str;
	}
	
	public function setAppVersion($str)
	{
		$this->app_version = $str;
	}
	
	public function setTimeout($str)
	{
		$this->timeout = $str;
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
