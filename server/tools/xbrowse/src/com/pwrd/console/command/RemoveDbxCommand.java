package com.pwrd.console.command;

import com.pwrd.command.Command;
import com.pwrd.dbx.DbxManager;
import com.pwrd.io.IO;

public class RemoveDbxCommand implements Command
{

	@Override
	public void execute(String commandStr)
	{
		String[] cmdArray = commandStr.split("[\t\n\f\r ]+");
		if(cmdArray.length == 2)
			DbxManager.getInstance().removeDbx(cmdArray[1]);
		else
			IO.getInstance().println("invalid removedbx syntax, the correct syntax is: removedbx db_name");
	}

}
