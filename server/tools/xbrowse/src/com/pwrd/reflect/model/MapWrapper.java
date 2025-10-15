package com.pwrd.reflect.model;

import java.util.TreeMap;
import java.util.Map;

import com.pwrd.util.Toolkit;

public class MapWrapper extends ObjectWrapper {

	MapWrapper(String name, Object object, ObjectWrapper parent, Object key) {
		super(name, object, parent, key);
	}
	
	public Map<String, ObjectWrapper> getFields() 
	{
		if(fields == null)
		{
			fields = new TreeMap<String, ObjectWrapper>();
			Map<?, ?> mapObject = (Map<?, ?>)varValue;
			for( Map.Entry<?, ?> entry : mapObject.entrySet())
			{
				String key = (entry.getKey() == null ? "null" : entry.getKey().toString());
				Object value = entry.getValue();
				fields.put(key, createObjectWrapper(key, value, this, entry.getKey()));
			}
		}
		return fields;
	}
	
	@Override
	public String toString() {
		Map<?, ?> mapObject = (Map<?, ?>)varValue;
		return "java.util.Map " + varName + " <size = " + mapObject.size() + ">";
	}
	
	@Override
	public String toCommandView(int tabCount) 
	{
		String tabString = Toolkit.getTabString(tabCount);
		String tabStringPlus = Toolkit.getTabString(tabCount + 1);
		StringBuffer viewBuffer = new StringBuffer();
		viewBuffer.append(tabString + toString())
				  .append("\n");
		viewBuffer.append(tabString + "{\n");
		Map<String, ObjectWrapper> tmpFields = getFields();
		for(String key : tmpFields.keySet())
		{
			viewBuffer.append(tabStringPlus + key)
					  .append(" --> \n")
					  .append(tmpFields.get(key).toCommandView(tabCount + 1))
					  .append("\n");
		}
		viewBuffer.append(tabString + "}");
		return viewBuffer.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setChildValue(ObjectWrapper child, Object objValue)
	{
		Map mapObj = (Map)varValue;
		mapObj.remove(child.key);
		mapObj.put(child.key, objValue);
	}
}
