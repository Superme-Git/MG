package com.pwrd.jmx.command;

import java.io.IOException;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;

import com.pwrd.io.IO;
import com.pwrd.jmx.JmxConnectorManager;

public class JmxInvokeCommand extends JmxCommand
{

	@Override
	public void execute(String[] args)
	{
		assert args.length >= 2;
		/* args[0] is the object name of MBean;
		 * args[1] is the index of operation to be invoked;
		 * args[2] is a string with all parameters concatenated if exist,
		 * the parameters is separated by comma.
		*/
		JMXConnector connector = JmxConnectorManager.getInstance().getCurrentConnector();
		if(connector == null)
		{
			IO.getInstance().println("The current connector is not set, please use \'use\' command at first");
			return;
		}
		try
		{
			MBeanServerConnection conn = connector.getMBeanServerConnection();
			MBeanInfo mbeanInfo = conn.getMBeanInfo(new ObjectName(args[0]));
			int operationIndex = Integer.parseInt(args[1]);
			MBeanOperationInfo[] operationInfos = mbeanInfo.getOperations();
			if(operationIndex >= operationInfos.length)
			{
				IO.getInstance().println("There isn't a operation at index " + operationIndex);
				return;
			}
			MBeanOperationInfo operationInfo = operationInfos[operationIndex];
			MBeanParameterInfo[] parameterInfos = operationInfo.getSignature();
			int paramsLen = parameterInfos.length;
			Object[] params = new Object[paramsLen];
			String[] signature = new String[paramsLen];
			if(paramsLen > 0)
			{
				if(args.length == 2)
				{
					IO.getInstance().println("lack of the arguments.");
					return;
				}
				String[] strArray = args[2].split(",");
				if(strArray.length != paramsLen)
				{
					IO.getInstance().println("Incorrect argument number.");
					return;
				}
				
				for(int i = 0; i < paramsLen; ++ i)
				{
					params[i] = getTypeValue(parameterInfos[i].getType(), strArray[i]);
					signature[i] = parameterInfos[i].getType();
				}
			}
			Object res = conn.invoke(new ObjectName(args[0]), operationInfo.getName(), params, signature);
			IO.getInstance().println(res);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		catch (InstanceNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IntrospectionException e)
		{
			e.printStackTrace();
		} 
		catch (MalformedObjectNameException e)
		{
			e.printStackTrace();
		}
		catch (ReflectionException e)
		{
			e.printStackTrace();
		} 
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (MBeanException e)
		{
			e.printStackTrace();
		}
	}

	private Object getTypeValue(String typeName, String value)
	{
//		if("boolean".equals(typeName))
//			return Boolean.parseBoolean(value);
//		else if("byte".equals(typeName))
//			return Byte.parseByte(value);
//		else if("short".equals(typeName))
//			return Short.parseShort(value);
//		else if("int".equals(typeName))
//			return Integer.parseInt(value);
//		else if("long".equals(typeName))
//			return Long.parseLong(value);
//		else if("float".equals(typeName))
//			return Float.parseFloat(value);
//		else if("double".equals(typeName))
//			return Double.parseDouble(value);
//		else
//			return typeName;
		
		/**
		 * 上面注释部分写的有错误，修改如下
		 * by 黄兆令20150906
		 */
		if("java.lang.Boolean".equals(typeName))
			return Boolean.parseBoolean(value);
		else if("java.lang.Byte".equals(typeName))
			return Byte.parseByte(value);
		else if("java.lang.Short".equals(typeName))
			return Short.parseShort(value);
		else if("java.lang.Integer".equals(typeName))
			return Integer.parseInt(value);
		else if("java.lang.Long".equals(typeName))
			return Long.parseLong(value);
		else if("java.lang.Float".equals(typeName))
			return Float.parseFloat(value);
		else if("java.lang.Double".equals(typeName))
			return Double.parseDouble(value);
		else
			return value;
		
		
		
		
		
	}
}
