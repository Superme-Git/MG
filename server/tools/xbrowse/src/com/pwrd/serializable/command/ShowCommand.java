package com.pwrd.serializable.command;

import java.util.Set;

import com.pwrd.command.Command;
import com.pwrd.io.IO;
import com.pwrd.serializable.JavaSerializationManager;

public class ShowCommand implements Command
{
	public static final String NAME = "show";
	
	@Override
	public void execute(String cmdStr)
	{
		// String[] cmdArray = cmdStr.split("[\t\n\f\r]+");
		// assert cmdArray.length == 2;
		Set<String> names = JavaSerializationManager.getInstance().getAll();
		for(String name : names)
			IO.getInstance().println(name);
	}

}
