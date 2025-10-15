<?php
/**
 *
 * 模型继承类
 *
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.3.20
 *
 */
class MY_Model extends CI_Model
{
	var $class_vars = array();
	var $write_db = null;
	var $read_db = null;
	
    public function __construct()
    {
		parent::__construct();
    }
    
    /**
     * 根据主键获取单条信息
     *
     * @param string $id
     * @param number $timeout
     * @return array|null
     */
    public function getInfo($primaryVal, $timeout = 1800)
    {
    	if ($timeout < 1)
    	{
    		$query = $this->db()->where($this->getPrimaryKey(), $primaryVal)->get($this->getTable());
    		$info = $query->row_array();
    		log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db()->last_query()));
    	}
    	else 
    	{
    		$cache_key = cache_key($this->getTable() . ':' . get_called_class(), $primaryVal);
    		if (!cache_has($cache_key))
    		{
    			$query = $this->db()->where($this->getPrimaryKey(), $primaryVal)->get($this->getTable());
    			$info = $query->row_array();
    			log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db()->last_query()));
    			 
    			if ($info)
    			{
    				cache_set($cache_key, $info, $timeout);
    			}
    		}
    		else
    		{
    			$info = cache_get($cache_key);
    		}
    	}
    
    	//挂载联表数据
    	if ($info && method_exists(get_called_class(), 'getInfoRelation'))
    	{
    		$info = $this->getInfoRelation($info);
    	}
    
    	return $info;
    }
    
    public function getInfoByAttribute($where, $timeout = 600, $mount = true)
    {
    	if ($timeout < 1)
    	{
    		foreach ($where as $key => $val)
    		{
    			$this->db()->where($key, $val);
    		}
    		
    		if ($mount)
    		{
    			$qurey = $this->db()->select(array($this->getPrimaryKey()))->offset(0)->limit(1)->get($this->getTable());
    			$info = $qurey->row_array();
    		}
    		else
    		{
    			$query = $this->db()->offset(0)->limit(1)->get($this->getTable());
    			$info = $query->row_array();
    		}
    		log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db()->last_query()));
    	}
    	else 
    	{
    		$cache_key = cache_key($this->getTable() .':'. get_called_class().':'.__FUNCTION__, md5(serialize($where)));
    		if (!cache_has($cache_key))
    		{
    			foreach ($where as $key => $val)
    			{
    				$this->db()->where($key, $val);
    			}
    			
    			if ($mount)
    			{
    				$qurey = $this->db()->select(array($this->getPrimaryKey()))->offset(0)->limit(1)->get($this->getTable());
    				$info = $qurey->row_array();
    			}
    			else
    			{
    				$query = $this->db()->offset(0)->limit(1)->get($this->getTable());
    				$info = $query->row_array();
    			}
    			log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db()->last_query()));
    			 
    			if ($info)
    			{
    				cache_set($cache_key, $info, $timeout);
    			}
    		}
    		else
    		{
    			$info = cache_get($cache_key);
    		}
    	}
    	
    	if ($mount && $info)
    	{
    		$info = $this->getInfo($info[$this->getPrimaryKey()]);
    	}
    
    	//挂载联表数据
    	elseif (!$mount && $info && method_exists(get_called_class(), 'getInfoRelation'))
    	{
    		$info = $this->getInfoRelation($info);
    	}
    
    	return $info;
    }
    
    /**
     * 查询列表
     *
     * v2015.3.12
     *
     * @param array $where
     * @param array $order
     * @param array $offset
     * @param int $limit
     * @param int $groupby 
     * @param int $timeout 查询缓存时间, 默认为0, 即不缓存, 建议使用默认值. 因此缓存后无法及时获取到最新增加的记录. 且只是查询id
     * @param bool $mount : true 启用挂载模式, false 取消挂载模式
     * @return array
     */
    public function getList($where, $order = array(), $offset = 0, $limit = 20, $groupby = null, $timeout = 0, $mount = true)
    {
    	if ($timeout < 1)
    	{
    		$where && $this->getWhere($where);
    		 
    		$order && $this->getOrder($order);
    		 
    		$groupby && $this->getGroupBy($groupby);
    		
    		if ($mount)
    		{
    			$qurey = $this->db()->select(array($this->getPrimaryKey()))->offset($offset)->limit($limit)->get($this->getTable());
    			$list = $qurey->result_array();
    		}
    		else
    		{
    			$qurey = $this->db()->select()->offset($offset)->limit($limit)->get($this->getTable());
    			$list = $qurey->result_array();
    		}
    		log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db()->last_query()));
    	}
    	else
    	{
    		$cache_key = cache_key($this->getTable() .':'. get_called_class().':'.__FUNCTION__, md5(serialize(array($where, $order, $offset, $limit, $groupby, $timeout))));
    		if (!cache_has($cache_key))
    		{
    			$where && $this->getWhere($where);
    			 
    			$order && $this->getOrder($order);
    			 
    			$groupby && $this->getGroupBy($groupby);
    		
    			if ($mount)
    			{
    				$qurey = $this->db()->select(array($this->getPrimaryKey()))->offset($offset)->limit($limit)->get($this->getTable());
    				$list = $qurey->result_array();
    			}
    			else
    			{
    				$qurey = $this->db()->select()->offset($offset)->limit($limit)->get($this->getTable());
    				$list = $qurey->result_array();
    			}
    			 
    			log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db()->last_query()));
    			if ($list)
    			{
    				cache_set($cache_key, $list, $timeout);
    			}
    		}
    		else
    		{
    			$list = cache_get($cache_key);
    		}
    	}
    
    	//挂载联表数据
    	if ($mount && $list)
    	{
    		$list_ = array();
    		foreach ($list as $item)
    		{
    			$info = $this->getInfo($item[$this->getPrimaryKey()]);
    			if ($info)
    			{
    				$list_[] = $info;
    			}
    		}
    
    		$list = $list_;
    	}
    	elseif (!$mount && $list && method_exists(get_called_class(), 'getInfoRelation'))
    	{
    		$list_ = array();
    		foreach ($list as $item)
    		{
    			$info = $this->getInfoRelation($item);
    			$list_[] = $info;
    		}
    
    		$list = $list_;
    	}
    
    	return $list;
    
    }
    
    /**
     * 查询统计数
     *
     * v2015.3.12
     *
     * @param array $where
     * @param int $timeout 缓存结果集
     * @return array
     */
    public function getCount($where, $timeout = 0)
    {
    	if ($timeout < 1)
    	{
    		$where && $this->getWhere($where);
    		 
    		$count = $this->db()->count_all_results($this->getTable());
    		log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db()->last_query()));
    	}
    	else 
    	{
    		$cache_key = cache_key($this->getTable() .':'. get_called_class().':'.__FUNCTION__, md5(serialize(array($where, $timeout))));
    		if (!cache_has($cache_key))
    		{
    			$where && $this->getWhere($where);
    			 
    			$count = $this->db()->count_all_results($this->getTable());
    			log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db()->last_query()));
    			 
    			if ($count)
    			{
    				cache_set($cache_key, $count, $timeout);
    			}
    		}
    		else
    		{
    			$count = cache_get($cache_key);
    		}
    	}
    	
    	return $count;
    }
    
    public function insertInfo($data)
    {
    	$this->db('write')->insert($this->getTable(), $data);
    	log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db('write')->last_query()));
    	return $this->db('write')->insert_id();
    }
    
    public function deleteInfo($primaryVal)
    {
    	$result = $this->db('write')->where($this->getPrimaryKey(), $primaryVal)->delete($this->getTable());
    	log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db('write')->last_query()));
    
    	//删除该条信息的缓存
    	$cache_key = cache_key($this->getTable() . ':' . get_called_class(), $primaryVal);
    	if (cache_has($cache_key))
    	{
    		cache_del($cache_key);
    	}
    
    	return $result;
    }
    
    public function updateInfo($primaryVal, $attributes)
    {
    	$result = $this->db('write')->set($attributes)->where($this->getPrimaryKey(), $primaryVal)->update($this->getTable());
    	log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db('write')->last_query()));
    	
    	//删除该条信息的缓存
    	$cache_key = cache_key($this->getTable() . ':' . get_called_class(), $primaryVal);
    	if (cache_has($cache_key))
    	{
    		$cacheInfo = cache_get($cache_key);
    		foreach ($attributes as $field => $val)
    		{
    			$cacheInfo[$field] = $val;
    		}
    		
    		cache_set($cache_key, $cacheInfo, 600);
    	}
    
    	return $result;
    }
    
    public function increment($primaryVal, $attributes)
    {
    	foreach ($attributes as $key => $val)
    	{
    		$this->db('write')->set($key, $key .'+'.$val, false);//第三个参数false意为不需要escape
    	}
    	
    	$result = $this->db('write')->where($this->getPrimaryKey(), $primaryVal)->update($this->getTable());
    	log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db('write')->last_query()));
    	
    	//更新缓存
    	$cache_key = cache_key($this->getTable() . ':' . get_called_class(), $primaryVal);
    	if (cache_has($cache_key))
    	{
    		$cacheInfo = cache_get($cache_key);
    			
    		foreach ($attributes as $field => $val)
    		{
    			if (is_numeric($field) && !empty($cacheInfo[$val]))
    			{
    				$cacheInfo[$val] = (int)$cacheInfo[$val] + 1;
    			}
    			elseif (is_numeric($field))
    			{
    				$cacheInfo[$val] = 1;
    			}
    			elseif (!empty($cacheInfo[$field]))
    			{
    				$cacheInfo[$field] = (int)$cacheInfo[$field] + $val;
    			}
    			else
    			{
    				$cacheInfo[$field] = $val;
    			}
    		}
    		
    		cache_set($cache_key, $cacheInfo, 600);
    	}
    
    	return $result;
    }
    
    public function decrement($primaryVal, $attributes)
    {
    	foreach ($attributes as $key => $val)
    	{
    		$this->db('write')->set($key, $key .'-'.$val, false);//第三个参数false意为不需要escape
    	}
    	
    	$result = $this->db('write')->where($this->getPrimaryKey(), $primaryVal)->update($this->getTable());
    	log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db('write')->last_query()));
    	
    	//更新缓存
    	$cache_key = cache_key($this->getTable() . ':' . get_called_class(), $primaryVal);
    	if (cache_has($cache_key))
    	{
    		$cacheInfo = cache_get($cache_key);
    
    		foreach ($attributes as $field => $val)
    		{
    			if (is_numeric($field) && isset($cacheInfo[$val]))
    			{
    				$cacheInfo[$val] = (int)$cacheInfo[$val] - 1;
    				$cacheInfo[$val] < 0 && ($cacheInfo[$val] = 0);
    			}
    			elseif (is_numeric($field))
    			{
    			    log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($attributes));
    				return false;
    			}
    			elseif (!empty($cacheInfo[$field]))
    			{
    				$cacheInfo[$field] = (int)$cacheInfo[$field] - $val;
    				$cacheInfo[$val] < 0 && ($cacheInfo[$val] = 0);
    			}
    			else
    			{
    			    log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' false '.serialize($attributes));
    				return false;
    			}
    		}
    
    		cache_set($cache_key, $cacheInfo, 600);
    	}
    
    	return $result;
    }
    
    protected function db($type = 'read')
    {
        if(!$this->write_db)
        {
            $this->write_db = $this->load->database('write', true);
        }
        
        if (!$this->read_db)
        {
            $this->read_db = $this->load->database('read', true);
        }
        
        $ret_db = ($type == 'write' ? $this->write_db : $this->read_db);
        log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' db '.serialize($type));
        
    	return $ret_db;
    }
    
    protected function get_class_vars($force = false)
    {
    	if (!$this->class_vars || $force === true)
    	{
    		$this->class_vars = get_class_vars(get_called_class());
    	}
    	
    	return $this->class_vars;
    }
    
    protected function getPrimaryKey()
    {
    	$vars = $this->get_class_vars();
    	
    	return $vars['primaryKey'];
    }
    
    protected function getTable()
    {
    	$vars = $this->get_class_vars();
    
    	return $vars['table'];
    }
    
    /**
     * 设置水平分表
     * 根据某值取余切分表
     * 
     * @param int $subtag : 分表标志, 如user_id
     */
    public function setSubtableTag($subtag)
    {
    	$vars = $this->get_class_vars();
    	
    	if (!empty($vars['subtableTagCount']))
    	{
    		$subtable = ($subtag % $vars['subtableTagCount']);
    		
    		//1-n
    		if ($subtable)
    		{
    			$this->class_vars['table'] = $vars['table'] .'_'. $subtable;
    			
    			log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' use ori table '.json_encode_cn(array($subtag, $this->class_vars)));
    		}
    		//0
    		else
    		{
    			//使用原表
    			log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' use ori table '.json_encode_cn(array($subtag, $vars)));
    		}
    	}
    	else 
    	{
    		log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' subtableTagCount empty '.json_encode_cn(array($subtag, $vars)));
    	}
    	
    	return ;
    }
    
    /**
     * 设置水平分表
     * 根据日期切分表
     * 
     * setSubtableSuffix(); 即使用原表
     *
     * @param string $subtag : 分表后缀, 如201512, 即匹配xxx_201512
     */
    public function setSubtableSuffix($subtag = null)
    {
    	//要取原始配置
    	$vars = $this->get_class_vars(true);
		
    	//有值时执行分表切换
    	if ($subtag)
    	{
    		$this->class_vars['table'] = $vars['table'] .'_'. $subtag;
    	}
    	
    	return ;
    }
    
    private function getWhere($where)
    {
    	if (!empty($where['like']))
    	{
    		foreach ($where['like'] as $key => $val)
    		{
    			$this->db()->like($key, $val);
    		}
    		$where['like'] = '';
    		unset($where['like']);
    	}
    	if (!empty($where['in']))
    	{
    		foreach ($where['in'] as $key => $val)
    		{
    			$this->db()->where_in($key, $val);
    		}
    		$where['in'] = '';
    		unset($where['in']);
    	}
    	if (!empty($where['notin']))
    	{
    		foreach ($where['notin'] as $key => $val)
    		{
    			$this->db()->where_not_in($key, $val);
    		}
    		$where['notin'] = '';
    		unset($where['notin']);
    	}
    	if (!empty($where['less']))
    	{
    		foreach ($where['less'] as $key => $val)
    		{
    			$this->db()->where($key. ' < ', $val, false);
    		}
    		$where['less'] = '';
    		unset($where['less']);
    	}
    	if (!empty($where['lessthan']))
    	{
    		foreach ($where['lessthan'] as $key => $val)
    		{
    			$this->db()->where($key. ' <= ', $val, false);
    		}
    		$where['lessthan'] = '';
    		unset($where['lessthan']);
    	}
    	if (!empty($where['great']))
    	{
    		foreach ($where['great'] as $key => $val)
    		{
    			$this->db()->where($key. ' > ', $val, false);
    		}
    		$where['great'] = '';
    		unset($where['great']);
    	}
    	if (!empty($where['greatthan']))
    	{
    		foreach ($where['greatthan'] as $key => $val)
    		{
    			$this->db()->where($key. ' >= ', $val, false);
    		}
    		$where['greatthan'] = '';
    		unset($where['greatthan']);
    	}
    	if (!empty($where['exist']))
    	{
    		foreach ($where['exist'] as $key => $val)
    		{
    			if (is_numeric($key))
    			{
    				$key = $val;
    			}
    			$this->db()->where_in($key, array('', null, 0));
    		}
    		$where['exist'] = '';
    		unset($where['exist']);
    	}
    	if (!empty($where['not_exist']))
    	{
    		foreach ($where['not_exist'] as $key => $val)
    		{
    			if (is_numeric($key))
    			{
    				$key = $val;
    			}
    			$this->db()->where_not_in($key, array('', null, 0));
    		}
    		$where['not_exist'] = '';
    		unset($where['not_exist']);
    	}
    	 
    	if (!empty($where))
    	{
    		foreach ($where as $key => $val)
    		{
    			$this->db()->where($key, $val);
    		}
    	}
    	
    	return true;
    }
    
    private function getOrder($order)
    {
    	if (is_array($order))
    	{
    		foreach ($order as $key_ => $val_)
    		{
    			$this->db()->order_by($key_, $val_);
    		}
    	}
    	else
    	{
    		$this->db()->order_by($order);
    	}
    }
    
    private function getGroupBy($groupby)
    {
    	if (is_array($groupby))
    	{
    		foreach ($groupby as $key_)
    		{
    			$this->db()->group_by($key_);
    		}
    	}
    	else
    	{
    		$this->db()->group_by($groupby);
    	}
    }
}
?>