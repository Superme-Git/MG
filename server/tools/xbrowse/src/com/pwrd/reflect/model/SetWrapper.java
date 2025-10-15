package com.pwrd.reflect.model;

import java.util.TreeMap;
import java.util.Map;
import java.util.Set;

import com.pwrd.util.Toolkit;

public class SetWrapper extends ObjectWrapper {

	static long uid = 0;
	SetWrapper(String name, Object object, ObjectWrapper parent, Object key) {
		super(name, object, parent, key);
	}

	@Override
	public Map<String, ObjectWrapper> getFields() 
	{
		if(fields == null)
		{
			fields = new TreeMap<String, ObjectWrapper>();
			java.util.Set<?> setObject = (java.util.Set<?>)varValue;
			
			for(Object setElement : setObject)
			{
				uid++;
				fields.put(uid+"", createObjectWrapper(uid+"", setElement, this, null));
			}
		}
		return fields;
	}

	@Override
	public String toString() {
		java.util.Set<?> setObject = (java.util.Set<?>)varValue;
		return "java.util.Set " + varName + " <size = " + setObject.size() + ">";
	}

	@Override
	public String toCommandView(int tabCount) {
		String tabString = Toolkit.getTabString(tabCount);
		StringBuffer viewBuffer = new StringBuffer();
		viewBuffer.append(tabString + toString() + "\n");
		viewBuffer.append(tabString + "{\n");
		Map<String, ObjectWrapper> tmpFields = getFields();
		for(String key : tmpFields.keySet()) {
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
		Set setObj = (Set)varValue;
		setObj.remove(child.varValue);
		setObj.add(objValue);
	}
}
