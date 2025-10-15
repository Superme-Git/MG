<?php

/**
 * cache 操作
 *
 * @copyright Locojoy.com
 * @author lihuixu@locojoy.com
 * @version v1.0 2014.5.19
 */

function redis_init()
{
	$CI =& get_instance();
	
	if (FALSE == $CI->load->is_loaded('redis_v1'))
	{
		$redisConfig = array();
		if ($CI->config->load('redis', TRUE, TRUE))
		{
			$redisConfig = $CI->config->item('redis');
		}
		
		if (!$redisConfig)
		{
			log_message('ERROR', __FUNCTION__ .' redis config empty ');
			return false;
		}
		
		if (!$redisConfig['master']['status'])
		{
			log_message('ERROR', __FUNCTION__ .' redis config closed ');
			return false;
		}
		
		//判断是否有从库
		$status = FALSE;
		if (isset($redisConfig['slave']) && count($redisConfig['slave']) > 0)
		{
			for ($i = 0; $i< count($redisConfig['slave']); $i++)
			{
				if ($redisConfig['slave'][$i]['status'] == true)
				{
					$status = TRUE	;
					break;			
				}
			}
		}
		
		if ($status)
		{
			$CI->load->library('redis_v1', true);
			$CI->redis_v1->connect(array('host'=>$redisConfig['master']['host'], 'port'=>$redisConfig['master']['port']), true);// master
			
			for ($i = 0; $i< count($redisConfig['slave']); $i++)
			{
				if ($redisConfig['slave'][$i]['status'] == true)
					$CI->redis_v1->connect(array('host'=>$redisConfig['slave'][$i]['host'], 'port'=>$redisConfig['slave'][$i]['port']), false);// slave
			}
		}
		else 
		{
			$CI->load->library('redis_v1');
			$CI->redis_v1->connect(array('host'=>$redisConfig['master']['host'], 'port'=>$redisConfig['master']['port']));
		}
	
		log_message('DEBUG', __FUNCTION__ . ' load redis ');
	}
	
	log_message('DEBUG', __FUNCTION__ . ' load redis ');
	return $CI->redis_v1;
}

function redis_key($pre, $var = '')
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
	$cache_key = ($siteKey ? $siteKey.':' : '') . trim($pre, ':') . $var_str;
	
	//win系统把 : 转换为 _
	if (strtoupper(substr(PHP_OS,0,3))==='WIN')
	{
		$cache_key = str_replace(':', '_', $cache_key);
	}
	
	//log_message('info', __FUNCTION__ .' succ '  . json_encode(array($redis_key, $pre, $var)));
	return $cache_key;
}

/**
 * redis cache has
 * @param string $key
 */
function redis_has($key)
{
	$cache = redis_init();
	if ($cache)
	{
		if ($cache->has($key))
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
function redis_get($key)
{
	$cache = redis_init();

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
function redis_set($key, $val, $sec = 7200)
{
	$cache = redis_init();

	if ($cache)
	{
		return $cache->set($key, $val, $sec);
	}
	
	return false;
}


function redis_inc($key, $val = 1, $sec = 300)
{
	$cache = redis_init();

	if ($cache)
	{
	    $cache->incr($key, $val);
	    
	    if ($sec)
	    {
	        $cache->expire($key, 20);
	    }
	    
		return true;
	}
	return false;
}

function redis_dec($key, $val = 1)
{
	$cache = redis_init();

	if ($cache)
	{
		return $cache->decr($key, $val);
	}
	return false;
}

/**
 * redis cache del
 * @param string $key
 */
function redis_del($key)
{
	$cache = redis_init();

	if ($cache)
	{
		return $cache->has($key) ? $cache->del($key) : false;
	}
	return false;
}

/**
 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
 * 
 * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
 * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
 * count = 0 : 移除表中所有与 value 相等的值。
 * 
 * @param unknown $key
 * @param unknown $value
 * @param number $count
 * @return boolean
 */
function redis_lrem($key, $value, $count = 0)
{
    $cache = redis_init();
    if ($cache === false)
    {
    	return false;
    }
    
    return $cache->lrem($key, $count, $value);
}


/**
 * 以数组返回列表的值
 * 
 * @param unknown $key
 * @return boolean
 */
function redis_lget($key)
{
    $cache = redis_init();
    if ($cache === false)
    {
    	return false;
    }
    
    return $cache->lget($key);
}

/**
 * 返回列表的长度
 * 
 * @param unknown $key
 * @return boolean
 */
function redis_llen($key){
    $cache = redis_init();
    if ($cache === false)
    {
    	return false;
    }
    
    return $cache->llen($key);
}

/**
 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
 * 
 * @param unknown $key
 * @param unknown $offset
 * @param unknown $limit
 */
function redis_lrange($key, $offset, $limit){
    $cache = redis_init();
    if ($cache === false)
    {
    	return false;
    }
    
    return $cache->lrange($key, $offset, $limit);
}

//移除并返回列表 key 的尾元素。
function redis_rpop($key){
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->rpop($key);
}

//移除并返回列表 key 的头元素。
function redis_lpop($key){
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->lpop($key);
}

//将一个或多个值 value 插入到列表 key 的表尾(最右边)。
function redis_rpush($key, $val)
{
    $cache = redis_init();
	if ($cache === false)
    {
    	return false;
    }
    
    return $cache->rpush($key, $val);
}

//将一个或多个值 value 插入到列表 key 的表头
function redis_lpush($key, $val, $limit=null){    //模拟顶部入栈操作,limit为栈容量
    $cache = redis_init();
	if ($cache === false)
    {
    	return false;
    }
    
    return $cache->lpush($key, $val);
}

//
function redis_hget($key, $field){
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->hget($key, $field);
}
function redis_hset($key, $field, $val){
    $cache = redis_init();
    if ($cache === false)
    {
    	return false;
    }
    
    return $cache->hset($key, $field, $val);
}
function redis_hexists($key, $field){
    $cache = redis_init();
    if ($cache === false)
    {
    	return false;
    }
    
    return $cache->hexists($key, $field);
}
function redis_hlen($key){
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->hlen($key);
}

function redis_hvals($key){
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->hvals($key);
}



//将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
function redis_sadd($key, $val, $expire = 0){
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}

	$cache->sadd($key, $val);
	if ($expire)
	{
		$cache->expire($key, $expire);
	}

	return true;
}

function redis_spop($key){
    $cache = redis_init();
    if ($cache === false)
    {
    	return false;
    }
    
    return $cache->spop($key);
}
function redis_sismember($key, $member){
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->sismember($key, $member);
}
function redis_ssize($key){
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->ssize($key);
}

/*****************************
 * 有序集合
****************************/
//添加member
function redis_zadd($key, $member, $score, $expire = 0)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	$count = $cache->zadd($key, $member, $score);
	
	if ($expire)
	{
		$cache->expire($key, $expire);
	}
}

//获取member的排名(从小到大)
function redis_zrank($key, $member)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->zrank($key, $member);
}

//获取member的排名(从大到小)
function redis_zrevrank($key, $member)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->zrevrank($key, $member);
}

//增加/减少member的排名
function redis_zincrby($key, $member, $score)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->zincrby($key, $member, $score);
}

//查询member的score值
function redis_zscore($key, $member)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}
	
	return $cache->zscore($key, $member);
}

//统计score 值在 min 和 max 之间成员的数量
function redis_zcount($key, $min, $max)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}

	return $cache->zcount($key, $min, $max);
}

//获取排名在 min 和 max 之间成员列表, 按 score 值递减(从小到大)
function redis_zrange($key, $min, $max)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}

	return $cache->zrange($key, $min, $max);
}

//获取score在 min 和 max 之间成员列表, 按 score 值递减(从小到大)
function redis_zrangescore($key, $min, $max)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}

	return $cache->zrangescore($key, $min, $max);
}


//获取排名前count为的成员列表, 按 score 值递减(从小到大)
function redis_ztoprange($key, $count)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}

	return $cache->zrange($key, 0, $count - 1);
}

//获取排名在 min 和 max 之间成员列表, 按 score 值递减(从大到小)
function redis_zrevrange($key, $min, $max)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}

	return $cache->zrevrange($key, $min, $max);
}

//获取排名前count为的成员列表, 按 score 值递减(从大到小)
function redis_ztoprevrange($key, $count)
{
	$cache = redis_init();
	if ($cache === false)
	{
		return false;
	}

	return $cache->zrevrange($key, 0, $count - 1);
}