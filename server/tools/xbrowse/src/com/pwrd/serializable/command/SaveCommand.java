package com.pwrd.serializable.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.pwrd.command.Command;
import com.pwrd.serializable.JavaSerializationManager;
import com.pwrd.util.Toolkit;

public class SaveCommand implements Command
{
	public static final String NAME = "save";
	
	@Override
	public void execute(String cmdStr)
	{
		String[] cmdArray = Toolkit.splitStringBySpace(cmdStr);
		assert cmdArray.length == 2;
		File file = new File(cmdArray[1]);
		ObjectOutputStream oos = null;
		try
		{
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(JavaSerializationManager.getInstance().getCurrent());
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(oos != null)
			{
				try
				{
					oos.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}
