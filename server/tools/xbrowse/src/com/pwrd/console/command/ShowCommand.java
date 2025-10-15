package com.pwrd.console.command;

import java.util.Collection;
import java.util.Set;

import mkdb.util.DatabaseMetaData;

import com.pwrd.command.Command;
import com.pwrd.dbx.DbxManager;
import com.pwrd.io.IO;

public class ShowCommand implements Command
{

	@Override
	public void execute(String cmdStr)
	{
		String[] cmdArray = cmdStr.split("[\t\n\f\r ]+");
		if(cmdArray.length == 2)
		{
			if("databases".equals(cmdArray[1]))
			{
				Set<String> dbNames = DbxManager.getInstance().getDbxNames();
				for(String dbName : dbNames)
					IO.getInstance().println(dbName);
				IO.getInstance().println("----------------------------------");
				IO.getInstance().println(dbNames.size() + " rows in set");
			}
			else if("tables".equals(cmdArray[1]))
			{
				Collection<DatabaseMetaData.Table> tables = DatabaseMetaData.getInstance().getTables();
				for(DatabaseMetaData.Table table : tables)
					IO.getInstance().println(table.getName());
				IO.getInstance().println("----------------------------------");
				IO.getInstance().println(tables.size() + " rows in set");
			}
		}
	}

}
