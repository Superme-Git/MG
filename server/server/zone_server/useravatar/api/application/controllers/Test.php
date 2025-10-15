<?php
class Test extends CI_Controller
{
	function __construct()
	{
		parent::__construct();
	
	}
	
	public function upload_avatar()
	{
	    $url = '59.151.73.69:8800/role/set';
	
	    $attributes = array();
	    $attributes['gameid'] = '1';
	    $attributes['userid'] = '1';
	    $attributes['roleid'] = '1';
	    $attributes['serverid'] = 1;
	    $attributes['icon'] = '1';
	    
	    $attributes['sign'] = md5($attributes['gameid'] .'|'.$attributes['userid'].'|'.$attributes['serverid']);
	   
	    $this->load->helper('url');
	    
	    $result = request($url, $attributes, 'post');
	    var_dump($result);
	}

}