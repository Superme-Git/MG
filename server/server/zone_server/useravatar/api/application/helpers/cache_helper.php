<?php

/**
 * cache 操作
 *
 * @copyright Locojoy.com
 * @author lihuixu@locojoy.com
 * @version v1.0 2014.5.19
 */

function cache_init()
{
	$CI =& get_instance();
	
	if (FALSE == $CI->load->is_loaded('cache'))
	{
		$cache_config = config_item('cache');
		if (empty($cache_config['status']))
		{
			log_message('DEBUG', __FUNCTION__ . ' cache config closed '.serialize($cache_config));
			return false;
		}
		
		if (!isset($cache_config['adapter']) || !in_array($cache_config['adapter'], array('file', 'redis')))
		{
			log_message('DEBUG', __FUNCTION__ . ' cache config adapter error '.serialize($cache_config));
			return false;
		}
		
		$config = array();
		$config['adapter'] = $cache_config['adapter'];
		!empty($cache_config['backup']) && $config['backup'] = $cache_config['backup'];
		
		$CI->load->driver('cache', $config);//file, redis
		log_message('DEBUG', __FUNCTION__ . ' load cache ');
	}
	
	//log_message('DEBUG', __FUNCTION__ . ' load cache '.serialize($CI->cache));
	return $CI->cache;
}

function cache_key($pre, $var = '')
{
	$var_str = '';
	if ($var)
	{
		$var_str .= ':';
		if (is_array($var))
		{
			$var_str .= md5(serialize($var));
		}
		else
		{
			$var_str .= (string)$var;
		}
	}
	
	$siteKey = getSite();
	$cache_key = ($siteKey ? $siteKey.':' : '') . $pre . $var_str;
	
	//win系统把 : 转换为 _
	if (strtoupper(substr(PHP_OS,0,3))==='WIN')
	{
		$cache_key = str_replace(':', '_', $cache_key);
	}
	
	//log_message('info', __FUNCTION__ .' succ '  . json_encode(array($cache_key, $pre, $var)));
	return $cache_key;
}

/**
 * redis cache has
 * @param string $key
 */
function cache_has($key)
{
	$cache = cache_init();
	if ($cache)
	{
		if ($cache->get($key))
		{
			
			return true;
		}
		
	}
	return false;
}

/**
 * redis cache get
 * @param string $key
 */
function cache_get($key)
{
	$cache = cache_init();

	if ($cache)
	{
		return $cache->get($key);
	}
	return false;
}

/**
 * redis cache set
 * @param string $key
 * @param int|string|array $val
 * @param string $sec : 0为不设定时间
 */
function cache_set($key, $val, $sec = 7200)
{
	$cache = cache_init();

	if ($cache)
	{
		return $cache->save($key, $val, $sec);
	}
	
	return false;
}


function cache_inc($key, $count = 1, $sec = 300)
{
	$cache = cache_init();

	if ($cache)
	{
		if (cache_has($key))
		{
			return $cache->increment($key, $count);
		}
		else 
		{
			return $cache->save($key, $count, $sec);
		}
		
	}
	return false;
}

function cache_dec($key, $val = 1)
{
	$cache = cache_init();

	if ($cache)
	{
		return $cache->decrement($key, $val);
	}
	return false;
}

/**
 * redis cache del
 * @param string $key
 */
function cache_del($key)
{
	$cache = cache_init();

	if ($cache)
	{
		return $cache->get($key) ? $cache->delete($key) : false;
	}
	return false;
}

function counter_valid($cache_key, $maxTimes = 5)
{
	$times = 0;
	if (cache_has($cache_key))
	{
		$times = (int)cache_get($cache_key);
	}

	if ($maxTimes && $times >= $maxTimes)
	{
		log_message('ERROR', __FUNCTION__.' times error '. json_encode_cn(array($times, $maxTimes, $cache_key)));
		return false;
	}

	log_message('debug', __FUNCTION__.' times '. json_encode_cn(array($times, $maxTimes, $cache_key)));
	return true;
}

function counter_inc($cache_key, $count = 1, $ttl = 1800)
{
	if (cache_has($cache_key))
	{
		$times = (int)cache_get($cache_key);
		cache_inc($cache_key, $count, $ttl);
	}
	else
	{
		$times = 0;
		cache_set($cache_key, $count, $ttl);
	}

	log_message('debug', __FUNCTION__.' $times +1 '. json_encode_cn(array($times+$count, $cache_key)));
	return true;
}

function captcha_set($captcha, $device)
{
	$CI =& get_instance();

	$cache_key = 'captcha:'.$device;
	$result = $CI->cache_model->set($cache_key, $captcha, 7200);
	if (!$result['data'])
	{
		log_message('error', __FUNCTION__.' error '.serialize(array($cache_key, $captcha)));
		return response('', 'captcha_set', lang('captcha_set'));
	}
	log_message('error', __FUNCTION__.' succ '.serialize(array($cache_key, $captcha)));
	return response(true);
}

function captcha_valid($captcha, $device, $captcha_cache_key)
{
	$CI =& get_instance();

	if (!counter_valid($captcha_cache_key, 5))
	{
		log_message('ERROR', __FUNCTION__.' captcha_times_limit '. json_encode(array($captcha_cache_key)));
		return response(null, 'captcha_times_limit', lang('captcha_times_limit'));
	}

	$cache_key = 'captcha:'.$device;
	$result = $CI->cache_model->get($cache_key);
	if (!$result['data'])
	{
		log_message('ERROR', __FUNCTION__.' captcha_timeout '. json_encode(array($cache_key)));
		return response(null, 'captcha_timeout', lang('captcha_timeout'));
	}
	$captchaCache = $result['data'];

	if (strtolower($captcha) != strtolower($captchaCache))
	{
		counter_inc($captcha_cache_key);

		log_message('ERROR', __FUNCTION__.' captcha_error '. json_encode(array($captchaCache, $captcha, $cache_key)));
		return response(null, 'captcha_error', lang('captcha_error'));
	}
	$CI->cache_model->del($cache_key);//登录成功清除错误数
	log_message('ERROR', __FUNCTION__.' succ '. json_encode(array($captchaCache, $captcha, $cache_key)));

	return response(true);
}