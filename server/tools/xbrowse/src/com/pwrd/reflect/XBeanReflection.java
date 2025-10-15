package com.pwrd.reflect;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Deprecated
public class XBeanReflection 
{
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(XBeanReflection.class);
	
	/**
	 *获取对象中给定成员的值。首先会尝试采用get方法
	 *获取该成员的值，如果不存在get方法，则采用直接
	 *访问成员的方式获取，如果这两种方法均没有获取到
	 *值，则返回null。
	 * 
	 * @param obj - 对象
	 * @param fieldName - 成员名
	 * @return - 成员的值如果成功，否则返回null
	 */
	public static Object getFieldValue(Object obj, String fieldName)
	{
		try 
		{
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} 
		catch (SecurityException e) 
		{
			e.printStackTrace();
		} 
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
