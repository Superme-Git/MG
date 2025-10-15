package com.pwrd.command;

import com.pwrd.io.IO;

public class InvalidCommand implements Command
{
	public static final String NAME = "invalid";
	
	@Override
	public void execute(String cmdStr)
	{
		IO.getInstance().println("Invalid command");
	}

}
