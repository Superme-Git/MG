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
	 *��ȡ�����и�����Ա��ֵ�����Ȼ᳢�Բ���get����
	 *��ȡ�ó�Ա��ֵ�����������get�����������ֱ��
	 *���ʳ�Ա�ķ�ʽ��ȡ����������ַ�����û�л�ȡ��
	 *ֵ���򷵻�null��
	 * 
	 * @param obj - ����
	 * @param fieldName - ��Ա��
	 * @return - ��Ա��ֵ����ɹ������򷵻�null
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
