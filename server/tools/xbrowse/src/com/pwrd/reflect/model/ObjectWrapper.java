package com.pwrd.reflect.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public abstract class ObjectWrapper 
{
	protected String varName;
	protected Object varValue;
	/**
	 * 因为<code>Map</code>的索引可以是对象类型，
	 * 因此需要保存。考虑是是否需要移走？ 
	 * 在往回写时需要。
	 */
	protected Object key;
	protected ObjectWrapper parent;
	protected Map<String, ObjectWrapper> fields;	
	List<String> path = new LinkedList<String>();
	
	protected ObjectWrapper(String name, Object object, ObjectWrapper parent, Object key)
	{
		this.varName = name;
		this.varValue = object;
		this.key = key;
		this.parent = parent;
		if(parent != null) 
			path.addAll(parent.path);
		path.add(name);
	}
	
	/**
	 * 得到成员
	 */
	public abstract Map<String, ObjectWrapper> getFields();
	/**
	 * 实现command查询显示
	 */
	public abstract String toCommandView(int tabCount);
	/**
	 * 更新子节点
	 * 
	 * @param child - 子节点
	 * @param objValue - 子节点的新值
	 */
	public abstract void setChildValue(ObjectWrapper child, Object objValue);

	public void traverse(IVisitor visitor)
	{
		visitor.visit(this);
		Map<String, ObjectWrapper> tmpFields = getFields();
		for(ObjectWrapper w : tmpFields.values())
			w.traverse(visitor);
	}
	
	@Override
	public String toString() 
	{
		return varValue.toString();
	}
	
	public Object getValue()
	{
		return varValue;
	}
	
	public void setValue(Object v)
	{
		varValue = v;
	}
	
	public ObjectWrapper getParent()
	{
		return parent;
	}
	
	public static ObjectWrapper createObjectWrapper(String name, Object object, ObjectWrapper parent, Object key)
	{
		boolean isCyclic = false;
		ObjectWrapper tmpParent = parent;
		while(tmpParent != null)
		{
			if(tmpParent.varValue == object)
			{
				isCyclic = true;
				break;
			}
			tmpParent = tmpParent.parent;
		}
		if(isCyclic)
		{
			// System.out.println("detect cyclic");
			String cyclicPath = "";
			for(int i = 0; i < tmpParent.path.size(); ++ i)
			{
				if(i != 0)
					cyclicPath += "." + tmpParent.path.get(i);
				else
					cyclicPath += tmpParent.path.get(i);
			}
			object = "Cycle to " + cyclicPath;
		}
		
		
		if(object == null) 
			return new PrimitiveWrapper(name, null, parent, key);
		else if(object instanceof java.lang.Enum) // 如果是枚举类型
			return new EnumWrapper(name, object, parent, key);
		else if (object instanceof java.util.Map) 
			return new MapWrapper(name, object, parent, key);
		else if (object instanceof java.util.List) 
			return new ListWrapper(name, object, parent, key);
		else if (object instanceof java.util.Set) 
			return new SetWrapper(name, object, parent, key);
		else if (object instanceof String) 
			return new StringWrapper(name, object, parent, key);
		// 处理primitive type
		else if (object instanceof Boolean ||
				 object instanceof Byte ||
				 object instanceof Short ||
				 object instanceof Integer ||
				 object instanceof Long ||
				 object instanceof Float ||
				 object instanceof Double
				)
			return new PrimitiveWrapper(name, object, parent, key);
		else // user-defined type
			return new CustomWrapper(name, object, parent, key);
	}
	
	public static interface IVisitor
	{
		public void visit(ObjectWrapper w);
	}
}
