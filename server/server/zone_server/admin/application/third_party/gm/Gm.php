<?php
/**
 *
 * 乐动短信接口
 *
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2014.12.24
 *
 */
class Gm
{
	public function mail_verify_avatar($roleid, $serverid, $type, $content)
	{
		//$url = 'http://testot.locojoy.com/api/AuditingPhoto/Auditing.ashx';
		$url = 'http://ot.locojoy.com/api/AuditingPhoto/Auditing.ashx';
	
		$params = array();
		$params['GameId'] = '88';
		$params['RoleId'] = $roleid;
		$params['ServerId'] = $serverid;
		$params['AuditingType'] = ($type == 2 ? 2 : 1);//整数，1 通过，2 不通过
		$params['Content'] = urlencode(base64_encode($content));
		$params['Time'] = time();//13581829882
		$params['sign'] = md5('b18a26ffc632752987bd24a7bf0353f3'.$params['GameId'].$roleid.$serverid.$params['Time']);//'27ee932a45b24fc75e7b4d8fe70db872';//
	
		$result = $this->request($url, $params, 'get');
		log_message('error', __CLASS__.' '.__FUNCTION__.' $result '.serialize(array($roleid, $serverid, $result)));
		//{"version":"1000","method":"3","accountname":"testxu4","password":"fc7ee1a1e4e6d151c1df505b7d866063","mobile":""}";s:4:"sign";s:32:"49aa141019a412b1180e1eafe366e2d4";}
	
		$messages = array(
		    1 => '成功',
			2 => 'Sign校验失败',
			3 => 'GameId格式不正确',
			4 => '游戏不存在或未配置',
	        5 => 'RoleId未填或格式不正确',
		    6 => 'ServerId未填或格式不正确',
		    7 => 'AuditingType未填或格式不正确',
		    8 => 'Time未填或格式不正确',
		    9 => 'Content格式不正确',
			99 => '其他错误',
		);
		
		$result['Code'] = intval($result['Code']);
		if ($result['Code'] == 1)
		{
			return $this->_rst(true);
		}
		elseif (!empty($messages[$result['Code']]))
		{
			return $this->_rst(null, $result['Code'], $messages[$result['Code']]);
		}
		else
		{
			return $this->_rst(null, 99, $messages[99]);
		}
	}
	
	public function get_dashi_gift_count($serverid)
	{
	    //$url = 'http://ot.locojoy.com/api/GMTool/SetRoomAmount.ashx';
	    $url = 'http://ot.locojoy.com/api/GMTool/QueryRoomAmount.ashx';
	
	    $params = array();
	    $params['GameId'] = '88';
	    $params['ServerId'] = $serverid;
	    $params['Time'] = time();//13581829882
	    $params['sign'] = md5('b18a26ffc632752987bd24a7bf0353f3'.$params['GameId'].$serverid.$params['Time']);//'27ee932a45b24fc75e7b4d8fe70db872';//
	
	    $result = $this->request($url, $params, 'get');
	    log_message('error', __CLASS__.' '.__FUNCTION__.' $result '.serialize(array($serverid, $result)));
	    //{"version":"1000","method":"3","accountname":"testxu4","password":"fc7ee1a1e4e6d151c1df505b7d866063","mobile":""}";s:4:"sign";s:32:"49aa141019a412b1180e1eafe366e2d4";}
	
	    $messages = array(
	        1 => '成功',
	        2 => 'Sign校验失败',
	        3 => 'GameId格式不正确',
	        4 => '游戏不存在或未配置',
	        5 => 'ServerId未填或格式不正确',
	        6 => 'Time未填或格式不正确',
	        99 => '其他错误',
	    );
	
	    $result['Code'] = intval($result['Code']);
	    if ($result['Code'] == 1)
	    {
	        return $this->_rst($result['Amount']);
	    }
	    elseif (!empty($messages[$result['Code']]))
	    {
	        return $this->_rst(null, $result['Code'], $messages[$result['Code']]);
	    }
	    else
	    {
	        return $this->_rst(null, 99, $messages[99]);
	    }
	}
	
	public function set_dashi_gift_count($serverid, $amount)
	{
	    //$url = 'http://ot.locojoy.com/api/GMTool/SetRoomAmount.ashx';
	    $url = 'http://ot.locojoy.com/api/GMTool/SetRoomAmount.ashx';
	
	    $params = array();
	    $params['GameId'] = '88';
	    $params['ServerId'] = $serverid;
	    $params['Amount'] = $amount;//整数
	    $params['Time'] = time();//13581829882
	    $params['sign'] = md5('b18a26ffc632752987bd24a7bf0353f3'.$params['GameId'].$serverid.$params['Time']);//'27ee932a45b24fc75e7b4d8fe70db872';//
	
	    $result = $this->request($url, $params, 'get');
	    log_message('error', __CLASS__.' '.__FUNCTION__.' $result '.serialize(array($serverid, $result)));
	    //{"version":"1000","method":"3","accountname":"testxu4","password":"fc7ee1a1e4e6d151c1df505b7d866063","mobile":""}";s:4:"sign";s:32:"49aa141019a412b1180e1eafe366e2d4";}
	
	    $messages = array(
	        1 => '成功',
	        2 => 'Sign校验失败',
	        3 => 'GameId格式不正确',
	        4 => '游戏不存在或未配置',
	        5 => 'ServerId未填或格式不正确',
	        6 => 'Amount未填或格式不正确',
	        7 => 'Time未填或格式不正确',
	        99 => '其他错误',
	    );
	
	    $result['Code'] = intval($result['Code']);
	    if ($result['Code'] == 1)
	    {
	        return $this->_rst(true);
	    }
	    elseif (!empty($messages[$result['Code']]))
	    {
	        return $this->_rst(null, $result['Code'], $messages[$result['Code']]);
	    }
	    else
	    {
	        return $this->_rst(null, 99, $messages[99]);
	    }
	}
	
	
	protected function _rst($data = null, $errno = null, $message = null)
	{
	    return array('data' => $data, 'errno' => $errno, 'message' => $message);
	}
	
	protected function request($url, $params, $method = 'post')
	{
		$start_time = microtime();
		log_message('debug', __CLASS__.' '. __FUNCTION__.' $url '. serialize($url));
		log_message('debug', __CLASS__.' '. __FUNCTION__.' $params '. serialize($params));
		log_message('debug', __CLASS__.' '. __FUNCTION__.' $method '. serialize($method));
	
		$result = $this->_request($url, $params, $method);
	
		log_message('debug', __CLASS__.' '. __FUNCTION__.' httpcode '. serialize($result['httpcode']));
		log_message('debug', __CLASS__.' '. __FUNCTION__.' data '. serialize(mb_substr($result['data'], 0, 500)));
		log_message('debug', __CLASS__.' '. __FUNCTION__.' _request time '. (microtime() - $start_time));
	
		if ($result['httpcode'] != 200)
		{
			log_message('ERROR', __CLASS__.' '. __FUNCTION__.' httpcode error '. serialize(array($url, $params, $method, $result)));
			return $this->_rst(null, 400, '请求异常');
		}
		
		if (json_decode($result['data']))
		{
		    return json_decode($result['data'], true);
		}
		
		return $result['data'];
	}
	
	protected function _request($url,$params=array(),$requestMethod='GET',$headers=array())
	{
		$ci = curl_init();
		curl_setopt($ci, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
		curl_setopt($ci, CURLOPT_USERAGENT, '1001 Magazine v1');
		curl_setopt($ci, CURLOPT_CONNECTTIMEOUT, 20);
		curl_setopt($ci, CURLOPT_TIMEOUT, 20);
		curl_setopt($ci, CURLOPT_RETURNTRANSFER, TRUE);
		curl_setopt($ci, CURLOPT_ENCODING, "");
		curl_setopt($ci, CURLOPT_SSL_VERIFYPEER, false);
		curl_setopt($ci, CURLOPT_HEADER, FALSE);
	
		$requestMethod = strtoupper($requestMethod);
		switch ($requestMethod) {
			case 'POST':
				curl_setopt($ci, CURLOPT_POST, TRUE);
				if ($params) {
					$params = http_build_query($params);
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
					$url .= $sep . urldecode(http_build_query($params));
				}
				break;
			case 'GET1':
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
	
		log_message('debug', __CLASS__.' '. __FUNCTION__.' curl url '. serialize(array($url)));
		
		//$headers[] = "APIWWW: " . $_SERVER['REMOTE_ADDR'];
		curl_setopt($ci, CURLOPT_URL, $url );
		curl_setopt($ci, CURLOPT_HTTPHEADER, $headers );
		curl_setopt($ci, CURLINFO_HEADER_OUT, TRUE );
	
		$response = curl_exec($ci);
		$httpCode = curl_getinfo($ci, CURLINFO_HTTP_CODE);
		$return = array(
				'httpcode' => $httpCode,
				'data' => $response,
		);
		//$httpInfo = curl_getinfo($ci);
		curl_close ($ci);
		return $return;
	}
	
	
}
