<?php

/**
 * 分发操作类
 * 用于分发礼包码等
 * 
 * 原理
 * 根据设置的链表的长度, 超过此长度, 将自动创建子链表
 * 
 * key
 * key1
 * key2
 * ...
 * 
 * 弊端
 * 过多的key会加重负载, 应控制在5个以内
 *
 * @copyright Locojoy.com
 * @author lihuixu@locojoy.com
 * @version v1.0 2015.12.16
 */

class Distribute_table
{
	//每个链表的数量
	private $count_pre_linktable = 10000;
	
	//添加元素
	public function add($activity_key, $code, $sub_tag = 0)
	{
		//链表的key
		$key = redis_key('distribute_table:linktable', $activity_key . ($sub_tag ? $sub_tag : ''));
		
		//判断链表的是否存在
		if (redis_has($key))
		{
			if (redis_ssize($key) >= $this->count_pre_linktable)
			{
				//尝试分配下一个链表
				return $this->add($activity_key, $code, $sub_tag + 1);
			}
			
			return redis_sadd($key, $code);
		}
		else
		{
			return redis_sadd($key, $code);
		}
	}
	
	//判断元素是否存在
	public function exist($activity_key, $code, $sub_tag = 0)
	{
		//链表的key
		$key = redis_key('distribute_table:linktable', $activity_key . ($sub_tag ? $sub_tag : ''));
	
		//判断链表的是否存在
		if (redis_has($key))
		{
			if (redis_ssize($key) > 0)
			{
				$is_exist = redis_sismember($key, $code);
				if ($is_exist)
				{
					return true;
				}
				else
				{
					//尝试分配下一个链表
					return $this->exist($activity_key, $sub_tag + 1);
				}
			}
			
			//尝试分配下一个链表
			return $this->exist($activity_key, $sub_tag + 1);
		}
	
		//若没有此链表, 即也无子链表, 返回false
		return false;
	}
	
	//分发元素
	public function distribute($activity_key, $sub_tag = 0)
	{
		//链表的key, 初始, 分表的Key
		$key = redis_key('distribute_table:linktable', $activity_key . ($sub_tag ? $sub_tag : ''));
		
		//判断链表的是否存在
		if (redis_has($key))
		{
			if (redis_ssize($key) > 0)
			{
				return redis_spop($key);
			}
			
			//尝试分配下一个链表
			return $this->distribute($activity_key, $sub_tag + 1);
		}
		
		//若没有此链表, 即也无子链表, 返回false
		return false;
	}
	
	public function set_count($count = 10000)
	{
		return $this->count_pre_linktable = $count;
	}
	
	public function get_count()
	{
		return $this->count_pre_linktable;
	}
	
}