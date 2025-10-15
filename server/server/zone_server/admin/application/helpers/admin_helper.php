<?php
function get_roles()
{
    $roles = array(0 => 'editor', 1 => 'manager', 9 => 'root');
    return $roles;
}

function get_role($k)
{
    $roles = get_roles();
    return isset($roles[$k]) ? $roles[$k] : null;
}

function check_auth($role = 'editor')
{

    $adminInfo = get_admininfo();

    if (!$adminInfo)
    {
        return true;
    }

    if (empty($adminInfo['role']))
    {
        $adminInfo['role'] = 0;
    }

    $roleKey = array_search($role, get_roles());
    if (!$roleKey)
    {
        $roleKey = 0;
    }

    if ($adminInfo['role'] < $roleKey)
    {
        log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $roles false '.serialize(array($roleKey, $role, $adminInfo)));
        return false;
    }

    log_message('error', __CLASS__ . ' ' . __FUNCTION__ . ' $roles true '.serialize(array($roleKey, $role, $adminInfo)));
    return true;
}

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