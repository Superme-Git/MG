package com.pwrd.command;

import com.pwrd.io.IO;

public class ExitCommand implements Command
{
	public static final String NAME = "exit";

	@Override
	public void execute(String cmdStr)
	{
		IO.getInstance().println("Bye");
		System.exit(0);
	}

}
