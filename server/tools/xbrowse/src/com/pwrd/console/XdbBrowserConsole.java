package com.pwrd.console;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.pwrd.command.Command;
import com.pwrd.command.CommandManager;
import com.pwrd.command.HelpCommand;
import com.pwrd.io.IO;

public class XdbBrowserConsole
{
	private static XdbBrowserConsole console;
	private static CommandManager consoleCommandManager;
	
	static
	{
		console = new XdbBrowserConsole();
		consoleCommandManager = new CommandManager();
		consoleCommandManager.register(HelpCommand.NAME, new HelpCommand("resources/console.help.txt"));
		consoleCommandManager.register("resources/console.commands.xml");
	}
	
	public static XdbBrowserConsole getInstance()
	{
		return console;
	}
	
	private XdbBrowserConsole()
	{	
	}
	
	public void execute()
	{
		IO.getInstance().println("Type 'help' for help");
		IO.getInstance().print("xbrowse> ");
		IO.getInstance().flush();
		String commandStr = null;
		// 如果读取命令时出现错误，那么继续循环
		COMMAND_INPUT_LOOP:
		try
		{
			while((commandStr = IO.getInstance().readLine()) != null)
			{
				try
				{
					execute(commandStr);
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
				IO.getInstance().print("xbrowse> ");
				IO.getInstance().flush();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			break COMMAND_INPUT_LOOP;
		}
		IO.getInstance().flush();
	}
	
	public void execute(String commandStr) throws IOException
	{
		String[] cmdArray = commandStr.split("[\t\n\f\r ><]+");
		if(cmdArray.length > 0)
		{
			Command command = consoleCommandManager.get(cmdArray[0]);
			if(command != null)
			{
				int redirectOutputTokenIndex = commandStr.indexOf(">");
				if(redirectOutputTokenIndex != -1)
				{
					int index = redirectOutputTokenIndex + 1;
					// 跳过重定向符后面的空格
					for( ; index < commandStr.length(); )
					{
						if(Character.isWhitespace(commandStr.charAt(index)))
							++ index;
						else
							break;
					}
					int endIndex = index + 1;
					for( ; endIndex < commandStr.length(); )
					{
						if(!Character.isWhitespace(commandStr.charAt(endIndex)) && 
								'<' != commandStr.charAt(endIndex))
							++ endIndex;
						else
							break;
					}
					// 此时index指向输入重定向符后面的非空格字符
					// endIndex指向自index开始遇到的第一个空格字符或<
					String redirectOutputFileName = commandStr.substring(index, endIndex);
					IO.getInstance().println(redirectOutputFileName);
					IO.getInstance().setOut(new FileWriter(redirectOutputFileName));
					commandStr = commandStr.replace(commandStr.substring(redirectOutputTokenIndex, endIndex), "");
					IO.getInstance().println(commandStr);
				}
				int redirectInputTokenIndex = commandStr.indexOf("<");
				if(redirectInputTokenIndex != -1)
				{
					int index = redirectInputTokenIndex + 1;
					// 跳过重定向符后面的空格
					for( ; index < commandStr.length(); )
					{
						if(Character.isWhitespace(commandStr.charAt(index)))
							++ index;
						else
							break;
					}
					int endIndex = index + 1;
					for( ; endIndex < commandStr.length(); )
					{
						if(!Character.isWhitespace(commandStr.charAt(endIndex)) && 
								'>' != commandStr.charAt(endIndex))
							++ endIndex;
						else
							break;
					}
					// 此时index指向输入重定向符后面的非空格字符
					// endIndex指向自index开始遇到的第一个空格字符或<
					String redirectInputFileName = commandStr.substring(index, endIndex);
					IO.getInstance().println(redirectInputFileName);
					IO.getInstance().setIn(new FileReader(redirectInputFileName));
					commandStr = commandStr.replace(commandStr.substring(redirectInputTokenIndex, endIndex), "");
					IO.getInstance().println(commandStr);
				}
				command.execute(commandStr);
				// 如果进行了重定向，那么命令结束后记得关闭重定向流，同时切回标准输入输出
				if(redirectInputTokenIndex != -1)
				{
					IO.getInstance().closeIn();
					IO.getInstance().setIn(System.in);
				}
				if(redirectOutputTokenIndex != -1)
				{
					IO.getInstance().closeOut();
					IO.getInstance().setOut(System.out);
				}
			}
			else
				IO.getInstance().println("命令 " + cmdArray[0] + " 不存在");
		}
	}
	
	public static void main(String[] args)
	{
		XdbBrowserConsole.getInstance().execute();
	}
}
