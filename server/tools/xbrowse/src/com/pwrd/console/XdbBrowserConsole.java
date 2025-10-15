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
		// �����ȡ����ʱ���ִ�����ô����ѭ��
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
					// �����ض��������Ŀո�
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
					// ��ʱindexָ�������ض��������ķǿո��ַ�
					// endIndexָ����index��ʼ�����ĵ�һ���ո��ַ���<
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
					// �����ض��������Ŀո�
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
					// ��ʱindexָ�������ض��������ķǿո��ַ�
					// endIndexָ����index��ʼ�����ĵ�һ���ո��ַ���<
					String redirectInputFileName = commandStr.substring(index, endIndex);
					IO.getInstance().println(redirectInputFileName);
					IO.getInstance().setIn(new FileReader(redirectInputFileName));
					commandStr = commandStr.replace(commandStr.substring(redirectInputTokenIndex, endIndex), "");
					IO.getInstance().println(commandStr);
				}
				command.execute(commandStr);
				// ����������ض�����ô���������ǵùر��ض�������ͬʱ�лر�׼�������
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
				IO.getInstance().println("���� " + cmdArray[0] + " ������");
		}
	}
	
	public static void main(String[] args)
	{
		XdbBrowserConsole.getInstance().execute();
	}
}
