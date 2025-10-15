package com.pwrd.jmx.command;

import com.pwrd.command.Command;
import com.pwrd.io.IO;
import com.pwrd.util.Toolkit;

public abstract class JmxCommand implements Command
{
	public void execute(String commandStr)
	{
		String[] cmdArray = Toolkit.splitStringBySpace(commandStr);
		if(cmdArray.length == 0)
		{
			IO.getInstance().println("It's incredible! How do you do this?");
			return;
		}
		String[] cmdArgs = new String[cmdArray.length > 0 ? cmdArray.length - 1 : 0];
		for(int i = 0; i < cmdArray.length - 1; ++ i)
			cmdArgs[i] = cmdArray[i + 1];
		execute(cmdArgs);
	}
	
	public abstract void execute(String[] args);
}
