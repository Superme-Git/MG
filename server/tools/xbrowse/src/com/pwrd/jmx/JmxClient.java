package com.pwrd.jmx;

import java.io.IOException;

import com.pwrd.command.Command;
import com.pwrd.command.CommandManager;
import com.pwrd.command.HelpCommand;
import com.pwrd.io.IO;
import com.pwrd.util.Toolkit;

public class JmxClient
{
	private static CommandManager jmxCommandManager;
	
	static
	{
		jmxCommandManager = new CommandManager();
		jmxCommandManager.register(HelpCommand.NAME, new HelpCommand("resources/jmx.help.txt"));
		jmxCommandManager.register("resources/jmx.commands.xml");
	}
	
	public static void main(String[] args)
	{
		IO.getInstance().println("Welcome to the JMX client.");
		IO.getInstance().println();
		IO.getInstance().println("Type help for help.");
		String commandStr = null;
		try
		{
			IO.getInstance().print("jmx>");
			IO.getInstance().flush();
			while((commandStr = IO.getInstance().readLine()) != null)
			{
				String[] cmdArray = Toolkit.splitStringBySpace(commandStr);
				if(cmdArray.length == 0)
				{
					IO.getInstance().println("It's incredible! How do you do this?");
					continue;
				}
				Command command = jmxCommandManager.get(cmdArray[0]);
				command.execute(commandStr);
				IO.getInstance().print("jmx>");
				IO.getInstance().flush();
			}
			IO.getInstance().flush();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
