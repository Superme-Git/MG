package com.pwrd.jmx;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.remote.JMXConnector;

public class JmxConnectorManager
{
	private static JmxConnectorManager instance = new JmxConnectorManager();

	private Map<String, JMXConnector> conns = new HashMap<String, JMXConnector>();
	private String curConn = null;
	
	public static JmxConnectorManager getInstance()
	{
		return instance;
	}

	private JmxConnectorManager()
	{
		Runtime.getRuntime().addShutdownHook(
				new Thread("jmxconnectormanager.ShutdownHook")
				{
					@Override
					public void run()
					{
						for (JMXConnector conn : conns.values())
						{
							if (conn != null)
							{
								try
								{
									conn.close();
								} 
								catch (IOException e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				});
	}

	/**
	 * @see {@link java.util.Map#put(Object, Object)}
	 */
	public JMXConnector addJMXConnector(String name, JMXConnector conn)
	{
		return conns.put(name, conn);
	}

	/**
	 * @see {@link java.util.Map#remove(Object)}
	 */
	public JMXConnector removeJMXConnector(String name)
	{
		return conns.remove(name);
	}
	
	public Set<String> availableJMXConnector()
	{
		return Collections.unmodifiableSet(conns.keySet());
	}
	
	public JMXConnector getCurrentConnector()
	{
		return conns.get(curConn);
	}
	
	public boolean setCurrentConnector(String conn)
	{
		if(conns.containsKey(conn))
		{
			curConn = conn;
			return true;
		}
		else
			return false;
		
		
	}
}
