package com.pwrd.serializable.command;

import com.pwrd.JavaLibraryLoader;
import com.pwrd.command.Command;
import com.pwrd.io.IO;
import com.pwrd.util.Toolkit;

public class LoadlibCommand implements Command
{
	public static final String NAME = "loadlib";
	
	@Override
	public void execute(String cmdStr)
	{
		String[] cmdArray = Toolkit.splitStringBySpace(cmdStr);
		assert cmdArray.length == 2;
		boolean res = JavaLibraryLoader.load(cmdArray[1]);
		if(res)
			IO.getInstance().println("成功载入" + cmdArray[1]);
		else
			IO.getInstance().println("载入" + cmdArray[1] + "失败");
	}

}
