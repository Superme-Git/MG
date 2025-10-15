package com.pwrd.definition;

import java.util.HashSet;
import java.util.Set;

@Deprecated
public abstract class XdbDataType 
{
	private static final String XML_TAG_NAME = "type";
	
	private static Set<String> availablePrimitiveType;
	private static Set<String> availableCollectionType;
	private static Set<String> availableMapType;
	
	static
	{
		availablePrimitiveType = new HashSet<String>();
		availableCollectionType = new HashSet<String>();
		availableMapType = new HashSet<String>();
		availablePrimitiveType.add("short");
		availablePrimitiveType.add("int");
		availablePrimitiveType.add("long");
		availablePrimitiveType.add("float");
		availablePrimitiveType.add("boolean");
		availablePrimitiveType.add("String");
		availableCollectionType.add("Set");
		availableCollectionType.add("HashSet");
		availableCollectionType.add("List");
		availableCollectionType.add("ArrayList");
		availableCollectionType.add("LinkedList");
		availableMapType.add("Map");
		availableMapType.add("HashMap");
		availableMapType.add("NavigableMap");
		availableMapType.add("TreeMap");
	}
	
	public static boolean isValidXdbType(String typeName)
	{
		return isValidXdbPrimitiveType(typeName) || isValidXdbCollectionType(typeName) || isValidXBeanType(typeName)
		|| isValidXdbMapType(typeName);
	}
	
	public static boolean isValidXdbPrimitiveType(String typeName)
	{
		return availablePrimitiveType.contains(typeName);
	}
	
	public static boolean isValidXdbCollectionType(String typeName)
	{
		return availableCollectionType.contains(typeName);
	}
	
	public static boolean isValidXdbMapType(String typeName)
	{
		return availableMapType.contains(typeName);
	}
	
	public static boolean isValidXBeanType(String typeName)
	{
		return XdbDefinition.getInstance().containsXBean(typeName);
	}
	
	protected String _type;
	
	public XdbDataType()
	{
		_type = null;
	}
	
	public XdbDataType(org.w3c.dom.Element elem)
	{
		fromXml(elem);
	}
	
	public String getType()
	{
		return _type;
	}
	
	public String toString()
	{
		return getType();
	}
	
	public void fromXml(org.w3c.dom.Element varElem)
	{
		assert varElem != null;
		if(varElem.hasAttribute(XML_TAG_NAME))
			_type = varElem.getAttribute(XML_TAG_NAME);
	}
	
	public static XdbDataType getXdbDataType(org.w3c.dom.Element varElem)
	{
		assert varElem != null;
		if(varElem.hasAttribute(XML_TAG_NAME))
		{
			String type = varElem.getAttribute(XML_TAG_NAME);
			if(XdbSimpleDataType.containsType(type))
				return new XdbSimpleDataType(varElem);
			else if(XdbMapDataType.containsType(type))
				return new XdbMapDataType(varElem);
			else if(XdbSequenceDataType.containsType(type))
				return new XdbSequenceDataType(varElem);
			else if(XdbBinaryDataType.containsType(type))
				return new XdbBinaryDataType(varElem);
			else
				return new XdbXBeanDataType(varElem);
		}
		
		return null;
	}
	
	/**
	 * short, int, long, float, boolean, string
	 * @author wp853
	 *
	 */
	public static class XdbSimpleDataType extends XdbDataType
	{
		private static Set<String> simpleDataTypes = new HashSet<String>();
		
		static
		{
			simpleDataTypes.add("short");
			simpleDataTypes.add("int");
			simpleDataTypes.add("long");
			simpleDataTypes.add("float");
			simpleDataTypes.add("boolean");
			simpleDataTypes.add("string");
		}
		
		private String _defaultValue;
		
		public XdbSimpleDataType()
		{
			_defaultValue = null;
		}
		
		public XdbSimpleDataType(org.w3c.dom.Element elem)
		{
			this();
			fromXml(elem);
		}
		
		public String getDefaultValue()
		{
			return _defaultValue;
		}
		
		public void fromXml(org.w3c.dom.Element varElem)
		{
			super.fromXml(varElem);
		}
		
		public String toString()
		{
			return super.toString();
		}
		
		public static boolean containsType(String type)
		{
			return simpleDataTypes.contains(type);
		}
		
		/**
		 *得到给定类型的宽类型，目前宽类型只适于整型。由于long
		 *能够表达所有的整型，因此整型的宽类型为long。
		 *<p>
		 *增加这个方法的原因是因为需要将Xdb定义的类型与
		 *语法解析器定义的类型对应起来。
		 * 
		 * @param type - 原始类型
		 * @return 给定类型的宽类型
		 */
		public static String getWideType(String type)
		{
			if("short".equals(type) || "int".equals(type) || "long".equals(type))
				return "long";
			return type;
		}
	}
	
	/**
	 * map or treemap
	 * @author wp853
	 *
	 */
	public static class XdbMapDataType extends XdbDataType
	{
		private static final String KEYTYPE_XML_TAG_NAME = "key";
		private static final String VALUETYPE_XML_TAG_NAME = "value";
		
		private static Set<String> mapDataTypes = new HashSet<String>();
		
		static
		{
			mapDataTypes.add("map");
			mapDataTypes.add("treemap");
		}
		
		private String _keyType;
		private String _valueType;
		
		public XdbMapDataType()
		{
			_keyType = "";
			_valueType = "";
		}
		
		public XdbMapDataType(org.w3c.dom.Element elem)
		{
			this();
			fromXml(elem);
		}
		
		public String getKeyType()
		{
			return _keyType; 
		}
		
		public String getValueType()
		{
			return _valueType;
		}
		
		public void fromXml(org.w3c.dom.Element elem)
		{
			super.fromXml(elem);
			if(elem.hasAttribute(KEYTYPE_XML_TAG_NAME))
				_keyType = elem.getAttribute(KEYTYPE_XML_TAG_NAME);
			if(elem.hasAttribute(VALUETYPE_XML_TAG_NAME))
				_valueType = elem.getAttribute(VALUETYPE_XML_TAG_NAME);
		}
		
		public String toString()
		{
			return getType() + "<" + getKeyType() + ", " + getValueType() + ">";
		}
		
		public static boolean containsType(String type)
		{
			return mapDataTypes.contains(type);
		}
	}
	
	/**
	 * set, list or vector
	 * @author wp853
	 *
	 */
	public static class XdbSequenceDataType extends XdbDataType
	{
		private static final String VALUETYPE_XML_TAG_NAME = "value";
		
		private static Set<String> seqDataTypes = new HashSet<String>();
		
		static
		{
			seqDataTypes.add("set");
			seqDataTypes.add("list");
			seqDataTypes.add("vector");
		}
		
		private String _valueType;
		
		XdbSequenceDataType()
		{
			_valueType = null;
		}
		
		XdbSequenceDataType(org.w3c.dom.Element elem)
		{
			this();
			fromXml(elem);
		}
		
		public String getValueType()
		{
			return _valueType;
		}
		
		public void fromXml(org.w3c.dom.Element elem)
		{
			super.fromXml(elem);
			if(elem.hasAttribute(VALUETYPE_XML_TAG_NAME))
				_valueType = elem.getAttribute(VALUETYPE_XML_TAG_NAME);
		}
		
		public String toString()
		{
			return getType() + "<" + getValueType() + ">";
		}
		
		public static boolean containsType(String type)
		{
			return seqDataTypes.contains(type);
		}
	}
	
	public static class XdbBinaryDataType extends XdbDataType
	{
		private static final String CAPACITY_XML_TAG_NAME = "capacity";
		
		private static Set<String> binaryDataTypes = new HashSet<String>();
		
		static
		{
			binaryDataTypes.add("binary");
		}
		
		private int _capacity;
		
		XdbBinaryDataType()
		{
			_capacity = 0;
		}
		
		XdbBinaryDataType(org.w3c.dom.Element elem)
		{
			this();
			fromXml(elem);
		}
		
		public int getCapacity()
		{
			return _capacity;
		}
		
		public void fromXml(org.w3c.dom.Element elem)
		{
			super.fromXml(elem);
			if(elem.hasAttribute(CAPACITY_XML_TAG_NAME))
			{
				try
				{
					_capacity = Integer.parseInt(elem.getAttribute(CAPACITY_XML_TAG_NAME));
				}
				catch(NumberFormatException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		public String toString()
		{
			return getType() + "[" + getCapacity() + "]";
		}
		
		public static boolean containsType(String type)
		{
			return binaryDataTypes.contains(type);
		}
	}
	
	public static class XdbXBeanDataType extends XdbDataType
	{
		public XdbXBeanDataType()
		{
		}
		
		public XdbXBeanDataType(org.w3c.dom.Element elem)
		{
			super(elem);
		}
	}
}
