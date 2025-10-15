package com.pwrd.console.command;

import com.pwrd.command.Command;
import com.pwrd.dbx.DbxManager;
import com.pwrd.io.IO;

public class AddDbxCommand implements Command {

	@Override
	public void execute(String commandStr) {
		String[] cmdArray = commandStr.split("[\t\n\f\r ]+");
		if (cmdArray.length == 2) {
			DbxManager.getInstance().addDbx(cmdArray[1]);
		} else
			IO.getInstance().println("invalid adddbx syntax, the correct syntax is: adddbx db_path");
	}

}
