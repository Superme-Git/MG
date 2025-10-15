package com.pwrd.serializable.command;

import com.pwrd.command.Command;
import com.pwrd.io.IO;
import com.pwrd.serializable.JavaSerializationManager;

public class UseCommand implements Command
{
	public static final String NAME = "use";
	
	@Override
	public void execute(String cmdStr)
	{
		String[] cmdArray = cmdStr.split("[\t\n\f\r ]+");
		assert cmdArray.length == 2;
		JavaSerializationManager.getInstance().setCurrent(cmdArray[1]);
		IO.getInstance().println("The current serializable object is: " + cmdArray[1]);
	}

}
