package com.pwrd.jmx.command;

import java.io.IOException;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
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

public class JmxShowCommand extends JmxCommand
{

	@Override
	public void execute(String[] args)
	{
		assert args.length == 1;
		if("connectors".equals(args[0]))
		{
			Set<String> connectors = JmxConnectorManager.getInstance().availableJMXConnector();
			IO.getInstance().println("There are " + connectors.size() + " connectors in set.");
			String[][] array = new String[connectors.size() + 1][];
			array[0] = new String[2];
			array[0][0] = "Index";
			array[0][1] = "Connector name";
			int index = 0;
			for(String connector : connectors)
			{
				array[index + 1] = new String[2];
				array[index + 1][0] = index + "";
				array[index + 1][1] = connector;
				++ index;
			}
			IO.getInstance().println(twoDimensionArrayString(array));
		}
		else if("mbeans".equals(args[0]))
		{
			JMXConnector connector  = JmxConnectorManager.getInstance().getCurrentConnector();
			if(connector != null)
			{
				try
				{
					MBeanServerConnection conn = connector.getMBeanServerConnection();
					Set<ObjectName> mbeans = conn.queryNames(null, null);
					IO.getInstance().println("There are " + mbeans.size() + " connectors in set.");
					String[][] array = new String[mbeans.size() + 1][];
					array[0] = new String[2];
					array[0][0] = "Index";
					array[0][1] = "MBean name";
					int index = 0;
					for(ObjectName mbean : mbeans)
					{
						array[index + 1] = new String[2];
						array[index + 1][0] = index + "";
						array[index + 1][1] = mbean.toString();
						++ index;
					}
					IO.getInstance().println(twoDimensionArrayString(array));
				}
				catch (IOException e)
				{
					e.printStackTrace();
					IO.getInstance().println("The connector has been closed.");
				}
				
			}
			else
				IO.getInstance().println("Please set current JmxConnector using command use.");
		}
		else // 显示某个mbean的详细信息
		{
			JMXConnector connector = JmxConnectorManager.getInstance().getCurrentConnector();
			if(connector != null)
			{
				try
				{
					MBeanServerConnection conn = connector.getMBeanServerConnection();
					MBeanInfo mbeanInfo = conn.getMBeanInfo(new ObjectName(args[0]));
					// mbeanInfo.
					IO.getInstance().println(mbeanInfoString(mbeanInfo));
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
			}
			else
				IO.getInstance().println("Please use a JmxConnector using command use.");
		}
	}

	private String mbeanInfoString(MBeanInfo mbeanInfo)
	{
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("类名称：" + mbeanInfo.getClassName() + "\n");
		strBuilder.append("描述：" + mbeanInfo.getDescription() + "\n");
		MBeanAttributeInfo[] attrs = mbeanInfo.getAttributes();
		strBuilder.append("mbean has " + attrs.length + " attributes.\n");
		String[][] attrArray = new String[attrs.length + 1][];
		attrArray[0] = new String[5];
		attrArray[0][0] = "Index";
		attrArray[0][1] = "Attribute name";
		attrArray[0][2] = "Attribute type";
		attrArray[0][3] = "Is readable";
		attrArray[0][4] = "Is writable";
		int index = 0;
		for(MBeanAttributeInfo attr : attrs)
		{
			attrArray[index + 1] = new String[5];
			attrArray[index + 1][0] = index + "";
			attrArray[index + 1][1] = attr.getName();
			attrArray[index + 1][2] = attr.getType();
			attrArray[index + 1][3] = attr.isReadable() + "";
			attrArray[index + 1][4] = attr.isWritable() + "";
			++ index;
		}
		strBuilder.append(twoDimensionArrayString(attrArray) + "\n");
		MBeanOperationInfo[] operations = mbeanInfo.getOperations();
		strBuilder.append("mbean has " + operations.length + " operations.\n");
		String[][] opArray = new String[operations.length + 1][];
		opArray[0] = new String[4];
		opArray[0][0] = "Index";
		opArray[0][1] = "Return type";
		opArray[0][2] = "Operation name";
		opArray[0][3] = "Operation signature";
		index = 0;
		for(MBeanOperationInfo operation : operations)
		{
			opArray[index + 1] = new String[4];
			opArray[index + 1][0] = index + "";
			opArray[index + 1][1] = operation.getReturnType();
			opArray[index + 1][2] = operation.getName();
			String signature = "(";
			MBeanParameterInfo[] params = operation.getSignature();
			if(params.length > 0)
			{
				for(int i = 0; i < params.length - 1; ++ i)
					signature += params[i].getType() + " " + params[i].getName() + ", ";
				signature += params[params.length - 1].getType() + " " + params[params.length - 1].getName();
			}
			signature += ")";
			opArray[index + 1][3] = signature;
			++ index;
		}
		strBuilder.append(twoDimensionArrayString(opArray));
		return strBuilder.toString();
	}
	
	private String twoDimensionArrayString(String[][] array)
	{
		StringBuilder strBuilder = new StringBuilder();
		if(array.length > 0)
		{
			int rowCount = array.length;
			int columnCount = array[0].length;
			if(columnCount > 0)
			{
				int[] columnWidths = new int[columnCount];
				for(int i = 0; i < columnCount; ++ i)
				{
					for(int j = 0; j < rowCount; ++ j)
					{
						if(columnWidths[i] < array[j][i].length())
							columnWidths[i] = array[j][i].length();
					}
				}
				String sepLine = "+";
				for(int columnWidth : columnWidths)
				{
					for(int i = 0; i < columnWidth + 2; ++ i)
						sepLine += '-';
					sepLine += '+';
				}
				strBuilder.append(sepLine + "\n");
				for(int i = 0; i < rowCount; ++ i)
				{
					strBuilder.append("| ");
					for(int j = 0; j < columnCount - 1; ++ j)
						strBuilder.append(paddingString(array[i][j], columnWidths[j], ' ') + " | ");
					strBuilder.append(paddingString(array[i][columnCount - 1], columnWidths[columnCount - 1], ' ') + " |\n");
					if(i == 0 || i == rowCount - 1)
						strBuilder.append(sepLine + '\n');
				}
			}
		}
		return strBuilder.toString();
	}
	
	private String paddingString(String str, int width, char paddingChar)
	{
		int len = str.length();
		for(int i = 0; i < width - len; ++ i)
			str += paddingChar;
		return str;
	}
}
