package com.pwrd.reflect.model;

import java.util.TreeMap;
import java.util.Map;

import com.pwrd.util.Toolkit;

public class PrimitiveWrapper extends ObjectWrapper {

	PrimitiveWrapper(String name, Object object, ObjectWrapper parent, Object key) {
		super(name, object, parent, key);
	}

	@Override
	public Map<String, ObjectWrapper> getFields() 
	{
		if(fields == null)
			fields = new TreeMap<String, ObjectWrapper>(); 
		return fields;
	}

	@Override
	public String toString() {
		if(varValue == null) 
			return varName + " = null"; 
		return varValue.getClass().getName() + " " + varName + " = " + varValue.toString();
	}

	@Override
	public String toCommandView(int tabCount)
	{
		if(varValue == null) 
			return varName + " = null"; 
		return Toolkit.getTabString(tabCount) + varValue.getClass().getName() + " " + varName + " = " + varValue.toString();
	}

	@Override
	public void setChildValue(ObjectWrapper child, Object objValue)
	{
		throw new UnsupportedOperationException("Enums don't have children.");
	}

}
