package com.pwrd.command;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.pwrd.io.IO;

public class HelpCommand implements Command
{
	public static final String NAME = "help";
	
	private String helpContent;
	
	public HelpCommand()
	{
		helpContent = "Please provide help info.";
	}
	
	public HelpCommand(String helpFile)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(helpFile));
			StringBuilder strBuilder = new StringBuilder();
			int readCount = 0;
			char[] buff = new char[64];
			while((readCount = br.read(buff)) != -1)
				strBuilder.append(buff, 0, readCount);
			helpContent = strBuilder.toString();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(String cmdStr)
	{
		IO.getInstance().println(helpContent);
	}

}
