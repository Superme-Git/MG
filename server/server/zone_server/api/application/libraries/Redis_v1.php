<?php
/**
 * Redis 操作，支持 Master/Slave 的负载集群
*
* @author V哥
*/
class Redis_v1
{

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
     * 构造函数
     * @param boolean $isUseCluster 是否采用 M/S 方案
     */
    public function __construct($isUseCluster=false)
    {
        $this->_isUseCluster = $isUseCluster;
    } 

    private function import_convert($data){
        return is_array($data) ? json_encode($data) : $data;
    }
    private function export_convert($data){
        return json_decode($data) ? json_decode($data, true) : $data;
    }

	/**
	 * 连接服务器,注意：这里使用长连接，提高效率，但不会自动关闭
	 *
	 * @param array $config Redis服务器配置
	 * @param boolean $isMaster 当前添加的服务器是否为 Master 服务器
	 * @return boolean
	 */
	public function connect($config=array('host'=>'127.0.0.1','port'=>6379), $isMaster=true){
		// default port
		if(!isset($config['port'])){
			$config['port'] = 6379;
		}
		// 设置 Master 连接
		if($isMaster){
			$this->_linkHandle['master'] = new Redis();
			$ret = $this->_linkHandle['master']->pconnect($config['host'],$config['port']);

			log_message('DEBUG', __CLASS__.' '.__FUNCTION__ . ' redis pconnect Master ');
		}else{
			// 多个 Slave 连接
			$this->_linkHandle['slave'][$this->_sn] = new Redis();
			$ret = $this->_linkHandle['slave'][$this->_sn]->pconnect($config['host'],$config['port']);
			++$this->_sn;
		}
		return $ret;
	}

	/**
	 * 关闭连接
	 *
	 * @param int $flag 关闭选择 0:关闭 Master 1:关闭 Slave 2:关闭所有
	 * @return boolean
	 */
	public function close($flag=2){
		switch($flag){
			// 关闭 Master
			case 0:
				$this->getRedis()->close();
				break;
				// 关闭 Slave
			case 1:
				for($i=0; $i<$this->_sn; ++$i){
					$this->_linkHandle['slave'][$i]->close();
				}
				break;
				// 关闭所有
			case 2:
				$this->getRedis()->close();
				for($i=0; $i<$this->_sn; ++$i){
					$this->_linkHandle['slave'][$i]->close();
				}
				break;
		}
		return true;
	}

	/**
	 * 得到 Redis 原始对象可以有更多的操作
	 *
	 * @param boolean $isMaster 返回服务器的类型 true:返回Master false:返回Slave
	 * @param boolean $slaveOne 返回的Slave选择 true:负载均衡随机返回一个Slave选择 false:返回所有的Slave选择
	 * @return redis object
	 */
	public function getRedis($isMaster=true,$slaveOne=true){
		// 只返回 Master
		if($isMaster){
			return $this->_linkHandle['master'];
		}else{
			return $slaveOne ? $this->_getSlaveRedis() : $this->_linkHandle['slave'];
		}
	}

	/**
	 * 写缓存
	 *
	 * @param string $key 组存KEY
	 * @param string $value 缓存值
	 * @param int $expire 过期时间， 0:表示无过期时间
	 */
	public function set($key, $value, $expire=0){
		// 永不超时
        $value = self::import_convert($value);
		if($expire == 0){
			$ret = $this->getRedis()->set($key, $value);
		}else{
			$ret = $this->getRedis()->setex($key, $expire, $value);
		}
		return $ret;
	}

	/**
	 * 读缓存
	 *
	 * @param string $key 缓存KEY,支持一次取多个 $key = array('key1','key2')
	 * @return string || boolean  失败返回 false, 成功返回字符串
	 */
	public function get($key){
		// 是否一次取多个值
		$func = is_array($key) ? 'mGet' : 'get';
		// 没有使用M/S
		if(! $this->_isUseCluster){
			$data = $this->getRedis()->{$func}($key);
            return self::export_convert($data);
		}
		// 使用了 M/S
		$data = $this->_getSlaveRedis()->{$func}($key);
        return self::export_convert($data);
	}
	
	/**
	 * key 是否存在
	 *
	 * @param string $key 缓存KEY
	 * @return boolean
	 */
	public function has($key){
		return $this->getRedis()->exists($key);
	}
	
	/**
	 * 删除缓存
	 *
	 * @param string || array $key 缓存KEY，支持单个健:"key1" 或多个健:array('key1','key2')
	 * @return int 删除的健的数量
	 */
	public function del($key){
		// $key => "key1" || array('key1','key2')
		return $this->getRedis()->delete($key);
	}
	
	/**
	 * 值加加操作,类似 ++$i ,如果 key 不存在时自动设置为 0 后进行加加操作
	 *
	 * @param string $key 缓存KEY
	 * @param int $default 操作时的默认值
	 * @return int　操作后的值
	 */
	public function incr($key,$default=1){
		if($default == 1){
			return $this->getRedis()->incr($key);
		}else{
			return $this->getRedis()->incrBy($key, $default);
		}
	}
	
	/**
	 * 值减减操作,类似 --$i ,如果 key 不存在时自动设置为 0 后进行减减操作
	 *
	 * @param string $key 缓存KEY
	 * @param int $default 操作时的默认值
	 * @return int　操作后的值
	 */
	public function decr($key,$default=1){
		if($default == 1){
			return $this->getRedis()->decr($key);
		}else{
			return $this->getRedis()->decrBy($key, $default);
		}
	}
	
	/**
	 * 添空当前数据库
	 *
	 * @return boolean
	 */
	public function clear(){
		return $this->getRedis()->flushDB();
	}
	
	public function expire($key, $expire = 600){
		return $this->getRedis()->expire($key, (int)$expire);
	}

	/**
	 * 条件形式设置缓存，如果 key 不存时就设置，存在时设置失败
	 *
	 * @param string $key 缓存KEY
	 * @param string $value 缓存值
	 * @return boolean
	 */
	public function setnx($key, $value){
		return $this->getRedis()->setnx($key, $value);
	}
    public function lrem($key, $count, $value){
        $value = self::import_convert($value);
        return $this->getRedis()->lRemove($key, $value, $count);
    }
    public function llen($key){
        return $this->getRedis()->llen($key);
    }
    public function lpush($key, $value){
        return $this->getRedis()->lpush($key, self::import_convert($value));
    }
    public function rpush($key, $value){
        return $this->getRedis()->rpush($key, self::import_convert($value));
    }
    public function lget($key){ //list get
        $list = array();
        $len = $this->llen($key);
        for($i = 0; $len >= $i; $i++){
            $data = $this->getRedis()->lindex($key, $i);
            array_push($list, self::export_convert($data));
        }
        return $list;
    }
    public function lrange($key, $start, $limit){
        $end = $start + $limit - 1;
        $data = $this->getRedis()->lrange($key, $start, $end);
        $list = array();
        foreach($data as $k => $v){
            $list[$k] = self::export_convert($v);
        }
        return $list;
    }
    public function rpop($key){
        return $this->getRedis()->rpop($key);
    }
    public function lpop($key){
        return $this->getRedis()->lpop($key);
    }
    public function sadd($key, $val){
        return $this->getRedis()->sadd($key, self::import_convert($val));
    }
    //-----------------------------------
    public function hset($key, $field, $val){
        return $this->getRedis()->hset($key, $field, $val);
    }
    public function hget($key, $field){
    	return $this->getRedis()->hget($key, $field);
    }
    public function hexists($key, $field){
        return $this->getRedis()->hexists($key, $field);
    }
    public function hlen($key)
    {
        return $this->getRedis()->hLen($key);
    }
    public function hvals($key)
    {
        return $this->getRedis()->hVals($key);
    }
    //-----------------------------------
    public function spop($key){
        return $this->getRedis()->spop($key);
    }
    public function srem($key, $field){
    	return $this->getRedis()->srem($key, $field);
    }
    public function sismember($key, $member){
    	return $this->getRedis()->sismember($key, $member);
    }
    public function ssize($key){
    	return $this->getRedis()->ssize($key);
    }
	
	
	/******************************************
	 * 有序集合
	 *******************************************/
	public function zadd($key, $member, $score){
		return $this->getRedis()->zAdd($key, (int)$score, $member);
	}
	public function zrank($key, $member){
		return $this->getRedis()->zRank($key, $member);
	}
	public function zrevrank($key, $member){
		return $this->getRedis()->zRevRank($key, $member);
	}
	public function zincrby($key, $member, $score){
		return $this->getRedis()->zIncrBy($key, (int)$score, $member);
	}
	public function zscore($key, $member){
		return $this->getRedis()->zScore($key, $member);
	}
	public function zcount($key, $min, $max){
		return $this->getRedis()->zCount($key, (int)$min, (int)$max);
	}
	public function zrange($key, $start, $stop){
		return $this->getRedis()->zRange($key, (int)$start, (int)$stop);
	}
	public function zrevrange($key, $start, $stop){
		return $this->getRedis()->zrevrange($key, (int)$start, (int)$stop);
	}
	public function zrangescore($key, $min, $max){
	    return $this->getRedis()->zRangeByScore($key, (int)$min, (int)$max);
	}
	
	
	/* =================== 以下私有方法 =================== */

	/**
	 * 随机 HASH 得到 Redis Slave 服务器句柄
	 *
	 * @return redis object
	 */
	private function _getSlaveRedis(){
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

}// End Class

// ================= TEST DEMO =================

//// 只有一台 Redis 的应用
// $redis = new RedisCluster();
// $redis->connect(array('host'=>'127.0.0.1','port'=>6379));
// $redis->set('id',35);
// var_dump($redis->get('id'));


// // 有一台 Master 和 多台Slave 的集群应用
// $redis = new RedisCluster(true);
// $redis->connect(array('host'=>'127.0.0.1','port'=>6379), true);// master
// $redis->connect(array('host'=>'127.0.0.1','port'=>63791), false);// slave 1
// $redis->connect(array('host'=>'127.0.0.1','port'=>63792), false);// slave 2
// $redis->set('id',100);
// for($i=1; $i<=100; ++$i){
// var_dump($redis->get('id')).PHP_EOL;
// }

// // phpRedis 扩展的更多高级操作
// $redis = new RedisCluster();
// $redis->connect(array('host'=>'127.0.0.1','port'=>6379));
// $ret = $redis->getRedis()->ping();// phpRedis 原始API
// var_dump($ret);
