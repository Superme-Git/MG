package com.pwrd.reflect.model;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;

import com.pwrd.util.Toolkit;

public class CustomWrapper extends ObjectWrapper {

	CustomWrapper(String name, Object object, ObjectWrapper parent, Object key) {
		super(name, object, parent, key);
	}

	@Override
	public String toString() {
		return varValue.getClass().getName() + " " + varName;
	}
	
	@Override
	public Map<String, ObjectWrapper> getFields()
	{
		if(fields == null)
		{
			fields = new TreeMap<String, ObjectWrapper>();
			for(Field field : varValue.getClass().getDeclaredFields()) 
			{
				try 
				{
					field.setAccessible(true);
					fields.put(field.getName(), createObjectWrapper(field.getName(), field.get(varValue), this, null));
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
		return Collections.unmodifiableMap(fields);
	}

	@Override
	public String toCommandView(int tabCount)
	{
		String tabString = Toolkit.getTabString(tabCount);
		StringBuffer viewBuffer = new StringBuffer();
		viewBuffer.append(tabString + toString() + "\n");
		viewBuffer.append(tabString + "{\n");
		Map<String, ObjectWrapper> tmpFields = getFields();
		for(String key : tmpFields.keySet()) 
		{
			viewBuffer.append(tmpFields.get(key).toCommandView(tabCount + 1))
					  .append("\n");
		}
		return viewBuffer.append(tabString + "}").toString();
	}

	@Override
	public void setChildValue(ObjectWrapper child, Object objValue)
	{
		for(Field field : varValue.getClass().getDeclaredFields())
		{
			field.setAccessible(true);
			if(child.varName.equals(field.getName()))
			{
				try
				{
					field.set(varValue, objValue);
				} 
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
				break;
			}
		}
	}
}
