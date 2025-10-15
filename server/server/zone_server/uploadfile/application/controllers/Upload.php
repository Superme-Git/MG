<?php
/**
 * 上传接口
 * 
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.15
 */
class Upload extends MY_Controller
{
    public function __construct()
    {
        parent::__construct();
    }
    
    //上传图片
    public function image()
    {
        //处理图片
    	$img_type = $this->input->post("img_type", true);
    	$img_data = $this->input->post("img_data", false);//勿设置第二参数true, xss会屏蔽掉data:image_apng;base64,
    	if (!$img_type || !$img_data)
    	{
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' params_error error '.json_encode(array($img_type, $img_data)));
    	    return response('', 'params_error', 'params_error');
    	}
    	
    	$img_data = base64_decode_($img_data);
    	$filesize = strlen($img_data);
    	$img_name = uniqid().'.'.$img_type;
    	log_message('error', __CLASS__.' '.__FUNCTION__.' preg_match info '.json_encode(array($img_type, $filesize, $img_name)));
    	 
    	$configs = config_item('img_upload');
    	 
    	//判断图片类型
    	if (!in_array($img_type, explode('|', $configs['allowed_types'])))
    	{
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' upload_notallow_ext error '.json_encode(array($img_type)));
    	    return response('', 'upload_notallow_ext', 'upload_notallow_ext');
    	}
    	 
    	//判断图片大小, 限制在200kb以内
    	if ($filesize > $configs['max_size'] * 300)
    	{
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' upload_greater_maxsize error '.json_encode(array($img_type)));
    	    return response('', 'upload_greater_maxsize', 'upload_greater_maxsize');
    	}
    	
    	//自动创建目录
    	if (!file_exists($configs['upload_path']))
    	{
    	    @mkdir($configs['upload_path'], 0777, true);
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' upload_path mkdir '.json_encode(array($configs['upload_path'], $img_name)));
    	}
    	
    	$realpath = realpath($configs['upload_path']) . '/'.$img_name;
    	$httpurl = $configs['http_path'] . $img_name;
    	log_message('error', __CLASS__.' '.__FUNCTION__.' path '.json_encode(array($httpurl, $realpath)));
    	
    	//保存图片到硬盘
    	write_file($realpath, $img_data);
    	
    	log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($httpurl, $realpath)));
    	return response($httpurl);
    }
    
    
    //上传语音
    public function sound()
    {
    	//处理图片
    	$sound_type = $this->input->post("sound_type", true);
    	$sound_data = $this->input->post("sound_data", false);//勿设置第二参数true, xss会屏蔽掉data:image_apng;base64,
    	if (!$sound_type || !$sound_data)
    	{
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' params_error error '.json_encode(array($sound_type)));
    	    return response('', 'params_error', 'params_error');
    	}
    	
    	$sound_data = base64_decode_($sound_data);
    	$filesize = strlen($sound_data);
    	$sound_name = uniqid().'.'.$sound_type;
    	log_message('error', __CLASS__.' '.__FUNCTION__.' preg_match info '.json_encode(array($sound_type, $filesize, $sound_name)));
    	 
    	$configs = config_item('sound_upload');
    	 
    	//判断类型
    	if (!in_array($sound_type, explode('|', $configs['allowed_types'])))
    	{
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' upload_notallow_ext error '.json_encode(array($sound_type)));
    	    return response('', 'upload_notallow_ext', 'upload_notallow_ext');
    	}
    	 
    	//判断大小
    	if ($filesize > $configs['max_size'] * 300)
    	{
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' upload_greater_maxsize error '.json_encode(array($sound_type)));
    	    return response('', 'upload_greater_maxsize', 'upload_greater_maxsize');
    	}
    	 
    	//自动创建目录
    	if (!file_exists($configs['upload_path']))
    	{
    	    @mkdir($configs['upload_path'], 0777, true);
    	    log_message('error', __CLASS__.' '.__FUNCTION__.' upload_path mkdir '.json_encode(array($configs['upload_path'], $sound_name)));
    	}
    	 
    	$realpath = realpath($configs['upload_path']) . '/'.$sound_name;
    	$httpurl = $configs['http_path'] . $sound_name;
    	log_message('error', __CLASS__.' '.__FUNCTION__.' path '.json_encode(array($httpurl, $realpath)));
    	
    	//保存图片到硬盘
    	write_file($realpath, $sound_data);
    	
    	log_message('error', __CLASS__.' '.__FUNCTION__.' succ '.json_encode(array($httpurl, $realpath)));
    	return response($httpurl);
    }
    
}