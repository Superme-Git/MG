<?php

function get_sessionid()
{
	$userData = get_userdata();

	if (empty($userData['session_id']))
	{
		return null;
	}

	return $userData['session_id'];
}


function get_userid()
{
	$userInfo = get_userinfo();
	if (empty($userInfo['user_id']))
	{
		return 0;
	}

	return $userInfo['user_id'];
}

function get_userdata()
{
	$ci = _get_object();
	$userData = $ci->session->all_userdata();

	if (!$userData)
	{
		return null;
	}

	return $userData;
}

function get_userinfo()
{
	$userData = get_userdata();

	if (empty($userData['user']))
	{
		return null;
	}
	$userInfo = $userData['user'];

	return $userInfo;
}

function get_userapp()
{
	$userData = get_userdata();

	if (empty($userData['app']))
	{
		return null;
	}
	$userApp = $userData['app'];

	return $userApp;
}

function get_usercity()
{
	$userData = get_userdata();

	if (empty($userData['city']))
	{
		return null;
	}
	$userApp = $userData['city'];

	return $userApp;
}

/**
 * 面向所有用户
 *
 * @param str $path
 */
function check_login($path)
{
	$exceptionPaths = array('login');
	 
	if (in_array(trim($path, '/'), $exceptionPaths))
	{
		return true;
	}
	 
	if (!get_userinfo())
	{
		return false;
	}
	
	return true;
}


function check_auth($path, $group = 'editor')
{
	$userInfo = get_userinfo();

	if (!$userInfo)
	{
		return true;
	}

	//超级管理员可访问任何地址
	if ($userInfo['group'] == 'root')
	{
		return true;
	}

	$publicPaths = array('index', 'login', 'login/logout');
	if (in_array($path, $publicPaths))
	{
		return true;
	}

	$userapp = get_userapp();
	if (!$userapp)
	{
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' get_userapp null '.serialize(array($path, $userInfo, $userapp)));
		return false;
	}
	
	$paths = array();
	foreach ($userapp as $item)
	{
		$paths[] = trim($item['path'], '/');
	}
	if (!in_array(trim($path, '/'), $paths))
	{
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $get_userapp false '.serialize(array($path, $userInfo, $userapp)));
		return false;
	}

	$groups = array('editor', 'manager', 'root');
	if (array_search($userInfo['group'], $groups) < array_search($group, $groups))
	{
		log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $groups false '.serialize(array($path, $userInfo, $userapp)));
		return false;
	}

	return true;
}

function get_apps()
{
	$userInfo = get_userinfo();
	if ($userInfo['group'] == 'root')
	{
		$paths = getfilesByDir(APPPATH . 'controllers');
		$paths_ = array();
		foreach ($paths as $item)
		{
			$paths_[] = str_replace(array(APPPATH . 'controllers/', '.php'), '', $item);
		}
		$paths = $paths_;
		
		$userAuth = $paths;
	}
	else
	{
		$userAuth = get_userauth();
	}
	
	if(!$userAuth)
	{
		return array();
	}
	
	$apps = array();
	foreach ($userAuth as $path)
	{
		if (!strpos($path, '/'))
		{
			continue;
		}
		
		$app = substr($path, 0, strpos($path, '/'));
		$app = $app.'/index';
		
		if (in_array($app, $apps))
		{
			continue;
		}
		
		$apps[] = $app;
	}
	
	return $apps;
}


function getDir($path)
{
	
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