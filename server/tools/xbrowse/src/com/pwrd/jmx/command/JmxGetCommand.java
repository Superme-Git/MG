package com.pwrd.jmx.command;

import java.io.IOException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;

import com.pwrd.io.IO;
import com.pwrd.jmx.JmxConnectorManager;

public class JmxGetCommand extends JmxCommand
{

	@Override
	public void execute(String[] args)
	{
		if(args.length >= 2)
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
				IO.getInstance().println(res);
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
