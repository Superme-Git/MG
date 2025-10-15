package com.pwrd.jmx.command;

import com.pwrd.io.IO;
import com.pwrd.jmx.JmxConnectorManager;

public class JmxUseCommand extends JmxCommand
{

	@Override
	public void execute(String[] args)
	{
		assert args.length == 1;
		if(JmxConnectorManager.getInstance().setCurrentConnector(args[0]))
			IO.getInstance().println("Set the current connector to " + args[0]);
		else	
			IO.getInstance().println("The connector named " + args[0] + " doesn't exist.");
	}

}
