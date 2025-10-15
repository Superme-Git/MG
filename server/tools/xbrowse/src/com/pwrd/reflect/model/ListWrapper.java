package com.pwrd.reflect.model;

import java.util.TreeMap;
import java.util.Map;

import com.pwrd.util.Toolkit;

public class ListWrapper extends ObjectWrapper {

	ListWrapper(String name, Object object, ObjectWrapper parent, Object key) 
	{
		super(name, object, parent, key);
	}

	@Override
	public Map<String, ObjectWrapper> getFields() 
	{
		if(fields == null)
		{
			fields = new TreeMap<String, ObjectWrapper>();
			java.util.List<?> listObject = (java.util.List<?>)varValue;
			
			for(int i = 0; i < listObject.size(); i++)
			{
				fields.put(i+"", createObjectWrapper(i+"", listObject.get(i), this, null));
			}
		}
		return fields;
	}

	@Override
	public String toString() 
	{
		java.util.List<?> listObject = (java.util.List<?>)varValue;
		return "java.util.List " + varName + " <size = " + listObject.size() + ">";
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
		viewBuffer.append(tabString + "}");
		return viewBuffer.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setChildValue(ObjectWrapper child, Object objValue)
	{
		java.util.List listObject = (java.util.List)varValue;
		listObject.remove(child.varValue);
		listObject.add(objValue);
	}
}
