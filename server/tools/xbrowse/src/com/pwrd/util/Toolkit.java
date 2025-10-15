package com.pwrd.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Toolkit
{
	private static Log logger = LogFactory.getLog(Toolkit.class);
	
	public static String getTabString(int tabCount)
	{
		String result = "";
		for(int i = 0; i < tabCount; ++ i)
			result += '\t';
		return result;
	}
	
	public static String[] splitStringBySpace(String str)
	{
		return str.split("[\t\n\f\r ]+");
	}
	
	/**
	 * 将给定的字符串<code>str</code>转换成
	 * <code>ref</code>的类型。
	 * 
	 * @param str - 待转换字符串
	 * @param ref - 参考对象
	 * @return 字符串转换后的对象，如果不能够转换
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convert(String str, Object ref)
	{
		assert str != null && ref != null;
		// logger.info(str + " " + ref.getClass().getName());
		if(ref.getClass().isInstance(str)) // 如果参考对象也是String类型
			return str;
		if(ref.getClass().isEnum())
			return Enum.valueOf((Class)ref.getClass(), str);
		else if(ref.getClass() == Boolean.TYPE ||
				ref.getClass() == Byte.TYPE ||
				ref.getClass() == Character.TYPE ||
				ref.getClass() == Short.TYPE ||
				ref.getClass() == Integer.TYPE || 
				ref.getClass() == Long.TYPE ||
				ref.getClass() == Float.TYPE ||
				ref.getClass() == Double.TYPE ||
				ref instanceof Boolean ||
				ref instanceof Byte ||
				ref instanceof Character ||
				ref instanceof Short ||
				ref instanceof Integer ||
				ref instanceof Long ||
				ref instanceof Float ||
				ref instanceof Double
				)
		{
			if(Boolean.TYPE == ref.getClass() || ref instanceof Boolean)
				return Boolean.valueOf(str);
			else if(Character.TYPE == ref.getClass() || ref instanceof Character)
			{
				if(str.length() == 1)
					return Character.valueOf(str.charAt(0));
				else
					throw new IllegalArgumentException(str + " is an invalid character.");
			}
			else if(Byte.TYPE == ref.getClass() || ref instanceof Byte)
				return Byte.valueOf(str);
			else if(Short.TYPE == ref.getClass() || ref instanceof Short)
				return Short.valueOf(str);
			else if(Integer.TYPE == ref.getClass() || ref instanceof Integer)
				return Integer.valueOf(str);
			else if(Long.TYPE == ref.getClass() || ref instanceof Long)
				return Long.valueOf(str);
			else if(Float.TYPE == ref.getClass() || ref instanceof Float)
				return Float.valueOf(str);
			else if(Double.TYPE == ref.getClass() || ref instanceof Double)
				return Double.valueOf(str);
			else
			{
				logger.error("Unsupported primitive type: " + ref.getClass().getName());
				throw new IllegalArgumentException("Unsupported primitive type: " + ref.getClass().getName());
			}
		}
		else
		{
			logger.error("Unsupported type: " + ref.getClass().getName());
			throw new IllegalArgumentException("Unsupported type: " + ref.getClass().getName());
		}
	}
}
