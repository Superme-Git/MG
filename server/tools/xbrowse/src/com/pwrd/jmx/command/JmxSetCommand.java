package com.pwrd.jmx.command;

import java.io.IOException;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;

import com.pwrd.io.IO;
import com.pwrd.jmx.JmxConnectorManager;

public class JmxSetCommand extends JmxCommand
{

	@Override
	public void execute(String[] args)
	{
		if(args.length >= 3)
		{
			JMXConnector connector = JmxConnectorManager.getInstance().getCurrentConnector();
			if(connector == null)
			{
				IO.getInstance().println("Please set current JMXConnector using command use");
				return;
			}
			try
			{
				MBeanServerConnection conn = connector.getMBeanServerConnection();
				String attrname = conn.getMBeanInfo(new ObjectName(args[0])).getAttributes()[Integer.parseInt(args[1])].getName();
				Object res = conn.getAttribute(new ObjectName(args[0]), attrname);
				Object value = null;
				if("java.lang.Boolean".equals(res.getClass().getName()))
					value = new Boolean(args[2]);
				else if("java.lang.Byte".equals(res.getClass().getName()))
					value = new Byte(args[2]);
				else if("java.lang.Short".equals(res.getClass().getName()))
					value = new Short(args[2]);
				else if("java.lang.Integer".equals(res.getClass().getName()))
					value = new Integer(args[2]);
				else if("java.lang.Long".equals(res.getClass().getName()))
					value = new Long(args[2]);
				else if("java.lang.float".equals(res.getClass().getName()))
					value = new Float(args[2]);
				else if("java.lang.Double".equals(res.getClass().getName()))
					value = new Double(args[2]);
				else
					value = args[2];
				conn.setAttribute(new ObjectName(args[0]), new Attribute(attrname, value));
				IO.getInstance().println("Set " + attrname + " to " + value);
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			} 
			catch (AttributeNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (InstanceNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (MalformedObjectNameException e)
			{
				e.printStackTrace();
			}
			catch (MBeanException e)
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
			catch (InvalidAttributeValueException e)
			{
				e.printStackTrace();
			}
			catch (IntrospectionException e)
			{
				e.printStackTrace();
			} 
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		else
			IO.getInstance().println("Insufficient arguments");
	}

}
