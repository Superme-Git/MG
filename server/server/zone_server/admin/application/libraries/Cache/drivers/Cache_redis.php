<?php
/**
 * CodeIgniter
 *
 * An open source application development framework for PHP
 *
 * This content is released under the MIT License (MIT)
 *
 * Copyright (c) 2014 - 2016, British Columbia Institute of Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @package	CodeIgniter
 * @author	EllisLab Dev Team
 * @copyright	Copyright (c) 2008 - 2014, EllisLab, Inc. (https://ellislab.com/)
 * @copyright	Copyright (c) 2014 - 2016, British Columbia Institute of Technology (http://bcit.ca/)
 * @license	http://opensource.org/licenses/MIT	MIT License
 * @link	https://codeigniter.com
 * @since	Version 3.0.0
 * @filesource
 */
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * CodeIgniter Redis Caching Class
 *
 * @package	   CodeIgniter
 * @subpackage Libraries
 * @category   Core
 * @author	   Anton Lindqvist <anton@qvister.se>
 * @link
 */
class CI_Cache_redis extends CI_Driver
{
	/**
	 * An internal cache for storing keys of serialized values.
	 *
	 * @var	array
	 */
	protected $_serialized = array();
	
	// 是否使用 M/S 的读写集群方案
	private $_isUseCluster = false;

	// Slave 句柄标记
	private $_sn = 0;

	// 服务器连接句柄
	private $_linkHandle = array(
			'master'=>null,// 只支持一台 Master
			'slave'=>array(),// 可以有多台 Slave
	);
	
	/**
	 * 是否采用 M/S 方案
	 */
	public function __construct()
	{
		if ( ! $this->is_supported())
		{
			log_message('error', 'Cache: Failed to create Redis object; extension not loaded?');
			return;
		}
		
		$redisConfig = array();
		$CI =& get_instance();
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
			$this->_isUseCluster = true;
			$this->connect(array('host'=>$redisConfig['master']['host'], 'port'=>$redisConfig['master']['port']), true);// master
			
			for ($i = 0; $i< count($redisConfig['slave']); $i++)
			{
				if ($redisConfig['slave'][$i]['status'] == true)
					$this->connect(array('host'=>$redisConfig['slave'][$i]['host'], 'port'=>$redisConfig['slave'][$i]['port']), false);// slave
			}
		}
		else 
		{
			$this->connect(array('host'=>$redisConfig['master']['host'], 'port'=>$redisConfig['master']['port']));
		}
	}
	
	/**
	 * 连接服务器,注意：这里使用长连接，提高效率，但不会自动关闭
	 *
	 * @param array $config Redis服务器配置
	 * @param boolean $isMaster 当前添加的服务器是否为 Master 服务器
	 * @return boolean
	 */
	public function connect($config=array('host'=>'127.0.0.1','port'=>6379), $isMaster=true)
	{
		// default port
		if (!isset($config['port']))
		{
			$config['port'] = 6379;
		}
		// 设置 Master 连接
		if ($isMaster)
		{
			$this->_linkHandle['master'] = new Redis();
			$ret = $this->_linkHandle['master']->pconnect($config['host'],$config['port']);

			log_message('DEBUG', __CLASS__.' '.__FUNCTION__ . ' redis pconnect Master ');
		}
		else
		{
			// 多个 Slave 连接
			$this->_linkHandle['slave'][$this->_sn] = new Redis();
			$ret = $this->_linkHandle['slave'][$this->_sn]->pconnect($config['host'],$config['port']);
			++$this->_sn;
		}
		return $ret;
	}
	
	/**
	 * Get cache
	 ** @param string $key 缓存KEY,支持一次取多个 $key = array('key1','key2')
	 * @return string || boolean  失败返回 false, 成功返回字符串
	 * @param	string	$key	Cache ID
	 * @return	mixed
	 */
	public function get($key)
	{
		// 是否一次取多个值
		$func = is_array($key) ? 'mGet' : 'get';
		// 没有使用M/S
		if (! $this->_isUseCluster)
		{
			$value = $this->getRedis()->{$func}($key);
		}
		// 使用了 M/S
		$value = $this->_getSlaveRedis()->{$func}($key);
		//if ($value !== FALSE && isset($this->_serialized[$key]))
		//{
		//	return unserialize($value);
		//}
		
		if (!$value)
		{
			return $value;
		}
		
		if(is_numeric($value))
		{
			return $value;
		}
		
		if (preg_match('/^\w+$/', $value))
		{
			return $value;
		}
		
		if (strpos($value, ':'))
		{
			return unserialize($value);
		}
		
		return $value;
	}

	// ------------------------------------------------------------------------

	/**
	 * Save cache
	 *
	 * @param	string	$id	Cache ID
	 * @param	mixed	$data	Data to save
	 * @param	int	$ttl	Time to live in seconds
	 * @param	bool	$raw	Whether to store the raw value (unused)
	 * @return	bool	TRUE on success, FALSE on failure
	 */
	public function save($id, $data, $ttl = 60, $raw = FALSE)
	{
		if (is_array($data) OR is_object($data))
		{
			//if ( ! $this->_redis->sIsMember('_ci_redis_serialized', $id) && ! $this->_redis->sAdd('_ci_redis_serialized', $id))
			//{
			//	return FALSE;
			//}

			//isset($this->_serialized[$id]) OR $this->_serialized[$id] = TRUE;
			$data = serialize($data);
		}
		//elseif (isset($this->_serialized[$id]))
		//{
			//$this->_serialized[$id] = NULL;
			//$this->_redis->sRemove('_ci_redis_serialized', $id);
		//}
		
		return $this->getRedis()->set($id, $data, $ttl);
	}

	// ------------------------------------------------------------------------

	/**
	 * Delete from cache
	 *
	 * @param	string	$key	Cache key
	 * @return	bool
	 */
	public function delete($key)
	{
		if ($this->getRedis()->delete($key) !== 1)
		{
			return FALSE;
		}

		//if (isset($this->_serialized[$key]))
		//{
		//	$this->_serialized[$key] = NULL;
		//	$this->_redis->sRemove('_ci_redis_serialized', $key);
		//}

		return TRUE;
	}

	// ------------------------------------------------------------------------

	/**
	 * Increment a raw value
	 *
	 * @param	string	$id	Cache ID
	 * @param	int	$offset	Step/value to add
	 * @return	mixed	New value on success or FALSE on failure
	 */
	public function increment($id, $offset = 1)
	{
		return $this->getRedis()->incr($id, $offset);
	}

	// ------------------------------------------------------------------------

	/**
	 * Decrement a raw value
	 *
	 * @param	string	$id	Cache ID
	 * @param	int	$offset	Step/value to reduce by
	 * @return	mixed	New value on success or FALSE on failure
	 */
	public function decrement($id, $offset = 1)
	{
		return $this->getRedis()->decr($id, $offset);
	}

	// ------------------------------------------------------------------------

	/**
	 * Clean cache
	 *
	 * @return	bool
	 * @see		Redis::flushDB()
	 */
	public function clean()
	{
		return $this->getRedis()->flushDB();
	}

	// ------------------------------------------------------------------------

	/**
	 * Get cache driver info
	 *
	 * @param	string	$type	Not supported in Redis.
	 *				Only included in order to offer a
	 *				consistent cache API.
	 * @return	array
	 * @see		Redis::info()
	 */
	public function cache_info($type = NULL)
	{
		return $this->getRedis()->info();
	}

	// ------------------------------------------------------------------------

	/**
	 * Get cache metadata
	 *
	 * @param	string	$key	Cache key
	 * @return	array
	 */
	public function get_metadata($key)
	{
		$value = $this->get($key);

		if ($value !== FALSE)
		{
			return array(
				'expire' => time() + $this->getRedis()->ttl($key),
				'data' => $value
			);
		}

		return FALSE;
	}

	// ------------------------------------------------------------------------

	/**
	 * Check if Redis driver is supported
	 *
	 * @return	bool
	 */
	public function is_supported()
	{
		return extension_loaded('redis');
	}

	// ------------------------------------------------------------------------

	/**
	 * Class destructor
	 *
	 * Closes the connection to Redis if present.
	 *
	 * @return	void
	 */
	public function __destruct()
	{
//		if ($this->_redis)
//		{
//			$this->_redis->close();
//		}
		
		$this->getRedis()->close();
		for($i=0; $i<$this->_sn; ++$i){
			$this->_linkHandle['slave'][$i]->close();
		}
		
	}
	
	/**
	 * 得到 Redis 原始对象可以有更多的操作
	 *
	 * @param boolean $isMaster 返回服务器的类型 true:返回Master false:返回Slave
	 * @param boolean $slaveOne 返回的Slave选择 true:负载均衡随机返回一个Slave选择 false:返回所有的Slave选择
	 * @return redis object
	 */
	public function getRedis($isMaster=true,$slaveOne=true)
	{
		// 只返回 Master
		if ($isMaster)
		{
			return $this->_linkHandle['master'];
		}
		else
		{
			return $slaveOne ? $this->_getSlaveRedis() : $this->_linkHandle['slave'];
		}
	}
	
	/* =================== 以下私有方法 =================== */

	/**
	 * 随机 HASH 得到 Redis Slave 服务器句柄
	 *
	 * @return redis object
	 */
	private function _getSlaveRedis()
	{
		// 就一台 Slave 机直接返回
		if($this->_sn <= 1){
			return $this->_linkHandle['slave'][0];
		}
		// 随机 Hash 得到 Slave 的句柄
		$hash = $this->_hashId(mt_rand(), $this->_sn);
		return $this->_linkHandle['slave'][$hash];
	}

	/**
	 * 根据ID得到 hash 后 0～m-1 之间的值
	 *
	 * @param string $id
	 * @param int $m
	 * @return int
	 */
	private function _hashId($id,$m=10)
	{
		//把字符串K转换为 0～m-1 之间的一个值作为对应记录的散列地址
		$k = md5($id);
		$l = strlen($k);
		$b = bin2hex($k);
		$h = 0;
		for($i=0;$i<$l;$i++)
		{
		//相加模式HASH
			$h += substr($b,$i*2,2);
		}
		$hash = ($h*1)%$m;
		return $hash;
	}
	
}
