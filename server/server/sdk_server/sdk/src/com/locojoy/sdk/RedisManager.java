package com.locojoy.sdk;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {
	private static JedisPool pool = null;
	
	public static JedisPool getPool() {
		if(pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(30);
			config.setMaxWaitMillis(1000 * 100);
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, Config.getInstance().getRedisServerIP(), Config.getInstance().getRedisServerPort());
		}
		return pool;
	}
	
	public static String get(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(jedis != null)
				jedis.close();
		}
		return value;
	}
	
	public static String set(String key, String value) {
		String ret = null;
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			ret = jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(jedis != null)
				jedis.close();
		}
		return ret;
	}
}
