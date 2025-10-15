<?php
class Uploadfile
{
    
    
    public function image($img_type, $img_data)
    {
        $url = 'http://mt3.pengyouquan001.locojoy.com:8811/upload/image';
        
        $params = array();
        $params['img_type'] = $img_type;
        $params['img_data'] = base64_encode_($img_data);
        
        //校验请求结果
        $result = $this->request($url, $params, 'post');
        if ($result['errno'])
        {
            return $this->_rst(null, $result['errno'], $result['message']);
        }
        
        //校验接口结果
        $api_result = $result['data'];
        if ($api_result['errno'])
        {
            return $this->_rst(null, $api_result['errno'], $api_result['message']);
        }
        
        return $this->_rst($api_result['data']);
    }
    
    public function sound($sound_type, $sound_data)
    {
        $url = 'http://mt3.pengyouquan001.locojoy.com:8811/upload/sound';
    
        $params = array();
        $params['sound_type'] = $sound_type;
        $params['sound_data'] = base64_encode_($sound_data);
    
        //校验请求结果
        $result = $this->request($url, $params, 'post');
        if ($result['errno'])
        {
            return $this->_rst(null, $result['errno'], $result['message']);
        }
        
        //校验接口结果
        $api_result = $result['data'];
        if ($api_result['errno'])
        {
            return $this->_rst(null, $api_result['errno'], $api_result['message']);
        }
        
        return $this->_rst($api_result['data']);
    }
    
    protected function _rst($data = null, $errno = null, $message = null)
    {
        return array('data' => $data, 'errno' => $errno, 'message' => $message);
    }
    
    protected function request($url, $params, $method = 'post')
    {
        $start_time = microtime();
        log_message('info', __CLASS__.' '. __FUNCTION__.' $url '. serialize($url));
        log_message('info', __CLASS__.' '. __FUNCTION__.' $params '. serialize($params));
        log_message('info', __CLASS__.' '. __FUNCTION__.' $method '. serialize($method));
    
        $result = $this->_request($url, $params, $method);
    
        log_message('info', __CLASS__.' '. __FUNCTION__.' httpcode '. serialize($result['httpcode']));
        log_message('info', __CLASS__.' '. __FUNCTION__.' data '. serialize(mb_substr($result['data'], 0, 500)));
        log_message('info', __CLASS__.' '. __FUNCTION__.' _request time '. (microtime() - $start_time));
    
        if ($result['httpcode'] != 200)
        {
            log_message('ERROR', __CLASS__.' '. __FUNCTION__.' httpcode error '. serialize(array($url, $params, $method, $result)));
            return $this->_rst(null, 400, '请求异常');
        }
    
        if (json_decode($result['data']))
        {
            return $this->_rst(json_decode($result['data'], true));
        }
    
        return $this->_rst($result['data']);
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