package com.pwrd.dbx;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

/**
 *<code>Dbx<code>实例管理器。本系统用到的所有
 *mkdb数据库都用它进行统一管理。 
 * 
 */
public class DbxManager
{
	private static Log logger = LogFactory.getLog(DbxManager.class);
	private static DbxManager dbxManager = new DbxManager();
	
	private ReentrantReadWriteLock dbxsLock;
	private ReadLock dbxsReadLock;
	private WriteLock dbxsWriteLock;
	private Map<String, Dbx> dbxs = null;
	private String curDbxName = null;
	
	public static DbxManager getInstance()
	{
		return dbxManager;
	}
	
	private DbxManager()
	{
		Dbx.start(".");
		Runtime.getRuntime().addShutdownHook(new Thread("mkdb.ShutdownHook") {
			@Override
			public void run() 
			{
				// logger.info("mkdb stop from ShutdownHook.");
				Dbx.stop();
			}
		});
		
		dbxs = new HashMap<String, Dbx>();
		dbxsLock = new ReentrantReadWriteLock();
		dbxsReadLock = dbxsLock.readLock();
		dbxsWriteLock = dbxsLock.writeLock();
		
		// 默认从mkdb_data目录载入dbx
		File dbxsDir = new File("./mkdb_data");
		if(dbxsDir.exists() && dbxsDir.isDirectory())
		{
			File[] files = dbxsDir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) 
				{
					if(pathname.isDirectory())
						return true;
					return false;
				}
				
			});
			for(File file : files)
			{
				addDbx(file);
			}
		}
	}
	
	public Dbx getDbx(String dbxName)
	{
		try
		{
			dbxsReadLock.lock();
			if(dbxs.containsKey(dbxName))
				return dbxs.get(dbxName);
			else
			{
				logger.error("There isn't a Db named " + dbxName);
				return null;
			}
		}
		finally
		{
			dbxsReadLock.unlock();
		}
	}
	
	public boolean addDbx(String dbPath)
	{
		File dbxFile = new File(dbPath);
		if(!dbxFile.exists())
			dbxFile = new File("mkdb_data" + dbPath);
		return addDbx(dbxFile);
	}
	
	public boolean addDbx(File dbxFile)
	{
		try
		{
			dbxsReadLock.lock();
			if(dbxs.containsKey(dbxFile.getName()))
			{
				logger.warn(dbxFile.getName() + " has existed!");
				return false;
			}
		}
		finally
		{
			dbxsReadLock.unlock();
		}
		
		if(dbxFile.exists() && dbxFile.isDirectory())
		{
			Dbx dbx = null;
			try
			{
				dbx = Dbx.open(dbxFile, DatabaseMetaData.getInstance());
			}
			catch(IllegalAccessError e)
			{
				// swallow
				logger.error(e.getMessage());
				return false;
			}
			try
			{
				dbxsWriteLock.lock();
				dbxs.put(dbxFile.getName(), dbx);
			}
			finally
			{
				dbxsWriteLock.unlock();
				// curDbx = dbx;
			}
			return true;
		}
		
		logger.error("Failed to open " + dbxFile.getPath());
		return false;
	}
	
	public boolean removeDbx(String dbxName)
	{
		try
		{
			dbxsWriteLock.lock();
			{
				Dbx dbx = dbxs.remove(dbxName);
				if(dbx != null)
				{
					dbx.close();
					return true;
				}
			}
		}
		finally
		{
			dbxsWriteLock.unlock();
		}
		
		return false;
	}
	
	public Set<String> getDbxNames()
	{
		try
		{
			dbxsReadLock.lock();
			return Collections.unmodifiableSet(dbxs.keySet());
		}
		finally
		{
			dbxsReadLock.unlock();
		}
	}
	
	public Dbx getCurrentDbx()
	{
		if(curDbxName == null)
		{
			logger.warn("The current dbx isn't set.");
			return null;
		}
		return getDbx(curDbxName);
	}
	
	public void setCurrentDbx(String dbxName)
	{
		logger.info("The current dbx is: " + dbxName);
		curDbxName = dbxName;
	}
}
