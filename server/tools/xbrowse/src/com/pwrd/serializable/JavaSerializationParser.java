package com.pwrd.serializable;

import java.io.IOException;

import com.pwrd.command.Command;
import com.pwrd.command.CommandManager;
import com.pwrd.command.HelpCommand;
import com.pwrd.io.IO;
import com.pwrd.serializable.command.LoadCommand;
import com.pwrd.serializable.command.LoadlibCommand;
import com.pwrd.serializable.command.OperateCommand;
import com.pwrd.serializable.command.SaveCommand;
import com.pwrd.serializable.command.ShowCommand;
import com.pwrd.serializable.command.UseCommand;


public class JavaSerializationParser 
{
	private static CommandManager serCommandManager;
	
	static
	{
		serCommandManager = new CommandManager();
		serCommandManager.register(HelpCommand.NAME, new HelpCommand("resources/serializable.help.txt"));
		serCommandManager.register(LoadCommand.NAME, new LoadCommand());
		serCommandManager.register(OperateCommand.NAME, new OperateCommand());
		serCommandManager.register(ShowCommand.NAME, new ShowCommand());
		serCommandManager.register(UseCommand.NAME, new UseCommand());
		serCommandManager.register(SaveCommand.NAME, new SaveCommand());
		serCommandManager.register(LoadlibCommand.NAME, new LoadlibCommand());
	}
	
	public static void main(String[] args)
	{
		IO.getInstance().println("Welcome to the Java Serialization Parser.");
		IO.getInstance().println();
		IO.getInstance().println("Type help for help.");
		String command = null;
		try
		{
			IO.getInstance().print("serialization>");
			IO.getInstance().flush();
			while((command = IO.getInstance().readLine()) != null)
			{
				String[] cmdArray = command.split("[\t\n\f\r ]+");
				if(cmdArray.length == 0)
				{
					IO.getInstance().println("It's incredible! How do you do this?");
					continue;
				}
				Command cmd = serCommandManager.get(cmdArray[0]);
				cmd.execute(command);
				IO.getInstance().print("serialization>");
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
