package com.pwrd.definition;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

public class DatabaseMetaDataWrapper 
{
	private static Log logger = LogFactory.getLog(DatabaseMetaDataWrapper.class);
	private static DatabaseMetaDataWrapper instance;
	private static ReentrantLock initLock;
	private Map<Class<?>, DatabaseMetaData.Type> class2Types;
	private Map<String, DatabaseMetaData.Table> tables;
	private Map<String, DatabaseMetaData.Bean> beans;
	
	static
	{
		initLock = new ReentrantLock();
	}
	
	private DatabaseMetaDataWrapper()
	{
		class2Types = new HashMap<Class<?>, DatabaseMetaData.Type>();
		for(DatabaseMetaData.Type metaType : DatabaseMetaData.getInstance().getTypes())
		{
			try
			{
				if(class2Types.containsKey(metaType.getClazz()))
					logger.warn("Duplicate class name: " + metaType.getClassName());
				else
					class2Types.put(metaType.getClazz(), metaType);
			}
			catch(Exception e)
			{
				// class not found exception, just swallow.
			}
		}
		tables = new TreeMap<String, DatabaseMetaData.Table>();
		for(DatabaseMetaData.Table table : DatabaseMetaData.getInstance().getTables())
			tables.put(table.getName(), table);
		beans = new TreeMap<String, DatabaseMetaData.Bean>();
		for(DatabaseMetaData.Bean bean : DatabaseMetaData.getInstance().getBeans())
			beans.put(bean.getName(), bean);
	}
	
	public static DatabaseMetaDataWrapper getInstance()
	{
		try
		{
			initLock.lock();
			if(instance == null)
				instance = new DatabaseMetaDataWrapper();
		}
		finally
		{
			initLock.unlock();
		}
		return instance;
	}
	
	public DatabaseMetaData.Type getType(Class<?> clazz)
	{
		if(class2Types.containsKey(clazz))
			return class2Types.get(clazz);
		else
		{
			logger.error("Invalid class type: " + clazz.getName());
			return null;
		}
	}
	
	public boolean containsType(Class<?> clazz)
	{
		return class2Types.containsKey(clazz);
	}
	
	public Collection<DatabaseMetaData.Table> getTables()
	{
		return Collections.unmodifiableCollection(tables.values());
	}
	
	public Collection<DatabaseMetaData.Bean> getBeans()
	{
		return Collections.unmodifiableCollection(beans.values());
	}
	
	public boolean containsTable(String tableName)
	{
		return tables.containsKey(tableName);
	}
	
	public boolean containsBean(String beanName)
	{
		return beans.containsKey(beanName);
	}
}
