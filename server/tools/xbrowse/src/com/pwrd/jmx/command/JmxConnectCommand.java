package com.pwrd.jmx.command;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.pwrd.io.IO;
import com.pwrd.jmx.JmxConnectorManager;

public class JmxConnectCommand extends JmxCommand
{

	@Override
	public void execute(String[] args)
	{
		
		assert args.length == 2 || args.length == 4;
		StringWriter sw = new StringWriter();
		Formatter urlFormatter = new Formatter(sw);
		urlFormatter.format("service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi", args[0], args[1]);
		try
		{
			JMXServiceURL serviceURL = new JMXServiceURL(sw.toString());
			JMXConnector jmxConnector = null;
			if(args.length == 2)
				jmxConnector = JMXConnectorFactory.connect(serviceURL);
			else if(args.length == 4)
			{
				String[] credentials = new String[]{args[2], args[3]};
				Map<String, String[]> props = new HashMap<String, String[]>();
				props.put("jmx.remote.credentials", credentials);
				jmxConnector = JMXConnectorFactory.connect(serviceURL, props);
			}
			else
			{
				IO.getInstance().println("Incorrent arguments quantity.");
				return;
			}
			String jmxConnName = args[0] + ":" + args[1];
			JmxConnectorManager.getInstance().addJMXConnector(jmxConnName, jmxConnector);
			IO.getInstance().println(jmxConnector.getConnectionId());
			IO.getInstance().println("Add a JMXConnector named: \'" + jmxConnName + "\', please use this name to refer the JMXConnector");
		} 
		catch (MalformedURLException e)
		{
			// e.printStackTrace();
			IO.getInstance().println(e.getMessage());
		} catch (IOException e)
		{
			e.printStackTrace();
			IO.getInstance().println("Failed to connect " + sw.toString() + ", please check the URL.");
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
	}

}
