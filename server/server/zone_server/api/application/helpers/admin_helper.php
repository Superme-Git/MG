<?php

function get_admindata()
{
	$ci = _get_object();
	$adminData = $ci->session->all_userdata();

	if (!$adminData)
	{
		return null;
	}

	return $adminData;
}

function get_adminid()
{
	$adminInfo = get_admininfo();
	if (empty($adminInfo['admin_id']))
	{
		return null;
	}

	return $adminInfo['admin_id'];
}

function get_admininfo($key = '')
{
	$adminData = get_admindata();

	if (empty($adminData['admin_info']))
	{
		return null;
	}
	$adminInfo = $adminData['admin_info'];

	return $key && isset($adminInfo[$key]) ? $adminInfo[$key] : $adminInfo;
}

/**
 * 面向所有用户
 *
 * @param str $path
 */
function check_adminlogin()
{
	if (!get_admininfo())
	{
		return false;
	}
	
	return true;
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