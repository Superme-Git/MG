package com.pwrd.serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.pwrd.io.IO;

public class JavaSerializationManager
{
	private static JavaSerializationManager instance = new JavaSerializationManager();
	
	private Map<String, Object> jobjs = new HashMap<String, Object>();
	public String curJobjName;
	
	public static JavaSerializationManager getInstance()
	{
		return instance;
	}
	
	public void put(String name, Object obj)
	{
		jobjs.put(name, obj);
	}
	
	public void setCurrent(String name)
	{
		curJobjName = name;
	}
	
	public Object getCurrent()
	{
		if(curJobjName == null)
		{
			IO.getInstance().println("Please select the current serializable object by use command.");
			return null;
		}
		return jobjs.get(curJobjName);
	}
	
	public Set<String> getAll()
	{
		return Collections.unmodifiableSet(jobjs.keySet());
	}
}
